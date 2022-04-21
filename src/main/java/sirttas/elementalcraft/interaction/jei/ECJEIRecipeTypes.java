package sirttas.elementalcraft.interaction.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.category.element.EvaporationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ImprovedExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.SolarSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.PurificationRecipeCategory;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class ECJEIRecipeTypes {

    private ECJEIRecipeTypes() {}

    public static final RecipeType<ElementType> EXTRACTION = create(ExtractionRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<ElementType> EXTRACTION_IMPROVED = create(ImprovedExtractionRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<Ingredient> EVAPORATION = create(EvaporationRecipeCategory.NAME, Ingredient.class);
    public static final RecipeType<Ingredient> SOLAR_SYNTHESIS = create(SolarSynthesisRecipeCategory.NAME, Ingredient.class);
    public static final RecipeType<IInfusionRecipe> INFUSION = create(IInfusionRecipe.NAME, IInfusionRecipe.class);
    public static final RecipeType<AbstractBindingRecipe> BINDING = create(AbstractBindingRecipe.NAME, AbstractBindingRecipe.class);
    public static final RecipeType<CrystallizationRecipe> CRYSTALLIZATION = create(CrystallizationRecipe.NAME, CrystallizationRecipe.class);
    public static final RecipeType<InscriptionRecipe> INSCRIPTION = create(InscriptionRecipe.NAME, InscriptionRecipe.class);
    public static final RecipeType<PureInfusionRecipe> PURE_INFUSION = create(PureInfusionRecipe.NAME, PureInfusionRecipe.class);
    public static final RecipeType<IPurifierRecipe> PURIFICATION = create(PurificationRecipeCategory.NAME, IPurifierRecipe.class);
    public static final RecipeType<IGrindingRecipe> GRINDING = create(IGrindingRecipe.NAME, IGrindingRecipe.class);
    public static final RecipeType<SpellCraftRecipe> SPELL_CRAFTING = create(SpellCraftRecipe.NAME, SpellCraftRecipe.class);

    private static <T> RecipeType<T> create(String path, Class<? extends T> recipeClass) {
        return RecipeType.create(ElementalCraftApi.MODID, path, recipeClass);
    }

}
