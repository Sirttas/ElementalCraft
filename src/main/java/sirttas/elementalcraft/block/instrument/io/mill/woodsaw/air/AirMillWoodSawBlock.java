package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AirMillWoodSawBlock extends AbstractAirMillBlock {

	public static final String NAME = "air_mill_wood_saw";

	public AirMillWoodSawBlock() {
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(HALF, DoubleBlockHalf.LOWER)
				.setValue(WATERLOGGED, false));
	}


	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return isLower(state) ? new AirMillWoodSawBlockEntity(pos, state) : null;
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return isLower(state) ? createInstrumentTicker(level, type, ECBlockEntityTypes.AIR_MILL_WOOD_SAW) : null;
	}
}
