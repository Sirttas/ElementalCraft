package sirttas.elementalcraft.block.instrument.infuser;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;

public class InfuserBlock extends AbstractECContainerBlock {

	public static final String NAME = "infuser";

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(4D, 1D, 4D, 12D, 2D, 12D);

	private static final VoxelShape PIPE_1 = Block.box(3D, 0D, 3D, 5D, 4D, 5D);
	private static final VoxelShape PIPE_2 = Block.box(11D, 0D, 3D, 13D, 4D, 5D);
	private static final VoxelShape PIPE_3 = Block.box(3D, 0D, 11D, 5D, 4D, 13D);
	private static final VoxelShape PIPE_4 = Block.box(11D, 0D, 11D, 13D, 4D, 13D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InfuserBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECTicker(level, type, InfuserBlockEntity.TYPE, AbstractInstrumentBlockEntity::tick);
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
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}
