package sirttas.elementalcraft.interaction.jei;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
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

@JeiPlugin
public class ElementalCraftJEIPlugin implements IModPlugin {

	private static final ResourceLocation ID = ElementalCraft.createRL("main");
	
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
		registry.addRecipeCatalyst(new ItemStack(ECItems.FIRE_FURNACE), VanillaRecipeCategoryUid.FURNACE);
		registry.addRecipeCatalyst(new ItemStack(ECItems.FIRE_BLAST_FURNACE), VanillaRecipeCategoryUid.BLASTING);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EXTRACTOR), ExtractionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EXTRACTOR_IMPROVED), ImprovedExtractionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EVAPORATOR), EvaporationRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.SOLAR_SYNTHESIZER), SolarSynthesisRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.INFUSER), InfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.BINDER_IMPROVED), InfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.BINDER), BindingRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.BINDER_IMPROVED), BindingRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.CRYSTALLIZER), CrystallizationRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.INSCRIBER), InscriptionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.CHISEL), InscriptionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.PURE_INFUSER), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.FIRE_PEDESTAL), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.WATER_PEDESTAL), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.EARTH_PEDESTAL), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.AIR_PEDESTAL), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.PURIFIER), PurificationRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.AIR_MILL), GrindingRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.SPELL_DESK), SpellCraftRecipeCategory.UID);
		
		if (ECinteractions.isMekanismActive()) {
			registry.addRecipeCatalyst(new ItemStack(ECItems.AIR_MILL), new ResourceLocation("mekanism", "crusher"));
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

		registry.addRecipes(ElementType.ALL_VALID, ExtractionRecipeCategory.UID);
		registry.addRecipes(ElementType.ALL_VALID, ImprovedExtractionRecipeCategory.UID);
		registry.addRecipes(EvaporationRecipeCategory.getShards(), EvaporationRecipeCategory.UID);
		registry.addRecipes(SolarSynthesisRecipeCategory.getLenses(), SolarSynthesisRecipeCategory.UID);
		registry.addRecipes(recipeManager.byType(IInfusionRecipe.TYPE).values(), InfusionRecipeCategory.UID);
		registry.addRecipes(recipeManager.byType(AbstractBindingRecipe.TYPE).values(), BindingRecipeCategory.UID);
		registry.addRecipes(recipeManager.byType(CrystallizationRecipe.TYPE).values(), CrystallizationRecipeCategory.UID);
		registry.addRecipes(recipeManager.byType(InscriptionRecipe.TYPE).values(), InscriptionRecipeCategory.UID);
		registry.addRecipes(recipeManager.byType(PureInfusionRecipe.TYPE).values(), PureInfusionRecipeCategory.UID);
		registry.addRecipes(recipeManager.byType(IGrindingRecipe.TYPE).values(), GrindingRecipeCategory.UID);
		registry.addRecipes(recipeManager.byType(SpellCraftRecipe.TYPE).values(), SpellCraftRecipeCategory.UID);
		registry.addRecipes(ElementalCraft.PURE_ORE_MANAGER.getRecipes(), PurificationRecipeCategory.UID);
		registry.addRecipes(createFocusStaffAnvilRecipes(registry.getVanillaRecipeFactory()), VanillaRecipeCategoryUid.ANVIL);
		
	}

	private List<?> createFocusStaffAnvilRecipes(IVanillaRecipeFactory factory) {
		return Spell.REGISTRY.getValues().stream().filter(Spell::isValid).flatMap(spell -> ECTags.Items.SPELL_CAST_TOOLS.getValues().stream().map(item -> {
			ItemStack scroll = new ItemStack(ECItems.SCROLL);
			ItemStack stack = new ItemStack(item);

			SpellHelper.setSpell(scroll, spell);
			SpellHelper.addSpell(stack, spell);
			return factory.createAnvilRecipe(new ItemStack(item), ImmutableList.of(scroll), ImmutableList.of(stack));
		})).collect(Collectors.toList());
	}
}