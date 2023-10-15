package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaterMillWoodSawBlock extends AbstractMillBlock {

	public static final String NAME = "water_mill_wood_saw";

	public WaterMillWoodSawBlock() {
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(WATERLOGGED, false));
	}


	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new WaterMillWoodSawBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.WATER_MILL_WOOD_SAW);
	}

}
