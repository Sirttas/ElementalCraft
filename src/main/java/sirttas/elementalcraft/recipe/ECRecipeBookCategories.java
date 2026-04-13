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
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipe;

public class ECRecipeBookCategories {
    private static final DeferredRegister<@NotNull RecipeBookCategory> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, ElementalCraftApi.MODID);

    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> INFUSION = register(InfusionRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> TOOL_INFUSION = register(ToolInfusionRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> BINDING = register(AbstractBindingRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> CRYSTALLIZATION = register(CrystallizationRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> INSCRIPTION = register(InscriptionRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> ORE_PURIFICATION = register(OrePurificationRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> GRINDING = register(GrindingRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> SAWING = register(SawingRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> PURE_INFUSION = register(PureInfusionRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> SPELL_CRAFT = register(SpellCraftRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> CRACKING = register(CrackingRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> SCULK_CRACKING = register(SculkCrackingRecipe.NAME);
    public static final DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> MELTING = register(MeltingRecipe.NAME);

    private static DeferredHolder<@NotNull RecipeBookCategory, @NotNull RecipeBookCategory> register(String id) {
        return DEFERRED_REGISTER.register(id, RecipeBookCategory::new);
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
