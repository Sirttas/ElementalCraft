package sirttas.elementalcraft.block.instrument.io.mill.grindstone.air;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class AirMillGrindstoneBlockEntity extends AbstractMillGrindstoneBlockEntity {

	private static final Config<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.AIR_MILL_GRINDSTONE,
			ECRecipeTypes.GRINDING,
			ECConfig.COMMON.airMillsTransferSpeed,
			ECConfig.COMMON.airMillsMaxRunes,
			1,
			false
	);

	public AirMillGrindstoneBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.AIR, pos, state);
	}
}
