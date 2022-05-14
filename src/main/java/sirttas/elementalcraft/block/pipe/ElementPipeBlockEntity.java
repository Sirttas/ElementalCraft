package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.CapabilityElementTransferer;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer.ConnectionType;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPath;
import sirttas.elementalcraft.api.element.transfer.path.SimpleElementTransferPathfinder;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class ElementPipeBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME) public static final BlockEntityType<ElementPipeBlockEntity> TYPE = null;

	private final ElementPipeTransferer transferer;
	private BlockState coverState;
	private SimpleElementTransferPathfinder pathfinder;
	private final Map<Direction, IElementTransferPath> pathMap;


	public ElementPipeBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		transferer = new ElementPipeTransferer(this);
		pathMap = new EnumMap<>(Direction.class);
	}


	private Optional<BlockEntity> getAdjacentTile(Direction face) {
		return this.hasLevel() ? BlockEntityHelper.getBlockEntity(this.getLevel(), this.getBlockPos().relative(face)) : Optional.empty();
	}

	public ConnectionType getConnection(Direction face) {
		return transferer.getConnection(face);
	}

	public boolean isPriority(Direction face) {
		return transferer.isPriority(face);
	}

	private void setConnection(Direction face, ConnectionType type) {
		transferer.setConnection(face, type);
		if (!type.isConnected() && isPriority(face)) {
			this.setPriority(face, false);
			dropPriorities(null, 1);
		}
		this.setChanged();
	}
	
	private void setPriority(Direction face, boolean value) {
		transferer.setPriority(face, value);
		this.setChanged();
	}

	private void refresh(Direction face) {
		var opposite = face.getOpposite();

		this.setConnection(face, getAdjacentTile(face).map(tile -> {
			ConnectionType connection = this.getConnection(face);

			if (connection != ConnectionType.NONE) {
				return connection;
			}
			if (tile instanceof ElementPipeBlockEntity) {
				return ConnectionType.CONNECT;
			}
			return CapabilityElementStorage.get(tile, opposite).map(storage -> {
				if (this.canInsertInStorage(storage, opposite)) {
					return ConnectionType.INSERT;
				} else if (this.canExtractFromStorage(storage, opposite)) {
					return ConnectionType.EXTRACT;
				}
				return ConnectionType.NONE;
			}).orElse(ConnectionType.NONE);
		}).orElse(ConnectionType.NONE));
	}

	void refresh() {
		for (Direction face : Direction.values()) {
			refresh(face);
		}
	}

	Map<Direction, IElementTransferPath> getPathMap() {
		return pathMap;
	}

	private void transferElement(IElementStorage sender, Direction side, ElementType type) {
		if (type != ElementType.NONE && this.pathfinder != null) {
			var path =  this.pathfinder.findPath(type, sender, transferer, this.getBlockPos());

			pathMap.put(side, path);
			path.transfer();
		}
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		pipe.refresh();
		pipe.transferer.resetTransferedAmount();
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		commonTick(level, pos, state, pipe);
		if (pipe.pathfinder == null) {
			pipe.pathfinder = new SimpleElementTransferPathfinder(level);
		}
		pipe.transferer.getConnections().entrySet().stream()
				.filter(entry -> entry.getValue() == ConnectionType.EXTRACT)
				.sorted(pipe.transferer.comparator)
				.map(Map.Entry::getKey)
				.forEach(side -> pipe.getAdjacentTile(side).flatMap(tile -> CapabilityElementStorage.get(tile, side.getOpposite()).resolve()).ifPresent(sender -> {
					if (sender instanceof IElementTypeProvider provider) {
						pipe.transferElement(sender, side, provider.getElementType());
					}
				}));
	}

	public InteractionResult activatePriority(Direction face, Player player, InteractionHand hand) {
		boolean priority = isPriority(face);
		
		this.setPriority(face, !priority);
		if (priority) {
			dropPriorities(player, 1);
		} else {
			ItemStack stack = player.getItemInHand(hand);
			
			if (!player.isCreative()) {
				stack.shrink(1);
				if (stack.isEmpty()) {
					player.setItemInHand(hand, ItemStack.EMPTY);
				}
			}
		}
		return InteractionResult.SUCCESS;
	}

	private void dropPriorities(@Nullable Player player, int size) {
		Level world = this.getLevel();
		
		if (world != null && !world.isClientSide) {
			Vec3 vect = player != null ? player.position().add(0, 0.25, 0) : Vec3.atCenterOf(this.getBlockPos());
			
			world.addFreshEntity(new ItemEntity(world, vect.x, vect.y, vect.z, new ItemStack(ECItems.PIPE_PRIORITY, size)));
		}
	}
	
	void dropAllPriorities() {
		dropPriorities(null, (int) this.transferer.priorities.values().stream().filter(Boolean.TRUE::equals).count());
	}
	
	public InteractionResult activatePipe(Direction face) {
		var opposite = face.getOpposite();

		return getAdjacentTile(face).map(tile -> {
			ConnectionType connection = this.getConnection(face);

			switch (connection) {
			case INSERT:
				if (CapabilityElementStorage.get(tile, opposite).filter(storage -> canExtractFromStorage(storage, opposite)).isPresent()) {
					this.setConnection(face, ConnectionType.EXTRACT);
				} else {
					this.setConnection(face, ConnectionType.DISCONNECT);
				}
				return InteractionResult.SUCCESS;
			case EXTRACT, CONNECT:
				this.setConnection(face, ConnectionType.DISCONNECT);
				if (tile instanceof ElementPipeBlockEntity pipe) {
					pipe.setConnection(face.getOpposite(), ConnectionType.DISCONNECT);
				}
				return InteractionResult.SUCCESS;
			case DISCONNECT:
				LazyOptional<IElementStorage> cap = CapabilityElementStorage.get(tile, face.getOpposite());

				if (cap.filter(storage -> canInsertInStorage(storage, opposite)).isPresent()) {
					this.setConnection(face, ConnectionType.INSERT);
				} else if (cap.filter(storage -> canExtractFromStorage(storage, opposite)).isPresent()) {
					this.setConnection(face, ConnectionType.EXTRACT);
				} else if (tile instanceof ElementPipeBlockEntity pipe) {
					this.setConnection(face, ConnectionType.CONNECT);
					pipe.setConnection(face.getOpposite(), ConnectionType.CONNECT);
				}
				return InteractionResult.SUCCESS;
			default:
				return InteractionResult.PASS;
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
		if (!this.remove && cap == CapabilityElementTransferer.ELEMENT_TRANSFERER_CAPABILITY) {
			return LazyOptional.of(() -> (T) this.transferer);
		}
		return super.getCapability(cap, side);
	}
}
