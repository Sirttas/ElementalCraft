package sirttas.elementalcraft.block.pipe;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.block.tile.element.IElementSender;
import sirttas.elementalcraft.config.ECConfig;

public class TileElementPipe extends TileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static TileEntityType<TileElementPipe> TYPE;

	private Map<Direction, ConnectionType> connections;
	private boolean updateState = true;

	public TileElementPipe() {
		super(TYPE);
		this.connections = new EnumMap<>(Direction.class);
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
		updateState = true;
	}

	private IElementReceiver searchReceiver(List<TileElementPipe> pipes, ElementType type, int count) {
		pipes.add(this);
		for (Entry<Direction, ConnectionType> connection : connections.entrySet()) {
			TileEntity entity = getAdjacentTile(connection.getKey());

			if (entity instanceof TileElementPipe && connection.getValue() == ConnectionType.CONNECT) {
				TileElementPipe other = (TileElementPipe) entity;

				if (!pipes.contains(other)) {
					IElementReceiver ret = other.searchReceiver(pipes, type, count);

					if (ret != null) {
						return ret;
					}
				}
			} else if (entity instanceof IElementReceiver && connection.getValue() == ConnectionType.INSERT) {
				IElementReceiver receiver = (IElementReceiver) entity;

				if (receiver.inserElement(count, type, true) < count) {
					return receiver;
				}
			} else {
				refresh(connection.getKey());
			}
		}
		return null;
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
		int transferAmount = ECConfig.CONFIG.pipeTransferAmount.get();

		ElementType type = sender.getElementType();

		if (type != ElementType.NONE) {
			IElementReceiver receiver = this.searchReceiver(new ArrayList<TileElementPipe>(), type, sender.extractElement(transferAmount, type, true));

			if (receiver != null) {
				receiver.inserElement(sender.extractElement(transferAmount, type, false), type, false);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		refresh();
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
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		connections.forEach((k, v) -> compound.putInt(k.getString(), v.getValue()));
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
}
