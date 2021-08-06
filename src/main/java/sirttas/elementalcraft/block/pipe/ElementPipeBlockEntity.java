package sirttas.elementalcraft.block.pipe;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.item.ECItems;

public class ElementPipeBlockEntity extends AbstractECBlockEntity implements IElementPipe {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementPipeBlock.NAME) public static final BlockEntityType<ElementPipeBlockEntity> TYPE = null;
	
	private final Map<Direction, ConnectionType> connections;
	private final Map<Direction, Boolean> priorities;
	private int transferedAmount;
	private int maxTransferAmount;
	private BlockState coverState;
	
	private final Comparator<Entry<Direction, ConnectionType>> comparator;


	public ElementPipeBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		this.connections = new EnumMap<>(Direction.class);
		this.priorities = new EnumMap<>(Direction.class);
		transferedAmount = 0;
		this.maxTransferAmount = ((ElementPipeBlock) state.getBlock()).getMaxTransferAmount();
		this.comparator = creatComparator();
	}

	private Comparator<Entry<Direction, ConnectionType>> creatComparator() {
		Comparator<Entry<Direction, ConnectionType>> cmp = (c1, c2) -> Boolean.compare(isPriority(c2.getKey()), isPriority(c1.getKey()));
		
		return cmp.thenComparing((c1, c2) -> c2.getValue().getValue() - c1.getValue().getValue());
	}

	private Optional<BlockEntity> getAdjacentTile(Direction face) {
		return this.hasLevel() ? BlockEntityHelper.getTileEntity(this.getLevel(), this.getBlockPos().relative(face)) : Optional.empty();
	}

	@Override
	public ConnectionType getConection(Direction face) {
		return connections.getOrDefault(face, ConnectionType.NONE);
	}
	
	@Override
	public boolean isPriority(Direction face) {
		return Boolean.TRUE.equals(priorities.get(face));
	}

	private void setConection(Direction face, ConnectionType type) {
		connections.put(face, type);
		if (type == ConnectionType.DISCONNECT && isPriority(face)) {
			this.setPriority(face, false);
			dropPriorities(null, 1);
		}
		this.setChanged();
	}
	
	private void setPriority(Direction face, boolean value) {
		priorities.put(face, value);
		this.setChanged();
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

	void refresh() {
		for (Direction face : Direction.values()) {
			refresh(face);
		}
	}

	private void transferElement(IElementStorage sender, ElementType type) {
		if (type != ElementType.NONE) {
			Path path = new Path(sender);

			path.searchReceiver(this, type, sender.extractElement(getRemainingAmount(), type, true)).ifPresent(receiver -> {
				int remainingAmount = path.amount - sender.transferTo(receiver, type, path.amount);

				path.pipes.forEach(p -> p.transferedAmount += remainingAmount);
			});
		}
	}

	private int getRemainingAmount() {
		return this.maxTransferAmount - this.transferedAmount;
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		pipe.refresh();
		pipe.transferedAmount = 0;
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, ElementPipeBlockEntity pipe) {
		commonTick(level, pos, state, pipe);
		pipe.connections.entrySet().stream()
				.filter(entry -> entry.getValue() == ConnectionType.EXTRACT)
				.sorted(pipe.comparator)
				.map(Map.Entry::getKey)
				.map(side -> pipe.getAdjacentTile(side).flatMap(tile -> CapabilityElementStorage.get(tile, side.getOpposite()).resolve()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(sender -> {
					if (sender instanceof IElementTypeProvider) {
						pipe.transferElement(sender, ((IElementTypeProvider) sender).getElementType());
					}
				});
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
		dropPriorities(null, (int) this.priorities.values().stream().filter(Boolean.TRUE::equals).count());
	}
	
	public InteractionResult activatePipe(Direction face) {
		return getAdjacentTile(face).map(tile -> {
			ConnectionType connection = this.getConection(face);

			switch (connection) {
			case INSERT:
				if (CapabilityElementStorage.get(tile, face.getOpposite()).filter(this::canExtractFromStorage).isPresent()) {
					this.setConection(face, ConnectionType.EXTRACT);
				} else {
					this.setConection(face, ConnectionType.DISCONNECT);
				}
				return InteractionResult.SUCCESS;
			case EXTRACT, CONNECT:
				this.setConection(face, ConnectionType.DISCONNECT);
				if (tile instanceof ElementPipeBlockEntity) {
					((ElementPipeBlockEntity) tile).setConection(face.getOpposite(), ConnectionType.DISCONNECT);
				}
				return InteractionResult.SUCCESS;
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
				return InteractionResult.SUCCESS;
			default:
				return InteractionResult.PASS;
			}
		}).orElse(InteractionResult.PASS);
	}

	private boolean canInsertInStorage(IElementStorage storage) {
		return ElementType.ALL_VALID.stream().anyMatch(storage::canPipeInsert);
	}
	
	private boolean canExtractFromStorage(IElementStorage storage) {
		return ElementType.ALL_VALID.stream().anyMatch(storage::canPipeExtract);
	}
	
	public Component getConnectionMessage(Direction face) {
		return this.getConection(face).getDisplayName();
	}

	public BlockState getCoverState() {
		return coverState;
	}
	
	public InteractionResult setCover(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();

		if (item instanceof BlockItem && !stack.isEmpty()) {
			BlockState state = ((BlockItem) item).getBlock().defaultBlockState();

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
	public void load(CompoundTag compound) {
		super.load(compound);
		for (Direction face : Direction.values()) {
			this.setConection(face, ConnectionType.fromInteger(compound.getInt(face.getSerializedName())));
			this.setPriority(face, compound.getBoolean(face.getSerializedName() + "_priority"));
		}
		this.maxTransferAmount = compound.getInt("max_transfer_amount");
		coverState = compound.contains(ECNames.COVER) ? NbtUtils.readBlockState(compound.getCompound(ECNames.COVER)) : null;
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		connections.forEach((k, v) -> compound.putInt(k.getSerializedName(), v.getValue()));
		priorities.forEach((k, v) -> compound.putBoolean(k.getSerializedName() + "_priority", v));
		compound.putInt("max_transfer_amount", this.maxTransferAmount);
		if (coverState != null) {
			compound.put(ECNames.COVER, NbtUtils.writeBlockState(coverState));
		} else if (compound.contains(ECNames.COVER)) {
			compound.remove(ECNames.COVER);
		}
		return compound;
	}

	private static class Path {
		private final IElementStorage sender;
		List<ElementPipeBlockEntity> visited = Lists.newArrayListWithCapacity(100);
		List<ElementPipeBlockEntity> pipes = Lists.newArrayListWithCapacity(100);
		int amount;

		public Path(IElementStorage sender) {
			this.sender = sender;
		}

		private Optional<IElementStorage> searchReceiver(ElementPipeBlockEntity pipe, ElementType type, int lastCount) {
			int count = Math.min(lastCount, pipe.getRemainingAmount());

			if (count > 0 && !visited.contains(pipe)) {
				visited.add(pipe);
				return getConnectionStream(pipe).map(entry -> {
					Direction side = entry.getKey();
					ConnectionType connection = entry.getValue();
				
					return pipe.getAdjacentTile(side).flatMap(entity -> {
						if (entity instanceof ElementPipeBlockEntity && connection == ConnectionType.CONNECT) {
							Optional<IElementStorage> receiver = searchReceiver((ElementPipeBlockEntity) entity, type, count);

							if (receiver.isPresent()) {
								this.pipes.add(pipe);
								return receiver;
							}
						} else if (connection == ConnectionType.INSERT) {
							Optional<IElementStorage> cap = CapabilityElementStorage.get(entity, side.getOpposite())
									.filter(receiver -> receiver != sender && receiver.canPipeInsert(type) && receiver.insertElement(count, type, true) < count);

							if (cap.isPresent()) {
								this.amount = count;
								this.pipes.add(pipe);
								return cap;
							}
						}
						return Optional.empty();
					});
				}).filter(Optional::isPresent).map(Optional::get).findAny();
			}
			return Optional.empty();
		}

		private Stream<Entry<Direction, ConnectionType>> getConnectionStream(ElementPipeBlockEntity pipe) {
			return pipe.connections.entrySet().stream().filter(entry -> {
				ConnectionType connection = entry.getValue();
				
				return connection == ConnectionType.CONNECT || connection == ConnectionType.INSERT;
			}).sorted(pipe.comparator);
		}
	}
}
