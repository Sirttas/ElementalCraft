package sirttas.elementalcraft.recipe.display;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.binding.BinderRecipeDisplay;
import sirttas.elementalcraft.recipe.instrument.binding.BindingRecipe;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipeDisplay;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipeDisplay;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipeDisplay;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipeDisplay;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipe;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipeDisplay;

public class ECRecipeDisplayTypes {

    private static final DeferredRegister<RecipeDisplay.Type<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_DISPLAY, ElementalCraftApi.MODID);

    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<BinderRecipeDisplay>> BINDING = register(BindingRecipe.NAME, BinderRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<CrystallizationRecipeDisplay>> CRYSTALLIZATION = register(CrystallizationRecipe.NAME, CrystallizationRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<InscriptionRecipeDisplay>> INSCRIPTION = register(InscriptionRecipe.NAME, InscriptionRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<IOInstrumentRecipeDisplay>> IO_INSTRUMENT = register(IOInstrumentRecipeDisplay.NAME, IOInstrumentRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<PureInfusionRecipeDisplay>> PURE_INFUSION = register(PureInfusionRecipe.NAME, PureInfusionRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<SpellCraftRecipeDisplay>> SPELL_CRAFT = register(SpellCraftRecipe.NAME, SpellCraftRecipeDisplay.TYPE);

    private ECRecipeDisplayTypes() {}

    private static <T extends RecipeDisplay> DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<T>> register(String name, RecipeDisplay.Type<T> type) {
        return DEFERRED_REGISTER.register(name, () -> type);
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }

}
