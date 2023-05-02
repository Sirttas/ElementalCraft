package sirttas.elementalcraft.block.instrument.io.firefurnace;

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
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FireFurnaceBlock extends AbstractFireFurnaceBlock {

	public static final String NAME = "firefurnace";

	private static final VoxelShape OVEN_SLAB = Block.box(3D, 2D, 3D, 13D, 12D, 13D);
	private static final VoxelShape TOP_BOWL = Block.box(5D, 11D, 5D, 11D, 12D, 11D);
	private static final VoxelShape MIDDLE_1 = Block.box(3D, 4D, 5D, 13D, 10D, 11D);
	private static final VoxelShape MIDDLE_2 = Block.box(5D, 4D, 3D, 11D, 10D, 13D);
	private static final VoxelShape EMPTY_SPACE = Shapes.or(TOP_BOWL, MIDDLE_1, MIDDLE_2);
	private static final VoxelShape OVEN = Shapes.join(OVEN_SLAB, EMPTY_SPACE, BooleanOp.ONLY_FIRST);
	private static final VoxelShape BOTTOM = Block.box(5D, 1D, 5D, 11D, 2D, 11D);
	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape SHAPE = Shapes.or(OVEN, BOTTOM, CONNECTION);

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new FireFurnaceBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.FIRE_FURNACE);
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
