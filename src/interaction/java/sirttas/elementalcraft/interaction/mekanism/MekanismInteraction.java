package sirttas.elementalcraft.interaction.mekanism;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.basic.BasicCrushingRecipe;
import mekanism.api.recipes.basic.BasicEnrichingRecipe;
import mekanism.api.recipes.basic.BasicInjectingRecipe;
import mekanism.api.recipes.basic.BasicPurifyingRecipe;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftInteraction;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactory;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.mekanism.injector.ChemicalDissolutionPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackGasToItemStackPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackToItemStackPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.mekanism.recipe.MekanismCrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.factory.PureOreRecipeFactoryTypes;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class MekanismInteraction implements ElementalCraftInteraction {

    @Override
    public boolean isActive() {
        return ModList.get().isLoaded("mekanism");
    }

    @Override
	public void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {
		register(registry, MekanismRecipeType.DISSOLUTION, ChemicalDissolutionPureOreRecipeFactory::new);
		register(registry, MekanismRecipeType.INJECTING, (m, t) -> new ItemStackGasToItemStackPureOreRecipeFactory<>(m, t, BasicInjectingRecipe::new));
		register(registry, MekanismRecipeType.PURIFYING, (m, t) -> new ItemStackGasToItemStackPureOreRecipeFactory<>(m, t, BasicPurifyingRecipe::new));
		register(registry, MekanismRecipeType.ENRICHING, (m, t) -> new ItemStackToItemStackPureOreRecipeFactory<>(m, t, BasicEnrichingRecipe::new));
		register(registry, MekanismRecipeType.CRUSHING, (m, t) -> new ItemStackToItemStackPureOreRecipeFactory<>(m, t, BasicCrushingRecipe::new));
	}

	private static <I extends RecipeInput, T extends MekanismRecipe<I>, C extends IInputRecipeCache> void register(RegisterEvent.RegisterHelper<IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, RecipeTypeRegistryObject<I, T, C> type, BiFunction<RecipeManager,  RecipeTypeRegistryObject<I, T, C>, IPureOreRecipeFactory<I, T>> factory) {
		PureOreRecipeFactoryTypes.register(registry, type.getRegistryName(), m -> factory.apply(m, type));
	}

	@Override
	public <I extends RecipeInput, T extends Recipe<I>> T lookupRecipe(@NotNull Level level, @NotNull RecipeType<T> type, @NotNull I recipeInput) {
		if (type == ECRecipeTypes.GRINDING.get()) {
			return (T) lookupGrindingRecipe(level, (SimpleIOInstrumentRecipeInput) recipeInput);
		}
		return null;
	}

	public GrindingRecipe lookupGrindingRecipe(@NotNull Level level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
		var stack = recipeInput.getItem(0);
		var crusherRecipe = MekanismRecipeType.CRUSHING.findFirst(level, recipe -> recipe.test(stack));
		var wrapper = crusherRecipe != null ? new MekanismCrusherRecipeWrapper(crusherRecipe) : null;

		return wrapper != null && wrapper.matches(recipeInput, level) ? wrapper : null;
	}

    public void addCraftingStations(BiConsumer<Object, ItemStack> consumer) {
        consumer.accept(MekanismJEI.holderRecipeType(RecipeViewerRecipeType.CRUSHING), new ItemStack(ECBlocks.WATER_MILL_GRINDSTONE.get()));
        consumer.accept(MekanismJEI.holderRecipeType(RecipeViewerRecipeType.CRUSHING), new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()));
    }
}
