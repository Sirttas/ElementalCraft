package sirttas.elementalcraft.interaction.jei;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
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
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.jei.category.PureInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.EvaporationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ImprovedExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.SolarSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.BindingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.CrystallizationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.InscriptionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.GrindingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.PurificationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.infusion.InfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.infusion.ToolInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientHelper;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientRenderer;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AbstractGrindingRecipe;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

@JeiPlugin
public class ElementalCraftJEIPlugin implements IModPlugin {

	private static final ResourceLocation ID = ElementalCraft.createRL("main");
	private static final ISubtypeInterpreter HOLDER_INTERPRETER = stack -> CapabilityElementStorage.get(stack).map(storage -> {
		StringBuilder stringBuilder = new StringBuilder();

		ElementType.allValid().forEach(type -> {
			int amount = storage.getElementAmount(type);
			
			if (amount > 0) {
				stringBuilder.append(type.getString());
				stringBuilder.append(':');
				stringBuilder.append(amount);
				stringBuilder.append(':');
				stringBuilder.append(storage.getElementCapacity(type));
				stringBuilder.append(';');
			}
		});
		return stringBuilder.length() > 0 ? stringBuilder.toString() : ISubtypeInterpreter.NONE;
	}).orElse(ISubtypeInterpreter.NONE);
	
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
		registry.registerSubtypeInterpreter(ECItems.FIRE_HOLDER, HOLDER_INTERPRETER);
		registry.registerSubtypeInterpreter(ECItems.WATER_HOLDER, HOLDER_INTERPRETER);
		registry.registerSubtypeInterpreter(ECItems.EARTH_HOLDER, HOLDER_INTERPRETER);
		registry.registerSubtypeInterpreter(ECItems.AIR_HOLDER, HOLDER_INTERPRETER);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ExtractionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ImprovedExtractionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new EvaporationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SolarSynthesisRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ToolInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CrystallizationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InscriptionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PureInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PurificationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new GrindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
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
		registry.addRecipeCatalyst(new ItemStack(ECItems.INFUSER), ToolInfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.BINDER_IMPROVED), ToolInfusionRecipeCategory.UID);
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
		
		if (ECinteractions.isMekanismActive()) {
			registry.addRecipeCatalyst(new ItemStack(ECItems.AIR_MILL), new ResourceLocation("mekanism", "crusher"));
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();

		registry.addRecipes(ElementType.allValid(), ExtractionRecipeCategory.UID);
		registry.addRecipes(ElementType.allValid(), ImprovedExtractionRecipeCategory.UID);
		registry.addRecipes(EvaporationRecipeCategory.getShards(), EvaporationRecipeCategory.UID);
		registry.addRecipes(SolarSynthesisRecipeCategory.getLenses(), SolarSynthesisRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(AbstractInfusionRecipe.TYPE).values(), InfusionRecipeCategory.UID);
		registry.addRecipes(InfusionHelper.getRecipes(), ToolInfusionRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(AbstractBindingRecipe.TYPE).values(), BindingRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(CrystallizationRecipe.TYPE).values(), CrystallizationRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(InscriptionRecipe.TYPE).values(), InscriptionRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(PureInfusionRecipe.TYPE).values(), PureInfusionRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(AbstractGrindingRecipe.TYPE).values(), GrindingRecipeCategory.UID);
		registry.addRecipes(ElementalCraft.PURE_ORE_MANAGER.getRecipes(), PurificationRecipeCategory.UID);
		registry.addRecipes(createFocusAnvilRecipes(registry.getVanillaRecipeFactory()), VanillaRecipeCategoryUid.ANVIL);
		
	}

	private List<?> createFocusAnvilRecipes(IVanillaRecipeFactory factory) {
		return Spell.REGISTRY.getValues().stream().filter(Spell::isValid).map(spell -> {
			ItemStack scroll = new ItemStack(ECItems.SCROLL);
			ItemStack focus = new ItemStack(ECItems.FOCUS);

			SpellHelper.setSpell(scroll, spell);
			SpellHelper.addSpell(focus, spell);
			return factory.createAnvilRecipe(new ItemStack(ECItems.FOCUS), ImmutableList.of(scroll), ImmutableList.of(focus));
		}).collect(Collectors.toList());
	}
}