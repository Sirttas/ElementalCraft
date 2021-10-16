package sirttas.elementalcraft.block.sorter;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

public class SorterBlock extends AbstractECEntityBlock implements ISorterBlock {

	public static final String NAME = "sorter";

	private static final VoxelShape CORE_VOID = Shapes.or(Block.box(5D, 6D, 6D, 11D, 10D, 10D), Block.box(6D, 5D, 6D, 10D, 11D, 10D),
			Block.box(6D, 6D, 5D, 10D, 10D, 11D));
	private static final VoxelShape CORE = Shapes.or(Shapes.join(Block.box(5D, 5D, 5D, 11D, 11D, 11D), CORE_VOID, BooleanOp.ONLY_FIRST),
			Block.box(6D, 6D, 6D, 10D, 10D, 10D));


	public SorterBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(SOURCE, Direction.SOUTH).setValue(TARGET, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(SOURCE, direction.getOpposite()).setValue(TARGET, direction);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SorterBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECServerTicker(level, type, SorterBlockEntity.TYPE, SorterBlockEntity::serverTick);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET);
	}

	@Override
	public VoxelShape getCoreShape(BlockState state) {
		return CORE;
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return worldIn instanceof Level && ((Level) worldIn).isClientSide ? getShape(state, pos, Minecraft.getInstance().hitResult) : getCurentShape(state);
	}
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return getCurentShape(state);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		VoxelShape shape = getShape(state, pos, hit);

		if (CORE.equals(shape)) {
			return BlockEntityHelper.getBlockEntityAs(world, pos, SorterBlockEntity.class).map(sorter -> sorter.addStack(player.getItemInHand(hand))).orElse(InteractionResult.PASS);
		} 
		return this.moveIO(state, world, pos, hit, shape);
	}
}
