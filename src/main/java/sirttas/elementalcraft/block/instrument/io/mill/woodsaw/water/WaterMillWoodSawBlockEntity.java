package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.AbstractMillWoodSawBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class WaterMillWoodSawBlockEntity extends AbstractMillWoodSawBlockEntity {

	private static final Config<AbstractMillWoodSawBlockEntity, SawingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.WATER_MILL_WOOD_SAW,
			ECRecipeTypes.SAWING,
			ECConfig.COMMON.waterMillsTransferSpeed,
			ECConfig.COMMON.waterMillsMaxRunes,
			1,
			false
	);

	public WaterMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.WATER, pos, state);
	}
}
