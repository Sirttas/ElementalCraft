package sirttas.elementalcraft.block.pipe;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pipe.BlockElementPipe.CoverType;
import sirttas.elementalcraft.block.tile.AbstractTileECTickable;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;

public class TileElementPipe extends AbstractTileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static final TileEntityType<TileElementPipe> TYPE = null;

	private Map<Direction, ConnectionType> connections;
	private boolean updateState = true;
	private int transferedAmount;
	private int maxTransferAmount;
	private BlockState coverState;


	public TileElementPipe() {
		this(ECConfig.COMMON.pipeTransferAmount.get());
	}

	public TileElementPipe(int maxTransferAmount) {
		super(TYPE);
		this.connections = new EnumMap<>(Direction.class);
		transferedAmount = 0;
		this.maxTransferAmount = maxTransferAmount;
	}

	private Optional<TileEntity> getAdjacentTile(Direction face) {
		return this.hasWorld() ? TileEntityHelper.getTileEntity(this.getWorld(), this.getPos().offset(face)) : Optional.empty();
	}

	private ConnectionType getConection(Direction face) {
		if (connections.containsKey(face)) {
			return connections.get(face);
		}
		return ConnectionType.NONE;
	}

	private void setConection(Direction face, ConnectionType type) {
		connections.put(face, type);
		this.markDirty();
		updateState = true;
	}

	private void refresh(Direction face) {
		this.setConection(face, getAdjacentTile(face).map(tile -> {
			ConnectionType connection = this.getConection(face);

			if (connection != ConnectionType.NONE) {
				return connection;
			}
			if (tile instanceof TileElementPipe) {
				return ConnectionType.CONNECT;
			}
			return CapabilityElementStorage.get(tile, face.getOpposite()).map(storage -> {
				if (this.canInsertInStorage(storage)) {
					return ConnectionType.INSERT;
				} else if (this.canExtractFromStorage(storage)) {
					return ConnectionType.EXTRACT;
				}
				return ConnectionType.NONE;
			}).orElse(ConnectionType.NONE);
		}).orElse(ConnectionType.NONE));
	}

	public void refresh() {
		for (Direction face : Direction.values()) {
			refresh(face);
		}
	}

	private void transferElement(IElementStorage sender, ElementType type) {
		int amount = this.maxTransferAmount - this.transferedAmount;

		if (type != ElementType.NONE) {
			Path path = new Path(sender);
			IElementStorage receiver = path.searchReceiver(this, type, sender.extractElement(amount, type, true));

			if (receiver != null) {
				int remainingAmount = path.amount - sender.transferTo(receiver, type, path.amount);

				path.pipes.forEach(p -> p.transferedAmount += remainingAmount);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		refresh();
		transferedAmount = 0;
		if (this.hasWorld() && !this.world.isRemote) {
			connections.forEach((side, connection) -> {
				if (connection == ConnectionType.EXTRACT) {
					getAdjacentTile(side).map(tile -> CapabilityElementStorage.get(tile, side.getOpposite())).orElseGet(LazyOptional::empty).ifPresent(sender -> {
						if (sender instanceof IElementTypeProvider) {
							this.transferElement(sender, ((IElementTypeProvider) sender).getElementType());
						}
					});
				}
			});
		}
		if (this.updateState && this.hasWorld()) {
			this.getWorld().setBlockState(getPos(),
					this.getWorld().getBlockState(pos)
							.with(BlockElementPipe.NORTH, getConection(Direction.NORTH).getStateConnection())
							.with(BlockElementPipe.EAST, getConection(Direction.EAST).getStateConnection())
							.with(BlockElementPipe.SOUTH, getConection(Direction.SOUTH).getStateConnection())
							.with(BlockElementPipe.WEST, getConection(Direction.WEST).getStateConnection())
							.with(BlockElementPipe.UP, getConection(Direction.UP).getStateConnection())
							.with(BlockElementPipe.DOWN, getConection(Direction.DOWN).getStateConnection()));
			updateState = false;
		}
	}

	public ActionResultType activatePipe(Direction face) {
		return getAdjacentTile(face).map(tile -> {
			ConnectionType connection = this.getConection(face);

			switch (connection) {
			case INSERT:
				if (CapabilityElementStorage.get(tile, face.getOpposite()).filter(this::canExtractFromStorage).isPresent()) {
					this.setConection(face, ConnectionType.EXTRACT);
				} else {
					this.setConection(face, ConnectionType.DISCONNECT);
				}
				return ActionResultType.SUCCESS;
			case EXTRACT:
			case CONNECT:
				this.setConection(face, ConnectionType.DISCONNECT);
				if (tile instanceof TileElementPipe) {
					((TileElementPipe) tile).setConection(face.getOpposite(), ConnectionType.DISCONNECT);
				}
				return ActionResultType.SUCCESS;
			case DISCONNECT:
				LazyOptional<IElementStorage> cap = CapabilityElementStorage.get(tile, face.getOpposite());

				if (cap.filter(this::canInsertInStorage).isPresent()) {
					this.setConection(face, ConnectionType.INSERT);
				} else if (cap.filter(this::canExtractFromStorage).isPresent()) {
					this.setConection(face, ConnectionType.EXTRACT);
				} else if (tile instanceof TileElementPipe) {
					this.setConection(face, ConnectionType.CONNECT);
					((TileElementPipe) tile).setConection(face.getOpposite(), ConnectionType.CONNECT);
				}
				return ActionResultType.SUCCESS;
			default:
				return ActionResultType.PASS;
			}
		}).orElse(ActionResultType.PASS);
	}

	private boolean canInsertInStorage(IElementStorage storage) {
		return ElementType.allValid().stream().anyMatch(storage::canPipeInsert);
	}
	
	private boolean canExtractFromStorage(IElementStorage storage) {
		return ElementType.allValid().stream().anyMatch(storage::canPipeExtract);
	}
	
	public ITextComponent getConnectionMessage(Direction face) {
		return this.getConection(face).getDisplayName();
	}

	public BlockState getCoverState() {
		return coverState;
	}
	
	public ActionResultType setCover(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		Item item = stack.getItem();

		if (item instanceof BlockItem && !stack.isEmpty()) {
			BlockState state = ((BlockItem) item).getBlock().getDefaultState();

			if (state != coverState) {
				if (coverState != null) {
					InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(coverState.getBlock()));
				}
				coverState = state;
				this.getWorld().setBlockState(getPos(), this.getWorld().getBlockState(pos).with(BlockElementPipe.COVER, CoverType.COVERED));

				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.setHeldItem(hand, ItemStack.EMPTY);
					}
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		for (Direction face : Direction.values()) {
			this.setConection(face, ConnectionType.fromInteger(compound.getInt(face.getString())));
		}
		this.maxTransferAmount = compound.getInt("max_transfer_amount");
		coverState = compound.contains(ECNames.COVER) ? NBTUtil.readBlockState(compound.getCompound(ECNames.COVER)) : null;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		connections.forEach((k, v) -> compound.putInt(k.getString(), v.getValue()));
		compound.putInt("max_transfer_amount", this.maxTransferAmount);
		if (coverState != null) {
			compound.put(ECNames.COVER, NBTUtil.writeBlockState(coverState));
		} else if (compound.contains(ECNames.COVER)) {
			compound.remove(ECNames.COVER);
		}
		return compound;
	}

	private enum ConnectionType {
		NONE(0, "none", BlockElementPipe.ConnectionType.NONE),
		CONNECT(1, "connect", BlockElementPipe.ConnectionType.CONNECTED),
		INSERT(2, "insert", BlockElementPipe.ConnectionType.CONNECTED),
		EXTRACT(3, "extract", BlockElementPipe.ConnectionType.EXTRACT),
		DISCONNECT(4, "disconnect", BlockElementPipe.ConnectionType.NONE);

		private BlockElementPipe.ConnectionType stateConnection;
		private final int value;
		private final String translationKey;

		private ConnectionType(int value, String key, BlockElementPipe.ConnectionType stateConnection) {
			this.value = value;
			this.translationKey = "message.elementalcraft." + key;
			this.stateConnection = stateConnection;
		}

		public int getValue() {
			return value;
		}

		public static ConnectionType fromInteger(int x) {
			for (ConnectionType type : values()) {
				if (type.getValue() == x) {
					return type;
				}
			}
			return NONE;
		}

		public ITextComponent getDisplayName() {
			return new TranslationTextComponent(translationKey);
		}
		
		public BlockElementPipe.ConnectionType getStateConnection() {
			return this.stateConnection;
		}
	}

	private static class Path {
		private final IElementStorage sender;
		List<TileElementPipe> visited = Lists.newArrayListWithCapacity(100);
		List<TileElementPipe> pipes = Lists.newArrayListWithCapacity(100);
		int amount;

		public Path(IElementStorage sender) {
			this.sender = sender;
		}

		private IElementStorage searchReceiver(TileElementPipe pipe, ElementType type, int lastCount) {
			int count = Math.min(lastCount, pipe.maxTransferAmount - pipe.transferedAmount);

			if (count > 0 && !visited.contains(pipe)) {
				visited.add(pipe);
				return pipe.connections.entrySet().stream().filter(entry -> {
					ConnectionType connection = entry.getValue();
					
					return connection == ConnectionType.CONNECT || connection == ConnectionType.INSERT;
				}).sorted((c1, c2) -> c1.getValue().value - c2.getValue().value).map(entry -> {
					Direction side = entry.getKey();
					ConnectionType connection = entry.getValue();
				
					return pipe.getAdjacentTile(side).map(entity -> {
						if (entity instanceof TileElementPipe && connection == ConnectionType.CONNECT) {
							IElementStorage ret = searchReceiver((TileElementPipe) entity, type, count);

							if (ret != null) {
								this.pipes.add(pipe);
								return ret;
							}
						} else if (entity != null && connection == ConnectionType.INSERT) {
							LazyOptional<IElementStorage> cap = CapabilityElementStorage.get(entity, side.getOpposite());

							if (cap.filter(receiver -> receiver != sender && receiver.canPipeInsert(type) && receiver.insertElement(count, type, true) < count).isPresent()) {
								this.amount = count;
								this.pipes.add(pipe);
								return cap.orElse(null);
							}
						}
						return null;
					});
				}).filter(Optional::isPresent).map(Optional::get).findAny().orElse(null);
			}
			return null;
		}
	}
}
