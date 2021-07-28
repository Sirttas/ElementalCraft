package sirttas.elementalcraft.block.pureinfuser;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.AbstractECContainerBlock;

public class PureInfuserBlock extends AbstractECContainerBlock {

	private static final VoxelShape BASE_1 = Block.box(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.box(2D, 3D, 2D, 14D, 9D, 14D);
	private static final VoxelShape BASE_3 = Block.box(0D, 9D, 0D, 16D, 12D, 16D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 3D, 1D, 3D, 16D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 3D, 1D, 15D, 16D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 3D, 13D, 3D, 16D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 3D, 13D, 15D, 16D, 15D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public static final String NAME = "pureinfuser";

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PureInfuserBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECTicker(level, type, PureInfuserBlockEntity.TYPE, PureInfuserBlockEntity::tick);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return onSingleSlotActivated(world, pos, player, hand);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}