package sirttas.elementalcraft.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.NBTIngredient;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.datagen.recipe.builder.PureInfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.SpellCraftRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.BindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.CrystallizationRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.GrindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.InscriptionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.SawingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.InfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.ToolInfusionRecipeBuilder;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;
import sirttas.elementalcraft.item.ECCreativeModeTabs;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.NaturalSourceIngredient;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.recipe.StaffRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.tag.ECTags;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nonnull;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ECRecipeProvider extends RecipeProvider {

	private static final String HAS_INERT_CRYSTAL = "has_inert_crystal";
	private static final String HAS_CONTAINED_CRYSTAL = "has_contained_crystal";
	private static final String HAS_PURECRYSTAL = "has_purecrystal";
	private static final String HAS_WHITEROCK = "has_whiterock";
	private static final String HAS_SHRINE_UPGRADE_CORE = "has_shrine_upgrade_core";
	private static final String HAS_SPRINGALINE_SHARD = "has_springaline_shard";

	private static final String HAS_DRENCHED_IRON_NUGGET = "has_drenched_iron_nugget";
	private static final String HAS_DRENCHED_IRON_INGOT = "has_drenched_iron_ingot";
	private static final String HAS_SWIFT_ALLOY_NUGGET = "has_swift_alloy_nugget";
	private static final String HAS_SWIFT_ALLOY_INGOT = "has_swift_alloy_ingot";
	private static final String HAS_FIREITE_INGOT = "has_fireite_ingot";
	public static final String FROM = "_from_";

	private final ExistingFileHelper existingFileHelper;

	public ECRecipeProvider(PackOutput pOutput, ExistingFileHelper exFileHelper) {
		super(pOutput);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
		registerSlabsStairsWalls(recipeOutput);
		registerInertCrystal(recipeOutput);
		registerNuggetIngotBlocks(recipeOutput);
		registerMaterials(recipeOutput);
		registerPipes(recipeOutput);
		registerContainers(recipeOutput);
		registerInstruments(recipeOutput);
		registerPureInfuser(recipeOutput);
		registerInfusions(recipeOutput);
		registerSpringaline(recipeOutput);
		registerHolders(recipeOutput);
		registerTools(recipeOutput);
		registerShards(recipeOutput);
		registerLenses(recipeOutput);
		registerShrines(recipeOutput);
		registerShrineUpgrades(recipeOutput);
		registerSourceDisplacementPlates(recipeOutput);
		registerJewels(recipeOutput);
		registerSpells(recipeOutput);
		registerToolInfusions(recipeOutput);
		registerGrinding(recipeOutput);
		registerSawing(recipeOutput);
		registerRunes(recipeOutput);
		registerEmptying(recipeOutput);
		registerCrystallizations(recipeOutput);
		registerDecorations(recipeOutput);
		registerSourceBreeding(recipeOutput);
	}

	private static void registerMaterials(@Nonnull RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.CONTAINED_CRYSTAL.get())
				.define('g', Tags.Items.NUGGETS_GOLD)
				.define('c', ECItems.INERT_CRYSTAL.get())
				.pattern(" g ")
				.pattern("gcg")
				.pattern(" g ")
				.unlockedBy(HAS_INERT_CRYSTAL, has(ECItems.INERT_CRYSTAL))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('g', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.pattern("sgs")
				.pattern("gcg")
				.pattern("sgs")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.SHRINE_BASE.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.INERT_CRYSTAL.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern(" p ")
				.pattern("pcp")
				.pattern("www")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.SHRINE_UPGRADE_CORE.get())
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("ici")
				.pattern("rir")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.SOLAR_PRISM.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', Tags.Items.INGOTS_COPPER)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" s ").pattern("cdc")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.DRENCHED_SAW_BLADE.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_DRENCHED_IRON)
				.define('r', Tags.Items.INGOTS_IRON)
				.pattern("nin")
				.pattern("iri")
				.pattern("nin")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(recipeOutput);

		BindingRecipeBuilder.bindingRecipe(ECItems.SWIFT_ALLOY_INGOT.get(), ElementType.AIR)
				.addIngredient(Tags.Items.INGOTS_GOLD)
				.addIngredient(ECTags.Items.INGOTS_DRENCHED_IRON)
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.withElementAmount(1250)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECItems.FIREITE_INGOT.get(), ElementType.FIRE)
				.addIngredient(Tags.Items.INGOTS_NETHERITE)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.withElementAmount(30000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARDENED_HANDLE.get(), ElementType.EARTH)
				.addIngredient(Tags.Items.RODS_WOODEN)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.withElementAmount(1250)
				.save(recipeOutput);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_CRYSTAL.get())
				.setIngredient(Tags.Items.GEMS_DIAMOND)
				.setIngredient(ElementType.WATER, ECItems.WATER_CRYSTAL.get())
				.setIngredient(ElementType.FIRE, ECItems.FIRE_CRYSTAL.get())
				.setIngredient(ElementType.EARTH, ECItems.EARTH_CRYSTAL.get())
				.setIngredient(ElementType.AIR, ECItems.AIR_CRYSTAL.get())
				.save(recipeOutput);
	}

	private static void registerLenses(@Nonnull RecipeOutput recipeOutput) {
		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_LENS.get(), ElementType.FIRE)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECItems.WATER_LENS.get(), ElementType.WATER)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECItems.EARTH_LENS.get(), ElementType.EARTH)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECItems.AIR_LENS.get(), ElementType.AIR)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.save(recipeOutput);
	}

	private static void registerSpringaline(@Nonnull RecipeOutput recipeOutput) {
		BindingRecipeBuilder.bindingRecipe(ECItems.SPRINGALINE_SHARD.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_SHARD)
				.addIngredient(Tags.Items.GEMS_QUARTZ)
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRINGALINE_CLUSTER.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_QUARTZ /* FIXME use all quartz blocks */)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(recipeOutput);
	}

	private static void registerTools(@Nonnull RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ECCreativeModeTabs.createElementopedia())
				.requires(ECItems.INERT_CRYSTAL.get())
				.requires(Items.BOOK)
				.unlockedBy(HAS_INERT_CRYSTAL, has(ECItems.INERT_CRYSTAL))
				.save(recipeOutput.withConditions(new ModLoadedCondition(PatchouliAPI.MOD_ID)), ElementalCraftApi.createRL("element_book"));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.SOURCE_STABILIZER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECBlocks.SPRINGALINE_GLASS.get())
				.pattern("sis")
				.pattern("i i")
				.pattern("sis")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.SOURCE_ANALYSIS_GLASS.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.pattern(" sg")
				.pattern(" is")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.CHISEL.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" i ")
				.pattern(" di")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECBlocks.TRANSLOCATION_ANCHOR.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('g', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('e', Items.ENDER_EYE)
				.pattern(" e ")
				.pattern("fgf")
				.pattern("www")
				.unlockedBy("has_fireite_nugget", has(ECTags.Items.NUGGETS_FIREITE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECBlocks.RETRIEVER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("iw ")
				.pattern("hdi")
				.pattern("iw "
				).save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECBlocks.SORTER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("ii ")
				.pattern("hdi")
				.pattern("ii ")
				.save(recipeOutput);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECBlocks.PURE_ROCK.get())
				.setIngredient(Items.OBSIDIAN)
				.setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, ECTags.Items.INGOTS_FIREITE)
				.setIngredient(ElementType.EARTH, ECBlocks.WHITE_ROCK.get())
				.setIngredient(ElementType.AIR, Items.PURPUR_BLOCK)
				.save(recipeOutput);

		BindingRecipeBuilder.bindingRecipe(ECItems.ELEMENTAL_FIREFUEL.get(), ElementType.FIRE)
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(ItemTags.COALS)
				.addIngredient(Tags.Items.RODS_BLAZE)
				.addIngredient(Items.LAVA_BUCKET)
				.withElementAmount(20000)
				.save(recipeOutput);
	}

	private void registerInstruments(@Nonnull RecipeOutput recipeOutput) {
		prepareInstrumentRecipe(ECBlocks.EXTRACTOR)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern(" c ").pattern(" i ")
				.pattern("ici")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.EXTRACTOR_IMPROVED.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('e', ECBlocks.EXTRACTOR.get())
				.pattern(" e ")
				.pattern("eie")
				.pattern("wcw")
				.save(recipeOutput);
		prepareInstrumentRecipe(ECBlocks.EVAPORATOR)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('g', Tags.Items.GLASS)
				.pattern("igi")
				.pattern("igi")
				.pattern("ici")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOLAR_SYNTHESIZER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('p', ECItems.SOLAR_PRISM.get())
				.pattern("dhd")
				.pattern("ipi")
				.pattern("wcw")
				.save(recipeOutput);
		/* prepareInstrumentRecipe(ECBlocks.MANA_SYNTHESIZER)
				.define('s', ECBlocks.SOLAR_SYNTHESIZER.get())
				.define('p', BotaniaBlocks.manaPool)
				.define('a', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('l', BotaniaBlocks.livingrock)
				.define('m', BotaniaItems.manaDiamond)
				.pattern("msm")
				.pattern("apa")
				.pattern("lcl")
				.save(recipeOutput.withConditions(new ModLoadedCondition(BotaniaAPI.MODID)), ElementalCraftApi.createRL(ManaSynthesizerBlock.NAME)); */
		prepareWhiterockInstrumentRecipe(ECBlocks.DIFFUSER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern(" c ")
				.pattern("did")
				.pattern("wcw")
				.save(recipeOutput);
		prepareInstrumentRecipe(ECBlocks.INFUSER)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('n', Tags.Items.NUGGETS_IRON)
				.pattern("n n")
				.pattern("ici")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("i i")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER_IMPROVED.get(), ECItems.PURE_CRYSTAL.get())
				.define('s', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('b', ECBlocks.BINDER.get())
				.define('i', ECBlocks.INFUSER.get())
				.pattern("did")
				.pattern("sbs")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.CRYSTALLIZER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("iwi")
				.pattern("i i")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.WATER_MILL_GRINDSTONE.get(), ECItems.WATER_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', Items.GRINDSTONE)
				.pattern("www")
				.pattern("igi")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_GRINDSTONE.get(), ECItems.AIR_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('p', ItemTags.WOOL_CARPETS)
				.define('g', Items.GRINDSTONE)
				.pattern("php")
				.pattern("igi")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.WATER_MILL_WOOD_SAW.get(), ECItems.WATER_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('s', ECItems.DRENCHED_SAW_BLADE.get())
				.pattern("www")
				.pattern("isi")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_WOOD_SAW.get(), ECItems.AIR_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('p', ItemTags.WOOL_CARPETS)
				.define('s', Items.GRINDSTONE)
				.pattern("php")
				.pattern("isi")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.INSCRIBER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" wi")
				.pattern("wdi")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('t', Items.ENCHANTING_TABLE)
				.define('e', Tags.Items.GEMS_EMERALD)
				.define('g', ECTags.Items.PRISTINE_WATER_GEMS)
				.pattern("ege")
				.pattern("iti")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_FURNACE.get(), ECItems.FIRE_CRYSTAL.get())
				.define('f', Blocks.FURNACE)
				.pattern("www")
				.pattern("wfw")
				.pattern("wcw")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_BLAST_FURNACE.get(), ECItems.FIRE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('F', Blocks.BLAST_FURNACE)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern("www")
				.pattern("gFg")
				.pattern("ici")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURIFIER.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('e', ECTags.Items.FINE_EARTH_GEMS)
				.define('g', Tags.Items.INGOTS_GOLD)
				.pattern("gig")
				.pattern("wew")
				.pattern("ici")
				.save(recipeOutput);
	}

	private void registerPureInfuser(@Nonnull RecipeOutput recipeOutput) {
		prepareWhiterockInstrumentRecipe(ECBlocks.PURE_INFUSER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECBlocks.INFUSER.get())
				.pattern("wnw")
				.pattern("ici")
				.pattern("www")
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PEDESTAL.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.WATER_PEDESTAL.get(), ElementType.WATER)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.EARTH_PEDESTAL.get(), ElementType.EARTH)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.AIR_PEDESTAL.get(), ElementType.AIR)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(recipeOutput);
	}

	private void registerContainers(@Nonnull RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SMALL_CONTAINER.get())
				.define('g', Tags.Items.GLASS)
				.define('p', ECBlocks.PIPE_IMPAIRED.get())
				.pattern(" p ")
				.pattern("pgp")
				.pattern(" p ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern("ici")
				.pattern("pgp")
				.pattern("www")
				.save(recipeOutput);

		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_RESERVOIR.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.withElementAmount(10000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.WATER_RESERVOIR.get(), ElementType.WATER)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS)
				.withElementAmount(10000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.EARTH_RESERVOIR.get(), ElementType.EARTH)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.withElementAmount(10000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.AIR_RESERVOIR.get(), ElementType.AIR)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS)
				.withElementAmount(10000)
				.save(recipeOutput);
	}

	private static void registerShards(@Nonnull RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.POWERFUL_FIRE_SHARD.get())
				.define('#', ECItems.FIRE_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_fire_shard", has(ECItems.FIRE_SHARD))
				.save(recipeOutput, ElementalCraftApi.createRL("powerful_fire_shard_from_fire_shards"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.FIRE_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_FIRE_SHARD.get())
				.unlockedBy("has_powerful_fire_shard", has(ECItems.POWERFUL_FIRE_SHARD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.POWERFUL_WATER_SHARD.get())
				.define('#', ECItems.WATER_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_water_shard", has(ECItems.WATER_SHARD))
				.save(recipeOutput, ElementalCraftApi.createRL("powerful_water_shard_from_water_shards"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.WATER_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_WATER_SHARD.get())
				.unlockedBy("has_powerful_water_shard", has(ECItems.POWERFUL_WATER_SHARD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.POWERFUL_EARTH_SHARD.get())
				.define('#', ECItems.EARTH_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_earth_shard", has(ECItems.EARTH_SHARD))
				.save(recipeOutput, ElementalCraftApi.createRL("powerful_earth_shard_from_earth_shards"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.EARTH_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_EARTH_SHARD.get())
				.unlockedBy("has_powerful_earth_shard", has(ECItems.POWERFUL_EARTH_SHARD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.POWERFUL_AIR_SHARD.get())
				.define('#', ECItems.AIR_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_air_shard", has(ECItems.AIR_SHARD))
				.save(recipeOutput, ElementalCraftApi.createRL("powerful_air_shard_from_air_shards"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.AIR_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_AIR_SHARD.get())
				.unlockedBy("has_powerful_air_shard", has(ECItems.POWERFUL_AIR_SHARD))
				.save(recipeOutput);
	}

	private static void registerHolders(@Nonnull RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.FIRE_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_firecrystal", has(ECItems.FIRE_CRYSTAL))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.WATER_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_watercrystal", has(ECItems.WATER_CRYSTAL))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.EARTH_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_earthcrystal", has(ECItems.EARTH_CRYSTAL))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.AIR_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.AIR_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_aircrystal", has(ECItems.AIR_CRYSTAL))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.PURE_HOLDER_CORE.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern(" i ")
				.pattern("ici")
				.pattern(" i ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(recipeOutput);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_HOLDER.get())
				.setIngredient(ECItems.PURE_HOLDER_CORE.get()
				).setIngredient(ElementType.WATER, ECItems.WATER_HOLDER.get())
				.setIngredient(ElementType.FIRE, ECItems.FIRE_HOLDER.get())
				.setIngredient(ElementType.EARTH, ECItems.EARTH_HOLDER.get())
				.setIngredient(ElementType.AIR, ECItems.AIR_HOLDER.get())
				.withElementAmount(100000)
				.save(recipeOutput);
	}

	private void registerSlabsStairsWalls(RecipeOutput recipeOutput) {
		BuiltInRegistries.BLOCK.entrySet().forEach(e -> {
			var block = e.getValue();
			var key = e.getKey().location();

			if (ElementalCraft.owns(key) && !exists(block) && (block instanceof SlabBlock || block instanceof StairBlock || block instanceof WallBlock)) {
				String name = key.getPath();
				String sourceName = name.substring(0, name.length() - (block instanceof StairBlock ? 7 : 5));
				ItemLike source = BuiltInRegistries.ITEM.get(ElementalCraftApi.createRL(sourceName));
				ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block, block instanceof StairBlock ? 4 : 6).define('#', source);

				if (block instanceof SlabBlock) {
					shaped.pattern("###");
				} else if (block instanceof StairBlock) {
					shaped.pattern("#  ").pattern("## ").pattern("###");
				} else if (block instanceof WallBlock) {
					shaped.pattern("###").pattern("###");
				}
				shaped.unlockedBy("has_" + sourceName, has(source)).save(recipeOutput);
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(source), RecipeCategory.DECORATIONS, block, block instanceof SlabBlock ? 2 : 1)
						.unlockedBy("has_" + sourceName, has(source))
						.save(recipeOutput, ElementalCraftApi.createRL(name + FROM + sourceName + "_stonecutting"));
			}
		});
	}

	private void registerInertCrystal(RecipeOutput recipeOutput) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), RecipeCategory.MISC, ECItems.INERT_CRYSTAL.get(), 0.5F, 200)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(recipeOutput);
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), RecipeCategory.MISC, ECItems.INERT_CRYSTAL.get(), 0.5F, 100)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(recipeOutput, ElementalCraftApi.createRL("inert_crystal_from_blasting"));
	}

	private void registerNuggetIngotBlocks(@Nonnull RecipeOutput recipeOutput) {
		createNuggetIngotBlock(ECItems.DRENCHED_IRON_NUGGET.get(), ECTags.Items.NUGGETS_DRENCHED_IRON, ECItems.DRENCHED_IRON_INGOT.get(), ECTags.Items.INGOTS_DRENCHED_IRON, ECBlocks.DRENCHED_IRON_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON, recipeOutput);
		createNuggetIngotBlock(ECItems.SWIFT_ALLOY_NUGGET.get(), ECTags.Items.NUGGETS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_INGOT.get(), ECTags.Items.INGOTS_SWIFT_ALLOY, ECBlocks.SWIFT_ALLOY_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY, recipeOutput);
		createNuggetIngotBlock(ECItems.FIREITE_NUGGET.get(), ECTags.Items.NUGGETS_FIREITE, ECItems.FIREITE_INGOT.get(), ECTags.Items.INGOTS_FIREITE, ECBlocks.FIREITE_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_FIREITE, recipeOutput);

		createStorageBlock(ECItems.INERT_CRYSTAL.get(), ECBlocks.INERT_CRYSTAL_BLOCK.get(), recipeOutput);
		createStorageBlock(ECItems.FIRE_CRYSTAL.get(), ECBlocks.FIRE_CRYSTAL_BLOCK.get(), recipeOutput);
		createStorageBlock(ECItems.WATER_CRYSTAL.get(), ECBlocks.WATER_CRYSTAL_BLOCK.get(), recipeOutput);
		createStorageBlock(ECItems.EARTH_CRYSTAL.get(), ECBlocks.EARTH_CRYSTAL_BLOCK.get(), recipeOutput);
		createStorageBlock(ECItems.AIR_CRYSTAL.get(), ECBlocks.AIR_CRYSTAL_BLOCK.get(), recipeOutput);
	}

	private void registerPipes(RecipeOutput recipeOutput) {
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPAIRED.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern("ici")
				.save(recipeOutput);
		prepareInstrumentRecipe(ECBlocks.PIPE.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("ici")
				.save(recipeOutput);
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPROVED.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("ici")
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE.get())
				.requires(ECBlocks.PIPE_IMPAIRED.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_DRENCHED_IRON), 5)
				.unlockedBy(HAS_DRENCHED_IRON_NUGGET, has(ECTags.Items.NUGGETS_DRENCHED_IRON))
				.save(recipeOutput, ElementalCraftApi.createRL("elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE.get(), 4)
				.requires(ECBlocks.PIPE_IMPAIRED.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_DRENCHED_IRON), 2)
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(recipeOutput, ElementalCraftApi.createRL("elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(recipeOutput, ElementalCraftApi.createRL("improved_elementpipe_from_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput, ElementalCraftApi.createRL("improved_elementpipe_from_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE_IMPAIRED.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(recipeOutput, ElementalCraftApi.createRL("improved_elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE_IMPAIRED.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput, ElementalCraftApi.createRL("improved_elementpipe_from_impaired_elementpipe_and_ingot"));

		shaped(ECItems.COVER_FRAME, 8)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("iii")
				.pattern("i i")
				.pattern("iii")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(recipeOutput);

		shaped(ECItems.PIPE_PRIORITY_RINGS, 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" i ")
				.pattern("ifi")
				.pattern(" i ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput);
		shaped(ECItems.ELEMENT_PUMP)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECTags.Items.NUGGETS_FIREITE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("cfi")
				.pattern("in ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.NUGGETS_FIREITE))
				.save(recipeOutput);
		shaped(ECItems.ELEMENT_VALVE)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" r ")
				.pattern("rfr")
				.pattern(" r ")
				.unlockedBy("has_cover_frame", has(ECItems.COVER_FRAME.get()))
				.save(recipeOutput);
		shaped(ECItems.ELEMENT_BEAM, 2)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("fic")
				.pattern("in ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput);
	}

	private void registerInfusions(@Nonnull RecipeOutput recipeOutput) {
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.FIRE_CRYSTAL.get(), ElementType.FIRE)
				.save(recipeOutput);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.WATER_CRYSTAL.get(), ElementType.WATER)
				.save(recipeOutput);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.AIR_CRYSTAL.get(), ElementType.AIR)
				.save(recipeOutput);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.EARTH_CRYSTAL.get(), ElementType.EARTH)
				.save(recipeOutput);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Items.STONE), ECBlocks.WHITE_ROCK.get(), ElementType.EARTH)
				.withElementAmount(500)
				.save(recipeOutput);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.INGOTS_IRON), ECItems.DRENCHED_IRON_INGOT.get(), ElementType.WATER)
				.withElementAmount(500)
				.save(recipeOutput);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.GLASS), ECBlocks.BURNT_GLASS.get(), ElementType.FIRE)
				.withElementAmount(500)
				.save(recipeOutput);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.STRING), ECItems.AIR_SILK.get(), ElementType.AIR)
				.withElementAmount(500)
				.save(recipeOutput);
	}

	private void registerShrines(RecipeOutput recipeOutput) {
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PYLON.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Tags.Items.INGOTS_GOLD)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.VACUUM_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROWTH_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(Items.WHEAT_SEEDS)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.LAVA_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.FIRE_PYLON.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.addIngredient(Blocks.OBSIDIAN)
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Items.BLAZE_ROD)
				.withElementAmount(20000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.HARVEST_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(Items.DIAMOND_HOE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.LUMBER_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(ECItems.DRENCHED_SAW_BLADE.get())
				.addIngredient(Items.DIAMOND_AXE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ORE_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.addIngredient(Items.DIAMOND_PICKAXE)
				.withElementAmount(20000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.OVERLOAD_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(Items.CLOCK)
				.addIngredient(Items.ENDER_EYE)
				.withElementAmount(20000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SWEET_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.MILK_BUCKET)
				.withElementAmount(5000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ENDER_LOCK_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DRAGON_BREATH)
				.addIngredient(Items.OBSIDIAN)
				.withElementAmount(5000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BREEDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Tags.Items.CROPS)
				.addIngredient(Tags.Items.LEATHER)
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.withElementAmount(5000)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROVE_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(ItemTags.FLOWERS)
				.addIngredient(Tags.Items.SEEDS)
				.addIngredient(Tags.Items.CROPS)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRING_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(Items.BUCKET)
				.addIngredient(ItemTags.FISHES)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BUDDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(recipeOutput);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPAWNING_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.SPIDER_EYE)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DIAMOND)
				.save(recipeOutput);
	}

	private void registerShrineUpgrades(@Nonnull RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.RANGE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("ggg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.CAPACITY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('b', Items.BUCKET)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("gbg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.EFFICIENCY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("gdg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.STRENGTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('r', Tags.Items.RODS_BLAZE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("grg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("dfd")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.FORTUNE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('l', Tags.Items.GEMS_LAPIS)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("lll")
				.pattern("wCw").
				pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.SILK_TOUCH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sss")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.PLANTING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Tags.Items.SEEDS)
				.define('h', Items.DIAMOND_HOE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BONE_BLOCK)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("bdb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.PICKUP_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_EYE)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.VORTEX_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_PEARL)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.AIR_CRYSTAL.get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.NECTAR_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('h', Items.HONEY_BLOCK)
				.define('s', Items.SUGAR)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		/* ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', BotaniaTags.Items.PETALS)
				.define('m', BotaniaTags.Items.INGOTS_MANASTEEL)
				.define('w', BotaniaBlocks.livingrock)
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("pmp").pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput.withConditions(new ModLoadedCondition(BotaniaAPI.MODID)), BuiltInRegistries.BLOCK.getKey(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get())); */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PUMPKIN)
				.define('b', Items.BONE_MEAL)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("bpb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.PROTECTION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Items.SHIELD)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("isi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.FILLING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BUCKET)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("ibi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PRISMARINE_CRYSTALS)
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sps")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.DIAMOND_PICKAXE)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("gpg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("ses")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get()).define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('t', createScrollIngredient(Spells.TRANSLOCATION))
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("ftf")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('p', ECBlocks.PIPE_IMPROVED.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('z', createRuneIngredient("zod"))
				.pattern("ziz")
				.pattern("pCp")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(recipeOutput);
	}

	private void registerSourceDisplacementPlates(RecipeOutput recipeOutput) {
		createSourceDisplacementPlate(ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_FIRE_GEMS, recipeOutput);
		createSourceDisplacementPlate(ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_EARTH_GEMS, recipeOutput);
		createSourceDisplacementPlate(ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_WATER_GEMS, recipeOutput);
		createSourceDisplacementPlate(ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_AIR_GEMS, recipeOutput);
	}

	private void registerJewels(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.UNSET_JEWEL.get())
				.pattern("sis")
				.pattern("idi")
				.pattern("sis")
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(recipeOutput);

		createJewelRecipe(Jewels.SALMON, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" a ")
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('a', ECTags.Items.CRUDE_AIR_GEMS)
				.define('c', ECItems.WATER_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.DOLPHIN, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" w ")
				.define('w', ECTags.Items.CRUDE_WATER_GEMS)
				.define('c', ECItems.WATER_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.LEOPARD, b -> b
				.pattern(" a ")
				.pattern("cUc")
				.pattern(" e ")
				.define('a', ECTags.Items.FINE_AIR_GEMS)
				.define('e', ECTags.Items.FINE_EARTH_GEMS)
				.define('c', ECItems.AIR_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.PHOENIX, b -> b
				.pattern("fgf")
				.pattern("bUb")
				.pattern("fpf")
				.define('g', ECTags.Items.PRISTINE_FIRE_GEMS)
				.define('b', Tags.Items.RODS_BLAZE)
				.define('f', Tags.Items.FEATHERS)
				.define('p', ECItems.PURE_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.TORTOISE, b -> b
				.pattern("ses")
				.pattern("gUg")
				.pattern("ses")
				.define('g', Tags.Items.GRAVEL)
				.define('e', ECTags.Items.CRUDE_EARTH_GEMS)
				.define('s', Items.SCUTE), recipeOutput);
		createJewelRecipe(Jewels.DEMIGOD, b -> b
				.pattern("tat")
				.pattern("cUc")
				.pattern("tet")
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('a', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('t', Items.TOTEM_OF_UNDYING)
				.define('c', ECItems.PURE_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.MOLE, b -> b
				.pattern(" e ")
				.pattern("sUa")
				.pattern(" p ")
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('a', Items.DIAMOND_PICKAXE)
				.define('s', Items.DIAMOND_SHOVEL)
				.define('p', ECItems.PURE_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.TIGER, b -> b
				.pattern("sas")
				.pattern("cUc")
				.pattern("sfs")
				.define('a', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('c', ECItems.AIR_CRYSTAL.get())
				.define('f', ECTags.Items.FINE_AIR_GEMS)
				.define('s', Items.SUGAR), recipeOutput);
		createJewelRecipe(Jewels.BEAR, b -> b
				.pattern("heh")
				.pattern("cUc")
				.pattern("hfh")
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.define('f', ECTags.Items.FINE_EARTH_GEMS)
				.define('h', Items.HONEY_BOTTLE), recipeOutput);
		createJewelRecipe(Jewels.VIPER, b -> b
				.pattern(" w ")
				.pattern("sUs")
				.pattern(" e ")
				.define('e', ECTags.Items.FINE_EARTH_GEMS)
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('s', Items.SPIDER_EYE), recipeOutput);
		createJewelRecipe(Jewels.HAWK, b -> b
				.pattern("gag")
				.pattern("cUc")
				.pattern("gag")
				.define('a', ECTags.Items.FINE_AIR_GEMS)
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('c', ECItems.AIR_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.KIRIN, b -> b
				.pattern("sfs")
				.pattern("cUc")
				.pattern("sas")
				.define('f', ECTags.Items.PRISTINE_FIRE_GEMS)
				.define('a', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', ECItems.PURE_CRYSTAL.get()), recipeOutput);
		createJewelRecipe(Jewels.ARCTIC_HARES, b -> b
				.pattern(" w ")
				.pattern("fUf")
				.pattern(" a ")
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('a', ECTags.Items.CRUDE_AIR_GEMS)
				.define('f', Items.RABBIT_FOOT), recipeOutput);
		createJewelRecipe(Jewels.STRIDER, b -> b
				.pattern(" f ")
				.pattern("bUb")
				.pattern(" f ")
				.define('f', ECTags.Items.FINE_FIRE_GEMS)
				.define('b', Items.LAVA_BUCKET), recipeOutput);
		createJewelRecipe(Jewels.WATER_STRIDER, b -> b
				.pattern(" w ")
				.pattern("bUb")
				.pattern(" w ")
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('b', Items.WATER_BUCKET), recipeOutput);
		createJewelRecipe(Jewels.BASILISK, b -> b
				.pattern("fwf")
				.pattern("cUc")
				.pattern("sws")
				.define('w', ECTags.Items.PRISTINE_WATER_GEMS)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('f', Items.FERMENTED_SPIDER_EYE)
				.define('s', Items.SCUTE), recipeOutput);
		createJewelRecipe(Jewels.PIGLIN, b -> b
				.pattern("gfg")
				.pattern("pUp")
				.pattern("gcg")
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.define('f', ECTags.Items.CRUDE_FIRE_GEMS)
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('p', Items.PORKCHOP), recipeOutput);
	}

	private void registerSpells(RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.SCROLL_PAPER.get())
				.requires(ECItems.AIR_SILK.get())
				.requires(Items.PAPER)
				.requires(Items.INK_SAC)
				.unlockedBy("has_air_silk", has(ECItems.AIR_SILK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPELL_DESK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('l', Blocks.LECTERN)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.pattern("wlw")
				.pattern(" i ")
				.pattern(" w ")
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.FOCUS.get())
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" ic").pattern(" si")
				.pattern("d  ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ECItems.STAFF.get())
				.define('s', ECTags.Items.STAFF_CRAFT_SWORD)
				.define('f', ECItems.FOCUS.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.pattern(" if")
				.pattern("ihi")
				.pattern("si ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(mapToStaff(recipeOutput));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ECItems.SPELL_BOOK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('l', Tags.Items.LEATHER)
				.define('p', ECItems.SCROLL_PAPER.get())
				.pattern("slp")
				.pattern("clp").pattern("slp")
				.unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
				.save(recipeOutput);

		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.GRAVEL_FALL)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.STONE_WALL)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FIRE_BALL)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ITEM_PULL)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ENDER_STRIKE)
				.setGem(ECTags.Items.CRUDE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ANIMAL_GROWTH)
				.setGem(ECTags.Items.CRUDE_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TREE_FALL)
				.setGem(ECTags.Items.FINE_EARTH_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.PURIFICATION)
				.setGem(ECTags.Items.CRUDE_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.RIPENING)
				.setGem(ECTags.Items.INPUT_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FLAME_CLEAVE)
				.setGem(ECTags.Items.CRUDE_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.INFERNO)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.DASH)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SILK_VEIN)
				.setGem(ECTags.Items.PRISTINE_EARTH_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TRANSLOCATION)
				.setGem(ECTags.Items.PRISTINE_AIR_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FEATHER_SPIKES)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.HEAL)
				.setGem(ECTags.Items.PRISTINE_WATER_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SPEED)
				.setGem(ECTags.Items.PRISTINE_AIR_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SHOCKWAVE)
				.setGem(ECTags.Items.CRUDE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.AIR_SHIELD)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(recipeOutput);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.REPAIR)
				.setGem(ECTags.Items.PRISTINE_FIRE_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(recipeOutput);
	}

	private void registerToolInfusions(RecipeOutput recipeOutput) {
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.MOB_LOOTING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.FIRE_ASPECT).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.SHARPNESS).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, ElementalCraftApi.createRL("attack_speed")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_FORTUNE).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_EFFICIENCY).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_FORTUNE).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_EFFICIENCY).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_FORTUNE).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_EFFICIENCY).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.MOB_LOOTING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.FIRE_ASPECT).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.SHARPNESS /* TODO cleaving ? */).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.BLOCK_EFFICIENCY).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.BLOCK_FORTUNE).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.BLOCK_EFFICIENCY).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.PUNCH_ARROWS).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.FLAMING_ARROWS).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, ElementalCraftApi.createRL(FastDrawToolInfusionEffect.NAME)).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.MULTISHOT).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.PIERCING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.QUICK_CHARGE).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_LUCK).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_SPEED).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.LOYALTY).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.IMPALING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.RIPTIDE).save(recipeOutput);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.RESPIRATION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.FIRE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.ALL_DAMAGE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.PROJECTILE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.BLAST_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.FIRE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.ALL_DAMAGE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, ElementalCraftApi.createRL(DodgeToolInfusionEffect.NAME)).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.BLAST_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.FIRE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.ALL_DAMAGE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, ElementalCraftApi.createRL("movement_speed")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.DEPTH_STRIDER).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FIRE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.ALL_DAMAGE_PROTECTION).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FALL_PROTECTION).save(recipeOutput);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("fire_reduction")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("water_reduction")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("earth_reduction")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("air_reduction")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("fire_staff")).withElementAmount(5000).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("water_staff")).withElementAmount(5000).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("earth_staff")).withElementAmount(5000).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("air_staff")).withElementAmount(5000).save(recipeOutput);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.FIRE_LENS.get(), ElementalCraftApi.createRL("fire_unbreaking")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.WATER_LENS.get(), ElementalCraftApi.createRL("water_unbreaking")).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.EARTH_LENS.get(), Enchantments.UNBREAKING).save(recipeOutput);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.AIR_LENS.get(), ElementalCraftApi.createRL("air_unbreaking")).save(recipeOutput);
	}

	private void registerGrinding(RecipeOutput recipeOutput) {
		GrindingRecipeBuilder.grindingRecipe(Items.COBBLESTONE)
				.withIngredient(Tags.Items.STONE)
				.withLuckRatio(1)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.GRAVEL)
				.withIngredient(Tags.Items.COBBLESTONE)
				.withLuckRatio(2)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.SAND)
				.withIngredient(Tags.Items.GRAVEL)
				.withLuckRatio(5)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.BLAZE_POWDER)
				.withCount(3)
				.withIngredient(Tags.Items.RODS_BLAZE)
				.withLuckRatio(3)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.NETHERITE_SCRAP)
				.withCount(2)
				.withIngredient(Tags.Items.ORES_NETHERITE_SCRAP)
				.withElementAmount(5000)
				.withLuckRatio(1)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(ECItems.INERT_CRYSTAL.get())
				.withCount(2)
				.withIngredient(ECTags.Items.ORES_INERT_CRYSTAL)
				.withLuckRatio(5)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.POINTED_DRIPSTONE)
				.withCount(3)
				.withIngredient(Items.DRIPSTONE_BLOCK)
				.withLuckRatio(1)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.AMETHYST_SHARD)
				.withCount(6)
				.withIngredient(Items.AMETHYST_CLUSTER)
				.withLuckRatio(5)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(ECItems.SPRINGALINE_SHARD.get())
				.withCount(6)
				.withIngredient(ECBlocks.SPRINGALINE_CLUSTER.get())
				.withLuckRatio(5)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.BONE_MEAL)
				.withCount(4)
				.withIngredient(Tags.Items.BONES)
				.withLuckRatio(3)
				.save(recipeOutput);
		GrindingRecipeBuilder.grindingRecipe(Items.STRING)
				.withCount(4)
				.withIngredient(ItemTags.WOOL)
				.save(recipeOutput);

		grindToDye(Items.GREEN_DYE, Items.CACTUS, recipeOutput);
		grindToDye(Items.WHITE_DYE, Items.BONE_MEAL, recipeOutput);
		grindToDye(Items.BLACK_DYE, Items.INK_SAC, recipeOutput);
		grindToDye(Items.BLUE_DYE, Tags.Items.GEMS_LAPIS, recipeOutput);
		grindToDye(Items.RED_DYE, Tags.Items.CROPS_BEETROOT, recipeOutput);
		grindToDye(Items.LIME_DYE, Items.SEA_PICKLE, recipeOutput);
		grindToDye(Items.BROWN_DYE, Items.COCOA_BEANS, recipeOutput);

		grindToDye(Items.WHITE_DYE, ECTags.Items.WHITE_FLOWERS, recipeOutput);
		grindToDye(Items.ORANGE_DYE, ECTags.Items.ORANGE_FLOWERS, recipeOutput);
		grindToDye(Items.MAGENTA_DYE, ECTags.Items.MAGENTA_FLOWERS, recipeOutput);
		grindToDye(Items.LIGHT_BLUE_DYE, ECTags.Items.LIGHT_BLUE_FLOWERS, recipeOutput);
		grindToDye(Items.YELLOW_DYE, ECTags.Items.YELLOW_FLOWERS, recipeOutput);
		grindToDye(Items.LIME_DYE, ECTags.Items.LIME_FLOWERS, recipeOutput);
		grindToDye(Items.PINK_DYE, ECTags.Items.PINK_FLOWERS, recipeOutput);
		grindToDye(Items.GRAY_DYE, ECTags.Items.GRAY_FLOWERS, recipeOutput);
		grindToDye(Items.LIGHT_GRAY_DYE, ECTags.Items.LIGHT_GRAY_FLOWERS, recipeOutput);
		grindToDye(Items.CYAN_DYE, ECTags.Items.CYAN_FLOWERS, recipeOutput);
		grindToDye(Items.PURPLE_DYE, ECTags.Items.PURPLE_FLOWERS, recipeOutput);
		grindToDye(Items.BLUE_DYE, ECTags.Items.BLUE_FLOWERS, recipeOutput);
		grindToDye(Items.BROWN_DYE, ECTags.Items.BROWN_FLOWERS, recipeOutput);
		grindToDye(Items.GREEN_DYE, ECTags.Items.GREEN_FLOWERS, recipeOutput);
		grindToDye(Items.BLACK_DYE, ECTags.Items.BLACK_FLOWERS, recipeOutput);
		grindToDye(Items.RED_DYE, ECTags.Items.RED_FLOWERS, recipeOutput);
	}

	private void grindToDye(ItemLike dye, ItemLike from, RecipeOutput recipeOutput) {
		GrindingRecipeBuilder.grindingRecipe(dye)
				.withCount(2)
				.withIngredient(from)
				.withLuckRatio(2)
				.save(recipeOutput, BuiltInRegistries.ITEM.getKey(dye.asItem()).getPath() + FROM + BuiltInRegistries.ITEM.getKey(from.asItem()).getPath());
	}

	private void grindToDye(ItemLike dye, TagKey<Item> from, RecipeOutput recipeOutput) {
		var tagName = from.location();

		GrindingRecipeBuilder.grindingRecipe(dye)
				.withCount(2)
				.withIngredient(from)
				.withLuckRatio(2)
				.save(recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tagName))), ElementalCraftApi.createRL(IGrindingRecipe.NAME + '/' + BuiltInRegistries.ITEM.getKey(dye.asItem()).getPath() + FROM + tagName.getNamespace() + '_' + StringUtils.replaceChars(tagName.getPath(), '/', '_')));
	}

	private void registerSawing(RecipeOutput recipeOutput) {
		sawingRecipe(Items.STRIPPED_OAK_LOG, Items.STRIPPED_OAK_WOOD, Items.OAK_PLANKS, Items.OAK_LOG, Items.OAK_WOOD, ECTags.Items.STRIPPED_OAK, recipeOutput);
		sawingRecipe(Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_WOOD, Items.DARK_OAK_PLANKS, Items.DARK_OAK_LOG, Items.DARK_OAK_WOOD, ECTags.Items.STRIPPED_DARK_OAK, recipeOutput);
		sawingRecipe(Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_BIRCH_WOOD, Items.BIRCH_PLANKS, Items.BIRCH_LOG, Items.BIRCH_WOOD, ECTags.Items.STRIPPED_BIRCH, recipeOutput);
		sawingRecipe(Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_ACACIA_WOOD, Items.ACACIA_PLANKS, Items.ACACIA_LOG, Items.ACACIA_WOOD, ECTags.Items.STRIPPED_ACACIA, recipeOutput);
		sawingRecipe(Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_JUNGLE_WOOD, Items.JUNGLE_PLANKS, Items.JUNGLE_LOG, Items.JUNGLE_WOOD, ECTags.Items.STRIPPED_JUNGLE, recipeOutput);
		sawingRecipe(Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_SPRUCE_WOOD, Items.SPRUCE_PLANKS, Items.SPRUCE_LOG, Items.SPRUCE_WOOD, ECTags.Items.STRIPPED_SPRUCE, recipeOutput);
		sawingRecipe(Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_MANGROVE_WOOD, Items.MANGROVE_PLANKS, Items.MANGROVE_LOG, Items.MANGROVE_WOOD, ECTags.Items.STRIPPED_MANGROVE, recipeOutput);
		sawingRecipe(Items.STRIPPED_CRIMSON_STEM, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.CRIMSON_STEM, Items.CRIMSON_HYPHAE, ECTags.Items.STRIPPED_CRIMSON, recipeOutput);
		sawingRecipe(Items.STRIPPED_WARPED_STEM, Items.STRIPPED_WARPED_HYPHAE, Items.WARPED_PLANKS, Items.WARPED_STEM, Items.WARPED_HYPHAE, ECTags.Items.STRIPPED_WARPED, recipeOutput);
		sawingRecipe(Items.STRIPPED_CHERRY_LOG, Items.STRIPPED_CHERRY_WOOD, Items.CHERRY_PLANKS, Items.CHERRY_LOG, Items.CHERRY_WOOD, ECTags.Items.STRIPPED_CHERRY, recipeOutput);
	}

	private void sawingRecipe(ItemLike stripedLog, ItemLike stripedWood, ItemLike planks, ItemLike log, ItemLike wood, TagKey<Item> stripped, RecipeOutput recipeOutput) {
		SawingRecipeBuilder.sawingRecipe(stripedLog)
				.withIngredient(log)
				.withElementAmount(250)
				.save(recipeOutput);
		SawingRecipeBuilder.sawingRecipe(stripedWood)
				.withIngredient(wood)
				.withElementAmount(250)
				.save(recipeOutput);
		SawingRecipeBuilder.sawingRecipe(planks)
				.withCount(6)
				.withIngredient(stripped)
				.withLuckRatio(3)
				.save(recipeOutput);
	}

	private void registerRunes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.MINOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.MAJOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(recipeOutput);

		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("wii"), ElementType.AIR)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_AIR_GEMS)
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.SUGAR)
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("fus"), ElementType.AIR)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Tags.Items.STRING)
				.addIngredient(Tags.Items.STRING)
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("zod"), ElementType.AIR)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS)
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("manx"), ElementType.FIRE)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_FIRE_GEMS)
				.addIngredient(ItemTags.COALS)
				.addIngredient(ItemTags.COALS)
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("jita"), ElementType.FIRE)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(Items.BLAZE_ROD)
				.addIngredient(Items.BLAZE_ROD)
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("tano"), ElementType.FIRE)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_COAL)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_COAL)
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("soaryn"), ElementType.EARTH)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(createRuneIngredient("wii"))
				.addIngredient(createRuneIngredient("manx"))
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("kaworu"), ElementType.EARTH)
				.withElementAmount(10000)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(createRuneIngredient("fus"))
				.addIngredient(createRuneIngredient("jita"))
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("mewtwo"), ElementType.EARTH)
				.withElementAmount(20000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.addIngredient(createRuneIngredient("zod"))
				.addIngredient(createRuneIngredient("tano"))
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("claptrap"), ElementType.WATER)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Tags.Items.GEMS_LAPIS)
				.addIngredient(Tags.Items.GEMS_LAPIS)
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("bombadil"), ElementType.WATER)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.save(recipeOutput);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraftApi.createRL("tzeentch"), ElementType.WATER)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS)
				.addIngredient(Tags.Items.GEMS_EMERALD)
				.addIngredient(Tags.Items.GEMS_EMERALD)
				.save(recipeOutput);
	}

	private void registerCrystallizations(@Nonnull RecipeOutput recipeOutput) {
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.setShard(ECTags.Items.FIRE_SHARDS)
				.addOutput(ECItems.CRUDE_FIRE_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_FIRE_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_FIRE_GEM.get(), 1, 2)
				.save(recipeOutput, "fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER)
				.setGem(ECTags.Items.INPUT_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.setShard(ECTags.Items.WATER_SHARDS)
				.addOutput(ECItems.CRUDE_WATER_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_WATER_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_WATER_GEM.get(), 1, 2)
				.save(recipeOutput, "water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.setShard(ECTags.Items.EARTH_SHARDS)
				.addOutput(ECItems.CRUDE_EARTH_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_EARTH_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_EARTH_GEM.get(), 1, 2)
				.save(recipeOutput, "earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR)
				.setGem(ECTags.Items.INPUT_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.setShard(ECTags.Items.AIR_SHARDS)
				.addOutput(ECItems.CRUDE_AIR_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_AIR_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_AIR_GEM.get(), 1, 2)
				.save(recipeOutput, "air_gem");

		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_FIRE_GEM.get(), 1)
				.save(recipeOutput, "pristine_fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER)
				.setGem(ECTags.Items.INPUT_WATER_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_WATER_GEM.get(), 1)
				.save(recipeOutput, "pristine_water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_EARTH_GEM.get(), 1)
				.save(recipeOutput, "pristine_earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR)
				.setGem(ECTags.Items.INPUT_AIR_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_AIR_GEM.get(), 1)
				.save(recipeOutput, "pristine_air_gem");
	}

	private Ingredient createScrollIngredient(DeferredHolder<Spell, ? extends Spell> spell) {
		var tag = new CompoundTag();
		var ecTag = new CompoundTag();

		tag.put(ECNames.EC_NBT, ecTag);
		ecTag.putString(ECNames.SPELL, spell.getKey().location().toString());
		return NBTIngredient.of(false, tag, ECItems.SCROLL.get());
	}

	private Ingredient createRuneIngredient(String name) {
		var tag = new CompoundTag();
		var ecTag = new CompoundTag();

		tag.put(ECNames.EC_NBT, ecTag);
		ecTag.putString(ECNames.RUNE, ElementalCraftApi.createRL(name).toString());
		return  NBTIngredient.of(false, tag, ECItems.RUNE.get());
	}

	private void registerEmptying(@Nonnull RecipeOutput recipeOutput) {
		registerEmptying(ECBlocks.SMALL_CONTAINER.get(), recipeOutput);
		registerEmptying(ECBlocks.CONTAINER.get(), recipeOutput);
		registerEmptying(ECBlocks.FIRE_RESERVOIR.get(), recipeOutput);
		registerEmptying(ECBlocks.WATER_RESERVOIR.get(), recipeOutput);
		registerEmptying(ECBlocks.EARTH_RESERVOIR.get(), recipeOutput);
		registerEmptying(ECBlocks.AIR_RESERVOIR.get(), recipeOutput);
		registerEmptying(ECBlocks.CREATIVE_CONTAINER.get(), recipeOutput);
		registerEmptying(ECItems.FIRE_HOLDER.get(), recipeOutput);
		registerEmptying(ECItems.WATER_HOLDER.get(), recipeOutput);
		registerEmptying(ECItems.EARTH_HOLDER.get(), recipeOutput);
		registerEmptying(ECItems.AIR_HOLDER.get(), recipeOutput);
		registerEmptying(ECItems.PURE_HOLDER.get(), recipeOutput);
	}


	private void registerEmptying(ItemLike item, RecipeOutput recipeOutput) {
		var name = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, item)
				.requires(item)
				.unlockedBy("has_" + name, has(item))
				.save(recipeOutput, ElementalCraftApi.createRL(name + "_emptying"));
	}

	private void registerDecorations(@Nonnull RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.BURNT_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.BURNT_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_burnt_glass", has(ECBlocks.BURNT_GLASS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.WHITE_ROCK_FENCE.get(), 16)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("#i#")
				.pattern("#i#")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.WHITE_ROCK_BRICK.get(), 4)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.pattern("##")
				.pattern("##")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(recipeOutput);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ECBlocks.WHITE_ROCK.get()), RecipeCategory.DECORATIONS, ECBlocks.WHITE_ROCK_BRICK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(recipeOutput, ElementalCraftApi.createRL("whiterock_brick_from_whiterock_stonecutting"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.MOSSY_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MOSS_BLOCK)
				.pattern("###").pattern("#$#").pattern("###")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.BURNT_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MAGMA_BLOCK)
				.pattern("###").pattern("#$#")
				.pattern("###").unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_BLOCK.get())
				.define('#', ECItems.SPRINGALINE_SHARD.get())
				.pattern("##")
				.pattern("##")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_GLASS.get(), 2)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern(" s ")
				.pattern("sgs")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.SPRINGALINE_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_springaline_glass", has(ECBlocks.SPRINGALINE_GLASS))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_LANTERN.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', Items.GLOWSTONE)
				.define('p', Tags.Items.GEMS_PRISMARINE)
				.pattern("psp")
				.pattern("sgs")
				.pattern("psp")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(recipeOutput);
	}

	private void registerSourceBreeding(@Nonnull RecipeOutput recipeOutput) {
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER_PEDESTAL.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.pattern("wsw")
				.pattern("ici")
				.pattern("www")
				.save(recipeOutput);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER.get(), ECItems.PURE_CRYSTAL.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('p', ECBlocks.SOURCE_BREEDER_PEDESTAL.get())
				.pattern("iwi")
				.pattern("fcf")
				.pattern("wpw")
				.save(recipeOutput);


		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.ARTIFICIAL_FIRE_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_FIRE_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.ARTIFICIAL_WATER_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_WATER_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.ARTIFICIAL_EARTH_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_EARTH_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_AIR_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.NATURAL_FIRE_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_FIRE_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.FIRE))
				.unlockedBy("has_artificial_fire_source_seed", has(ECItems.ARTIFICIAL_FIRE_SOURCE_SEED.get()))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.NATURAL_WATER_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_WATER_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.WATER))
				.unlockedBy("has_artificial_water_source_seed", has(ECItems.ARTIFICIAL_WATER_SOURCE_SEED.get()))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.NATURAL_EARTH_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_EARTH_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.EARTH))
				.unlockedBy("has_artificial_earth_source_seed", has(ECItems.ARTIFICIAL_EARTH_SOURCE_SEED.get()))
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ECItems.NATURAL_AIR_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.AIR))
				.unlockedBy("has_artificial_air_source_seed", has(ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get()))
				.save(recipeOutput);
	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(BuiltInRegistries.BLOCK.getKey(block), PackType.SERVER_DATA, ".json", "recipes");
	}

	private void createSourceDisplacementPlate(Supplier<? extends ItemLike> plate, TagKey<Item> gem, RecipeOutput recipeOutput) {
		var plateItem = plate.get().asItem();

		prepareWhiterockInstrumentRecipe(plateItem, ECItems.PURE_CRYSTAL.get())
				.define('s', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('g', gem).pattern(" g ")
				.pattern("scs").pattern("www")
				.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, plateItem)
				.requires(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE.get())
				.requires(gem)
				.unlockedBy("has_broken_source_displacement_plate", has(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE))
				.save(recipeOutput, ElementalCraftApi.createRL(BuiltInRegistries.ITEM.getKey(plateItem).getPath() + "_repair"));

	}

	private void createNuggetIngotBlock(ItemLike nugget, TagKey<Item> nuggetTag, ItemLike ingot, TagKey<Item> ingotTag, ItemLike block, TagKey<Item> blockTag, RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot).define('#', nuggetTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(nugget), has(nuggetTag))
				.save(recipeOutput, from(nugget, ingot));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
				.requires(ingotTag)
				.unlockedBy(buildHas(ingot), has(ingotTag))
				.save(recipeOutput, from(ingot, nugget));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block).define('#', ingotTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(ingot), has(ingotTag)
				).save(recipeOutput, from(ingot, block));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 9)
				.requires(blockTag)
				.unlockedBy(buildHas(block), has(blockTag))
				.save(recipeOutput, from(block, ingot));
	}

	private void createStorageBlock(ItemLike item, ItemLike block, RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block)
				.define('#', item)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(item), has(item))
				.save(recipeOutput, from(item, block));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, 9)
				.requires(block)
				.unlockedBy(buildHas(block), has(block))
				.save(recipeOutput, from(block, item));
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get(), 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(Supplier<? extends ItemLike> result) {
		return prepareInstrumentRecipe(result.get());
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result, ItemLike crystal, int count) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, count)
				.define('c', crystal)
				.unlockedBy(buildHas(crystal), has(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get(), 1)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result, ItemLike crystal) {
		return prepareInstrumentRecipe(result, crystal, 1)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	public static ShapedRecipeBuilder shaped(Supplier<? extends ItemLike> item) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item.get(), 1);
	}

	public static ShapedRecipeBuilder shaped(Supplier<? extends ItemLike> item, int count) {
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item.get(), count);
	}


	private RecipeOutput mapToStaff(RecipeOutput recipeOutput) {
		return new RecipeOutput() {
			@Override
			@NotNull
			public Advancement.Builder advancement() {
				return recipeOutput.advancement();
			}

			@Override
			public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition @NotNull ... iConditions) {
				recipeOutput.accept(resourceLocation, recipe instanceof ShapedRecipe shaped ? new StaffRecipe(shaped) : recipe, advancementHolder, iConditions);
			}
		};
	}

	private void createJewelRecipe(Supplier<? extends Jewel> jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder, RecipeOutput recipeOutput) {
		createJewelRecipe(jewel.get(), patternBuilder, recipeOutput);
	}

	private void createJewelRecipe(Jewel jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder, RecipeOutput recipeOutput) {
		var result = new ItemStack(ECItems.JEWEL.get());

		JewelHelper.setJewel(result, jewel);

		var builder = ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result);
		var jewelKey = jewel.getKey();


		patternBuilder.apply(builder)
				.define('U', ECItems.UNSET_JEWEL.get())
				.unlockedBy("has_unset_jewel", has(ECItems.UNSET_JEWEL))
				.save(recipeOutput, new ResourceLocation(jewelKey.getNamespace(), "jewel/" + jewelKey.getPath()));
	}

	private ResourceLocation from(ItemLike from, ItemLike to) {
		return  ElementalCraftApi.createRL(BuiltInRegistries.ITEM.getKey(to.asItem()).getPath() + FROM + BuiltInRegistries.ITEM.getKey(from.asItem()).getPath());
	}

	private String buildHas(ItemLike item) {
		return "has_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(Supplier<? extends ItemLike> itemLike) {
		return has(itemLike.get().asItem());
	}
}
