package sirttas.elementalcraft.block.pipe;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.block.tile.AbstractTileECTickable;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;

public class TileElementPipe extends AbstractTileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static final TileEntityType<TileElementPipe> TYPE = null;

	private Map<Direction, ConnectionType> connections;
	private boolean updateState = true;
	private int transferedAmount;
	private int maxTransferAmount;


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

	private boolean isConnectedTo(Direction face) {
		ConnectionType type = this.getConection(face);
		return type != ConnectionType.NONE && type != ConnectionType.DISCONNECT;
	}

	private boolean isExtracting(Direction face) {
		return this.getConection(face) == ConnectionType.EXTRACT;
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
			Path path = new Path();
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
							.with(BlockElementPipe.NORTH, isConnectedTo(Direction.NORTH))
							.with(BlockElementPipe.EAST, isConnectedTo(Direction.EAST))
							.with(BlockElementPipe.SOUTH, isConnectedTo(Direction.SOUTH))
							.with(BlockElementPipe.WEST, isConnectedTo(Direction.WEST))
							.with(BlockElementPipe.UP, isConnectedTo(Direction.UP))
							.with(BlockElementPipe.DOWN, isConnectedTo(Direction.DOWN))
							.with(BlockElementPipe.NORTH_EXTRACT, isExtracting(Direction.NORTH))
							.with(BlockElementPipe.EAST_EXTRACT, isExtracting(Direction.EAST))
							.with(BlockElementPipe.SOUTH_EXTRACT, isExtracting(Direction.SOUTH))
							.with(BlockElementPipe.WEST_EXTRACT, isExtracting(Direction.WEST))
							.with(BlockElementPipe.UP_EXTRACT, isExtracting(Direction.UP))
							.with(BlockElementPipe.DOWN_EXTRACT, isExtracting(Direction.DOWN)));
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

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		for (Direction face : Direction.values()) {
			this.setConection(face, ConnectionType.fromInteger(compound.getInt(face.getString())));
		}
		this.maxTransferAmount = compound.getInt("max_transfer_amount");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		connections.forEach((k, v) -> compound.putInt(k.getString(), v.getValue()));
		compound.putInt("max_transfer_amount", this.maxTransferAmount);
		return compound;
	}

	private enum ConnectionType {
		NONE(0, "none"), CONNECT(1, "connect"), INSERT(2, "insert"), EXTRACT(3, "extract"), DISCONNECT(4, "disconnect");

		private final int value;
		private final String translationKey;

		private ConnectionType(int value, String key) {
			this.value = value;
			this.translationKey = "message.elementalcraft." + key;
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
	}

	private static class Path {
		List<TileElementPipe> visited = Lists.newArrayListWithCapacity(100);
		List<TileElementPipe> pipes = Lists.newArrayListWithCapacity(100);
		int amount;

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

							if (cap.filter(receiver -> receiver.canPipeInsert(type) && receiver.insertElement(count, type, true) < count).isPresent()) {
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
