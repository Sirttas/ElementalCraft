package sirttas.elementalcraft.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class ECRecipeBookCategories {
    private static final DeferredRegister<@NotNull RecipeBookCategory> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, ElementalCraftApi.MODID);

    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> INFUSION = register(IInfusionRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> BINDING = register(AbstractBindingRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> CRYSTALLIZATION = register(CrystallizationRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> INSCRIPTION = register(InscriptionRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> SPELL_CRAFT = register(SpellCraftRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> CRACKING = register(CrackingRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> SCULK_CRACKING = register(SculkCrackingRecipe.NAME);

    private static DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> register(String id) {
        return DEFERRED_REGISTER.register(id, RecipeBookCategory::new);
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
