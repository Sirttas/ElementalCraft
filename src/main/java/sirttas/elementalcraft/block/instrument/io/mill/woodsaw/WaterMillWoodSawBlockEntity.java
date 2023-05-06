package sirttas.elementalcraft.block.instrument.io.mill.woodsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class WaterMillWoodSawBlockEntity extends AbstractMillBlockEntity<WaterMillWoodSawBlockEntity, SawingRecipe> {

	private static final Config<WaterMillWoodSawBlockEntity, SawingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.WATER_MILL_WOOD_SAW,
			ECRecipeTypes.SAWING,
			ECConfig.COMMON.waterMillWoodSawTransferSpeed,
			ECConfig.COMMON.waterMillWoodSawMaxRunes
	);

	public WaterMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.WATER, pos, state);
	}
}
