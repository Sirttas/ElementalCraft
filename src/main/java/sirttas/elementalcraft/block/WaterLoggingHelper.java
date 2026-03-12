package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class WaterLoggingHelper {

	private WaterLoggingHelper() {}
	
	public static boolean isWaterlogged(BlockState state) {
		return state.getOptionalValue(BlockStateProperties.WATERLOGGED).orElse(false);
	}
	
	public static void scheduleWaterTick(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos) {
		if (isWaterlogged(state)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
	}
	
	public static boolean isPlacedInWater(BlockPlaceContext context) {
		return context.getLevel().getFluidState(context.getClickedPos()).is(FluidTags.WATER);
	}
	
}
