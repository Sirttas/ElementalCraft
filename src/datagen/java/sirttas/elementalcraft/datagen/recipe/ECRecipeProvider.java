package sirttas.elementalcraft.datagen.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.datagen.definition.ECBlockFamilies;
import sirttas.elementalcraft.datagen.recipe.builder.CrackingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.MeltingRecipeBuilder;
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
import sirttas.elementalcraft.interaction.patchouli.PatchouliInteraction;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.recipe.StaffRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.tag.ECTags;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ECRecipeProvider extends RecipeProvider {

	private static final String HAS_INERT_CRYSTAL = "has_inert_crystal";
	private static final String HAS_CONTAINED_CRYSTAL = "has_contained_crystal";
	private static final String HAS_PURECRYSTAL = "has_purecrystal";
	private static final String HAS_WHITEROCK = "has_whiterock";
	private static final String HAS_SHRINE_UPGRADE_CORE = "has_shrine_upgrade_core";
	private static final String HAS_ADVANCED_SHRINE_UPGRADE_CORE = "has_advanced_shrine_upgrade_core";
	private static final String HAS_SPRINGALINE_SHARD = "has_springaline_shard";

	private static final String HAS_DRENCHED_IRON_NUGGET = "has_drenched_iron_nugget";
	private static final String HAS_DRENCHED_IRON_INGOT = "has_drenched_iron_ingot";
	private static final String HAS_SWIFT_ALLOY_NUGGET = "has_swift_alloy_nugget";
	private static final String HAS_SWIFT_ALLOY_INGOT = "has_swift_alloy_ingot";
	private static final String HAS_FIREITE_INGOT = "has_fireite_ingot";
	public static final String FROM = "_from_";

	protected final HolderGetter<Block> blocks;

	public ECRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		super(registries, output);
		blocks = registries.lookupOrThrow(Registries.BLOCK);
	}

	@Override
	protected void buildRecipes() {
		generateForEnabledBlockFamilies(FeatureFlags.DEFAULT_FLAGS);
		generateInertCrystal();
		generateNuggetIngotBlocks();
		generateMaterials();
		generatePipes();
		generateContainers();
		generateExtractors();
		generateSynthesizers();
		generateInstruments();
		generatePureInfuser();
		generateInfusions();
		generateSpringaline();
		generateHolders();
		generateTools();
		generateShrines();
		generateShrineUpgrades();
		generateJewels();
		generateSpells();
		generateToolInfusions();
		generateGrinding();
		generateSawing();
		generateRunes();
		generateEmptying();
		generateCrystallizations();
		generateDecorations();
		generateSourceBreeding();
		generateCracking();
		generateMelting();
	}

	@Override
	protected void generateForEnabledBlockFamilies(FeatureFlagSet flagSet) {
		ECBlockFamilies.getAllFamilies().forEach(family -> this.generateRecipes(family, flagSet));
	}

	private void generateMaterials() {
		shaped(RecipeCategory.MISC, ECItems.CONTAINED_CRYSTAL.get())
				.define('g', Tags.Items.NUGGETS_GOLD)
				.define('c', ECItems.INERT_CRYSTAL.get())
				.pattern(" g ")
				.pattern("gcg")
				.pattern(" g ")
				.unlockedBy(HAS_INERT_CRYSTAL, has(ECItems.INERT_CRYSTAL))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('g', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.pattern("sgs")
				.pattern("gcg")
				.pattern("sgs")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.SHRINE_BASE.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.INERT_CRYSTAL.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern(" p ")
				.pattern("pcp")
				.pattern("www")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.SHRINE_UPGRADE_CORE.get())
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("ici")
				.pattern("rir")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('i', Tags.Items.INGOTS_GOLD)
				.define('s', ECItems.SOLAR_PRISM.get())
				.define('n', ECTags.Items.NUGGETS_FIREITE)
				.pattern("nsn")
				.pattern("iCi")
				.pattern("ncn")
				.unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.SOLAR_PRISM.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', Tags.Items.INGOTS_COPPER)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" s ")
				.pattern("cdc")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.DRENCHED_SAW_BLADE.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_DRENCHED_IRON)
				.define('r', Tags.Items.INGOTS_IRON)
				.pattern("nin")
				.pattern("iri")
				.pattern("nin")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(this.output);

		BindingRecipeBuilder.bindingRecipe(ECItems.SWIFT_ALLOY_INGOT.get(), ElementType.AIR)
				.addIngredient(tag(Tags.Items.INGOTS_GOLD))
				.addIngredient(tag(ECTags.Items.INGOTS_DRENCHED_IRON))
				.addIngredient(tag(Tags.Items.INGOTS_COPPER))
				.addIngredient(tag(Tags.Items.DUSTS_REDSTONE))
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.withElementAmount(1250)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECItems.FIREITE_INGOT.get(), ElementType.FIRE)
				.addIngredient(tag(Tags.Items.INGOTS_NETHERITE))
				.addIngredient(tag(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.withElementAmount(30000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARDENED_HANDLE.get(), ElementType.EARTH)
				.addIngredient(tag(Tags.Items.RODS_WOODEN))
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.withElementAmount(1250)
				.save(this.output);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_CRYSTAL.get())
				.setIngredient(tag(Tags.Items.GEMS_DIAMOND))
				.setIngredient(ElementType.WATER, ECItems.WATER_CRYSTAL.get())
				.setIngredient(ElementType.FIRE, ECItems.FIRE_CRYSTAL.get())
				.setIngredient(ElementType.EARTH, ECItems.EARTH_CRYSTAL.get())
				.setIngredient(ElementType.AIR, ECItems.AIR_CRYSTAL.get())
				.save(this.output);

		shapeless(RecipeCategory.MISC, ECItems.PRISTINE_SHARD.get(), 8)
				.requires(ECItems.PURE_CRYSTAL.get())
				.requires(ECTags.Items.CHISELS)
				.unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
				.save(this.output);
	}


	private void generateSpringaline() {
		BindingRecipeBuilder.bindingRecipe(ECItems.SPRINGALINE_SHARD.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_SHARD)
				.addIngredient(tag(Tags.Items.GEMS_QUARTZ))
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRINGALINE_CLUSTER.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(Items.QUARTZ_BLOCK /* FIXME use all quartz blocks */)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(this.output);
	}

	private void generateTools() {
		shapeless(RecipeCategory.TOOLS, PatchouliInteraction.createElementopedia())
				.requires(ECItems.INERT_CRYSTAL.get())
				.requires(Items.BOOK)
				.unlockedBy(HAS_INERT_CRYSTAL, has(ECItems.INERT_CRYSTAL))
				.save(this.output.withConditions(new ModLoadedCondition(PatchouliAPI.MOD_ID)), createRecipeKey("element_book"));

		shaped(RecipeCategory.TOOLS, ECItems.EMPTY_RECEPTACLE.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.pattern(" g ")
				.pattern("ici")
				.pattern(" g ")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.SOURCE_STABILIZER.get(), 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.pattern("sis")
				.pattern("i i")
				.pattern("sis")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.SOURCE_ANALYSIS_GLASS.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.pattern(" sg")
				.pattern(" is")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.DRENCHED_IRON_CHISEL.get())
				.define('h', Tags.Items.RODS_WOODEN)
				.define('s', ECItems.AIR_SILK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern(" i ")
				.pattern(" si")
				.pattern("h  ")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.SWIFT_ALLOY_CHISEL.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('s', ECItems.AIR_SILK.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" i ")
				.pattern(" si")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.FIREITE_CHISEL.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('s', ECItems.AIR_SILK.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.pattern(" i ")
				.pattern(" si")
				.pattern("h  ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECBlocks.TRANSLOCATION_ANCHOR.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('g', ECItems.PRISTINE_AIR_GEM.get())
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('e', Items.ENDER_EYE)
				.pattern(" e ")
				.pattern("fgf")
				.pattern("www")
				.unlockedBy("has_fireite_nugget", has(ECTags.Items.NUGGETS_FIREITE))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECBlocks.RETRIEVER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("iw ")
				.pattern("hdi")
				.pattern("iw "
				).save(this.output);
		shaped(RecipeCategory.TOOLS, ECBlocks.ORDERED_SORTER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("ii ")
				.pattern("hdi")
				.pattern("ii ")
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.AIR_MILL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('c', ItemTags.WOOL_CARPETS)
				.pattern("cic")
				.pattern("cic")
				.pattern(" h ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECBlocks.PURE_ROCK.get())
				.setIngredient(Items.OBSIDIAN)
				.setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, tag(ECTags.Items.INGOTS_FIREITE))
				.setIngredient(ElementType.EARTH, ECBlocks.WHITE_ROCK.get())
				.setIngredient(ElementType.AIR, Items.PURPUR_BLOCK)
				.save(this.output);

		BindingRecipeBuilder.bindingRecipe(ECItems.ELEMENTAL_FIREFUEL.get(), ElementType.FIRE)
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(tag(ItemTags.COALS))
				.addIngredient(tag(Tags.Items.RODS_BLAZE))
				.addIngredient(Items.LAVA_BUCKET)
				.withElementAmount(20000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_LENS.get(), ElementType.FIRE)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(tag(Tags.Items.INGOTS_COPPER))
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.save(this.output);
	}

	private void generateExtractors() {
		prepareInstrumentRecipe(ECBlocks.RUDIMENTARY_EXTRACTOR)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern(" c ")
				.pattern(" i ")
				.pattern("ici")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.EXTRACTOR.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('e', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
				.pattern("e e")
				.pattern("idi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.IMPROVED_EXTRACTOR.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('r', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
				.pattern(" r ")
				.pattern("eie")
				.pattern("wcw")
				.save(this.output);
	}

	private void generateSynthesizers() {
		prepareInstrumentRecipe(ECBlocks.CRACKING_SYNTHESIZER)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('p', Items.STONE_PICKAXE)
				.pattern("ipi")
				.pattern(" c ")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.COMBUSTION_SYNTHESIZER.get())
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern("i i")
				.pattern("wcw")
				.save(this.output);
		prepareInstrumentRecipe(ECBlocks.DRAINING_SYNTHESIZER)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern("igi")
				.pattern(" c ")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.VIBRATION_SYNTHESIZER.get())
				.define('i', ECTags.Items.NUGGETS_DRENCHED_IRON)
				.define('p', ItemTags.WOOL_CARPETS)
				.pattern("ipi")
				.pattern("wcw")
				.save(this.output);

		prepareWhiterockInstrumentRecipe(ECBlocks.SOLAR_SYNTHESIZER.get(), ECItems.FIRE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('p', ECItems.SOLAR_PRISM.get())
				.pattern("dhd")
				.pattern("ipi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.CULINARY_SYNTHESIZER.get(), ECItems.WATER_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('l', Items.CAKE)
				.define('b', Items.BUCKET)
				.pattern(" l ")
				.pattern("ibi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get(), ECItems.EARTH_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('h', Items.DIAMOND_HOE)
				.pattern("ihi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_SYNTHESIZER.get(), ECItems.AIR_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('a', ECItems.AIR_MILL.get())
				.pattern("iai")
				.pattern("wcw")
				.save(this.output);
	}

	private void generateInstruments() {
		prepareWhiterockInstrumentRecipe(ECBlocks.DIFFUSER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern(" c ")
				.pattern("did")
				.pattern("wcw")
				.save(this.output);
		prepareInstrumentRecipe(ECBlocks.INFUSER)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('n', Tags.Items.NUGGETS_IRON)
				.pattern("n n")
				.pattern("ici")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("i i")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER_IMPROVED.get(), ECItems.PURE_CRYSTAL.get())
				.define('s', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('b', ECBlocks.BINDER.get())
				.define('i', ECBlocks.INFUSER.get())
				.pattern("did")
				.pattern("sbs")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.CRYSTALLIZER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("iwi")
				.pattern("i i")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.WATER_MILL_GRINDSTONE.get(), ECItems.WATER_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', Items.GRINDSTONE)
				.pattern("www")
				.pattern("igi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_GRINDSTONE.get(), ECItems.AIR_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('a', ECItems.AIR_MILL.get())
				.define('g', Items.GRINDSTONE)
				.pattern(" a ")
				.pattern("igi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.WATER_MILL_WOOD_SAW.get(), ECItems.WATER_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('s', ECItems.DRENCHED_SAW_BLADE.get())
				.pattern("www")
				.pattern("isi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_WOOD_SAW.get(), ECItems.AIR_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('a', ECItems.AIR_MILL.get())
				.define('s', Items.GRINDSTONE)
				.pattern(" a ")
				.pattern("isi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.INSCRIBER.get(), ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" wi")
				.pattern("wdi")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('t', Items.ENCHANTING_TABLE)
				.define('e', Tags.Items.GEMS_EMERALD)
				.define('g', ECItems.PRISTINE_WATER_GEM.get())
				.pattern("ege")
				.pattern("iti")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_FURNACE.get(), ECItems.FIRE_CRYSTAL.get())
				.define('f', Blocks.FURNACE)
				.pattern("www")
				.pattern("wfw")
				.pattern("wcw")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_BLAST_FURNACE.get(), ECItems.FIRE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('F', Blocks.BLAST_FURNACE)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern("www")
				.pattern("gFg")
				.pattern("ici")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURIFIER.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('e', ECItems.FINE_EARTH_GEM.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.pattern("gig")
				.pattern("wew")
				.pattern("ici")
				.save(this.output);
	}

	private void generatePureInfuser() {
		prepareWhiterockInstrumentRecipe(ECBlocks.PURE_INFUSER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECBlocks.INFUSER.get())
				.pattern("wnw")
				.pattern("ici")
				.pattern("www")
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PEDESTAL.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_FIRE_GEM.get())
				.addIngredient(tag(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.WATER_PEDESTAL.get(), ElementType.WATER)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_WATER_GEM.get())
				.addIngredient(tag(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.EARTH_PEDESTAL.get(), ElementType.EARTH)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_EARTH_GEM.get())
				.addIngredient(tag(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.AIR_PEDESTAL.get(), ElementType.AIR)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECItems.FINE_AIR_GEM.get())
				.addIngredient(tag(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(this.output);
	}

	private void generateContainers() {
		shaped(RecipeCategory.DECORATIONS, ECBlocks.SMALL_CONTAINER.get())
				.define('g', Tags.Items.GLASS_BLOCKS)
				.define('p', ECBlocks.PIPE_RUDIMENTARY.get())
				.pattern(" p ")
				.pattern("pgp")
				.pattern(" p ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern("ici")
				.pattern("pgp")
				.pattern("www")
				.save(this.output);

		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_RESERVOIR.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECItems.PRISTINE_FIRE_GEM.get())
				.withElementAmount(10000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.WATER_RESERVOIR.get(), ElementType.WATER)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECItems.PRISTINE_WATER_GEM.get())
				.withElementAmount(10000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.EARTH_RESERVOIR.get(), ElementType.EARTH)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECItems.PRISTINE_EARTH_GEM.get())
				.withElementAmount(10000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.AIR_RESERVOIR.get(), ElementType.AIR)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECItems.PRISTINE_AIR_GEM.get())
				.withElementAmount(10000)
				.save(this.output);
	}

	private void generateHolders() {
		shaped(RecipeCategory.TOOLS, ECItems.FIRE_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_firecrystal", has(ECItems.FIRE_CRYSTAL))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.WATER_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_watercrystal", has(ECItems.WATER_CRYSTAL))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.EARTH_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_earthcrystal", has(ECItems.EARTH_CRYSTAL))
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.AIR_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.RUDIMENTARY_EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.AIR_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_aircrystal", has(ECItems.AIR_CRYSTAL))
				.save(this.output);

		shaped(RecipeCategory.MISC, ECItems.PURE_HOLDER_CORE.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern(" i ")
				.pattern("ici")
				.pattern(" i ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(this.output);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_HOLDER.get())
				.setIngredient(ECItems.PURE_HOLDER_CORE.get()
				).setIngredient(ElementType.WATER, ECItems.WATER_HOLDER.get())
				.setIngredient(ElementType.FIRE, ECItems.FIRE_HOLDER.get())
				.setIngredient(ElementType.EARTH, ECItems.EARTH_HOLDER.get())
				.setIngredient(ElementType.AIR, ECItems.AIR_HOLDER.get())
				.withElementAmount(100000)
				.save(this.output);
	}

	private void generateInertCrystal() {
		SimpleCookingRecipeBuilder.smelting(tag(ECTags.Items.ORES_INERT_CRYSTAL), RecipeCategory.MISC, CookingBookCategory.BLOCKS, ECItems.INERT_CRYSTAL.get(), 0.5F, 200)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(this.output);
		SimpleCookingRecipeBuilder.blasting(tag(ECTags.Items.ORES_INERT_CRYSTAL), RecipeCategory.MISC, CookingBookCategory.BLOCKS, ECItems.INERT_CRYSTAL.get(), 0.5F, 100)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(this.output, createRecipeKey("inert_crystal_from_blasting"));

	}

	private void generateNuggetIngotBlocks() {
		createNuggetIngotBlock(ECItems.DRENCHED_IRON_NUGGET.get(), ECTags.Items.NUGGETS_DRENCHED_IRON, ECItems.DRENCHED_IRON_INGOT.get(), ECTags.Items.INGOTS_DRENCHED_IRON, ECBlocks.DRENCHED_IRON_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON);
		createNuggetIngotBlock(ECItems.SWIFT_ALLOY_NUGGET.get(), ECTags.Items.NUGGETS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_INGOT.get(), ECTags.Items.INGOTS_SWIFT_ALLOY, ECBlocks.SWIFT_ALLOY_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY);
		createNuggetIngotBlock(ECItems.FIREITE_NUGGET.get(), ECTags.Items.NUGGETS_FIREITE, ECItems.FIREITE_INGOT.get(), ECTags.Items.INGOTS_FIREITE, ECBlocks.FIREITE_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_FIREITE);

		createStorageBlock(ECItems.INERT_CRYSTAL.get(), ECBlocks.INERT_CRYSTAL_BLOCK.get());
		createStorageBlock(ECItems.FIRE_CRYSTAL.get(), ECBlocks.FIRE_CRYSTAL_BLOCK.get());
		createStorageBlock(ECItems.WATER_CRYSTAL.get(), ECBlocks.WATER_CRYSTAL_BLOCK.get());
		createStorageBlock(ECItems.EARTH_CRYSTAL.get(), ECBlocks.EARTH_CRYSTAL_BLOCK.get());
		createStorageBlock(ECItems.AIR_CRYSTAL.get(), ECBlocks.AIR_CRYSTAL_BLOCK.get());
	}

	private void generatePipes() {
		prepareInstrumentRecipe(ECBlocks.PIPE_RUDIMENTARY.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern("ici")
				.save(this.output);
		prepareInstrumentRecipe(ECBlocks.PIPE.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("ici")
				.save(this.output);
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPROVED.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("ici")
				.save(this.output);

		shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE.get())
				.requires(ECBlocks.PIPE_RUDIMENTARY.get())
				.requires(tag(ECTags.Items.NUGGETS_DRENCHED_IRON), 5)
				.unlockedBy(HAS_DRENCHED_IRON_NUGGET, has(ECTags.Items.NUGGETS_DRENCHED_IRON))
				.save(this.output, createRecipeKey("elementpipe_from_rudimentary_elementpipe_and_nugget"));
		shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE.get(), 4)
				.requires(ECBlocks.PIPE_RUDIMENTARY.get(), 4)
				.requires(tag(ECTags.Items.INGOTS_DRENCHED_IRON), 2)
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(this.output, createRecipeKey("elementpipe_from_rudimentary_elementpipe_and_ingot"));
		shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE.get())
				.requires(tag(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(this.output, createRecipeKey("improved_elementpipe_from_elementpipe_and_nugget"));
		shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE.get(), 4)
				.requires(tag(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output, createRecipeKey("improved_elementpipe_from_elementpipe_and_ingot"));
		shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE_RUDIMENTARY.get())
				.requires(tag(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(this.output, createRecipeKey("improved_elementpipe_from_rudimentary_elementpipe_and_nugget"));
		shapeless(RecipeCategory.DECORATIONS, ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE_RUDIMENTARY.get(), 4)
				.requires(tag(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output, createRecipeKey("improved_elementpipe_from_rudimentary_elementpipe_and_ingot"));

		shaped(ECItems.COVER_FRAME, 8)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("iii")
				.pattern("i i")
				.pattern("iii")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(this.output);

		shaped(ECItems.PIPE_PRIORITY_RINGS, 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" i ")
				.pattern("ifi")
				.pattern(" i ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);
		shaped(PipeUpgradeTypes.ELEMENT_PUMP)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECTags.Items.NUGGETS_FIREITE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("cfi")
				.pattern("in ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.NUGGETS_FIREITE))
				.save(this.output);
		shaped(ECItems.ELEMENT_VALVE)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" r ")
				.pattern("rfr")
				.pattern(" r ")
				.unlockedBy("has_cover_frame", has(ECItems.COVER_FRAME.get()))
				.save(this.output);
		shaped(ECItems.ELEMENT_BEAM, 2)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("fic")
				.pattern("in ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);
	}

	private void generateInfusions() {
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.FIRE_CRYSTAL.get(), ElementType.FIRE)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.WATER_CRYSTAL.get(), ElementType.WATER)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.EARTH_CRYSTAL.get(), ElementType.EARTH)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.AIR_CRYSTAL.get(), ElementType.AIR)
				.save(this.output);

		InfusionRecipeBuilder.infusionRecipe(tag(Tags.Items.GEMS_DIAMOND), ECItems.CRUDE_FIRE_GEM.get(), ElementType.FIRE)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(tag(Tags.Items.GEMS_DIAMOND), ECItems.CRUDE_WATER_GEM.get(), ElementType.WATER)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(tag(Tags.Items.GEMS_DIAMOND), ECItems.CRUDE_EARTH_GEM.get(), ElementType.EARTH)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(tag(Tags.Items.GEMS_DIAMOND), ECItems.CRUDE_AIR_GEM.get(), ElementType.AIR)
				.save(this.output);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Items.STONE), ECBlocks.WHITE_ROCK.get(), ElementType.EARTH)
				.withElementAmount(500)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(tag(Tags.Items.INGOTS_IRON), ECItems.DRENCHED_IRON_INGOT.get(), ElementType.WATER)
				.withElementAmount(500)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(tag(Tags.Items.GLASS_BLOCKS), ECBlocks.BURNT_GLASS.get(), ElementType.FIRE)
				.withElementAmount(500)
				.save(this.output);
		InfusionRecipeBuilder.infusionRecipe(tag(Tags.Items.STRINGS), ECItems.AIR_SILK.get(), ElementType.AIR)
				.withElementAmount(500)
				.save(this.output);
	}

	private void generateShrines() {
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PYLON.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(tag(Tags.Items.INGOTS_GOLD))
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.VACUUM_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER)
				.addIngredient(tag(Tags.Items.GEMS_DIAMOND))
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROWTH_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(Items.WHEAT_SEEDS)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(tag(Tags.Items.GEMS_DIAMOND))
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.MELTING_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.FIRE_PYLON.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(ECItems.PRISTINE_FIRE_GEM.get())
				.addIngredient(Blocks.OBSIDIAN)
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Items.BLAZE_ROD)
				.withElementAmount(20000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.HARVEST_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECItems.CRUDE_EARTH_GEM.get())
				.addIngredient(Items.DIAMOND_HOE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.LUMBER_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECItems.CRUDE_EARTH_GEM.get())
				.addIngredient(ECItems.DRENCHED_SAW_BLADE.get())
				.addIngredient(Items.DIAMOND_AXE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ORE_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECItems.PRISTINE_EARTH_GEM.get())
				.addIngredient(Items.DIAMOND_PICKAXE)
				.withElementAmount(20000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.OVERLOAD_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(Items.CLOCK)
				.addIngredient(Items.ENDER_EYE)
				.withElementAmount(20000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SWEET_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.MILK_BUCKET)
				.withElementAmount(5000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ENDER_LOCK_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.FINE_AIR_GEM.get())
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DRAGON_BREATH)
				.addIngredient(Items.OBSIDIAN)
				.withElementAmount(5000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BREEDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECItems.CRUDE_WATER_GEM.get())
				.addIngredient(tag(Tags.Items.CROPS))
				.addIngredient(tag(Tags.Items.LEATHERS))
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(tag(Tags.Items.GEMS_DIAMOND))
				.withElementAmount(5000)
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROVE_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.CRUDE_EARTH_GEM.get())
				.addIngredient(tag(ItemTags.FLOWERS))
				.addIngredient(tag(Tags.Items.SEEDS))
				.addIngredient(tag(Tags.Items.CROPS))
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRING_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(Items.BUCKET)
				.addIngredient(tag(ItemTags.FISHES))
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BUDDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECItems.CRUDE_WATER_GEM.get())
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(tag(Tags.Items.GEMS_DIAMOND))
				.save(this.output);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPAWNING_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(ECItems.FINE_EARTH_GEM.get())
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.SPIDER_EYE)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DIAMOND)
				.save(this.output);
	}

	private void generateShrineUpgrades() {
		shaped(RecipeCategory.MISC, ECBlocks.ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.RANGE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("ggg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.CAPACITY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('b', Items.BUCKET)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("gbg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.EFFICIENCY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("gdg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.STRENGTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('r', Tags.Items.RODS_BLAZE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("grg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("dfd")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.FORTUNE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('l', Tags.Items.GEMS_LAPIS)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("lll")
				.pattern("wCw").
				pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.SILK_TOUCH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sss")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.PLANTING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Tags.Items.SEEDS)
				.define('h', Items.DIAMOND_HOE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BONE_BLOCK)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("bdb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.PICKUP_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_EYE)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.VORTEX_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_PEARL)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.AIR_CRYSTAL.get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.NECTAR_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('h', Items.HONEY_BLOCK)
				.define('s', Items.SUGAR)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		/* shaped(RecipeCategory.MISC, ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', BotaniaTags.Items.PETALS)
				.define('m', BotaniaTags.Items.INGOTS_MANASTEEL)
				.define('w', BotaniaBlocks.livingrock)
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("pmp").pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output.withConditions(new ModLoadedCondition(BotaniaAPI.MODID)), BuiltInRegistries.BLOCK.getKey(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get())); */
        shaped(RecipeCategory.MISC, ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PUMPKIN)
				.define('b', Items.BONE_MEAL)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("bpb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.PROTECTION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Items.SHIELD)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("isi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.FILLING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BUCKET)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("ibi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PRISMARINE_CRYSTALS)
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sps")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.DIAMOND_PICKAXE)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("gpg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('e', ECItems.PRISTINE_EARTH_GEM.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("ses")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(this.output);

		shaped(RecipeCategory.MISC, ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('t', createScrollIngredient(Spells.TRANSLOCATION))
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('u', ECBlocks.RANGE_SHRINE_UPGRADE.get())
				.pattern("ftf")
				.pattern("wCw")
				.pattern(" u ")
				.unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('p', ECBlocks.PIPE_IMPROVED.get())
				.define('u', ECBlocks.ACCELERATION_SHRINE_UPGRADE.get())
				.define('z', createRuneIngredient(Runes.ZOD))
				.pattern("ziz")
				.pattern("pCp")
				.pattern(" u ")
				.unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get())
				.define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
				.define('l', Tags.Items.GEMS_LAPIS)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('u', ECBlocks.FORTUNE_SHRINE_UPGRADE.get())
				.define('t', createRuneIngredient(Runes.TZEENTCH))
				.pattern("tlt")
				.pattern("wCw")
				.pattern(" u ")
				.unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECBlocks.OVERWHELMING_STRENGTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get())
				.define('f', ECItems.PRISTINE_FIRE_GEM.get())
				.define('r', Tags.Items.RODS_BLAZE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('u', ECBlocks.STRENGTH_SHRINE_UPGRADE.get())
				.pattern("frf")
				.pattern("wCw")
				.pattern(" u ")
				.unlockedBy(HAS_ADVANCED_SHRINE_UPGRADE_CORE, has(ECItems.ADVANCED_SHRINE_UPGRADE_CORE))
				.save(this.output);
	}

	private void generateJewels() {
		shaped(RecipeCategory.MISC, ECItems.UNSET_JEWEL.get())
				.pattern("sis")
				.pattern("idi")
				.pattern("sis")
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(this.output);

		createJewelRecipe(Jewels.SALMON, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" a ")
				.define('w', ECItems.FINE_WATER_GEM.get())
				.define('a', ECItems.CRUDE_AIR_GEM.get())
				.define('c', ECItems.WATER_CRYSTAL.get()));
		createJewelRecipe(Jewels.DOLPHIN, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" w ")
				.define('w', ECItems.CRUDE_WATER_GEM.get())
				.define('c', ECItems.WATER_CRYSTAL.get()));
		createJewelRecipe(Jewels.LEOPARD, b -> b
				.pattern(" a ")
				.pattern("cUc")
				.pattern(" e ")
				.define('a', ECItems.FINE_AIR_GEM.get())
				.define('e', ECItems.FINE_EARTH_GEM.get())
				.define('c', ECItems.AIR_CRYSTAL.get()));
		createJewelRecipe(Jewels.PHOENIX, b -> b
				.pattern("fgf")
				.pattern("bUb")
				.pattern("fpf")
				.define('g', ECItems.PRISTINE_FIRE_GEM.get())
				.define('b', Tags.Items.RODS_BLAZE)
				.define('f', Tags.Items.FEATHERS)
				.define('p', ECItems.PURE_CRYSTAL.get()));
		createJewelRecipe(Jewels.TORTOISE, b -> b
				.pattern("ses")
				.pattern("gUg")
				.pattern("ses")
				.define('g', Tags.Items.GRAVELS)
				.define('e', ECItems.CRUDE_EARTH_GEM.get())
				.define('s', Items.TURTLE_SCUTE));
		createJewelRecipe(Jewels.DEMIGOD, b -> b
				.pattern("tat")
				.pattern("cUc")
				.pattern("tet")
				.define('e', ECItems.PRISTINE_EARTH_GEM.get())
				.define('a', ECItems.PRISTINE_AIR_GEM.get())
				.define('t', Items.TOTEM_OF_UNDYING)
				.define('c', ECItems.PURE_CRYSTAL.get()));
		createJewelRecipe(Jewels.MOLE, b -> b
				.pattern(" e ")
				.pattern("sUa")
				.pattern(" p ")
				.define('e', ECItems.PRISTINE_EARTH_GEM.get())
				.define('a', Items.DIAMOND_PICKAXE)
				.define('s', Items.DIAMOND_SHOVEL)
				.define('p', ECItems.PURE_CRYSTAL.get()));
		createJewelRecipe(Jewels.TIGER, b -> b
				.pattern("sas")
				.pattern("cUc")
				.pattern("sfs")
				.define('a', ECItems.PRISTINE_AIR_GEM.get())
				.define('c', ECItems.AIR_CRYSTAL.get())
				.define('f', ECItems.FINE_AIR_GEM.get())
				.define('s', Items.SUGAR));
		createJewelRecipe(Jewels.BEAR, b -> b
				.pattern("heh")
				.pattern("cUc")
				.pattern("hfh")
				.define('e', ECItems.PRISTINE_EARTH_GEM.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.define('f', ECItems.FINE_EARTH_GEM.get())
				.define('h', Items.HONEY_BOTTLE));
		createJewelRecipe(Jewels.VIPER, b -> b
				.pattern(" w ")
				.pattern("sUs")
				.pattern(" e ")
				.define('e', ECItems.FINE_EARTH_GEM.get())
				.define('w', ECItems.FINE_WATER_GEM.get())
				.define('s', Items.SPIDER_EYE));
		createJewelRecipe(Jewels.HAWK, b -> b
				.pattern("gag")
				.pattern("cUc")
				.pattern("gag")
				.define('a', ECItems.FINE_AIR_GEM.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('c', ECItems.AIR_CRYSTAL.get()));
		createJewelRecipe(Jewels.KIRIN, b -> b
				.pattern("sfs")
				.pattern("cUc")
				.pattern("sas")
				.define('f', ECItems.PRISTINE_FIRE_GEM.get())
				.define('a', ECItems.PRISTINE_AIR_GEM.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', ECItems.PURE_CRYSTAL.get()));
		createJewelRecipe(Jewels.ARCTIC_HARE, b -> b
				.pattern(" w ")
				.pattern("fUf")
				.pattern(" a ")
				.define('w', ECItems.FINE_WATER_GEM.get())
				.define('a', ECItems.CRUDE_AIR_GEM.get())
				.define('f', Items.RABBIT_FOOT));
		createJewelRecipe(Jewels.STRIDER, b -> b
				.pattern(" f ")
				.pattern("bUb")
				.pattern(" f ")
				.define('f', ECItems.FINE_FIRE_GEM.get())
				.define('b', Items.LAVA_BUCKET));
		createJewelRecipe(Jewels.WATER_STRIDER, b -> b
				.pattern(" w ")
				.pattern("bUb")
				.pattern(" w ")
				.define('w', ECItems.FINE_WATER_GEM.get())
				.define('b', Items.WATER_BUCKET));
		createJewelRecipe(Jewels.BASILISK, b -> b
				.pattern("fwf")
				.pattern("cUc")
				.pattern("sws")
				.define('w', ECItems.PRISTINE_WATER_GEM.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('f', Items.FERMENTED_SPIDER_EYE)
				.define('s', Items.TURTLE_SCUTE));
		createJewelRecipe(Jewels.PIGLIN, b -> b
				.pattern("gfg")
				.pattern("pUp")
				.pattern("gcg")
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.define('f', ECItems.CRUDE_FIRE_GEM.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('p', Items.PORKCHOP));
	}

	private void generateSpells() {
		shapeless(RecipeCategory.MISC, ECItems.SCROLL_PAPER.get())
				.requires(ECItems.AIR_SILK.get())
				.requires(Items.PAPER)
				.requires(Items.INK_SAC)
				.unlockedBy("has_air_silk", has(ECItems.AIR_SILK))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.SPELL_DESK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('l', Blocks.LECTERN)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.pattern("wlw")
				.pattern(" i ")
				.pattern(" w ")
				.save(this.output);
		shaped(RecipeCategory.TOOLS, ECItems.FOCUS.get())
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" ic").pattern(" si")
				.pattern("d  ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(this.output);
		shaped(RecipeCategory.COMBAT, ECItems.STAFF.get())
				.define('s', ECTags.Items.STAFF_CRAFT_SWORD)
				.define('f', ECItems.FOCUS.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.pattern(" if")
				.pattern("ihi")
				.pattern("si ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(staffOutput());
		shaped(RecipeCategory.TOOLS, ECItems.SPELL_BOOK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('l', Tags.Items.LEATHERS)
				.define('p', ECItems.SCROLL_PAPER.get())
				.pattern("slp")
				.pattern("clp").pattern("slp")
				.unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
				.save(this.output);

		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.GRAVEL_FALL)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.STONE_WALL)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FIRE_BALL)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ITEM_PULL)
				.setGem(ECItems.FINE_AIR_GEM.get())
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ENDER_STRIKE)
				.setGem(ECItems.CRUDE_AIR_GEM.get())
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ANIMAL_GROWTH)
				.setGem(ECItems.CRUDE_WATER_GEM.get())
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TREE_FALL)
				.setGem(ECItems.FINE_EARTH_GEM.get())
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.PURIFICATION)
				.setGem(ECItems.CRUDE_WATER_GEM.get())
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.RIPENING)
				.setGem(ECItems.CRUDE_EARTH_GEM.get())
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FLAME_CLEAVE)
				.setGem(ECItems.CRUDE_FIRE_GEM.get())
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.INFERNO)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.DASH)
				.setGem(ECItems.FINE_AIR_GEM.get())
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SILK_VEIN)
				.setGem(ECItems.PRISTINE_EARTH_GEM.get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TRANSLOCATION)
				.setGem(ECItems.PRISTINE_AIR_GEM.get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FEATHER_SPIKES)
				.setGem(ECItems.FINE_AIR_GEM.get())
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.HEAL)
				.setGem(ECItems.PRISTINE_WATER_GEM.get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SPEED)
				.setGem(ECItems.PRISTINE_AIR_GEM.get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SHOCKWAVE)
				.setGem(ECItems.CRUDE_AIR_GEM.get())
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.AIR_SHIELD)
				.setGem(ECItems.FINE_AIR_GEM.get())
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.REPAIR)
				.setGem(ECItems.PRISTINE_FIRE_GEM.get())
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(this.output);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.LIGHT)
				.setGem(Items.DIAMOND)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(this.output);
	}

	private void generateToolInfusions() {
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.LOOTING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.FIRE_ASPECT).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.SHARPNESS).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, ElementalCraftApi.createRL("attack_speed")).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.FORTUNE).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.EFFICIENCY).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.FORTUNE).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.EFFICIENCY).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.FORTUNE).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.EFFICIENCY).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.LOOTING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.FIRE_ASPECT).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.SHARPNESS /* TODO cleaving ? */).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.EFFICIENCY).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.FORTUNE).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.EFFICIENCY).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.PUNCH).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.FLAME).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, ElementalCraftApi.createRL(FastDrawToolInfusionEffect.NAME)).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.MULTISHOT).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.PIERCING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.QUICK_CHARGE).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.LUCK_OF_THE_SEA).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, ElementalCraftApi.createRL(AutoSmeltToolInfusionEffect.NAME)).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.LURE).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.LOYALTY).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.IMPALING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.UNBREAKING).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.RIPTIDE).save(this.output);

		toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.RESPIRATION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.FIRE_PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.PROJECTILE_PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.BLAST_PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.FIRE_PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, ElementalCraftApi.createRL(DodgeToolInfusionEffect.NAME)).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.BLAST_PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.FIRE_PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, ElementalCraftApi.createRL("movement_speed")).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.DEPTH_STRIDER).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FIRE_PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.PROTECTION).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FEATHER_FALLING).save(this.output);

		toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("fire_reduction")).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("water_reduction")).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("earth_reduction")).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraftApi.createRL("air_reduction")).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("fire_staff")).withElementAmount(5000).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("water_staff")).withElementAmount(5000).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("earth_staff")).withElementAmount(5000).save(this.output);
		toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraftApi.createRL("air_staff")).withElementAmount(5000).save(this.output);
	}

	public ToolInfusionRecipeBuilder toolInfusionRecipe(TagKey<Item> ingredient, Identifier infusion) {
		return ToolInfusionRecipeBuilder.toolInfusionRecipe(items, ingredient, infusion);
	}

	public ToolInfusionRecipeBuilder toolInfusionRecipe(TagKey<Item> ingredient, ResourceKey<Enchantment> enchantment) {
		return ToolInfusionRecipeBuilder.toolInfusionRecipe(items, ingredient, enchantment);
	}

	private void generateGrinding() {
		GrindingRecipeBuilder.grindingRecipe(Items.COBBLESTONE)
				.withIngredient(tag(Tags.Items.STONES))
				.withLuckRatio(1)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.GRAVEL)
				.withIngredient(tag(Tags.Items.COBBLESTONES))
				.withLuckRatio(2)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.SAND)
				.withIngredient(tag(Tags.Items.GRAVELS))
				.withLuckRatio(5)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.BLAZE_POWDER)
				.withCount(3)
				.withIngredient(tag(Tags.Items.RODS_BLAZE))
				.withLuckRatio(3)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.NETHERITE_SCRAP)
				.withCount(2)
				.withIngredient(tag(Tags.Items.ORES_NETHERITE_SCRAP))
				.withElementAmount(5000)
				.withLuckRatio(1)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(ECItems.INERT_CRYSTAL.get())
				.withCount(2)
				.withIngredient(tag(ECTags.Items.ORES_INERT_CRYSTAL))
				.withLuckRatio(5)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.POINTED_DRIPSTONE)
				.withCount(3)
				.withIngredient(Items.DRIPSTONE_BLOCK)
				.withLuckRatio(1)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.AMETHYST_SHARD)
				.withCount(6)
				.withIngredient(Items.AMETHYST_CLUSTER)
				.withLuckRatio(5)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(ECItems.SPRINGALINE_SHARD.get())
				.withCount(6)
				.withIngredient(ECBlocks.SPRINGALINE_CLUSTER.get())
				.withLuckRatio(5)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.BONE_MEAL)
				.withCount(4)
				.withIngredient(tag(Tags.Items.BONES))
				.withLuckRatio(3)
				.save(this.output);
		GrindingRecipeBuilder.grindingRecipe(Items.STRING)
				.withCount(4)
				.withIngredient(tag(ItemTags.WOOL))
				.save(this.output);

		grindToDye(Items.GREEN_DYE, Items.CACTUS);
		grindToDye(Items.WHITE_DYE, Items.BONE_MEAL);
		grindToDye(Items.BLACK_DYE, Items.INK_SAC);
		grindToDye(Items.BLUE_DYE, Tags.Items.GEMS_LAPIS);
		grindToDye(Items.RED_DYE, Tags.Items.CROPS_BEETROOT);
		grindToDye(Items.LIME_DYE, Items.SEA_PICKLE);
		grindToDye(Items.BROWN_DYE, Items.COCOA_BEANS);

		grindToDye(Items.WHITE_DYE, ECTags.Items.WHITE_FLOWERS);
		grindToDye(Items.ORANGE_DYE, ECTags.Items.ORANGE_FLOWERS);
		grindToDye(Items.MAGENTA_DYE, ECTags.Items.MAGENTA_FLOWERS);
		grindToDye(Items.LIGHT_BLUE_DYE, ECTags.Items.LIGHT_BLUE_FLOWERS);
		grindToDye(Items.YELLOW_DYE, ECTags.Items.YELLOW_FLOWERS);
		grindToDye(Items.LIME_DYE, ECTags.Items.LIME_FLOWERS);
		grindToDye(Items.PINK_DYE, ECTags.Items.PINK_FLOWERS);
		grindToDye(Items.GRAY_DYE, ECTags.Items.GRAY_FLOWERS);
		grindToDye(Items.LIGHT_GRAY_DYE, ECTags.Items.LIGHT_GRAY_FLOWERS);
		grindToDye(Items.CYAN_DYE, ECTags.Items.CYAN_FLOWERS);
		grindToDye(Items.PURPLE_DYE, ECTags.Items.PURPLE_FLOWERS);
		grindToDye(Items.BLUE_DYE, ECTags.Items.BLUE_FLOWERS);
		grindToDye(Items.BROWN_DYE, ECTags.Items.BROWN_FLOWERS);
		grindToDye(Items.GREEN_DYE, ECTags.Items.GREEN_FLOWERS);
		grindToDye(Items.BLACK_DYE, ECTags.Items.BLACK_FLOWERS);
		grindToDye(Items.RED_DYE, ECTags.Items.RED_FLOWERS);
	}

	private void grindToDye(ItemLike dye, ItemLike from) {
		GrindingRecipeBuilder.grindingRecipe(dye)
				.withCount(2)
				.withIngredient(from)
				.withLuckRatio(2)
				.save(this.output, BuiltInRegistries.ITEM.getKey(dye.asItem()).getPath() + FROM + BuiltInRegistries.ITEM.getKey(from.asItem()).getPath());
	}

	private void grindToDye(ItemLike dye, TagKey<@NotNull Item> from) {
		var tagName = from.location();

		GrindingRecipeBuilder.grindingRecipe(dye)
				.withCount(2)
				.withIngredient(tag(from))
				.withLuckRatio(2)
				.save(this.output.withConditions(new NotCondition(new TagEmptyCondition<>(from))), ElementalCraftApi.createRL(GrindingRecipe.NAME + '/' + BuiltInRegistries.ITEM.getKey(dye.asItem()).getPath() + FROM + tagName.getNamespace() + '_' + StringUtils.replaceChars(tagName.getPath(), '/', '_')));
	}

	private void generateSawing() {
		sawingRecipe(Items.STRIPPED_OAK_LOG, Items.STRIPPED_OAK_WOOD, Items.OAK_PLANKS, Items.OAK_LOG, Items.OAK_WOOD, ECTags.Items.STRIPPED_OAK);
		sawingRecipe(Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_WOOD, Items.DARK_OAK_PLANKS, Items.DARK_OAK_LOG, Items.DARK_OAK_WOOD, ECTags.Items.STRIPPED_DARK_OAK);
		sawingRecipe(Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_BIRCH_WOOD, Items.BIRCH_PLANKS, Items.BIRCH_LOG, Items.BIRCH_WOOD, ECTags.Items.STRIPPED_BIRCH);
		sawingRecipe(Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_ACACIA_WOOD, Items.ACACIA_PLANKS, Items.ACACIA_LOG, Items.ACACIA_WOOD, ECTags.Items.STRIPPED_ACACIA);
		sawingRecipe(Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_JUNGLE_WOOD, Items.JUNGLE_PLANKS, Items.JUNGLE_LOG, Items.JUNGLE_WOOD, ECTags.Items.STRIPPED_JUNGLE);
		sawingRecipe(Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_SPRUCE_WOOD, Items.SPRUCE_PLANKS, Items.SPRUCE_LOG, Items.SPRUCE_WOOD, ECTags.Items.STRIPPED_SPRUCE);
		sawingRecipe(Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_MANGROVE_WOOD, Items.MANGROVE_PLANKS, Items.MANGROVE_LOG, Items.MANGROVE_WOOD, ECTags.Items.STRIPPED_MANGROVE);
		sawingRecipe(Items.STRIPPED_CRIMSON_STEM, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.CRIMSON_STEM, Items.CRIMSON_HYPHAE, ECTags.Items.STRIPPED_CRIMSON);
		sawingRecipe(Items.STRIPPED_WARPED_STEM, Items.STRIPPED_WARPED_HYPHAE, Items.WARPED_PLANKS, Items.WARPED_STEM, Items.WARPED_HYPHAE, ECTags.Items.STRIPPED_WARPED);
		sawingRecipe(Items.STRIPPED_CHERRY_LOG, Items.STRIPPED_CHERRY_WOOD, Items.CHERRY_PLANKS, Items.CHERRY_LOG, Items.CHERRY_WOOD, ECTags.Items.STRIPPED_CHERRY);

		SawingRecipeBuilder.sawingRecipe(Items.STRIPPED_BAMBOO_BLOCK)
				.withIngredient(Items.BAMBOO_BLOCK)
				.withElementAmount(250)
				.save(this.output);
		SawingRecipeBuilder.sawingRecipe(Items.BAMBOO_PLANKS)
				.withCount(6)
				.withIngredient(Items.STRIPPED_BAMBOO_BLOCK)
				.withLuckRatio(3)
				.save(this.output);
	}

	private void sawingRecipe(ItemLike stripedLog, ItemLike stripedWood, ItemLike planks, ItemLike log, ItemLike wood, TagKey<@NotNull Item> stripped) {
		SawingRecipeBuilder.sawingRecipe(stripedLog)
				.withIngredient(log)
				.withElementAmount(250)
				.save(this.output);
		SawingRecipeBuilder.sawingRecipe(stripedWood)
				.withIngredient(wood)
				.withElementAmount(250)
				.save(this.output);
		SawingRecipeBuilder.sawingRecipe(planks)
				.withCount(6)
				.withIngredient(tag(stripped))
				.withLuckRatio(3)
				.save(this.output);
	}

	private void generateRunes() {
		shaped(RecipeCategory.MISC, ECItems.MINOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(this.output);
		shaped(RecipeCategory.MISC, ECItems.MAJOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(this.output);

		InscriptionRecipeBuilder.inscriptionRecipe(Runes.WII, ElementType.AIR)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_AIR_GEM.get())
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.SUGAR)
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.FUS, ElementType.AIR)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.WII))
				.addIngredient(tag(Tags.Items.STRINGS))
				.addIngredient(tag(Tags.Items.STRINGS))
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.ZOD, ElementType.AIR)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.FUS))
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.save(this.output);

		InscriptionRecipeBuilder.inscriptionRecipe(Runes.MANX, ElementType.FIRE)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_FIRE_GEM.get())
				.addIngredient(tag(ItemTags.COALS))
				.addIngredient(tag(ItemTags.COALS))
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.JITA, ElementType.FIRE)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.MANX))
				.addIngredient(Items.BLAZE_ROD)
				.addIngredient(Items.BLAZE_ROD)
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.TANO, ElementType.FIRE)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.JITA))
				.addIngredient(tag(Tags.Items.STORAGE_BLOCKS_COAL))
				.addIngredient(tag(Tags.Items.STORAGE_BLOCKS_COAL))
				.save(this.output);

		InscriptionRecipeBuilder.inscriptionRecipe(Runes.KIRBY, ElementType.AIR)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_AIR_GEM.get())
				.addIngredient(Items.TORCH)
				.addIngredient(Items.TORCH)
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.WHALE, ElementType.AIR)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.KIRBY))
				.addIngredient(tag(Tags.Items.DUSTS_GLOWSTONE))
				.addIngredient(tag(Tags.Items.DUSTS_GLOWSTONE))
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.TYRIA, ElementType.AIR)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.TYRIA))
				.addIngredient(Items.GLOWSTONE)
				.addIngredient(Items.GLOWSTONE)
				.save(this.output);

		InscriptionRecipeBuilder.inscriptionRecipe(Runes.SOARYN, ElementType.EARTH)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_EARTH_GEM.get())
				.addIngredient(createRuneIngredient(Runes.WII))
				.addIngredient(createRuneIngredient(Runes.MANX))
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.KAWORU, ElementType.EARTH)
				.withElementAmount(10000)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.SOARYN))
				.addIngredient(createRuneIngredient(Runes.FUS))
				.addIngredient(createRuneIngredient(Runes.JITA))
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.MEWTWO, ElementType.EARTH)
				.withElementAmount(20000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.KAWORU))
				.addIngredient(createRuneIngredient(Runes.ZOD))
				.addIngredient(createRuneIngredient(Runes.TANO))
				.save(this.output);

		InscriptionRecipeBuilder.inscriptionRecipe(Runes.CLAPTRAP, ElementType.WATER)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECItems.CRUDE_WATER_GEM.get())
				.addIngredient(tag(Tags.Items.GEMS_LAPIS))
				.addIngredient(tag(Tags.Items.GEMS_LAPIS))
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.BOMBADIL, ElementType.WATER)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.CLAPTRAP))
				.addIngredient(tag(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.addIngredient(tag(Tags.Items.STORAGE_BLOCKS_LAPIS))
				.save(this.output);
		InscriptionRecipeBuilder.inscriptionRecipe(Runes.TZEENTCH, ElementType.WATER)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(createRuneIngredient(Runes.BOMBADIL))
				.addIngredient(tag(Tags.Items.GEMS_EMERALD))
				.addIngredient(tag(Tags.Items.GEMS_EMERALD))
				.save(this.output);
	}

	private void generateCrystallizations() {
		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.FINE_FIRE_GEM.get(), ElementType.FIRE)
				.setGem(ECItems.CRUDE_FIRE_GEM.get())
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(this.output);
		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.FINE_WATER_GEM.get(), ElementType.WATER)
				.setGem(ECItems.CRUDE_WATER_GEM.get())
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(this.output);
		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.FINE_EARTH_GEM.get(), ElementType.EARTH)
				.setGem(ECItems.CRUDE_EARTH_GEM.get())
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(this.output);
		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.FINE_AIR_GEM.get(), ElementType.AIR)
				.setGem(ECItems.CRUDE_AIR_GEM.get())
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(this.output);

		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.PRISTINE_FIRE_GEM.get(), ElementType.FIRE)
				.withElementAmount(10000)
				.setGem(ECItems.FINE_FIRE_GEM.get())
				.setCrystal(ECItems.PRISTINE_SHARD.get())
				.save(this.output);
		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.PRISTINE_WATER_GEM.get(), ElementType.WATER)
				.withElementAmount(10000)
				.setGem(ECItems.FINE_WATER_GEM.get())
				.setCrystal(ECItems.PRISTINE_SHARD.get())
				.save(this.output);
		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.PRISTINE_EARTH_GEM.get(), ElementType.EARTH)
				.withElementAmount(10000)
				.setGem(ECItems.FINE_EARTH_GEM.get())
				.setCrystal(ECItems.PRISTINE_SHARD.get())
				.save(this.output);
		CrystallizationRecipeBuilder.crystallizationRecipe(ECItems.PRISTINE_AIR_GEM.get(), ElementType.AIR)
				.withElementAmount(10000)
				.setGem(ECItems.FINE_AIR_GEM.get())
				.setCrystal(ECItems.PRISTINE_SHARD.get())
				.save(this.output);
	}

	private Ingredient createScrollIngredient(Holder<@NotNull Spell> spell) {
		return DataComponentIngredient.of(true, ECDataComponents.SPELL, spell, ECItems.SCROLL.get());
	}

	private Ingredient createRuneIngredient(ResourceKey<@NotNull Rune> rune) {
		return createRuneIngredient(rune.identifier());
	}

	private Ingredient createRuneIngredient(Identifier rune) {
		return DataComponentIngredient.of(true, ECDataComponents.RUNE, ElementalCraftApi.RUNE_MANAGER.getOrCreateHolder(rune), ECItems.RUNE.get());
	}

	private void generateEmptying() {
		generateEmptying(ECBlocks.SMALL_CONTAINER.get());
		generateEmptying(ECBlocks.CONTAINER.get());
		generateEmptying(ECBlocks.FIRE_RESERVOIR.get());
		generateEmptying(ECBlocks.WATER_RESERVOIR.get());
		generateEmptying(ECBlocks.EARTH_RESERVOIR.get());
		generateEmptying(ECBlocks.AIR_RESERVOIR.get());
		generateEmptying(ECBlocks.CREATIVE_CONTAINER.get());
		generateEmptying(ECItems.FIRE_HOLDER.get());
		generateEmptying(ECItems.WATER_HOLDER.get());
		generateEmptying(ECItems.EARTH_HOLDER.get());
		generateEmptying(ECItems.AIR_HOLDER.get());
		generateEmptying(ECItems.PURE_HOLDER.get());
	}


	private void generateEmptying(ItemLike item) {
		var name = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();

		shapeless(RecipeCategory.DECORATIONS, item)
				.requires(item)
				.unlockedBy("has_" + name, has(item))
				.save(this.output, createRecipeKey(name + "_emptying"));
	}

	private void generateDecorations() {
		shaped(RecipeCategory.DECORATIONS, ECBlocks.BURNT_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.BURNT_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_burnt_glass", has(ECBlocks.BURNT_GLASS))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.WHITE_ROCK_FENCE.get(), 16)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("#i#")
				.pattern("#i#")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.MOSSY_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MOSS_BLOCK)
				.pattern("###").pattern("#$#").pattern("###")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.BURNT_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MAGMA_BLOCK)
				.pattern("###").pattern("#$#")
				.pattern("###").unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_BLOCK.get())
				.define('#', ECItems.SPRINGALINE_SHARD.get())
				.pattern("##")
				.pattern("##")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_GLASS.get(), 2)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern(" s ")
				.pattern("sgs")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.SPRINGALINE_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_springaline_glass", has(ECBlocks.SPRINGALINE_GLASS))
				.save(this.output);
		shaped(RecipeCategory.DECORATIONS, ECBlocks.SPRINGALINE_LANTERN.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', Items.GLOWSTONE)
				.define('p', Tags.Items.GEMS_PRISMARINE)
				.pattern("psp")
				.pattern("sgs")
				.pattern("psp")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(this.output);
	}

	private void generateSourceBreeding() {
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER_PEDESTAL.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.pattern("wsw")
				.pattern("ici")
				.pattern("www")
				.save(this.output);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER.get(), ECItems.PURE_CRYSTAL.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('p', ECBlocks.SOURCE_BREEDER_PEDESTAL.get())
				.pattern("iwi")
				.pattern("fcf")
				.pattern("wpw")
				.save(this.output);

		createSourceSeed(ECItems.PRISTINE_FIRE_GEM, ECItems.FIRE_SOURCE_SEED);
		createSourceSeed(ECItems.PRISTINE_WATER_GEM, ECItems.WATER_SOURCE_SEED);
		createSourceSeed(ECItems.PRISTINE_EARTH_GEM, ECItems.EARTH_SOURCE_SEED);
		createSourceSeed(ECItems.PRISTINE_AIR_GEM, ECItems.AIR_SOURCE_SEED);
	}

	private void generateCracking() {
		CrackingRecipeBuilder.crackingRecipe(Blocks.STONE)
				.result(Blocks.COBBLESTONE)
				.elementAmount(200)
				.save(this.output, "stone");
		CrackingRecipeBuilder.crackingRecipe(Blocks.GRANITE)
				.result(Blocks.COBBLESTONE)
				.elementAmount(200)
				.save(this.output, "granite");
		CrackingRecipeBuilder.crackingRecipe(Blocks.ANDESITE)
				.result(Blocks.COBBLESTONE)
				.elementAmount(200)
				.save(this.output, "andesite");
		CrackingRecipeBuilder.crackingRecipe(Blocks.DIORITE)
				.result(Blocks.COBBLESTONE)
				.elementAmount(200)
				.save(this.output, "dioriote");
		CrackingRecipeBuilder.crackingRecipe(Blocks.DEEPSLATE)
				.result(Blocks.COBBLED_DEEPSLATE)
				.elementAmount(200)
				.save(this.output, "deepslate");
		CrackingRecipeBuilder.crackingRecipe(blocks.getOrThrow(Tags.Blocks.COBBLESTONES))
				.result(Blocks.GRAVEL)
				.elementAmount(100)
				.save(this.output, "cobblestones");
		CrackingRecipeBuilder.crackingRecipe(blocks.getOrThrow(Tags.Blocks.GRAVELS))
				.result(Blocks.SAND)
				.elementAmount(75)
				.save(this.output, "gravel");
		CrackingRecipeBuilder.crackingRecipe(blocks.getOrThrow(BlockTags.DIRT))
				.result(Blocks.SAND)
				.elementAmount(75)
				.save(this.output, "dirt");
		CrackingRecipeBuilder.crackingRecipe(blocks.getOrThrow(BlockTags.SAND))
				.elementAmount(50)
				.save(this.output, "sand");

		CrackingRecipeBuilder.sculkCrackingRecipe(Blocks.SCULK)
				.elementAmount(1000)
				.save(this.output, "sculk");
		CrackingRecipeBuilder.sculkCrackingRecipe(Blocks.SCULK_VEIN)
				.elementAmount(100)
				.save(this.output, "sculk_vein");
		CrackingRecipeBuilder.sculkCrackingRecipe(Blocks.SCULK_CATALYST)
				.elementAmount(5000)
				.save(this.output, "sculk_catalyst");
	}

	private void generateMelting() {
		MeltingRecipeBuilder.melting(blocks.getOrThrow(ECTags.Blocks.SHRINES_MELTING_LIQUIFIABLES_LAVA), Fluids.LAVA)
				.cooldown(1200)
				.elementAmount(5000)
				.fillingAmount(1000)
				.save(this.output);
		MeltingRecipeBuilder.melting(blocks.getOrThrow(ECTags.Blocks.SHRINES_MELTING_LIQUIFIABLES_WATER), Fluids.WATER)
				.cooldown(10)
				.elementAmount(1)
				.fillingAmount(1000)
				.save(this.output);
	}

	private void createSourceSeed(Supplier<? extends Item> gem, Supplier<? extends Item> seed) {
		shaped(RecipeCategory.MISC, seed.get())
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', gem.get())
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(this.output);
	}

	private void createNuggetIngotBlock(ItemLike nugget, TagKey<@NotNull Item> nuggetTag, ItemLike ingot, TagKey<@NotNull Item> ingotTag, ItemLike block, TagKey<@NotNull Item> blockTag) {
		shaped(RecipeCategory.MISC, ingot).define('#', nuggetTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(nugget), has(nuggetTag))
				.save(this.output, from(nugget, ingot));
		shapeless(RecipeCategory.MISC, nugget, 9)
				.requires(ingotTag)
				.unlockedBy(buildHas(ingot), has(ingotTag))
				.save(this.output, from(ingot, nugget));
		shaped(RecipeCategory.MISC, block).define('#', ingotTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(ingot), has(ingotTag)
				).save(this.output, from(ingot, block));
		shapeless(RecipeCategory.MISC, ingot, 9)
				.requires(blockTag)
				.unlockedBy(buildHas(block), has(blockTag))
				.save(this.output, from(block, ingot));
	}

	private void createStorageBlock(ItemLike item, ItemLike block) {
		shaped(RecipeCategory.MISC, block)
				.define('#', item)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(item), has(item))
				.save(this.output, from(item, block));
		shapeless(RecipeCategory.MISC, item, 9)
				.requires(block)
				.unlockedBy(buildHas(block), has(block))
				.save(this.output, from(block, item));
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get(), 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(Supplier<? extends ItemLike> result) {
		return prepareInstrumentRecipe(result.get());
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result, ItemLike crystal, int count) {
		return shaped(RecipeCategory.DECORATIONS, result, count)
				.define('c', crystal)
				.unlockedBy(buildHas(crystal), has(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result) {
		return prepareWhiterockInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get());
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result, ItemLike crystal) {
		return prepareInstrumentRecipe(result, crystal, 1)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	public ShapedRecipeBuilder shaped(Supplier<? extends ItemLike> item) {
		return shaped(RecipeCategory.DECORATIONS, item.get(), 1);
	}

	public ShapedRecipeBuilder shaped(Supplier<? extends ItemLike> item, int count) {
		return shaped(RecipeCategory.DECORATIONS, item.get(), count);
	}


	private RecipeOutput staffOutput() {
		return new RecipeOutput() {
			@Override
			public void accept(@NonNull ResourceKey<Recipe<?>> key, @NonNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NonNull ... conditions) {
				ECRecipeProvider.this.output.accept(key, recipe instanceof ShapedRecipe shaped ? new StaffRecipe(shaped) : recipe, advancement, conditions);
			}

			@Override
			@NotNull
			public Advancement.Builder advancement() {
				return ECRecipeProvider.this.output.advancement();
			}

			@Override
			public void includeRootAdvancement() {
				ECRecipeProvider.this.output.includeRootAdvancement();
			}
		};
	}

	private void createJewelRecipe(Supplier<? extends Jewel> jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder) {
		createJewelRecipe(jewel.get(), patternBuilder);
	}

	private void createJewelRecipe(Jewel jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder) {
		var builder = shaped(RecipeCategory.DECORATIONS, jewel);
		var jewelKey = jewel.getKey();

		patternBuilder.apply(builder)
				.define('U', ECItems.UNSET_JEWEL.get())
				.unlockedBy("has_unset_jewel", has(ECItems.UNSET_JEWEL))
				.save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(jewelKey.getNamespace(), "jewel/" + jewelKey.getPath())));
	}

	private ResourceKey<Recipe<?>> from(ItemLike from, ItemLike to) {
		return createRecipeKey(BuiltInRegistries.ITEM.getKey(to.asItem()).getPath() + FROM + BuiltInRegistries.ITEM.getKey(from.asItem()).getPath());
	}

	private String buildHas(ItemLike item) {
		return "has_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
	}

	protected Criterion<InventoryChangeTrigger.@NotNull TriggerInstance> has(Supplier<? extends ItemLike> itemLike) {
		return has(itemLike.get().asItem());
	}

	private static @NonNull ResourceKey<Recipe<?>> createRecipeKey(String name) {
		return ResourceKey.create(Registries.RECIPE, ElementalCraftApi.createRL(name));
	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
			super(packOutput, registries);
		}

		@Override
		protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
			return new ECRecipeProvider(registries, output);
		}

		@Override
		public @NonNull String getName() {
			return "Elementalcraft Recipes";
		}
	}
}
