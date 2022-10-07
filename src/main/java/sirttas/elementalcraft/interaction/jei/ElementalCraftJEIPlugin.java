package sirttas.elementalcraft.interaction.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
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
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlock;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.ie.IEInteraction;
import sirttas.elementalcraft.interaction.jei.category.PureInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.SpellCraftRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.DisplacementRecipeCategory;
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
import sirttas.elementalcraft.interaction.jei.category.instrument.io.SawingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.ToolInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.shrine.BuddingShrineRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.shrine.LavaShrineRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.shrine.SpringShrineRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientHelper;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientRenderer;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.source.IngredientSource;
import sirttas.elementalcraft.interaction.jei.ingredient.source.SourceIngredientHelper;
import sirttas.elementalcraft.interaction.jei.ingredient.source.SourceIngredientRenderer;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

@JeiPlugin
public class ElementalCraftJEIPlugin implements IModPlugin {

	private static final ResourceLocation ID = ElementalCraft.createRL("main");

	private final Supplier<HolderSet.Named<Item>> spellCastTools;
	private final Supplier<HolderSet.Named<Item>> jewelSocketalbes;

	public ElementalCraftJEIPlugin() {
		spellCastTools = () -> ECTags.Items.getTag(ECTags.Items.SPELL_CAST_TOOLS);
		jewelSocketalbes = () -> ECTags.Items.getTag(ECTags.Items.JEWEL_SOCKETABLES);
	}

	@Nonnull
	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(ECIngredientTypes.ELEMENT, IngredientElementType.all(), new ElementIngredientHelper(), new ElementIngredientRenderer());
		registration.register(ECIngredientTypes.SOURCE, IngredientSource.all(), new SourceIngredientHelper(), new SourceIngredientRenderer());
	}

	@Override
	public void registerItemSubtypes(@Nonnull ISubtypeRegistration registry) {
		useNbtForSubtypes(registry, ECItems.SCROLL);
		useNbtForSubtypes(registry, ECItems.RECEPTACLE);
		useNbtForSubtypes(registry, ECItems.PURE_ORE);
		useNbtForSubtypes(registry, ECItems.RUNE);
		useNbtForSubtypes(registry, ECItems.JEWEL);
		useNbtForSubtypes(registry, ECBlocks.CONTAINER, ECBlocks.SMALL_CONTAINER, ECBlocks.CREATIVE_CONTAINER);
		useNbtForSubtypes(registry, ECBlocks.FIRE_RESERVOIR, ECBlocks.WATER_RESERVOIR, ECBlocks.EARTH_RESERVOIR, ECBlocks.AIR_RESERVOIR);
		useNbtForSubtypes(registry, ECItems.FIRE_HOLDER, ECItems.WATER_HOLDER, ECItems.EARTH_HOLDER, ECItems.AIR_HOLDER, ECItems.PURE_HOLDER);

		if (!ECinteractions.isBotaniaActive()) {
			excludeSubtypes(registry, ECBlocks.MANA_SYNTHESIZER, ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE);
		}
	}

	@SafeVarargs
	private void excludeSubtypes(ISubtypeRegistration registry, RegistryObject<? extends ItemLike>... items) {
		for (var item : items) {
			registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item.get().asItem(), (i, c) -> IIngredientSubtypeInterpreter.NONE);
		}
	}

	@SafeVarargs
	private void useNbtForSubtypes(ISubtypeRegistration registry, RegistryObject<? extends ItemLike>... items) {
		for (var item : items) {
			registry.useNbtForSubtypes(item.get().asItem());
		}
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
		registry.addRecipeCategories(new SawingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SpellCraftRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new DisplacementRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BuddingShrineRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new LavaShrineRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SpringShrineRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.FIRE_FURNACE.get()), RecipeTypes.SMELTING);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.FIRE_BLAST_FURNACE.get()), RecipeTypes.BLASTING);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.EXTRACTOR.get()), ECJEIRecipeTypes.EXTRACTION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.EXTRACTOR_IMPROVED.get()), ECJEIRecipeTypes.EXTRACTION_IMPROVED);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.EVAPORATOR.get()), ECJEIRecipeTypes.EVAPORATION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.SOLAR_SYNTHESIZER.get()), ECJEIRecipeTypes.SOLAR_SYNTHESIS);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.INFUSER.get()), ECJEIRecipeTypes.INFUSION, ECJEIRecipeTypes.TOOL_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.BINDER.get()), ECJEIRecipeTypes.BINDING);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.BINDER_IMPROVED.get()), ECJEIRecipeTypes.BINDING, ECJEIRecipeTypes.INFUSION, ECJEIRecipeTypes.TOOL_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.CRYSTALLIZER.get()), ECJEIRecipeTypes.CRYSTALLIZATION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.INSCRIBER.get()), ECJEIRecipeTypes.INSCRIPTION);
		registry.addRecipeCatalyst(new ItemStack(ECItems.CHISEL.get()), ECJEIRecipeTypes.INSCRIPTION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.PURE_INFUSER.get()), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.FIRE_PEDESTAL.get()), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.WATER_PEDESTAL.get()), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.EARTH_PEDESTAL.get()), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_PEDESTAL.get()), ECJEIRecipeTypes.PURE_INFUSION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.PURIFIER.get()), ECJEIRecipeTypes.PURIFICATION);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()), ECJEIRecipeTypes.GRINDING);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.WATER_MILL_WOOD_SAW.get()), ECJEIRecipeTypes.SAWING);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.SPELL_DESK.get()), ECJEIRecipeTypes.SPELL_CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE.get()), ECJEIRecipeTypes.DISPLACEMENT);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE.get()), ECJEIRecipeTypes.DISPLACEMENT);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE.get()), ECJEIRecipeTypes.DISPLACEMENT);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE.get()), ECJEIRecipeTypes.DISPLACEMENT);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.BUDDING_SHRINE.get()), ECJEIRecipeTypes.BUDDING_SHRINE);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.LAVA_SHRINE.get()), ECJEIRecipeTypes.LAVA_SHRINE);
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.SPRING_SHRINE.get()), ECJEIRecipeTypes.SPRING_SHRINE);

		if (ECinteractions.isMekanismActive()) {
			MekanismInteraction.addAirMillToCrushing(registry);
		}
		if (ECinteractions.isImmersiveEngineeringActive()) {
			IEInteraction.addAirMillToCrushing(registry);
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
		registry.addRecipes(ECJEIRecipeTypes.INFUSION, recipeManager.getAllRecipesFor(ECRecipeTypes.INFUSION.get()).stream()
				.filter(r -> !(r instanceof ToolInfusionRecipe))
				.toList());
		registry.addRecipes(ECJEIRecipeTypes.TOOL_INFUSION, recipeManager.getAllRecipesFor(ECRecipeTypes.INFUSION.get()).stream()
				.filter(ToolInfusionRecipe.class::isInstance)
				.toList());
		registry.addRecipes(ECJEIRecipeTypes.BINDING, recipeManager.getAllRecipesFor(ECRecipeTypes.BINDING.get()));
		registry.addRecipes(ECJEIRecipeTypes.CRYSTALLIZATION, recipeManager.getAllRecipesFor(ECRecipeTypes.CRYSTALLIZATION.get()));
		registry.addRecipes(ECJEIRecipeTypes.INSCRIPTION, recipeManager.getAllRecipesFor(ECRecipeTypes.INSCRIPTION.get()));
		registry.addRecipes(ECJEIRecipeTypes.PURE_INFUSION, recipeManager.getAllRecipesFor(ECRecipeTypes.PURE_INFUSION.get()));
		registry.addRecipes(ECJEIRecipeTypes.GRINDING, recipeManager.getAllRecipesFor(ECRecipeTypes.AIR_MILL_GRINDING.get()));
		registry.addRecipes(ECJEIRecipeTypes.SAWING, recipeManager.getAllRecipesFor(ECRecipeTypes.SAWING.get()));
		registry.addRecipes(ECJEIRecipeTypes.SPELL_CRAFTING, recipeManager.getAllRecipesFor(ECRecipeTypes.SPELL_CRAFT.get()));
		registry.addRecipes(ECJEIRecipeTypes.PURIFICATION, ElementalCraft.PURE_ORE_MANAGER.getRecipes());
		registry.addRecipes(RecipeTypes.ANVIL, createCastToolsAnvilRecipes(registry.getVanillaRecipeFactory()));
		registry.addRecipes(RecipeTypes.ANVIL, createJewelsAnvilRecipes(registry.getVanillaRecipeFactory()));
		registry.addRecipes(ECJEIRecipeTypes.DISPLACEMENT, ElementType.ALL_VALID);
		registry.addRecipes(ECJEIRecipeTypes.BUDDING_SHRINE, List.of(BuddingShrineBlock.CrystalType.values()));
		registry.addRecipes(ECJEIRecipeTypes.LAVA_SHRINE, List.of(ECBlocks.LAVA_SHRINE.get()));
		registry.addRecipes(ECJEIRecipeTypes.SPRING_SHRINE, List.of(ECBlocks.SPRING_SHRINE.get()));
	}

	private List<IJeiAnvilRecipe> createCastToolsAnvilRecipes(IVanillaRecipeFactory factory) {
		return Spells.REGISTRY.get().getValues().stream()
				.filter(Spell::isVisible)
				.<IJeiAnvilRecipe>mapMulti((spell, downstream) -> spellCastTools.get().forEach(item -> {
					ItemStack scroll = new ItemStack(ECItems.SCROLL.get());
					ItemStack stack = new ItemStack(item);

					SpellHelper.setSpell(scroll, spell);
					SpellHelper.addSpell(stack, spell);
					downstream.accept(factory.createAnvilRecipe(new ItemStack(item), List.of(scroll), List.of(stack)));
				})).toList();
	}

	private List<IJeiAnvilRecipe> createJewelsAnvilRecipes(IVanillaRecipeFactory factory) {
		return Jewels.REGISTRY.get().getValues().stream().flatMap(jewel -> jewelSocketalbes.get().stream().map(item -> {
			ItemStack jewelItem = new ItemStack(ECItems.JEWEL.get());
			ItemStack stack = new ItemStack(item);

			JewelHelper.setJewel(jewelItem, jewel);
			JewelHelper.setJewel(stack, jewel);
			return factory.createAnvilRecipe(new ItemStack(item), List.of(jewelItem), List.of(stack));
		})).toList();
	}
}
