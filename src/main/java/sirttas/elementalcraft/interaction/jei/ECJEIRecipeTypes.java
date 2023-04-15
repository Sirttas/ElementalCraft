package sirttas.elementalcraft.interaction.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlock;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlock;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.interaction.jei.category.element.CrystalThrowingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.DisplacementRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.EvaporationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ImprovedExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.SolarSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.SourceBreedingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.PurificationRecipeCategory;
import sirttas.elementalcraft.item.elemental.ElementalItem;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class ECJEIRecipeTypes {

    private ECJEIRecipeTypes() {}

    public static final RecipeType<ElementType> EXTRACTION = create(ExtractionRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<ElementType> EXTRACTION_IMPROVED = create(ImprovedExtractionRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<Ingredient> EVAPORATION = create(EvaporationRecipeCategory.NAME, Ingredient.class);
    public static final RecipeType<Ingredient> SOLAR_SYNTHESIS = create(SolarSynthesisRecipeCategory.NAME, Ingredient.class);
    public static final RecipeType<IInfusionRecipe> INFUSION = create(IInfusionRecipe.NAME, IInfusionRecipe.class);
    public static final RecipeType<IInfusionRecipe> TOOL_INFUSION = create(ToolInfusionRecipe.NAME, IInfusionRecipe.class);
    public static final RecipeType<AbstractBindingRecipe> BINDING = create(AbstractBindingRecipe.NAME, AbstractBindingRecipe.class);
    public static final RecipeType<CrystallizationRecipe> CRYSTALLIZATION = create(CrystallizationRecipe.NAME, CrystallizationRecipe.class);
    public static final RecipeType<InscriptionRecipe> INSCRIPTION = create(InscriptionRecipe.NAME, InscriptionRecipe.class);
    public static final RecipeType<PureInfusionRecipe> PURE_INFUSION = create(PureInfusionRecipe.NAME, PureInfusionRecipe.class);
    public static final RecipeType<IPurifierRecipe> PURIFICATION = create(PurificationRecipeCategory.NAME, IPurifierRecipe.class);
    public static final RecipeType<IGrindingRecipe> GRINDING = create(IGrindingRecipe.NAME, IGrindingRecipe.class);
    public static final RecipeType<SawingRecipe> SAWING = create(SawingRecipe.NAME, SawingRecipe.class);
    public static final RecipeType<SpellCraftRecipe> SPELL_CRAFTING = create(SpellCraftRecipe.NAME, SpellCraftRecipe.class);
    public static final RecipeType<ElementType> DISPLACEMENT = create(DisplacementRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<BuddingShrineBlock.CrystalType> BUDDING_SHRINE = create(BuddingShrineBlock.NAME, BuddingShrineBlock.CrystalType.class);
    public static final RecipeType<LavaShrineBlock> LAVA_SHRINE = create(LavaShrineBlock.NAME, LavaShrineBlock.class);
    public static final RecipeType<SpringShrineBlock> SPRING_SHRINE = create(SpringShrineBlock.NAME, SpringShrineBlock.class);
    public static final RecipeType<ElementType> CRYSTAL_THROWING = create(CrystalThrowingRecipeCategory.NAME, ElementType.class);
    public static final RecipeType<ElementalItem> SOURCE_BREEDING = create(SourceBreedingRecipeCategory.NAME, ElementalItem.class);

    private static <T> RecipeType<T> create(String path, Class<? extends T> recipeClass) {
        return RecipeType.create(ElementalCraftApi.MODID, path, recipeClass);
    }

}
