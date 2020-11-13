package sirttas.elementalcraft.block.pipe;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementReceiver;
import sirttas.elementalcraft.api.element.storage.IElementSender;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.config.ECConfig;

public class TileElementPipe extends TileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static TileEntityType<TileElementPipe> TYPE;

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

	private TileEntity getAdjacentTile(Direction face) {
		return this.hasWorld() ? this.getWorld().getTileEntity(this.getPos().offset(face)) : null;
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
		this.forceSync();
		updateState = true;
	}

	private void refresh(Direction face) {
		TileEntity other = getAdjacentTile(face);

		if (this.getConection(face) == ConnectionType.NONE) {
			if (other instanceof TileElementPipe) {
				this.setConection(face, ConnectionType.CONNECT);
			} else if (other instanceof IElementReceiver) {
				this.setConection(face, ConnectionType.INSERT);
			} else if (other instanceof IElementSender) {
				this.setConection(face, ConnectionType.EXTRACT);
			}
		} else {
			if (other == null) {
				this.setConection(face, ConnectionType.NONE);
			}
		}
	}

	public void refresh() {
		for (Direction face : Direction.values()) {
			refresh(face);
		}
	}

	private void transferElement(IElementSender sender) {
		int amount = this.maxTransferAmount - this.transferedAmount;
		ElementType type = sender.getElementType();

		if (type != ElementType.NONE) {
			Path path = new Path();
			IElementReceiver receiver = path.searchReceiver(this, type, sender.extractElement(amount, type, true));

			if (receiver != null) {
				int resultingAmount = path.amount - receiver.inserElement(sender.extractElement(path.amount, type, false), type, false);

				path.pipes.forEach(p -> p.transferedAmount += resultingAmount);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		refresh();
		transferedAmount = 0;
		connections.forEach((k, v) -> {
			if (v == ConnectionType.EXTRACT) {
				TileEntity entity = getAdjacentTile(k);

				if (entity instanceof IElementSender) {
					transferElement((IElementSender) entity);
				}
			}
		});

		if (this.updateState && this.hasWorld()) {
			this.getWorld().setBlockState(getPos(),
					this.getWorld().getBlockState(pos).with(BlockElementPipe.NORTH, isConnectedTo(Direction.NORTH)).with(BlockElementPipe.EAST, isConnectedTo(Direction.EAST))
							.with(BlockElementPipe.SOUTH, isConnectedTo(Direction.SOUTH)).with(BlockElementPipe.WEST, isConnectedTo(Direction.WEST))
							.with(BlockElementPipe.UP, isConnectedTo(Direction.UP)).with(BlockElementPipe.DOWN, isConnectedTo(Direction.DOWN))
							.with(BlockElementPipe.NORTH_EXTRACT, isExtracting(Direction.NORTH)).with(BlockElementPipe.EAST_EXTRACT, isExtracting(Direction.EAST))
							.with(BlockElementPipe.SOUTH_EXTRACT, isExtracting(Direction.SOUTH)).with(BlockElementPipe.WEST_EXTRACT, isExtracting(Direction.WEST))
							.with(BlockElementPipe.UP_EXTRACT, isExtracting(Direction.UP)).with(BlockElementPipe.DOWN_EXTRACT, isExtracting(Direction.DOWN)));
			updateState = false;
		}
	}

	public ActionResultType activatePipe(Direction face) {
		TileEntity tile = getAdjacentTile(face);
		ConnectionType connection = this.getConection(face);

		switch (connection) {
		case INSERT:
			if (tile instanceof IElementSender) {
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
			if (tile instanceof IElementReceiver) {
				this.setConection(face, ConnectionType.INSERT);
			} else if (tile instanceof IElementSender) {
				this.setConection(face, ConnectionType.EXTRACT);
			} else if (tile instanceof TileElementPipe) {
				this.setConection(face, ConnectionType.CONNECT);
				((TileElementPipe) tile).setConection(face.getOpposite(), ConnectionType.CONNECT);
			}
			return ActionResultType.SUCCESS;
		default:
			return ActionResultType.PASS;
		}
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
		NONE(0), CONNECT(1), INSERT(2), EXTRACT(3), DISCONNECT(4);

		private final int value;

		private ConnectionType(int value) {
			this.value = value;
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
	}

	private static class Path {
		List<TileElementPipe> visited = Lists.newArrayListWithCapacity(100);
		List<TileElementPipe> pipes = Lists.newArrayListWithCapacity(100);
		int amount;

		private IElementReceiver searchReceiver(TileElementPipe pipe, ElementType type, int lastCount) {
			int count = Math.min(lastCount, pipe.maxTransferAmount - pipe.transferedAmount);

			if (count > 0 && !visited.contains(pipe)) {
				visited.add(pipe);
				for (Entry<Direction, ConnectionType> connection : pipe.connections.entrySet()) {
					TileEntity entity = pipe.getAdjacentTile(connection.getKey());

					if (entity instanceof TileElementPipe && connection.getValue() == ConnectionType.CONNECT) {
						IElementReceiver ret = searchReceiver((TileElementPipe) entity, type, count);

						if (ret != null) {
							this.pipes.add(pipe);
							return ret;
						}
					} else if (entity instanceof IElementReceiver && connection.getValue() == ConnectionType.INSERT) {
						IElementReceiver receiver = (IElementReceiver) entity;

						if (receiver.inserElement(count, type, true) < count) {
							this.amount = count;
							this.pipes.add(pipe);
							return receiver;
						}
					} else {
						pipe.refresh(connection.getKey());
					}
				}
			}
			return null;
		}
	}
}
