package sirttas.elementalcraft.interaction.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.util.Lazy;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.jei.category.PureInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.SpellCraftRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.EvaporationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ImprovedExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.SolarSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.BindingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.CrystallizationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.InscriptionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.GrindingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.InfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.PurificationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientHelper;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientRenderer;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

@JeiPlugin
public class ElementalCraftJEIPlugin implements IModPlugin {

	private static final Lazy<HolderSet.Named<Item>> SPELL_CAST_TOOLS = Lazy.of(() -> ECTags.Items.getTag(ECTags.Items.SPELL_CAST_TOOLS));
	private static final Lazy<HolderSet.Named<Item>> JEWEL_SOCKETALBES = Lazy.of(() -> ECTags.Items.getTag(ECTags.Items.JEWEL_SOCKETABLES));
	private static final ResourceLocation ID = ElementalCraft.createRL("main");
	
	@Nonnull
	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(ECIngredientTypes.ELEMENT, IngredientElementType.all(), new ElementIngredientHelper(), new ElementIngredientRenderer());
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registry) {
		registry.useNbtForSubtypes(ECItems.SCROLL);
		registry.useNbtForSubtypes(ECItems.RECEPTACLE, ECItems.RECEPTACLE_IMPROVED);
		registry.useNbtForSubtypes(ECItems.PURE_ORE);
		registry.useNbtForSubtypes(ECItems.RUNE);
		registry.useNbtForSubtypes(ECItems.JEWEL);
		registry.useNbtForSubtypes(ECItems.TANK, ECItems.TANK_SMALL, ECItems.TANK_CREATIVE);
		registry.useNbtForSubtypes(ECItems.FIRE_RESERVOIR, ECItems.WATER_RESERVOIR, ECItems.EARTH_RESERVOIR, ECItems.AIR_RESERVOIR);
		registry.useNbtForSubtypes(ECItems.FIRE_HOLDER, ECItems.WATER_HOLDER, ECItems.EARTH_HOLDER, ECItems.AIR_HOLDER, ECItems.PURE_HOLDER);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ExtractionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ImprovedExtractionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new EvaporationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SolarSynthesisRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CrystallizationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InscriptionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PureInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PurificationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new GrindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SpellCraftRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ECItems.FIRE_FURNACE), RecipeTypes.SMELTING);
		registry.addRecipeCatalyst(new ItemStack(ECItems.FIRE_BLAST_FURNACE), RecipeTypes.BLASTING);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EXTRACTOR), ECJEIRecipeTypes.EXTRACTION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EXTRACTOR_IMPROVED), ECJEIRecipeTypes.EXTRACTION_IMPROVED);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EVAPORATOR), ECJEIRecipeTypes.EVAPORATION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.SOLAR_SYNTHESIZER), ECJEIRecipeTypes.SOLAR_SYNTHESIS);
		registry.addRecipeCatalyst(new ItemStack(ECItems.INFUSER), ECJEIRecipeTypes.INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.BINDER_IMPROVED), ECJEIRecipeTypes.INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.BINDER), ECJEIRecipeTypes.BINDING);
		registry.addRecipeCatalyst(new ItemStack(ECItems.BINDER_IMPROVED), ECJEIRecipeTypes.BINDING);
		registry.addRecipeCatalyst(new ItemStack(ECItems.CRYSTALLIZER), ECJEIRecipeTypes.CRYSTALLIZATION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.INSCRIBER), ECJEIRecipeTypes.INSCRIPTION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.CHISEL), ECJEIRecipeTypes.INSCRIPTION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.PURE_INFUSER), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.FIRE_PEDESTAL), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.WATER_PEDESTAL), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EARTH_PEDESTAL), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.AIR_PEDESTAL), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.PURIFIER), ECJEIRecipeTypes.PURIFICATION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.AIR_MILL), ECJEIRecipeTypes.GRINDING);
		registry.addRecipeCatalyst(new ItemStack(ECItems.SPELL_DESK), ECJEIRecipeTypes.SPELL_CRAFTING);
		
		if (ECinteractions.isMekanismActive()) {
			registry.addRecipeCatalyst(new ItemStack(ECItems.AIR_MILL), new ResourceLocation("mekanism", "crusher"));
		}
	}

	@SuppressWarnings({"resource", "ConstantConditions"})
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

		registry.addRecipes(ECJEIRecipeTypes.EXTRACTION, ElementType.ALL_VALID);
		registry.addRecipes(ECJEIRecipeTypes.EXTRACTION_IMPROVED, ElementType.ALL_VALID);
		registry.addRecipes(ECJEIRecipeTypes.EVAPORATION, EvaporationRecipeCategory.getShards());
		registry.addRecipes(ECJEIRecipeTypes.SOLAR_SYNTHESIS, SolarSynthesisRecipeCategory.getLenses());
		registry.addRecipes(ECJEIRecipeTypes.INFUSION, recipeManager.getAllRecipesFor(IInfusionRecipe.TYPE));
		registry.addRecipes(ECJEIRecipeTypes.BINDING, recipeManager.getAllRecipesFor(AbstractBindingRecipe.TYPE));
		registry.addRecipes(ECJEIRecipeTypes.CRYSTALLIZATION, recipeManager.getAllRecipesFor(CrystallizationRecipe.TYPE));
		registry.addRecipes(ECJEIRecipeTypes.INSCRIPTION, recipeManager.getAllRecipesFor(InscriptionRecipe.TYPE));
		registry.addRecipes(ECJEIRecipeTypes.PURE_INFUSION, recipeManager.getAllRecipesFor(PureInfusionRecipe.TYPE));
		registry.addRecipes(ECJEIRecipeTypes.GRINDING, recipeManager.getAllRecipesFor(IGrindingRecipe.TYPE));
		registry.addRecipes(ECJEIRecipeTypes.SPELL_CRAFTING, recipeManager.getAllRecipesFor(SpellCraftRecipe.TYPE));
		registry.addRecipes(ECJEIRecipeTypes.PURIFICATION, ElementalCraft.PURE_ORE_MANAGER.getRecipes());
		registry.addRecipes(RecipeTypes.ANVIL, createFocusStaffAnvilRecipes(registry.getVanillaRecipeFactory()));
		registry.addRecipes(RecipeTypes.ANVIL, createJewelsAnvilRecipes(registry.getVanillaRecipeFactory()));
	}

	private List<IJeiAnvilRecipe> createFocusStaffAnvilRecipes(IVanillaRecipeFactory factory) {
		return Spell.REGISTRY.getValues().stream().filter(Spell::isValid).flatMap(spell -> SPELL_CAST_TOOLS.get().stream().map(item -> {
			ItemStack scroll = new ItemStack(ECItems.SCROLL);
			ItemStack stack = new ItemStack(item);

			SpellHelper.setSpell(scroll, spell);
			SpellHelper.addSpell(stack, spell);
			return factory.createAnvilRecipe(new ItemStack(item), List.of(scroll), List.of(stack));
		})).toList();
	}

	private List<IJeiAnvilRecipe> createJewelsAnvilRecipes(IVanillaRecipeFactory factory) {


		return Jewel.REGISTRY.getValues().stream().flatMap(jewel -> JEWEL_SOCKETALBES.get().stream().map(item -> {
			ItemStack jewelItem = new ItemStack(ECItems.JEWEL);
			ItemStack stack = new ItemStack(item);

			JewelHelper.setJewel(jewelItem, jewel);
			JewelHelper.setJewel(stack, jewel);
			return factory.createAnvilRecipe(new ItemStack(item), List.of(jewelItem), List.of(stack));
		})).toList();
	}
}
