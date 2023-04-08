package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPath;
import sirttas.elementalcraft.api.element.transfer.path.SimpleElementTransferPathfinder;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ElementPipeBlockEntity extends AbstractECBlockEntity {

	private final ElementPipeTransferer transferer;
	private BlockState coverState;
	private final Map<Direction, IElementTransferPath> pathMap;

	public ElementPipeBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.PIPE, pos, state);
		transferer = new ElementPipeTransferer(this);
		pathMap = new EnumMap<>(Direction.class);
	}


	private Optional<BlockEntity> getAdjacentTile(Direction face) {
		return this.hasLevel() ? BlockEntityHelper.getBlockEntity(this.getLevel(), this.getBlockPos().relative(face)) : Optional.empty();
	}

	public ConnectionType getConnection(Direction face) {
		return transferer.getConnection(face);
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
		// TODO cache

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
		var opposite = face.getOpposite();

		var connection = getAdjacentTile(face).map(tile -> {
			ConnectionType c = this.getConnection(face);

			if (c != ConnectionType.NONE) {
				return c;
			}
			if (tile instanceof ElementPipeBlockEntity) {
				return ConnectionType.CONNECT;
			}
			return ElementStorageHelper.get(tile, opposite).map(storage -> {
				if (this.canInsertInStorage(storage, opposite)) {
					return ConnectionType.INSERT;
				} else if (this.canExtractFromStorage(storage, opposite)) {
					return ConnectionType.EXTRACT;
				}
				return ConnectionType.NONE;
			}).orElse(ConnectionType.NONE);
		}).orElse(ConnectionType.NONE);
		setConnection(face, connection);

		var upgrade = transferer.getUpgrade(face);

		if (upgrade != null && !upgrade.canPlace(connection)) {
			this.removeUpgrade(face);
		}
	}

	void refresh() {
		for (Direction face : Direction.values()) {
			refresh(face);
		}
		// TODO remove caches
	}

	Map<Direction, IElementTransferPath> getPathMap() {
		return pathMap;
	}

	private void transferElement(IElementStorage sender, Direction side, ElementType type) {
		var pos = this.getBlockPos();

		if (type != ElementType.NONE && level != null) {
			var upgrade = transferer.getUpgrade(side);

			if ((upgrade != null && !upgrade.canTransfer(type, ConnectionType.EXTRACT)) || !canExtractFromStorage(sender, side.getOpposite())) {
				return;
			}

			var pathfinder = new SimpleElementTransferPathfinder(level);
			var path = pathfinder.findPath(type, new ElementPipeTransferer.Node(pos.relative(side), null, sender), new ElementPipeTransferer.Node(pos, transferer, null));

			pathMap.put(side, path);


			if (upgrade != null) {
				path = upgrade.alterPath(path);
			}
			path.transfer();
		}
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		pipe.refresh();
		pipe.transferer.resetTransferedAmount();
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		commonTick(level, pos, state, pipe);
		pipe.transferer.getConnections().entrySet().stream()
				.filter(entry -> entry.getValue() == ConnectionType.EXTRACT)
				.sorted(pipe.transferer.comparator)
				.map(Map.Entry::getKey)
				.forEach(side -> pipe.getAdjacentTile(side).flatMap(tile -> ElementStorageHelper.get(tile, side.getOpposite()).resolve()).ifPresent(sender -> {
					if (sender instanceof IElementTypeProvider provider) {
						pipe.transferElement(sender, side, provider.getElementType());
					}
				}));
	}

	public PipeUpgrade getUpgrade(Direction face) {
		return this.transferer.getUpgrade(face);
	}

	public void setUpgrade(Direction face, PipeUpgrade upgrade) {
		this.transferer.setUpgrade(face, Objects.requireNonNull(upgrade));
		upgrade.added();
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
		upgrade.removed();
	}
	
	void removeAllUpgrades() {
		for (Direction side : Direction.values()) {
			removeUpgrade(side);
		}
	}
	
	public InteractionResult activatePipe(@Nullable Player player, Direction face) {
		var upgrade = getUpgrade(face);

		if (upgrade != null) {
			removeUpgrade(player, face);
			return InteractionResult.SUCCESS;
		}

		var opposite = face.getOpposite();

		return getAdjacentTile(face).map(tile -> {
			ConnectionType connection = this.getConnection(face);

			switch (connection) {
				case INSERT -> {
					if (ElementStorageHelper.get(tile, opposite).filter(storage -> canExtractFromStorage(storage, opposite)).isPresent()) {
						this.setConnection(face, ConnectionType.EXTRACT);
					} else {
						this.setConnection(face, ConnectionType.DISCONNECT);
					}
					return InteractionResult.SUCCESS;
				}
				case EXTRACT, CONNECT -> {
					this.setConnection(face, ConnectionType.DISCONNECT);
					if (tile instanceof ElementPipeBlockEntity pipe) {
						pipe.setConnection(face.getOpposite(), ConnectionType.DISCONNECT);
					}
					return InteractionResult.SUCCESS;
				}
				case DISCONNECT -> {
					LazyOptional<IElementStorage> cap = ElementStorageHelper.get(tile, face.getOpposite());
					if (cap.filter(storage -> canInsertInStorage(storage, opposite)).isPresent()) {
						this.setConnection(face, ConnectionType.INSERT);
					} else if (cap.filter(storage -> canExtractFromStorage(storage, opposite)).isPresent()) {
						this.setConnection(face, ConnectionType.EXTRACT);
					} else if (tile instanceof ElementPipeBlockEntity pipe) {
						this.setConnection(face, ConnectionType.CONNECT);
						pipe.setConnection(face.getOpposite(), ConnectionType.CONNECT);
					}
					return InteractionResult.SUCCESS;
				}
				default -> {
					return InteractionResult.PASS;
				}
			}
		}).orElse(InteractionResult.PASS);
	}

	private boolean canInsertInStorage(IElementStorage storage, Direction face) {
		return ElementType.ALL_VALID.stream().anyMatch(type -> storage.canPipeInsert(type, face));
	}
	
	private boolean canExtractFromStorage(IElementStorage storage, Direction face) {
		return ElementType.ALL_VALID.stream().anyMatch(type -> storage.canPipeExtract(type, face));
	}
	
	public Component getConnectionMessage(Direction face) {
		return this.getConnection(face).getDisplayName();
	}

	public BlockState getCoverState() {
		return coverState;
	}
	
	public InteractionResult setCover(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();

		if (item instanceof BlockItem blockItem && !stack.isEmpty()) {
			BlockState state = blockItem.getBlock().defaultBlockState();

			if (state != coverState) {
				if (coverState != null) {
					Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), new ItemStack(coverState.getBlock()));
				}
				coverState = state;
				this.getLevel().setBlockAndUpdate(getBlockPos(), this.getLevel().getBlockState(worldPosition).setValue(ElementPipeBlock.COVER, CoverType.COVERED));

				if (!player.isCreative()) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						player.setItemInHand(hand, ItemStack.EMPTY);
					}
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public void load(@NotNull CompoundTag compound) {
		super.load(compound);
		transferer.load(compound.getCompound(ECNames.TRANSFERER));
		coverState = compound.contains(ECNames.COVER) ? NbtUtils.readBlockState(compound.getCompound(ECNames.COVER)) : null;
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.TRANSFERER, transferer.save(new CompoundTag()));
		if (coverState != null) {
			compound.put(ECNames.COVER, NbtUtils.writeBlockState(coverState));
		} else if (compound.contains(ECNames.COVER)) {
			compound.remove(ECNames.COVER);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (!this.remove && cap == ElementalCraftCapabilities.ELEMENT_TRANSFERER) {
			return LazyOptional.of(() -> (T) this.transferer);
		}
		return super.getCapability(cap, side);
	}
}
