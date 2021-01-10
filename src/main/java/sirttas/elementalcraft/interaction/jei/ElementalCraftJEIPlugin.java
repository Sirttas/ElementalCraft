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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.interaction.jei.category.PureInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.EvaporationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ImprovedExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.BindingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.CrystallizationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.InscriptionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.PurificationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.infusion.InfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.infusion.ToolInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientHelper;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientRenderer;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.BindingRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

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
		registry.useNbtForSubtypes(ECItems.scroll);
		registry.useNbtForSubtypes(ECItems.receptacle);
		registry.useNbtForSubtypes(ECItems.pureOre);
		registry.useNbtForSubtypes(ECItems.airElementHolder);
		registry.useNbtForSubtypes(ECItems.earthElementHolder);
		registry.useNbtForSubtypes(ECItems.fireElementHolder);
		registry.useNbtForSubtypes(ECItems.waterElementHolder);
		registry.useNbtForSubtypes(ECItems.rune);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ExtractionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ImprovedExtractionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new EvaporationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ToolInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CrystallizationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InscriptionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PureInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PurificationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ECItems.fireFurnace), VanillaRecipeCategoryUid.FURNACE);
		registry.addRecipeCatalyst(new ItemStack(ECItems.fireBlastFurnace), VanillaRecipeCategoryUid.BLASTING);
		registry.addRecipeCatalyst(new ItemStack(ECItems.extractor), ExtractionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.improvedExtractor), ImprovedExtractionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.evaporator), EvaporationRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.infuser), InfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.infuser), ToolInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.binder), BindingRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.crystallizer), CrystallizationRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.inscriber), InscriptionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.chisel), InscriptionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.pureInfuser), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.firePedestal), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.waterPedestal), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.earthPedestal), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.airPedestal), PureInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.purifier), PurificationRecipeCategory.UID);
	}

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();

		registry.addRecipes(ElementType.allValid(), ExtractionRecipeCategory.UID);
		registry.addRecipes(ElementType.allValid(), ImprovedExtractionRecipeCategory.UID);
		registry.addRecipes(EvaporationRecipeCategory.getShards(), EvaporationRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(AbstractInfusionRecipe.TYPE).values(), InfusionRecipeCategory.UID);
		registry.addRecipes(InfusionHelper.getRecipes(), ToolInfusionRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(BindingRecipe.TYPE).values(), BindingRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(CrystallizationRecipe.TYPE).values(), CrystallizationRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(InscriptionRecipe.TYPE).values(), InscriptionRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(PureInfusionRecipe.TYPE).values(), PureInfusionRecipeCategory.UID);
		registry.addRecipes(ElementalCraft.PURE_ORE_MANAGER.getRecipes(), PurificationRecipeCategory.UID);
		registry.addRecipes(createFocusAnvilRecipes(registry.getVanillaRecipeFactory()), VanillaRecipeCategoryUid.ANVIL);
	}

	private List<?> createFocusAnvilRecipes(IVanillaRecipeFactory factory) {
		return Spell.REGISTRY.getValues().stream().filter(Spell::isValid).map(spell -> {
			ItemStack scroll = new ItemStack(ECItems.scroll);
			ItemStack focus = new ItemStack(ECItems.focus);

			SpellHelper.setSpell(scroll, spell);
			SpellHelper.addSpell(focus, spell);
			return factory.createAnvilRecipe(new ItemStack(ECItems.focus), ImmutableList.of(scroll), ImmutableList.of(focus));
		}).collect(Collectors.toList());
	}
}