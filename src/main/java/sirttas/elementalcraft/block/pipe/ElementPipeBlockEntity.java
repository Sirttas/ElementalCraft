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
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECTickableBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.config.ECConfig;

public class ElementPipeBlockEntity extends AbstractECTickableBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME) public static final TileEntityType<ElementPipeBlockEntity> TYPE = null;

	private Map<Direction, ConnectionType> connections;
	private boolean updateState = true;
	private int transferedAmount;
	private int maxTransferAmount;
	private BlockState coverState;


	public ElementPipeBlockEntity() {
		this(ECConfig.COMMON.pipeTransferAmount.get());
	}

	public ElementPipeBlockEntity(int maxTransferAmount) {
		super(TYPE);
		this.connections = new EnumMap<>(Direction.class);
		transferedAmount = 0;
		this.maxTransferAmount = maxTransferAmount;
	}

	private Optional<TileEntity> getAdjacentTile(Direction face) {
		return this.hasLevel() ? BlockEntityHelper.getTileEntity(this.getLevel(), this.getBlockPos().relative(face)) : Optional.empty();
	}

	private ConnectionType getConection(Direction face) {
		if (connections.containsKey(face)) {
			return connections.get(face);
		}
		return ConnectionType.NONE;
	}

	private void setConection(Direction face, ConnectionType type) {
		connections.put(face, type);
		this.setChanged();
		updateState = true;
	}

	private void refresh(Direction face) {
		this.setConection(face, getAdjacentTile(face).map(tile -> {
			ConnectionType connection = this.getConection(face);

			if (connection != ConnectionType.NONE) {
				return connection;
			}
			if (tile instanceof ElementPipeBlockEntity) {
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
		if (this.hasLevel() && !this.level.isClientSide) {
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
		if (this.updateState && this.hasLevel()) {
			this.getLevel().setBlockAndUpdate(getBlockPos(),
					this.getLevel().getBlockState(worldPosition)
							.setValue(ElementPipeBlock.NORTH, getConection(Direction.NORTH).getStateConnection())
							.setValue(ElementPipeBlock.EAST, getConection(Direction.EAST).getStateConnection())
							.setValue(ElementPipeBlock.SOUTH, getConection(Direction.SOUTH).getStateConnection())
							.setValue(ElementPipeBlock.WEST, getConection(Direction.WEST).getStateConnection())
							.setValue(ElementPipeBlock.UP, getConection(Direction.UP).getStateConnection())
							.setValue(ElementPipeBlock.DOWN, getConection(Direction.DOWN).getStateConnection()));
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
				if (tile instanceof ElementPipeBlockEntity) {
					((ElementPipeBlockEntity) tile).setConection(face.getOpposite(), ConnectionType.DISCONNECT);
				}
				return ActionResultType.SUCCESS;
			case DISCONNECT:
				LazyOptional<IElementStorage> cap = CapabilityElementStorage.get(tile, face.getOpposite());

				if (cap.filter(this::canInsertInStorage).isPresent()) {
					this.setConection(face, ConnectionType.INSERT);
				} else if (cap.filter(this::canExtractFromStorage).isPresent()) {
					this.setConection(face, ConnectionType.EXTRACT);
				} else if (tile instanceof ElementPipeBlockEntity) {
					this.setConection(face, ConnectionType.CONNECT);
					((ElementPipeBlockEntity) tile).setConection(face.getOpposite(), ConnectionType.CONNECT);
				}
				return ActionResultType.SUCCESS;
			default:
				return ActionResultType.PASS;
			}
		}).orElse(ActionResultType.PASS);
	}

	private boolean canInsertInStorage(IElementStorage storage) {
		return ElementType.ALL_VALID.stream().anyMatch(storage::canPipeInsert);
	}
	
	private boolean canExtractFromStorage(IElementStorage storage) {
		return ElementType.ALL_VALID.stream().anyMatch(storage::canPipeExtract);
	}
	
	public ITextComponent getConnectionMessage(Direction face) {
		return this.getConection(face).getDisplayName();
	}

	public BlockState getCoverState() {
		return coverState;
	}
	
	public ActionResultType setCover(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();

		if (item instanceof BlockItem && !stack.isEmpty()) {
			BlockState state = ((BlockItem) item).getBlock().defaultBlockState();

			if (state != coverState) {
				if (coverState != null) {
					InventoryHelper.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), new ItemStack(coverState.getBlock()));
				}
				coverState = state;
				this.getLevel().setBlockAndUpdate(getBlockPos(), this.getLevel().getBlockState(worldPosition).setValue(ElementPipeBlock.COVER, CoverType.COVERED));

				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.setItemInHand(hand, ItemStack.EMPTY);
					}
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		for (Direction face : Direction.values()) {
			this.setConection(face, ConnectionType.fromInteger(compound.getInt(face.getSerializedName())));
		}
		this.maxTransferAmount = compound.getInt("max_transfer_amount");
		coverState = compound.contains(ECNames.COVER) ? NBTUtil.readBlockState(compound.getCompound(ECNames.COVER)) : null;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		connections.forEach((k, v) -> compound.putInt(k.getSerializedName(), v.getValue()));
		compound.putInt("max_transfer_amount", this.maxTransferAmount);
		if (coverState != null) {
			compound.put(ECNames.COVER, NBTUtil.writeBlockState(coverState));
		} else if (compound.contains(ECNames.COVER)) {
			compound.remove(ECNames.COVER);
		}
		return compound;
	}

	private enum ConnectionType {
		NONE(0, "none", ElementPipeBlock.ConnectionType.NONE),
		CONNECT(1, "connect", ElementPipeBlock.ConnectionType.CONNECTED),
		INSERT(2, "insert", ElementPipeBlock.ConnectionType.CONNECTED),
		EXTRACT(3, "extract", ElementPipeBlock.ConnectionType.EXTRACT),
		DISCONNECT(4, "disconnect", ElementPipeBlock.ConnectionType.NONE);

		private ElementPipeBlock.ConnectionType stateConnection;
		private final int value;
		private final String translationKey;

		private ConnectionType(int value, String key, ElementPipeBlock.ConnectionType stateConnection) {
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
		
		public ElementPipeBlock.ConnectionType getStateConnection() {
			return this.stateConnection;
		}
	}

	private static class Path {
		private final IElementStorage sender;
		List<ElementPipeBlockEntity> visited = Lists.newArrayListWithCapacity(100);
		List<ElementPipeBlockEntity> pipes = Lists.newArrayListWithCapacity(100);
		int amount;

		public Path(IElementStorage sender) {
			this.sender = sender;
		}

		private IElementStorage searchReceiver(ElementPipeBlockEntity pipe, ElementType type, int lastCount) {
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
						if (entity instanceof ElementPipeBlockEntity && connection == ConnectionType.CONNECT) {
							IElementStorage ret = searchReceiver((ElementPipeBlockEntity) entity, type, count);

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
