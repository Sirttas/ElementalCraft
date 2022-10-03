package sirttas.elementalcraft.block.pipe;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ElementPipeBlock extends AbstractECEntityBlock {

	public static final String NAME = "elementpipe";
	public static final String NAME_IMPAIRED = NAME + "_impaired";
	public static final String NAME_IMPROVED = NAME + "_improved";
	public static final String NAME_CREATIVE = NAME + "_creative";

	private static final VoxelShape BASE_SHAPE = Block.box(6.5D, 6.5D, 6.5D, 9.5D, 9.5D, 9.5D);
	private static final VoxelShape WEST_SHAPE = Block.box(0, 7D, 7D, 6.5D, 9D, 9D);
	private static final VoxelShape DOWN_SHAPE = Block.box(7D, 0, 7D, 9D, 6.5D, 9D);
	private static final VoxelShape NORTH_SHAPE = Block.box(7D, 7D, 0, 9D, 9D, 6.5D);
	private static final VoxelShape EAST_SHAPE = Block.box(9.5D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape UP_SHAPE = Block.box(7D, 9.5D, 7D, 9D, 16D, 9D);
	private static final VoxelShape SOUTH_SHAPE = Block.box(7D, 7D, 9.5D, 9D, 9D, 16D);

	private static final VoxelShape FRAME_SHAPE = Shapes.join(Shapes.block(),
			Shapes.or(Block.box(0D, 1D, 1D, 16D, 15D, 15D), Block.box(1D, 0D, 1D, 15D, 16D, 15D), Block.box(1D, 1D, 0D, 15D, 15D, 16D)),
			BooleanOp.ONLY_FIRST);
	
	private static final List<VoxelShape> SHAPES = List.of(EAST_SHAPE, NORTH_SHAPE, WEST_SHAPE, SOUTH_SHAPE, UP_SHAPE, DOWN_SHAPE, BASE_SHAPE, FRAME_SHAPE);

	public static final EnumProperty<CoverType> COVER = EnumProperty.create("cover", CoverType.class);

	private final PipeType type;

	public ElementPipeBlock(PipeType type) {
		super(BlockBehaviour.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().randomTicks());
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(COVER, CoverType.NONE));
		this.type = type;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(COVER);
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new ElementPipeBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECTicker(level, type, ECBlockEntityTypes.PIPE, level.isClientSide ? ElementPipeBlockEntity::commonTick : ElementPipeBlockEntity::serverTick);
	}

	@Override
	@Deprecated
	public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
		if (level.getBlockEntity(pos) instanceof ElementPipeBlockEntity pipe) {
			pipe.refresh();
		}
	}

	@Override
	@Deprecated
	public void tick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource rand) {
		if (level.getBlockEntity(pos) instanceof ElementPipeBlockEntity pipe) {
			pipe.refresh();
		}
	}

	private boolean compareShapes(VoxelShape shape1, VoxelShape shape2) {
		return shape1.bounds().equals(shape2.bounds());
	}

	private Direction getFace(VoxelShape shape, BlockHitResult hit) {
		if (compareShapes(shape, DOWN_SHAPE)) {
			return Direction.DOWN;
		} else if (compareShapes(shape, UP_SHAPE)) {
			return Direction.UP;
		} else if (compareShapes(shape, NORTH_SHAPE)) {
			return Direction.NORTH;
		} else if (compareShapes(shape, SOUTH_SHAPE)) {
			return Direction.SOUTH;
		} else if (compareShapes(shape, WEST_SHAPE)) {
			return Direction.WEST;
		} else if (compareShapes(shape, EAST_SHAPE)) {
			return Direction.EAST;
		} else if (shape == BASE_SHAPE) {
			return hit.getDirection();
		}
		return null;
	}

	public static boolean showCover(BlockState state, Player player) {
		return isCovered(state) && (player == null || EntityHelper.handStream(player).noneMatch(stack -> !stack.isEmpty() && stack.is(ECTags.Items.PIPE_COVER_HIDING)));
	}

	private static boolean isCovered(BlockState state) {
		return state.getValue(COVER) == CoverType.COVERED;
	}

	private boolean isRendered(VoxelShape shape, ElementPipeBlockEntity entity, BlockState state) {
		return state.is(this) && entity != null && (compareShapes(shape, BASE_SHAPE)
				|| (compareShapes(shape, DOWN_SHAPE) && entity.getConnection(Direction.DOWN).isConnected())
				|| (compareShapes(shape, UP_SHAPE) && entity.getConnection(Direction.UP).isConnected())
				|| (compareShapes(shape, NORTH_SHAPE) && entity.getConnection(Direction.NORTH).isConnected())
				|| (compareShapes(shape, SOUTH_SHAPE) && entity.getConnection(Direction.SOUTH).isConnected())
				|| (compareShapes(shape, WEST_SHAPE) && entity.getConnection(Direction.WEST).isConnected())
				|| (compareShapes(shape, EAST_SHAPE) && entity.getConnection(Direction.EAST).isConnected())
				|| (compareShapes(shape, FRAME_SHAPE) && state.getValue(COVER) == CoverType.FRAME));
	}

	private VoxelShape getCurrentShape(BlockState state, ElementPipeBlockEntity entity, Player player) {
		VoxelShape result = Shapes.empty();

		if (showCover(state, entity != null ? player : null)) {
			return Shapes.block();
		}
		for (final VoxelShape shape : SHAPES) {
			if (isRendered(shape, entity, state)) {
				result = Shapes.or(result, shape);
			}
		}
		return result;
	}

	@Override
	@Deprecated
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		Player player = getPlayer(context);
		ElementPipeBlockEntity blockEntity = getBlockEntity(blockGetter, pos);

		return blockGetter instanceof Level level && level.isClientSide ? getShape(state, pos, blockEntity, Minecraft.getInstance().hitResult, player)
				: getCurrentShape(state, blockEntity, player);
	}

	private Player getPlayer(CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext && entityContext.getEntity() instanceof Player player) {
			return player;
		}
		return null;
	}

	public VoxelShape getShape(BlockState state, BlockPos pos, ElementPipeBlockEntity blockEntity, HitResult result, Player player) {
		if (!showCover(state, player) && result != null && result.getType() == HitResult.Type.BLOCK && ((BlockHitResult) result).getBlockPos().equals(pos)) {
			final Vec3 hit = result.getLocation();

			for (final VoxelShape shape : SHAPES) {
				if (isRendered(shape, blockEntity, state) && ShapeHelper.vectorCollideWithShape(shape, pos, hit)) {
					return shape;
				}
			}
		}
		return getCurrentShape(state, blockEntity, player);
	}

	@Override
	@Deprecated
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return getCurrentShape(state, getBlockEntity(world, pos), null);
	}

	@Override
	@Deprecated
	public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		final ElementPipeBlockEntity pipe = (ElementPipeBlockEntity) level.getBlockEntity(pos);

		if (pipe != null) {
			final VoxelShape shape = getShape(state, pos, getBlockEntity(level, pos), hit, player);

			if (compareShapes(shape, FRAME_SHAPE) || state.getValue(COVER) == CoverType.FRAME) {
				return pipe.setCover(player, hand);
			} else {
				Direction face = getFace(shape, hit);
				InteractionResult value = onShapeActivated(face, pipe, player, hand);
	
				if (value != InteractionResult.PASS) {
					player.displayClientMessage(pipe.getConnectionMessage(face), true);
					level.updateNeighborsAt(pos, this);
				}
				return value;
			}
		}
		return InteractionResult.PASS;
	}

	private InteractionResult onShapeActivated(Direction face, ElementPipeBlockEntity pipe, Player player, InteractionHand hand) {
		if (face != null) {
			ItemStack stack = player.getItemInHand(hand);

			if (!stack.isEmpty() && stack.getItem() == ECItems.PIPE_PRIORITY.get()) {
				return pipe.activatePriority(face, player, hand);
			}
			return pipe.activatePipe(face);
		}
		return InteractionResult.PASS;
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			var te = level.getBlockEntity(pos);

			if (te  instanceof ElementPipeBlockEntity pipe) {
				if (isCovered(state)) {
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(pipe.getCoverState().getBlock()));
				}
				pipe.dropAllPriorities();
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	private static ElementPipeBlockEntity getBlockEntity(BlockGetter world, BlockPos pos) {
		return BlockEntityHelper.getBlockEntityAs(world, pos, ElementPipeBlockEntity.class).orElse(null);
	}

	public PipeType getType() {
		return type;
	}

	public enum CoverType implements StringRepresentable {
		NONE("none"), FRAME("frame"), COVERED("covered");

		public static final Codec<CoverType> CODEC = StringRepresentable.fromEnum(CoverType::values);

		private final String name;

		CoverType(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static CoverType byName(String name) {
			for (CoverType bonusType : values()) {
				if (bonusType.name.equals(name)) {
					return bonusType;
				}
			}
			return NONE;
		}
	}

	public enum PipeType {
		IMPAIRED, STANDARD, IMPROVED, CREATIVE;
	}

}
