package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.container.ECContainerHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class AbstractMillBlock extends AbstractECContainerBlock implements IInstrumentBlock {

	private static final VoxelShape OVEN_SLAB = Block.box(3D, 2D, 1D, 13D, 4D, 15D);
	private static final VoxelShape OVEN_SLAB_2 = Block.box(3D, 10D, 1D, 13D, 12D, 15D);
	private static final VoxelShape OVEN_CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape OVEN_PILLAR_1 = Block.box(4D, 4D, 2D, 6D, 10D, 4D);
	private static final VoxelShape OVEN_PILLAR_2 = Block.box(10D, 4D, 2D, 12D, 10D, 4D);
	private static final VoxelShape OVEN_PILLAR_3 = Block.box(4D, 4D, 12D, 6D, 10D, 14D);
	private static final VoxelShape OVEN_PILLAR_4 = Block.box(10D, 4D, 12D, 12D, 10D, 14D);
	private static final VoxelShape OVEN_SHAFT = Block.box(7D, 4D, 7D, 9D, 10D, 9D);
	protected static final VoxelShape SHAPE_NORTH = Shapes.or(OVEN_SLAB, OVEN_SLAB_2, OVEN_CONNECTION, OVEN_PILLAR_1, OVEN_PILLAR_2, OVEN_PILLAR_3, OVEN_PILLAR_4, OVEN_SHAFT);
	protected static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Direction.NORTH, SHAPE_NORTH);


	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	protected AbstractMillBlock() {
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(WATERLOGGED, false));
	}

	@Nonnull
    @Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		var mill = (AbstractMillGrindstoneBlockEntity) world.getBlockEntity(pos);
		var inv = ECContainerHelper.getItemHandlerAt(world, pos, null);
		var heldItem = player.getItemInHand(hand);

		if (mill != null && hand == InteractionHand.MAIN_HAND) {
			if (!mill.getInventory().getItem(1).isEmpty()) {
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			}
			return this.onSlotActivated(inv, player, heldItem, 0);
		}
		return InteractionResult.PASS;
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, WaterLoggingHelper.isPlacedInWater(context));
	}

	@Nonnull
    @Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Nonnull
    @Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, FACING);
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state, level, pos.below());
	}
	
	@Nonnull
    @Override
	@Deprecated
	public FluidState getFluidState(@Nonnull BlockState state) {
		return WaterLoggingHelper.isWaterlogged(state) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}
}
