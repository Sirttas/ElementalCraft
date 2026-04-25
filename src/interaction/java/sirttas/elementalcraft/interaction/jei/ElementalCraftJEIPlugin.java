package sirttas.elementalcraft.interaction.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.common.Internal;
import mezz.jei.library.plugins.vanilla.cooking.fuel.FuelingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.category.PureInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.SpellCraftRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.ExtractionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.AirMillSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.CombustionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.CulinaryRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.DrainingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.SolarSynthesisRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.VibrationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.cracking.CrackingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.element.synthesis.cracking.SculkCrackingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.BindingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.CrystallizationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.EnchantmentLiquefactionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.InscriptionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.InfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.PurificationRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.ToolInfusionRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.mill.GrindingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.mill.SawingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.shrine.BuddingShrineRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.shrine.MeltingShrineRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.shrine.SpringShrineRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.source.DisplacementRecipeCategory;
import sirttas.elementalcraft.interaction.jei.category.source.SourceBreedingRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientHelper;
import sirttas.elementalcraft.interaction.jei.ingredient.element.ElementIngredientRenderer;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.source.IngredientSource;
import sirttas.elementalcraft.interaction.jei.ingredient.source.SourceIngredientHelper;
import sirttas.elementalcraft.interaction.jei.ingredient.source.SourceIngredientRenderer;
import sirttas.elementalcraft.interaction.jei.ingredient.subtype.ElementStorageSubtypeInterpreter;
import sirttas.elementalcraft.interaction.jei.ingredient.subtype.PureElementHolderSubtypeInterpreter;
import sirttas.elementalcraft.interaction.jei.ingredient.subtype.PureOreSubtypeInterpreter;
import sirttas.elementalcraft.interaction.jei.ingredient.subtype.RuneSubtypeInterpreter;
import sirttas.elementalcraft.interaction.jei.ingredient.subtype.SpellSubtypeInterpreter;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction.EnchantmentLiquefactionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@JeiPlugin
public class ElementalCraftJEIPlugin implements IModPlugin {

	private static final Identifier ID = ElementalCraftApi.createRL("main");

	private final Supplier<HolderSet.Named<@NotNull Item>> spellCastTools;
	private final Supplier<HolderSet.Named<@NotNull Item>> jewelSocketalbes;

	public ElementalCraftJEIPlugin() {
		spellCastTools = () -> ECTags.Items.getTag(ECTags.Items.SPELL_CAST_TOOLS);
		jewelSocketalbes = () -> ECTags.Items.getTag(ECTags.Items.JEWEL_SOCKETABLES);
	}

	@Nonnull
	@Override
	public Identifier getPluginUid() {
		return ID;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(ECIngredientTypes.ELEMENT, IngredientElementType.all(), new ElementIngredientHelper(), new ElementIngredientRenderer(), IngredientElementType.CODEC);
		registration.register(ECIngredientTypes.SOURCE, IngredientSource.all(), new SourceIngredientHelper(), new SourceIngredientRenderer(), IngredientSource.CODEC);
	}

	@Override
	public void registerItemSubtypes(@Nonnull ISubtypeRegistration registry) {
		registry.registerSubtypeInterpreter(ECItems.SCROLL.get(), new SpellSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECItems.PURE_ORE.get(), new PureOreSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECItems.RUNE.get(), new RuneSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECBlocks.SMALL_CONTAINER.get().asItem(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECBlocks.CONTAINER.get().asItem(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECBlocks.CREATIVE_CONTAINER.get().asItem(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECBlocks.FIRE_RESERVOIR.get().asItem(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECBlocks.WATER_RESERVOIR.get().asItem(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECBlocks.EARTH_RESERVOIR.get().asItem(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECBlocks.AIR_RESERVOIR.get().asItem(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECItems.FIRE_HOLDER.get(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECItems.WATER_HOLDER.get(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECItems.EARTH_HOLDER.get(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECItems.AIR_HOLDER.get(), new ElementStorageSubtypeInterpreter());
		registry.registerSubtypeInterpreter(ECItems.PURE_HOLDER.get(), new PureElementHolderSubtypeInterpreter());
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ExtractionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CrackingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SculkCrackingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CombustionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SolarSynthesisRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new DrainingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CulinaryRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new VibrationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new AirMillSynthesisRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ToolInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CrystallizationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new InscriptionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new EnchantmentLiquefactionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PureInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PurificationRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new GrindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SawingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SpellCraftRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new DisplacementRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BuddingShrineRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new MeltingShrineRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SpringShrineRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SourceBreedingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addCraftingStation(RecipeTypes.SMELTING, new ItemStack(ECBlocks.FIRE_FURNACE.get()));
		registry.addCraftingStation(RecipeTypes.BLASTING, new ItemStack(ECBlocks.FIRE_BLAST_FURNACE.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.EXTRACTION, new ItemStack(ECBlocks.RUDIMENTARY_EXTRACTOR.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.EXTRACTION, new ItemStack(ECBlocks.EXTRACTOR.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.EXTRACTION, new ItemStack(ECBlocks.IMPROVED_EXTRACTOR.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.CRACKING, new ItemStack(ECBlocks.CRACKING_SYNTHESIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.COMBUSTION, new ItemStack(ECBlocks.COMBUSTION_SYNTHESIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.SOLAR_SYNTHESIS, new ItemStack(ECBlocks.SOLAR_SYNTHESIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.DRAINING, new ItemStack(ECBlocks.DRAINING_SYNTHESIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.CULINARY, new ItemStack(ECBlocks.CULINARY_SYNTHESIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.VIBRATION, new ItemStack(ECBlocks.VIBRATION_SYNTHESIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.AIR_MILL_SYNTHESIS, new ItemStack(ECBlocks.AIR_MILL_SYNTHESIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.INFUSION, new ItemStack(ECBlocks.INFUSER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.TOOL_INFUSION, new ItemStack(ECBlocks.INFUSER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.BINDING, new ItemStack(ECBlocks.BINDER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.BINDING, new ItemStack(ECBlocks.BINDER_IMPROVED.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.INFUSION, new ItemStack(ECBlocks.BINDER_IMPROVED.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.TOOL_INFUSION, new ItemStack(ECBlocks.BINDER_IMPROVED.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.CRYSTALLIZATION, new ItemStack(ECBlocks.CRYSTALLIZER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.INSCRIPTION, new ItemStack(ECBlocks.INSCRIBER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.INSCRIPTION, new ItemStack(ECItems.DRENCHED_IRON_CHISEL));
		registry.addCraftingStation(ECJEIRecipeTypes.INSCRIPTION, new ItemStack(ECItems.SWIFT_ALLOY_CHISEL));
		registry.addCraftingStation(ECJEIRecipeTypes.INSCRIPTION, new ItemStack(ECItems.FIREITE_CHISEL));
		registry.addCraftingStation(ECJEIRecipeTypes.ENCHANTMENT_LIQUEFACTION, new ItemStack(ECBlocks.ENCHANTMENT_LIQUEFIER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.PURE_INFUSION, new ItemStack(ECBlocks.PURE_INFUSER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.PURE_INFUSION, new ItemStack(ECBlocks.FIRE_PEDESTAL.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.PURE_INFUSION, new ItemStack(ECBlocks.WATER_PEDESTAL.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.PURE_INFUSION, new ItemStack(ECBlocks.EARTH_PEDESTAL.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.PURE_INFUSION, new ItemStack(ECBlocks.AIR_PEDESTAL.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.ORE_PURIFICATION, new ItemStack(ECBlocks.PURIFIER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.GRINDING, new ItemStack(ECBlocks.WATER_MILL_GRINDSTONE.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.GRINDING, new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.SAWING, new ItemStack(ECBlocks.WATER_MILL_WOOD_SAW.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.SAWING, new ItemStack(ECBlocks.AIR_MILL_WOOD_SAW.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.SPELL_CRAFTING, new ItemStack(ECBlocks.SPELL_DESK.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.DISPLACEMENT, new ItemStack(ECItems.EMPTY_RECEPTACLE));
		registry.addCraftingStation(ECJEIRecipeTypes.BUDDING_SHRINE, new ItemStack(ECBlocks.BUDDING_SHRINE.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.MELTING_SHRINE, new ItemStack(ECBlocks.MELTING_SHRINE.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.SPRING_SHRINE, new ItemStack(ECBlocks.SPRING_SHRINE.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.SOURCE_BREEDING, new ItemStack(ECBlocks.SOURCE_BREEDER.get()));
		registry.addCraftingStation(ECJEIRecipeTypes.SOURCE_BREEDING, new ItemStack(ECBlocks.SOURCE_BREEDER_PEDESTAL.get()));

        ElementalCraft.interactions().addCraftingStations((o, s) -> {
            if (o instanceof IRecipeType<?> recipeType) {
                try {
                    registry.addCraftingStation(recipeType, s);
                } catch (Exception e) {
                    ElementalCraftApi.LOGGER.error("Failed to add crafting station from interaction", e);
                }
            }
        });

	}

	@SuppressWarnings({"ConstantConditions"})
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
        RecipeMap clientSyncedRecipes;

        try {
            clientSyncedRecipes = Internal.getClientSyncedRecipes();

            if (clientSyncedRecipes.values().isEmpty()) {
                return;
            }
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.error("Failed to get client synced recipes, skipping recipe registration. I know I shouldn't have reached for JEI Internal class", e);
            return;
        }

		registry.addRecipes(ECJEIRecipeTypes.EXTRACTION, getExtractionRecipes());
		registry.addRecipes(ECJEIRecipeTypes.CRACKING, getRecipes(clientSyncedRecipes, ECRecipeTypes.CRACKING));
		registry.addRecipes(ECJEIRecipeTypes.SCULK_CRACKING, getRecipes(clientSyncedRecipes, ECRecipeTypes.SCULK_CRACKING));
		registry.addRecipes(ECJEIRecipeTypes.COMBUSTION, getFuels(registry));
		registry.addRecipes(ECJEIRecipeTypes.SOLAR_SYNTHESIS, List.of(Ingredient.of(ECItems.FIRE_LENS.get())));
		registry.addRecipes(ECJEIRecipeTypes.DRAINING, List.of(new IngredientElementType(ElementType.WATER, 1)));
		registry.addRecipes(ECJEIRecipeTypes.CULINARY, registry.getIngredientManager().getAllItemStacks().stream()
				.filter(s -> s.get(DataComponents.FOOD) != null)
				.sorted(Comparator.comparing((ItemStack s) -> s.get(DataComponents.FOOD).nutrition())
						.thenComparing(s -> s.get(DataComponents.FOOD).saturation()))
				.toList());
		registry.addRecipes(ECJEIRecipeTypes.VIBRATION, List.of(new IngredientElementType(ElementType.AIR, 1)));
		registry.addRecipes(ECJEIRecipeTypes.AIR_MILL_SYNTHESIS, List.of(new IngredientElementType(ElementType.AIR, 3)));
		registry.addRecipes(ECJEIRecipeTypes.INFUSION, clientSyncedRecipes.byType(ECRecipeTypes.INFUSION.get()).stream()
				.map(RecipeHolder::value)
				.filter(r -> !(r instanceof ToolInfusionRecipe))
				.toList());
		registry.addRecipes(ECJEIRecipeTypes.TOOL_INFUSION, clientSyncedRecipes.byType(ECRecipeTypes.INFUSION.get()).stream()
				.map(RecipeHolder::value)
				.filter(ToolInfusionRecipe.class::isInstance)
				.toList());
		registry.addRecipes(ECJEIRecipeTypes.BINDING, getRecipes(clientSyncedRecipes, ECRecipeTypes.BINDING));
		registry.addRecipes(ECJEIRecipeTypes.CRYSTALLIZATION, getRecipes(clientSyncedRecipes, ECRecipeTypes.CRYSTALLIZATION));
		registry.addRecipes(ECJEIRecipeTypes.INSCRIPTION, getRecipes(clientSyncedRecipes, ECRecipeTypes.INSCRIPTION));
		registry.addRecipes(ECJEIRecipeTypes.ENCHANTMENT_LIQUEFACTION, getEnchantmentLiquefactionRecipes(registry));
		registry.addRecipes(ECJEIRecipeTypes.PURE_INFUSION, getRecipes(clientSyncedRecipes, ECRecipeTypes.PURE_INFUSION));
		registry.addRecipes(ECJEIRecipeTypes.GRINDING, getRecipes(clientSyncedRecipes, ECRecipeTypes.GRINDING));
		registry.addRecipes(ECJEIRecipeTypes.SAWING, getRecipes(clientSyncedRecipes, ECRecipeTypes.SAWING));
		registry.addRecipes(ECJEIRecipeTypes.SPELL_CRAFTING, getRecipes(clientSyncedRecipes, ECRecipeTypes.SPELL_CRAFT));
		registry.addRecipes(ECJEIRecipeTypes.ORE_PURIFICATION, getRecipes(clientSyncedRecipes, ECRecipeTypes.ORE_PURIFICATION));
		registry.addRecipes(RecipeTypes.ANVIL, createCastToolsAnvilRecipes(registry.getVanillaRecipeFactory()));
		registry.addRecipes(RecipeTypes.ANVIL, createJewelsAnvilRecipes(registry.getVanillaRecipeFactory()));
		registry.addRecipes(ECJEIRecipeTypes.DISPLACEMENT, ElementType.ALL_VALID);
		registry.addRecipes(ECJEIRecipeTypes.BUDDING_SHRINE, getBudTypes());
		registry.addRecipes(ECJEIRecipeTypes.MELTING_SHRINE, getRecipes(clientSyncedRecipes, ECRecipeTypes.MELTING));
		registry.addRecipes(ECJEIRecipeTypes.SPRING_SHRINE, List.of(ECBlocks.SPRING_SHRINE.get()));
		registry.addRecipes(ECJEIRecipeTypes.SOURCE_BREEDING, List.of(
				ECItems.FIRE_SOURCE_SEED.get(),
				ECItems.WATER_SOURCE_SEED.get(),
				ECItems.EARTH_SOURCE_SEED.get(),
				ECItems.AIR_SOURCE_SEED.get()
		));
	}

	private static @NotNull List<IJeiFuelingRecipe> getFuels(@NotNull IRecipeRegistration registry) {
		return registry.getIngredientManager().getAllItemStacks().stream()
				.<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
					int burnTime = stack.getBurnTime(RecipeType.SMELTING, Minecraft.getInstance().level.fuelValues());

					if (burnTime > 0) {
						consumer.accept(new FuelingRecipe(List.of(stack), burnTime));
					}
				})
				.sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
				.toList();
	}

	private List<ExtractionRecipeCategory.ExtractionRecipe> getExtractionRecipes() {
		var list = new ArrayList<ExtractionRecipeCategory.ExtractionRecipe>();

		for (var elementType : ElementType.ALL_VALID) {
			list.add(new ExtractionRecipeCategory.ExtractionRecipe(new IngredientElementType(elementType, 1), new ItemStack(ECBlocks.RUDIMENTARY_EXTRACTOR.get()), List.of(new ItemStack(ECBlocks.CONTAINER.get()), new ItemStack(ECBlocks.SMALL_CONTAINER.get()))));
			list.add(new ExtractionRecipeCategory.ExtractionRecipe(new IngredientElementType(elementType, 2), new ItemStack(ECBlocks.EXTRACTOR.get()), List.of(new ItemStack(ECBlocks.CONTAINER.get()))));
			list.add(new ExtractionRecipeCategory.ExtractionRecipe(new IngredientElementType(elementType, 3), new ItemStack(ECBlocks.IMPROVED_EXTRACTOR.get()), List.of(new ItemStack(ECBlocks.CONTAINER.get()))));
		}
		return list;
	}

	private List<EnchantmentLiquefactionRecipeCategory.RecipeWrapper> getEnchantmentLiquefactionRecipes(@Nonnull IRecipeRegistration registry) {
		var stacks = registry.getIngredientManager().getAllItemStacks();

		return Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements()
				.flatMap(e -> IntStream.range(1, e.value().getMaxLevel())
						.mapToObj(l -> new EnchantmentLiquefactionRecipeCategory.RecipeWrapper(new EnchantmentLiquefactionRecipe(e), l, stacks.stream()
								.filter(ItemStack::isEnchantable)
                        		.filter(s -> s.supportsEnchantment(e))
								.toList())))
				.toList();
	}

	private <C extends RecipeInput, R extends Recipe<@NotNull C>, T extends RecipeType<@NotNull R>> List<R> getRecipes(RecipeMap recipeMap, Supplier<T> typeSupplier) {
		return recipeMap.byType(typeSupplier.get()).stream()
				.map(RecipeHolder::value)
				.toList();
	}

	private List<IJeiAnvilRecipe> createCastToolsAnvilRecipes(IVanillaRecipeFactory factory) {
		return Spells.REGISTRY.listElements()
				.filter(SpellHelper::isVisible)
				.<IJeiAnvilRecipe>mapMulti((spell, downstream) -> spellCastTools.get().forEach(item -> {
					ItemStack scroll = new ItemStack(ECItems.SCROLL);
					ItemStack stack = new ItemStack(item);

					SpellHelper.setSpell(scroll, spell);
					SpellHelper.addSpell(stack, spell);
					downstream.accept(factory.createAnvilRecipe(new ItemStack(item), List.of(scroll), List.of(stack), spell.key().identifier()));
				})).toList();
	}

	private List<IJeiAnvilRecipe> createJewelsAnvilRecipes(IVanillaRecipeFactory factory) {
		return Jewels.REGISTRY.stream().flatMap(jewel -> jewelSocketalbes.get().stream().map(item -> {
			ItemStack stack = new ItemStack(item);

			JewelHelper.setJewel(stack, jewel);
			return factory.createAnvilRecipe(new ItemStack(item), List.of(new ItemStack(jewel)), List.of(stack), jewel.getKey());
		})).toList();
	}


    private List<BuddingShrineBudType> getBudTypes() {
        return new ArrayList<>(ElementalCraftApi.BUD_TYPE_MANAGER.getData().values());
    }
}
