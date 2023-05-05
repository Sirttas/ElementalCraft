package sirttas.elementalcraft.block.instrument.io.mill.grindstone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.ie.IEInteraction;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class AirMillGrindstoneBlockEntity extends AbstractMillBlockEntity<AirMillGrindstoneBlockEntity, IGrindingRecipe> {

	private static final Config<AirMillGrindstoneBlockEntity, IGrindingRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.AIR_MILL_GRINDSTONE,
			ECRecipeTypes.GRINDING,
			ECConfig.COMMON.airMillGrindstoneTransferSpeed,
			ECConfig.COMMON.airMillGrindstoneMaxRunes
	);

	public AirMillGrindstoneBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, ElementType.AIR, pos, state);
	}

	@Override
	protected IGrindingRecipe lookupRecipe() {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}

		var recipe = super.lookupRecipe();
		
		if (recipe == null && ECinteractions.isMekanismActive()) {
			recipe = MekanismInteraction.lookupCrusherRecipe(level, this);
		}
		if (recipe == null && ECinteractions.isImmersiveEngineeringActive()) {
			recipe = IEInteraction.lookupCrusherRecipe(level, this);
		}
		return recipe;
	}
}
