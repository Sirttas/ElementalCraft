package sirttas.elementalcraft.interaction.jei;

import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlock;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineBlock;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlock;
import sirttas.elementalcraft.interaction.jei.category.element.ExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.AirMillSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.CombustionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.CulinaryRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.DrainingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.SolarSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.VibrationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.EnchantmentLiquefactionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.PurificationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.source.DisplacementRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.source.SourceBreedingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.elemental.ElementalItem;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction.EnchantmentLiquefactionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipe;

public class ECJEIRecipeTypes {

    private ECJEIRecipeTypes() {}

    public static final IRecipeType<ExtractionRecipeCategory.@NotNull ExtractionRecipe> EXTRACTION = create(ExtractionRecipeCategory.NAME, ExtractionRecipeCategory.ExtractionRecipe.class);
    public static final IRecipeType<@NotNull CrackingRecipe> CRACKING = create(CrackingRecipe.NAME, CrackingRecipe.class);
    public static final IRecipeType<@NotNull SculkCrackingRecipe> SCULK_CRACKING = create(SculkCrackingRecipe.NAME, SculkCrackingRecipe.class);
    public static final IRecipeType<@NotNull IJeiFuelingRecipe> COMBUSTION = create(CombustionRecipeCategory.NAME, IJeiFuelingRecipe.class);
    public static final IRecipeType<@NotNull Ingredient> SOLAR_SYNTHESIS = create(SolarSynthesisRecipeCategory.NAME, Ingredient.class);
    public static final IRecipeType<@NotNull IngredientElementType> DRAINING = create(DrainingRecipeCategory.NAME, IngredientElementType.class);
    public static final IRecipeType<@NotNull ItemStack> CULINARY = create(CulinaryRecipeCategory.NAME, ItemStack.class);
    public static final IRecipeType<@NotNull IngredientElementType> VIBRATION = create(VibrationRecipeCategory.NAME, IngredientElementType.class);
    public static final IRecipeType<@NotNull IngredientElementType> AIR_MILL_SYNTHESIS = create(AirMillSynthesisRecipeCategory.NAME, IngredientElementType.class);
    public static final IRecipeType<@NotNull InfusionRecipe> INFUSION = create(InfusionRecipe.NAME, InfusionRecipe.class);
    public static final IRecipeType<@NotNull InfusionRecipe> TOOL_INFUSION = create(ToolInfusionRecipe.NAME, InfusionRecipe.class);
    public static final IRecipeType<@NotNull AbstractBindingRecipe> BINDING = create(AbstractBindingRecipe.NAME, AbstractBindingRecipe.class);
    public static final IRecipeType<@NotNull CrystallizationRecipe> CRYSTALLIZATION = create(CrystallizationRecipe.NAME, CrystallizationRecipe.class);
    public static final IRecipeType<@NotNull InscriptionRecipe> INSCRIPTION = create(InscriptionRecipe.NAME, InscriptionRecipe.class);
    public static final IRecipeType<EnchantmentLiquefactionRecipeCategory.@NotNull RecipeWrapper> ENCHANTMENT_LIQUEFACTION = create(EnchantmentLiquefactionRecipe.NAME, EnchantmentLiquefactionRecipeCategory.RecipeWrapper.class);
    public static final IRecipeType<@NotNull PureInfusionRecipe> PURE_INFUSION = create(PureInfusionRecipe.NAME, PureInfusionRecipe.class);
    public static final IRecipeType<@NotNull OrePurificationRecipe> ORE_PURIFICATION = create(PurificationRecipeCategory.NAME, OrePurificationRecipe.class);
    public static final IRecipeType<@NotNull GrindingRecipe> GRINDING = create(GrindingRecipe.NAME, GrindingRecipe.class);
    public static final IRecipeType<@NotNull SawingRecipe> SAWING = create(SawingRecipe.NAME, SawingRecipe.class);
    public static final IRecipeType<@NotNull SpellCraftRecipe> SPELL_CRAFTING = create(SpellCraftRecipe.NAME, SpellCraftRecipe.class);
    public static final IRecipeType<@NotNull ElementType> DISPLACEMENT = create(DisplacementRecipeCategory.NAME, ElementType.class);
    public static final IRecipeType<@NotNull BuddingShrineBudType> BUDDING_SHRINE = create(BuddingShrineBlock.NAME, BuddingShrineBudType.class);
    public static final IRecipeType<@NotNull MeltingRecipe> MELTING_SHRINE = create(MeltingShrineBlock.NAME, MeltingRecipe.class);
    public static final IRecipeType<@NotNull SpringShrineBlock> SPRING_SHRINE = create(SpringShrineBlock.NAME, SpringShrineBlock.class);
    public static final IRecipeType<@NotNull ElementalItem> SOURCE_BREEDING = create(SourceBreedingRecipeCategory.NAME, ElementalItem.class);

    private static <T> IRecipeType<@NotNull T> create(String path, Class<? extends T> recipeClass) {
        return IRecipeType.create(ElementalCraftApi.MODID, path, recipeClass);
    }

}
