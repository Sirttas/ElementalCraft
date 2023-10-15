package sirttas.elementalcraft.block.instrument.io.mill.grindstone.air;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AirMillGrindstoneBlock extends AbstractAirMillBlock {

	public static final String NAME = "air_mill_grindstone";

	private static final VoxelShape SHAPE_LOWER = Shapes.or(AbstractAirMillBlock.SHAPE_LOWER, Block.box(4D, 5D, 4D, 12D, 8D, 12D));

	public AirMillGrindstoneBlock() {
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(HALF, DoubleBlockHalf.LOWER)
				.setValue(WATERLOGGED, false));
	}


	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return isLower(state) ? new AirMillGrindstoneBlockEntity(pos, state) : null;
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return isLower(state) ? createInstrumentTicker(level, type, ECBlockEntityTypes.AIR_MILL_GRINDSTONE) : null;
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return isLower(state) ? SHAPE_LOWER : SHAPE_UPPER;
	}

}
