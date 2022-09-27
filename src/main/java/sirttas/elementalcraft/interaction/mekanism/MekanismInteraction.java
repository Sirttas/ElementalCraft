package sirttas.elementalcraft.interaction.mekanism;

import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.impl.CrushingIRecipe;
import mekanism.common.recipe.impl.EnrichingIRecipe;
import mekanism.common.recipe.impl.InjectingIRecipe;
import mekanism.common.recipe.impl.PurifyingIRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.mekanism.injector.ChemicalDissolutionPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackGasToItemStackPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackToItemStackPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.recipe.MekanismCrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.injector.PureOreRecipeInjectors;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class MekanismInteraction {

	private MekanismInteraction() {}
	
	public static void registerPureOreRecipeInjectors(IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry) {
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreDissolutionRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ChemicalDissolutionPureOreRecipeInjector(MekanismRecipeType.DISSOLUTION));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreInjectingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackGasToItemStackPureOreRecipeInjector<>(MekanismRecipeType.INJECTING, InjectingIRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOrePurifyingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackGasToItemStackPureOreRecipeInjector<>(MekanismRecipeType.PURIFYING, PurifyingIRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreEnrichingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackToItemStackPureOreRecipeInjector<>(MekanismRecipeType.ENRICHING, EnrichingIRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.mekanismPureOreCrushingRecipe.get())) {
			PureOreRecipeInjectors.register(registry, new ItemStackToItemStackPureOreRecipeInjector<>(MekanismRecipeType.CRUSHING, CrushingIRecipe::new));
		}
	}

	public static IGrindingRecipe lookupCrusherRecipe(Level level, AirMillBlockEntity airMillGrindstone) {
		var stack = airMillGrindstone.getInventory().getItem(0);
		var crusherRecipe = MekanismRecipeType.CRUSHING.findFirst(level, recipe -> recipe.test(stack));
		var wrapper = crusherRecipe != null ? new MekanismCrusherRecipeWrapper(crusherRecipe) : null;

		return wrapper != null && wrapper.matches(airMillGrindstone) ? wrapper : null;
	}
}
