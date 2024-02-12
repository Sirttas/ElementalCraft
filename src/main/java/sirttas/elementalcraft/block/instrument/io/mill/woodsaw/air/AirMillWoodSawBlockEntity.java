package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.AbstractMillWoodSawBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class AirMillWoodSawBlockEntity extends AbstractMillWoodSawBlockEntity {

	private static final Config<AbstractMillWoodSawBlockEntity, SawingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.AIR_MILL_WOOD_SAW,
			ECRecipeTypes.SAWING,
			ECConfig.COMMON.airMillsTransferSpeed,
			ECConfig.COMMON.airMillsMaxRunes,
			1,
			false,
			false
	);

	public AirMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.AIR, pos, state);
	}
}
