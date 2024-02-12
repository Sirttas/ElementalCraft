package sirttas.elementalcraft.block.instrument.io.mill.grindstone.water;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class WaterMillGrindstoneBlockEntity extends AbstractMillGrindstoneBlockEntity {

	private static final Config<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.WATER_MILL_GRINDSTONE,
			ECRecipeTypes.GRINDING,
			ECConfig.COMMON.waterMillsTransferSpeed,
			ECConfig.COMMON.waterMillsMaxRunes,
			1,
			false,
			false
	);

	public WaterMillGrindstoneBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.WATER, pos, state);
	}
}
