package sirttas.elementalcraft.recipe.display;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
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

    private static final DeferredRegister<RecipeDisplay.@NotNull Type<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_DISPLAY, ElementalCraftApi.MODID);

    public static final DeferredHolder<RecipeDisplay.@NotNull Type<?>, RecipeDisplay.@NotNull Type<@NotNull BinderRecipeDisplay>> BINDING = register(BindingRecipe.NAME, BinderRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.@NotNull Type<?>, RecipeDisplay.@NotNull Type<@NotNull CrystallizationRecipeDisplay>> CRYSTALLIZATION = register(CrystallizationRecipe.NAME, CrystallizationRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.@NotNull Type<?>, RecipeDisplay.@NotNull Type<@NotNull InscriptionRecipeDisplay>> INSCRIPTION = register(InscriptionRecipe.NAME, InscriptionRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.@NotNull Type<?>, RecipeDisplay.@NotNull Type<@NotNull IOInstrumentRecipeDisplay>> IO_INSTRUMENT = register(IOInstrumentRecipeDisplay.NAME, IOInstrumentRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.@NotNull Type<?>, RecipeDisplay.@NotNull Type<@NotNull PureInfusionRecipeDisplay>> PURE_INFUSION = register(PureInfusionRecipe.NAME, PureInfusionRecipeDisplay.TYPE);
    public static final DeferredHolder<RecipeDisplay.@NotNull Type<?>, RecipeDisplay.@NotNull Type<@NotNull SpellCraftRecipeDisplay>> SPELL_CRAFT = register(SpellCraftRecipe.NAME, SpellCraftRecipeDisplay.TYPE);

    private ECRecipeDisplayTypes() {}

    private static <T extends RecipeDisplay> DeferredHolder<RecipeDisplay.@NotNull Type<?>, RecipeDisplay.@NotNull Type<@NotNull T>> register(String name, RecipeDisplay.@NotNull Type<@NotNull T> type) {
        return DEFERRED_REGISTER.register(name, () -> type);
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }

}
