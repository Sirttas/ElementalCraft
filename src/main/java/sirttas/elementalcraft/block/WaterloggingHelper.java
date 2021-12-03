package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class WaterloggingHelper {

	private WaterloggingHelper() {}
	
	public static boolean isWaterlogged(BlockState state) {
		return state.getOptionalValue(BlockStateProperties.WATERLOGGED).orElse(false);
	}
	
	public static void sheduleWaterTick(BlockState state, LevelAccessor level, BlockPos pos) {
		if (isWaterlogged(state)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
	}
	
	public static Boolean isPlacedInWater(BlockPlaceContext context) {
		return Boolean.valueOf(context.getLevel().getFluidState(context.getClickedPos()).is(FluidTags.WATER));
	}
	
}
