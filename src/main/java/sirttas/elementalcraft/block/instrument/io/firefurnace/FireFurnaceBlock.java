package sirttas.elementalcraft.block.instrument.io.firefurnace;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FireFurnaceBlock extends AbstractFireFurnaceBlock {

	public static final String NAME = "firefurnace";

	private static final VoxelShape OVEN_SLAB = Block.box(0D, 2D, 0D, 16D, 12D, 16D);
	private static final VoxelShape TOP_BOWL = Block.box(3D, 11D, 3D, 13D, 12D, 13D);
	private static final VoxelShape MIDDLE_1 = Block.box(0D, 4D, 3D, 16D, 10D, 13D);
	private static final VoxelShape MIDDLE_2 = Block.box(3D, 4D, 0D, 13D, 10D, 16D);
	private static final VoxelShape EMPTY_SPACE = Shapes.or(TOP_BOWL, MIDDLE_1, MIDDLE_2);
	private static final VoxelShape OVEN = Shapes.join(OVEN_SLAB, EMPTY_SPACE, BooleanOp.ONLY_FIRST);
	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.box(1D, 0D, 1D, 3D, 2D, 3D);
	private static final VoxelShape PILLAT_2 = Block.box(13D, 0D, 1D, 15D, 2D, 3D);
	private static final VoxelShape PILLAT_3 = Block.box(1D, 0D, 13D, 3D, 2D, 15D);
	private static final VoxelShape PILLAT_4 = Block.box(13D, 0D, 13D, 15D, 2D, 15D);
	private static final VoxelShape SHAPE = Shapes.or(OVEN, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4);

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FireFurnaceBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, FireFurnaceBlockEntity.TYPE);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
