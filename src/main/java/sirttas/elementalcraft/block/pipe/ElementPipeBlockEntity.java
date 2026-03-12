package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.element.transfer.path.SimpleElementTransferPathfinder;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.cover.CoverableBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

public class ElementPipeBlockEntity extends CoverableBlockEntity {

	private final ElementPipeTransferer transferer;

	public ElementPipeBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.PIPE, pos, state);
		transferer = new ElementPipeTransferer(this);
	}

	public ConnectionType getConnection(Direction face) {
		return transferer.getConnection(face);
	}

	public void copyTo(ElementPipeBlockEntity newBlockEntity) {
		transferer.copyTo(newBlockEntity.transferer);
		newBlockEntity.coverState = coverState;
	}

	public VoxelShape getShape(@Nullable Direction face) {
		if (face == null) {
			return ElementPipeShapes.BASE_SHAPE;
		}

		var connexion = transferer.getConnection(face);
		var shape = Shapes.empty();
		var upgrade = transferer.getUpgrade(face);

		if (upgrade != null) {
			shape = upgrade.getShape();
		}
		if (connexion.isConnected()) {
			if (upgrade == null || !upgrade.replaceSection()) {
				shape = Shapes.or(shape, ElementPipeShapes.SECTION_SHAPES.get(face));
			}
			if (connexion == ConnectionType.EXTRACT && (upgrade == null || !upgrade.replaceExtraction())) {
				shape = Shapes.or(shape, ElementPipeShapes.EXTRACTION_SHAPES.get(face));
			}
		}
		return shape;
	}

	public VoxelShape getShape() {
		var shape = ElementPipeShapes.BASE_SHAPE;

		for (Direction face : Direction.values()) {
			shape = Shapes.or(shape, getShape(face));
		}
		return shape;
	}

	private void setConnection(Direction face, ConnectionType type) {
		if (transferer.getConnection(face) == type) {
			return;
		}
		transferer.setConnection(face, type);
		this.setChanged();
		if (this.level != null) {
			this.getBlockState().updateNeighbourShapes(this.level, this.getBlockPos(), 3);
		}
	}

	private void refresh(Direction face) {
		if (level == null) {
			return;
		}

		var connection = lookupConnection(face);

		setConnection(face, connection);

		var upgrade = transferer.getUpgrade(face);

		if (upgrade != null && !upgrade.canPlace(connection)) {
			this.removeUpgrade(face);
		}
	}

	private ConnectionType lookupConnection(Direction face) {
		var adjacent = this.getBlockPos().relative(face);
		var connection = this.getConnection(face);
		var opposite = face.getOpposite();

		var t = level.getCapability(ElementalCraftCapabilities.ElementTransferers.BLOCK, adjacent, opposite);

		if (t != null) {
			if (t.canConnectTo(this.getBlockPos())) {
				return connectionOrDefault(connection, ConnectionType.CONNECT);
			}
			return ConnectionType.NONE;
		}

		var storage = level.getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, adjacent, opposite);

		if (storage != null) {
			if (this.canInsertInStorage(storage, opposite)) {
				return connectionOrDefault(connection, ConnectionType.INSERT);
			} else if (this.canExtractFromStorage(storage, opposite)) {
				return connectionOrDefault(connection, ConnectionType.EXTRACT);
			}
			return ConnectionType.NONE;
		}
		return ConnectionType.NONE;
	}

	private static ConnectionType connectionOrDefault(ConnectionType connection, ConnectionType defaultConnection) {
		return connection != ConnectionType.NONE ? connection : defaultConnection;
	}

	void refresh() {
		for (Direction face : Direction.values()) {
			refresh(face);
		}
	}

	private void transferElement(IElementStorage sender, Direction side, ElementType type) {
		var pos = this.getBlockPos();

		if (type != ElementType.NONE && level != null) {
			var upgrade = transferer.getUpgrade(side);

			if ((upgrade != null && !upgrade.canTransfer(type, ConnectionType.EXTRACT)) || !canExtractFromStorage(sender, side.getOpposite())) {
				return;
			}

			var pathfinder = new SimpleElementTransferPathfinder(level);
			var paths = pathfinder.findPaths(type, new ElementPipeTransferer.Node(pos.relative(side), null, sender), new ElementPipeTransferer.Node(pos, transferer, null));

			for (var path : paths) {
				if (!transferer.isValid()) {
					return;
				} else if (!path.isValid()) {
					continue;
				} else if (upgrade != null) {
					path = upgrade.alterPath(path);
				}
				path.transfer();
			}
		}
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		pipe.refresh(); // TODO only refresh if needed
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		pipe.transferer.init();
		commonTick(level, pos, state, pipe);
		if (!pipe.transferer.isValid()) {
			return;
		}

		var profiler = level.getProfiler();

		profiler.push("elementalcraft:element_pipe_element_transfer");
		pipe.transferer.getConnections().entrySet().stream()
				.filter(entry -> entry.getValue() == ConnectionType.EXTRACT)
				.sorted(Comparator.comparing(entry -> pipe.transferer.getUpgradeWeight(entry.getKey())))
				.map(Map.Entry::getKey)
				.forEach(side -> {
					if (!pipe.transferer.isValid()) {
						return;
					}

					var adjacent = pos.relative(side);
					var sender = level.getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, adjacent, side.getOpposite());

					if (sender instanceof IElementTypeProvider provider) {
						pipe.transferElement(sender, side, provider.getElementType());
					}
				});
		profiler.pop();
	}

	public IElementTransferer getTransferer() {
		return transferer;
	}

	public Map<Direction, PipeUpgrade> getUpgrades() {
		return transferer.getUpgrades();
	}

	public PipeUpgrade getUpgrade(Direction face) {
		return this.transferer.getUpgrade(face);
	}

	public void setUpgrade(Direction face, PipeUpgrade upgrade) {
		this.transferer.setUpgrade(face, Objects.requireNonNull(upgrade));
		upgrade.onAdded();
	}

	private void removeUpgrade(Direction side) {
		removeUpgrade(null, side);
	}

	private void removeUpgrade(@Nullable Player player, Direction side) {
		var upgrade = getUpgrade(side);

		if (upgrade == null) {
			return;
		}
		upgrade.dropAll(player);
		transferer.removeUpgrade(side);
		upgrade.onRemoved();
	}
	
	void removeAllUpgrades() {
		for (Direction side : Direction.values()) {
			removeUpgrade(side);
		}
	}
	
	public InteractionResult activatePipe(@Nullable Player player, Direction face) {
		if (level == null) {
			return InteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		var upgrade = getUpgrade(face);

		if (upgrade != null) {
			removeUpgrade(player, face);
			return InteractionResult.SUCCESS;
		}

		var opposite = face.getOpposite();
		var adjacent = this.getBlockPos().relative(face);
		var connection = this.getConnection(face);

		switch (connection) {
			case INSERT -> {
				var storage = level.getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, adjacent, opposite);

				if (canExtractFromStorage(storage, opposite)) {
					this.setConnection(face, ConnectionType.EXTRACT);
				} else {
					this.setConnection(face, ConnectionType.DISCONNECT);
				}
				return InteractionResult.SUCCESS;
			}
			case EXTRACT, CONNECT -> {
				this.setConnection(face, ConnectionType.DISCONNECT);
				if (level.getBlockEntity(adjacent) instanceof ElementPipeBlockEntity pipe) {
					pipe.setConnection(face.getOpposite(), ConnectionType.DISCONNECT);
				}
				return InteractionResult.SUCCESS;
			}
			case DISCONNECT -> {
				var storage = level.getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, adjacent, opposite);

				if (canInsertInStorage(storage, opposite)) {
					this.setConnection(face, ConnectionType.INSERT);
				} else if (canExtractFromStorage(storage, opposite)) {
					this.setConnection(face, ConnectionType.EXTRACT);
				} else if (level.getBlockEntity(adjacent) instanceof ElementPipeBlockEntity pipe) {
					this.setConnection(face, ConnectionType.CONNECT);
					pipe.setConnection(face.getOpposite(), ConnectionType.CONNECT);
				}
				return InteractionResult.SUCCESS;
			}
			default -> {
				return InteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			}
		}
	}

	private boolean canInsertInStorage(@Nullable IElementStorage storage, Direction face) {
		if (storage == null) {
			return false;
		}

		return ElementType.ALL_VALID.stream().anyMatch(type -> storage.canPipeInsert(type, face));
	}
	
	private boolean canExtractFromStorage(IElementStorage storage, Direction face) {
		if (storage == null) {
			return false;
		}

		return ElementType.ALL_VALID.stream().anyMatch(type -> storage.canPipeExtract(type, face));
	}
	
	public Component getConnectionMessage(Direction face) {
		return this.getConnection(face).getDisplayName();
	}

	public int getMaxTransferAmount() {
		return transferer.maxTransferAmount;
	}
	
	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		if (compound.contains(ECNames.TRANSFERER)) {
		    transferer.deserializeNBT(provider, compound.getCompound(ECNames.TRANSFERER));
		}
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put(ECNames.TRANSFERER, transferer.serializeNBT(provider));
	}
}
