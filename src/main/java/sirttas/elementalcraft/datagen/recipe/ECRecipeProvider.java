package sirttas.elementalcraft.datagen.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.datagen.recipe.builder.PureInfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.AirMillGrindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.BindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.CrystallizationRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.InfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.InscriptionRecipeBuilder;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

public class ECRecipeProvider extends RecipeProvider {

	private static final String HAS_INERTCRYSTAL = "has_inertcrystal";
	private static final String HAS_CONTAINEDCRYSTAL = "has_containedcrystal";
	private static final String HAS_PURECRYSTAL = "has_purecrystal";
	private static final String HAS_WHITEROCK = "has_whiterock";
	private static final String HAS_SHRINE_UPGRADE_CORE = "has_shrine_upgrade_core";

	private static final String HAS_DRENCHED_IRON_NUGGET = "has_drenched_iron_nugget";
	private static final String HAS_DRENCHED_IRON_INGOT = "has_drenched_iron_ingot";
	private static final String HAS_SWIFT_ALLOY_NUGGET = "has_swift_alloy_nugget";
	private static final String HAS_SWIFT_ALLOY_INGOT = "has_swift_alloy_ingot";

	private ExistingFileHelper existingFileHelper;

	public ECRecipeProvider(DataGenerator generatorIn, ExistingFileHelper exFileHelper) {
		super(generatorIn);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		registerSlabsStairsWalls(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.CONTAINED_CRYSTAL).key('g', Tags.Items.NUGGETS_GOLD).key('c', ECItems.INERT_CRYSTAL).patternLine(" g ").patternLine("gcg").patternLine(" g ")
				.addCriterion(HAS_INERTCRYSTAL, hasItem(ECItems.INERT_CRYSTAL)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.SHRINE_BASE).key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.INERT_CRYSTAL).key('p', ECBlocks.PIPE).patternLine(" p ").patternLine("pcp")
				.patternLine("www").addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.EMPTY_RECEPTACLE).key('c', ECItems.PURE_CRYSTAL).key('d', Tags.Items.GEMS_DIAMOND).key('g', Tags.Items.INGOTS_GOLD)
				.key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine("gig").patternLine("dcd").patternLine("gig").addCriterion(HAS_PURECRYSTAL, hasItem(ECItems.PURE_CRYSTAL)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.EMPTY_RECEPTACLE_IMPROVED).key('c', ECItems.PURE_CRYSTAL).key('r', ECItems.EMPTY_RECEPTACLE).key('f', ECTags.Items.INGOTS_FIREITE)
				.key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine("ifi").patternLine("crc").patternLine("ifi").addCriterion("has_fireite_ingot", hasItem(ECTags.Items.INGOTS_FIREITE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.CHISEL).key('h', ECItems.HARDENED_HANDLE).key('d', Tags.Items.GEMS_DIAMOND).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine(" i ")
				.patternLine(" di").patternLine("h  ").addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.SHRINE_UPGRADE_CORE).key('c', ECItems.CONTAINED_CRYSTAL).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('r', Tags.Items.DUSTS_REDSTONE).patternLine("rir")
				.patternLine("ici").patternLine("rir").addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.SCROLL_PAPER).addIngredient(ECItems.AIR_CRYSTAL).addIngredient(Items.PAPER).addIngredient(Items.INK_SAC)
				.addCriterion("has_air_silk", hasItem(ECItems.AIR_SILK)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.FIRE_HOLDER).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.EXTRACTOR).key('t', ECBlocks.TANK_SMALL).key('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.key('c', ECItems.FIRE_CRYSTAL).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_firecrystal", hasItem(ECItems.FIRE_CRYSTAL)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.WATER_HOLDER).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.EXTRACTOR).key('t', ECBlocks.TANK_SMALL).key('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.key('c', ECItems.WATER_CRYSTAL).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_watercrystal", hasItem(ECItems.WATER_CRYSTAL)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.EARTH_HOLDER).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.EXTRACTOR).key('t', ECBlocks.TANK_SMALL).key('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.key('c', ECItems.EARTH_CRYSTAL).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_earthcrystal", hasItem(ECItems.EARTH_CRYSTAL)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.AIR_HOLDER).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.EXTRACTOR).key('t', ECBlocks.TANK_SMALL).key('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.key('c', ECItems.AIR_CRYSTAL).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_aircrystal", hasItem(ECItems.AIR_CRYSTAL)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.FOCUS).key('d', Tags.Items.GEMS_DIAMOND).key('c', ECItems.CONTAINED_CRYSTAL).key('s', ECItems.HARDENED_HANDLE).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.patternLine(" ic").patternLine(" si").patternLine("d  ").addCriterion(HAS_CONTAINEDCRYSTAL, hasItem(ECItems.CONTAINED_CRYSTAL)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.SPELL_BOOK).key('c', ECItems.PURE_CRYSTAL).key('s', ECItems.AIR_SILK).key('l', Tags.Items.LEATHER).key('p', ECItems.SCROLL_PAPER).patternLine("slp")
				.patternLine("clp").patternLine("slp").addCriterion(HAS_PURECRYSTAL, hasItem(ECItems.PURE_CRYSTAL)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECBlocks.BURNT_GLASS_PANE, 16).key('#', ECBlocks.BURNT_GLASS).patternLine("###").patternLine("###").addCriterion("has_burnt_glass", hasItem(ECBlocks.BURNT_GLASS))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.WHITE_ROCK_FENCE, 16).key('#', ECBlocks.WHITE_ROCK).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).patternLine("#i#").patternLine("#i#")
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.WHITE_ROCK_BRICK, 4).key('#', ECBlocks.WHITE_ROCK).patternLine("##").patternLine("##").addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK))
				.build(consumer);
		SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(ECBlocks.WHITE_ROCK), ECBlocks.WHITE_ROCK_BRICK).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK)).build(consumer,
				ElementalCraft.createRL("whiterock_brick_from_whiterock_stonecutting"));

		createNuggetIngotBlock(ECItems.DRENCHED_IRON_NUGGET, ECTags.Items.NUGGETS_DRENCHED_IRON, ECItems.DRENCHED_IRON_INGOT, ECTags.Items.INGOTS_DRENCHED_IRON, ECItems.DRENCHED_IRON_BLOCK,
				ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON, consumer);
		createNuggetIngotBlock(ECItems.SWIFT_ALLOY_NUGGET, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_INGOT, ECTags.Items.INGOTS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_BLOCK,
				ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY, consumer);
		createNuggetIngotBlock(ECItems.FIREITE_NUGGET, ECTags.Items.NUGGETS_FIREITE, ECItems.FIREITE_INGOT, ECTags.Items.INGOTS_FIREITE, ECItems.FIREITE_BLOCK, ECTags.Items.STORAGE_BLOCKS_FIREITE,
				consumer);

		createStorageBlock(ECItems.INERT_CRYSTAL, ECBlocks.INERT_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.FIRE_CRYSTAL, ECBlocks.FIRE_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.WATER_CRYSTAL, ECBlocks.WATER_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.EARTH_CRYSTAL, ECBlocks.EARTH_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.AIR_CRYSTAL, ECBlocks.AIR_CRYSTAL_BLOCK, consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.POWERFUL_FIRE_SHARD).key('#', ECItems.FIRE_SHARD).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_fire_shard", hasItem(ECItems.FIRE_SHARD)).build(consumer, ElementalCraft.createRL("powerful_fire_shard_from_fire_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.FIRE_SHARD, 9).addIngredient(ECItems.POWERFUL_FIRE_SHARD).addCriterion("has_powerful_fire_shard", hasItem(ECItems.POWERFUL_FIRE_SHARD))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.POWERFUL_WATER_SHARD).key('#', ECItems.WATER_SHARD).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_water_shard", hasItem(ECItems.WATER_SHARD)).build(consumer, ElementalCraft.createRL("powerful_water_shard_from_water_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.WATER_SHARD, 9).addIngredient(ECItems.POWERFUL_WATER_SHARD).addCriterion("has_powerful_water_shard", hasItem(ECItems.POWERFUL_WATER_SHARD))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.POWERFUL_EARTH_SHARD).key('#', ECItems.EARTH_SHARD).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_earth_shard", hasItem(ECItems.EARTH_SHARD)).build(consumer, ElementalCraft.createRL("powerful_earth_shard_from_earth_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.EARTH_SHARD, 9).addIngredient(ECItems.POWERFUL_EARTH_SHARD).addCriterion("has_powerful_earth_shard", hasItem(ECItems.POWERFUL_EARTH_SHARD))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.POWERFUL_AIR_SHARD).key('#', ECItems.AIR_SHARD).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_air_shard", hasItem(ECItems.AIR_SHARD)).build(consumer, ElementalCraft.createRL("powerful_air_shard_from_air_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.AIR_SHARD, 9).addIngredient(ECItems.POWERFUL_AIR_SHARD).addCriterion("has_powerful_air_shard", hasItem(ECItems.POWERFUL_AIR_SHARD)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECBlocks.TANK_SMALL).key('g', Tags.Items.GLASS).key('p', ECBlocks.PIPE_IMPAIRED).patternLine(" p ").patternLine("pgp").patternLine(" p ")
				.addCriterion(HAS_CONTAINEDCRYSTAL, hasItem(ECItems.CONTAINED_CRYSTAL)).build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.TANK_SMALL).addIngredient(ECBlocks.TANK_SMALL).addCriterion("has_tank_small", hasItem(ECBlocks.TANK_SMALL)).build(consumer, "tank_small_emptying");
		prepareWhiterockInstrumentRecipe(ECBlocks.TANK).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('g', ECBlocks.BURNT_GLASS).key('p', ECBlocks.PIPE).patternLine("ici").patternLine("pgp")
				.patternLine("www").build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.TANK).addIngredient(ECBlocks.TANK).addCriterion("has_tank", hasItem(ECBlocks.TANK)).build(consumer, "tank_emptying");
		prepareInstrumentRecipe(ECBlocks.EXTRACTOR).key('i', Tags.Items.INGOTS_IRON).patternLine(" c ").patternLine(" i ").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.EXTRACTOR_IMPROVED, ECItems.PURE_CRYSTAL).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('e', ECBlocks.EXTRACTOR).patternLine(" e ").patternLine("eie")
				.patternLine("wcw").build(consumer);
		prepareInstrumentRecipe(ECBlocks.EVAPORATOR).key('i', Tags.Items.INGOTS_IRON).key('g', Tags.Items.GLASS).patternLine("igi").patternLine("igi").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOLAR_SYNTHESIZER).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('d', ECTags.Items.INGOTS_DRENCHED_IRON).key('h', ECItems.HARDENED_HANDLE)
				.patternLine("dhd").patternLine("i i").patternLine("wcw").build(consumer);
		prepareInstrumentRecipe(ECBlocks.INFUSER).key('i', Tags.Items.INGOTS_IRON).key('n', Tags.Items.NUGGETS_IRON).patternLine("n n").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).patternLine("i i").patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER_IMPROVED, ECItems.PURE_CRYSTAL).key('f', ECTags.Items.INGOTS_FIREITE).key('d', Tags.Items.GEMS_DIAMOND).key('b', ECItems.BINDER)
				.key('i', ECItems.INFUSER).patternLine("did").patternLine("fbf").patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.CRYSTALLIZER).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine("iwi").patternLine("i i").patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('p', ItemTags.CARPETS).key('g', Items.GRINDSTONE).patternLine("pip").patternLine("igi")
				.patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.INSCRIBER).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('d', Tags.Items.GEMS_DIAMOND).patternLine(" wi").patternLine("wdi").patternLine("wcw")
				.build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURE_INFUSER).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('n', ECBlocks.INFUSER).patternLine("wnw").patternLine("ici").patternLine("www")
				.build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_FURNACE, ECItems.FIRE_CRYSTAL).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('f', Blocks.FURNACE).patternLine("www").patternLine("wfw")
				.patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_BLAST_FURNACE, ECItems.FIRE_CRYSTAL).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('F', Blocks.BLAST_FURNACE).key('g', ECBlocks.BURNT_GLASS)
				.patternLine("www").patternLine("gFg").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURIFIER, ECItems.PURE_CRYSTAL).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('e', ECTags.Items.FINE_EARTH_GEMS).key('g', Tags.Items.INGOTS_GOLD)
				.patternLine("gig").patternLine("wew").patternLine("ici").build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.RETRIEVER).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('h', Blocks.HOPPER).key('d', Blocks.DISPENSER).key('w', ECBlocks.WHITE_ROCK)
				.addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).patternLine("iw ").patternLine("hdi").patternLine("iw ").build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.SORTER).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('h', Blocks.HOPPER).key('d', Blocks.DISPENSER)
				.addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).patternLine("ii ").patternLine("hdi").patternLine("ii ").build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.SPELL_DESK).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('l', Blocks.LECTERN).key('w', ECBlocks.WHITE_ROCK)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK)).patternLine("wlw").patternLine(" i ").patternLine(" w ").build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.ACCELERATION_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('i', Items.CLOCK).key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.PURE_CRYSTAL)
				.key('r', Tags.Items.DUSTS_REDSTONE).patternLine("rir").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.RANGE_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('g', Tags.Items.DUSTS_GLOWSTONE).key('w', ECBlocks.WHITE_ROCK)
				.key('c', ECItems.EARTH_CRYSTAL).patternLine("ggg").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.CAPACITY_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('g', ECItems.BURNT_GLASS).key('b', Items.BUCKET).key('w', ECBlocks.WHITE_ROCK)
				.key('c', ECItems.WATER_CRYSTAL).patternLine("gbg").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.EFFICIENCY_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('g', Tags.Items.INGOTS_GOLD).key('d', Tags.Items.GEMS_DIAMOND)
				.key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.FIRE_CRYSTAL).patternLine("gdg").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.STRENGTH_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('g', Tags.Items.DUSTS_GLOWSTONE).key('r', Tags.Items.RODS_BLAZE)
				.key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.FIRE_CRYSTAL).patternLine("grg").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.OPTIMIZATION_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('f', ECTags.Items.INGOTS_FIREITE).key('d', Tags.Items.GEMS_DIAMOND)
				.key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.PURE_CRYSTAL).patternLine("dfd").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.FORTUNE_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('l', Tags.Items.GEMS_LAPIS).key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.WATER_CRYSTAL)
				.patternLine("lll").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.SILK_TOUCH_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('s', ECItems.AIR_SILK).key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.PURE_CRYSTAL)
				.patternLine("sss").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.PLANTING_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('s', Tags.Items.SEEDS).key('h', Items.DIAMOND_HOE).key('w', ECBlocks.WHITE_ROCK)
				.key('c', ECItems.EARTH_CRYSTAL).patternLine("shs").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.BONELESS_GROWTH_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('b', Items.BONE_BLOCK).key('d', Tags.Items.GEMS_DIAMOND)
				.key('w', ECBlocks.WHITE_ROCK).key('c', ECItems.PURE_CRYSTAL).patternLine("bdb").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.PICKUP_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('e', Items.ENDER_EYE).key('h', Items.HOPPER).key('w', ECBlocks.WHITE_ROCK)
				.key('c', ECItems.PURE_CRYSTAL).patternLine("ehe").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.NECTOR_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('h', Items.HONEY_BLOCK).key('s', Items.SUGAR).key('w', ECBlocks.WHITE_ROCK)
				.key('c', ECItems.WATER_CRYSTAL).patternLine("shs").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.MYSTICAL_GROVE_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('p', ECTags.Items.BOTANIA_PETALS).key('m', ECTags.Items.INGOTS_MANASTEEL)
				.key('w', ECTags.Items.BOTANIA_LIVINGROCK).key('c', ECItems.WATER_CRYSTAL).patternLine("pmp").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.STEM_POLLINATION_SHRINE_UPGRADE).key('C', ECItems.SHRINE_UPGRADE_CORE).key('p', Items.PUMPKIN).key('b', Items.BONE_MEAL).key('w', ECBlocks.WHITE_ROCK)
				.key('c', ECItems.EARTH_CRYSTAL).patternLine("bpb").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.SHRINE_UPGRADE_CORE)).build(consumer);

		prepareInstrumentRecipe(ECBlocks.PIPE_IMPAIRED, ECItems.CONTAINED_CRYSTAL, 4).key('i', Tags.Items.INGOTS_IRON).patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE, ECItems.CONTAINED_CRYSTAL, 4).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPROVED, ECItems.CONTAINED_CRYSTAL, 4).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine("ici").build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.PIPE).addIngredient(ECBlocks.PIPE_IMPAIRED).addIngredient(Ingredient.fromTag(ECTags.Items.NUGGETS_DRENCHED_IRON), 5)
				.addCriterion(HAS_DRENCHED_IRON_NUGGET, hasItem(ECTags.Items.NUGGETS_DRENCHED_IRON)).build(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.PIPE, 4).addIngredient(ECBlocks.PIPE_IMPAIRED, 4).addIngredient(Ingredient.fromTag(ECTags.Items.INGOTS_DRENCHED_IRON), 2)
				.addCriterion(HAS_DRENCHED_IRON_INGOT, hasItem(ECTags.Items.INGOTS_DRENCHED_IRON)).build(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.PIPE_IMPROVED).addIngredient(ECBlocks.PIPE).addIngredient(Ingredient.fromTag(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.addCriterion(HAS_SWIFT_ALLOY_NUGGET, hasItem(ECTags.Items.NUGGETS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.PIPE_IMPROVED, 4).addIngredient(ECBlocks.PIPE, 4).addIngredient(Ingredient.fromTag(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.PIPE_IMPROVED).addIngredient(ECBlocks.PIPE_IMPAIRED).addIngredient(Ingredient.fromTag(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.addCriterion(HAS_SWIFT_ALLOY_NUGGET, hasItem(ECTags.Items.NUGGETS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.PIPE_IMPROVED, 4).addIngredient(ECBlocks.PIPE_IMPAIRED, 4).addIngredient(Ingredient.fromTag(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_ingot"));

		ShapedRecipeBuilder.shapedRecipe(ECItems.MINOR_RUNE_SLATE, 4).patternLine("www").patternLine("wiw").patternLine("www").key('w', ECBlocks.WHITE_ROCK).key('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.RUNE_SLATE, 4).patternLine("www").patternLine("wiw").patternLine("www").key('w', ECBlocks.WHITE_ROCK).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.MAJOR_RUNE_SLATE, 4).patternLine("www").patternLine("wiw").patternLine("www").key('w', ECBlocks.WHITE_ROCK).key('i', ECTags.Items.INGOTS_FIREITE)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK)).build(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.INERT_CRYSTAL), ECItems.FIRE_CRYSTAL, ElementType.FIRE).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.INERT_CRYSTAL), ECItems.WATER_CRYSTAL, ElementType.WATER).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.INERT_CRYSTAL), ECItems.AIR_CRYSTAL, ElementType.AIR).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.INERT_CRYSTAL), ECItems.EARTH_CRYSTAL, ElementType.EARTH).build(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(Items.STONE), ECItems.WHITE_ROCK, ElementType.EARTH).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.INGOTS_IRON), ECItems.DRENCHED_IRON_INGOT, ElementType.WATER).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.GLASS), ECBlocks.BURNT_GLASS, ElementType.FIRE).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.STRING), ECItems.AIR_SILK, ElementType.AIR).withElementAmount(500).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_PYLON, ElementType.FIRE).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.FIRE_CRYSTAL).addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Tags.Items.INGOTS_GOLD).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.VACUUM_SHRINE, ElementType.AIR).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.AIR_CRYSTAL).addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.GROWTH_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(Items.WHEAT_SEEDS).addIngredient(Items.BONE_MEAL).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.LAVA_SHRINE, ElementType.FIRE).addIngredient(ECItems.FIRE_PYLON).addIngredient(ECItems.FIRE_CRYSTAL).addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.addIngredient(Blocks.OBSIDIAN).addIngredient(Items.LAVA_BUCKET).addIngredient(Items.BLAZE_ROD).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARVEST_SHRINE, ElementType.EARTH).addIngredient(ECItems.GROWTH_SHRINE).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS).addIngredient(Items.DIAMOND_HOE).addIngredient(Items.DIAMOND_AXE).addIngredient(Items.SHEARS).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.ORE_SHRINE, ElementType.EARTH).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.EARTH_CRYSTAL).addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.addIngredient(Items.DIAMOND_PICKAXE).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.OVERLOAD_SHRINE, ElementType.AIR).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.AIR_CRYSTAL).addIngredient(ECItems.PURE_CRYSTAL)
				.addIngredient(Items.CLOCK).addIngredient(Items.ENDER_EYE).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.SWEET_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(Items.SUGAR).addIngredient(Items.HONEY_BOTTLE).addIngredient(Items.MILK_BUCKET).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.ENDER_LOC_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL).addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Items.ENDER_EYE).addIngredient(Items.DRAGON_BREATH).addIngredient(Items.OBSIDIAN).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.BREEDING_SHRINE, ElementType.EARTH).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.EARTH_CRYSTAL).addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Tags.Items.CROPS).addIngredient(Tags.Items.LEATHER).addIngredient(Items.MILK_BUCKET).addIngredient(Tags.Items.GEMS_DIAMOND).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.GROVE_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL)
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(ItemTags.FLOWERS).addIngredient(Tags.Items.SEEDS).addIngredient(Tags.Items.CROPS).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_PEDESTAL, ElementType.FIRE).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.WATER_PEDESTAL, ElementType.WATER).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.EARTH_PEDESTAL, ElementType.EARTH).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.AIR_PEDESTAL, ElementType.AIR).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_AIR_GEMS).addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.SWIFT_ALLOY_INGOT, ElementType.AIR).addIngredient(Tags.Items.INGOTS_GOLD).addIngredient(ECTags.Items.INGOTS_DRENCHED_IRON)
				.addIngredient(Tags.Items.DUSTS_REDSTONE).addIngredient(ECItems.AIR_CRYSTAL).withElementAmount(1250).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.FIREITE_INGOT, ElementType.FIRE).addIngredient(Tags.Items.INGOTS_NETHERITE).addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECItems.PURE_CRYSTAL).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARDENED_HANDLE, ElementType.EARTH).addIngredient(Items.STICK).addIngredient(ECBlocks.WHITE_ROCK).addIngredient(ECItems.AIR_SILK)
				.addIngredient(ECItems.EARTH_CRYSTAL).withElementAmount(1250).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_LENSE, ElementType.FIRE).addIngredient(Tags.Items.GEMS_QUARTZ).addIngredient(ECBlocks.BURNT_GLASS_PANE).addIngredient(ECItems.FIRE_CRYSTAL)
				.withElementAmount(1000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.WATER_LENSE, ElementType.WATER).addIngredient(Tags.Items.GEMS_QUARTZ).addIngredient(ECBlocks.BURNT_GLASS_PANE).addIngredient(ECItems.WATER_CRYSTAL)
				.withElementAmount(1000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.EARTH_LENSE, ElementType.EARTH).addIngredient(Tags.Items.GEMS_QUARTZ).addIngredient(ECBlocks.BURNT_GLASS_PANE).addIngredient(ECItems.EARTH_CRYSTAL)
				.withElementAmount(1000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.AIR_LENSE, ElementType.AIR).addIngredient(Tags.Items.GEMS_QUARTZ).addIngredient(ECBlocks.BURNT_GLASS_PANE).addIngredient(ECItems.AIR_CRYSTAL)
				.withElementAmount(1000).build(consumer);

		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE).setGem(ECTags.Items.INPUT_FIRE_GEMS).setCrystal(ECItems.FIRE_CRYSTAL).setShard(ECTags.Items.FIRE_SHARDS)
				.addOutput(ECItems.CRUDE_FIRE_GEM, 15).addOutput(ECItems.FINE_FIRE_GEM, 4).addOutput(ECItems.PRISTINE_FIRE_GEM, 1).build(consumer, "fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER).setGem(ECTags.Items.INPUT_WATER_GEMS).setCrystal(ECItems.WATER_CRYSTAL).setShard(ECTags.Items.WATER_SHARDS)
				.addOutput(ECItems.CRUDE_WATER_GEM, 15).addOutput(ECItems.FINE_WATER_GEM, 4).addOutput(ECItems.PRISTINE_WATER_GEM, 1).build(consumer, "water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH).setGem(ECTags.Items.INPUT_EARTH_GEMS).setCrystal(ECItems.EARTH_CRYSTAL).setShard(ECTags.Items.EARTH_SHARDS)
				.addOutput(ECItems.CRUDE_EARTH_GEM, 15).addOutput(ECItems.FINE_EARTH_GEM, 4).addOutput(ECItems.PRISTINE_EARTH_GEM, 1).build(consumer, "earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR).setGem(ECTags.Items.INPUT_AIR_GEMS).setCrystal(ECItems.AIR_CRYSTAL).setShard(ECTags.Items.AIR_SHARDS)
				.addOutput(ECItems.CRUDE_AIR_GEM, 15).addOutput(ECItems.FINE_AIR_GEM, 4).addOutput(ECItems.PRISTINE_AIR_GEM, 1).build(consumer, "air_gem");

		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE).setGem(ECTags.Items.INPUT_FIRE_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.FIRE_SHARDS)
				.addOutput(ECItems.PRISTINE_FIRE_GEM, 1).build(consumer, "pristine_fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER).setGem(ECTags.Items.INPUT_WATER_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.WATER_SHARDS)
				.addOutput(ECItems.PRISTINE_WATER_GEM, 1).build(consumer, "pristine_water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH).setGem(ECTags.Items.INPUT_EARTH_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.EARTH_SHARDS)
				.addOutput(ECItems.PRISTINE_EARTH_GEM, 1).build(consumer, "pristine_earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR).setGem(ECTags.Items.INPUT_AIR_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.AIR_SHARDS)
				.addOutput(ECItems.PRISTINE_AIR_GEM, 1).build(consumer, "pristine_air_gem");

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_CRYSTAL).setIngredient(Tags.Items.GEMS_DIAMOND).setIngredient(ElementType.WATER, ECItems.WATER_CRYSTAL)
				.setIngredient(ElementType.FIRE, ECItems.FIRE_CRYSTAL).setIngredient(ElementType.EARTH, ECItems.EARTH_CRYSTAL).setIngredient(ElementType.AIR, ECItems.AIR_CRYSTAL).build(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_ROCK).setIngredient(Items.OBSIDIAN).setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, ECTags.Items.INGOTS_FIREITE).setIngredient(ElementType.EARTH, ECItems.WHITE_ROCK).setIngredient(ElementType.AIR, Items.PURPUR_BLOCK).build(consumer);

		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("manx"), ElementType.AIR).withElementAmount(2000).setSlate(ECItems.MINOR_RUNE_SLATE).addIngredient(ECTags.Items.CRUDE_AIR_GEMS)
				.addIngredient(Items.SUGAR).addIngredient(Items.SUGAR).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("jita"), ElementType.AIR).setSlate(ECItems.RUNE_SLATE).addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Tags.Items.STRING).addIngredient(Tags.Items.STRING).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tano"), ElementType.AIR).withElementAmount(10000).setSlate(ECItems.MAJOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS).addIngredient(ECItems.AIR_SILK).addIngredient(ECItems.AIR_SILK).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("wii"), ElementType.FIRE).withElementAmount(2000).setSlate(ECItems.MINOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.CRUDE_FIRE_GEMS).addIngredient(ItemTags.COALS).addIngredient(ItemTags.COALS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("fus"), ElementType.FIRE).setSlate(ECItems.RUNE_SLATE).addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(Items.BLAZE_ROD).addIngredient(Items.BLAZE_ROD).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("zod"), ElementType.FIRE).withElementAmount(10000).setSlate(ECItems.MAJOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS).addIngredient(Tags.Items.STORAGE_BLOCKS_COAL).addIngredient(Tags.Items.STORAGE_BLOCKS_COAL).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("claptrap"), ElementType.WATER).withElementAmount(2000).setSlate(ECItems.MINOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS).addIngredient(Tags.Items.GEMS_LAPIS).addIngredient(Tags.Items.GEMS_LAPIS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("bombadil"), ElementType.WATER).setSlate(ECItems.RUNE_SLATE).addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS).addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tzeentch"), ElementType.WATER).withElementAmount(10000).setSlate(ECItems.MAJOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS).addIngredient(Tags.Items.GEMS_EMERALD).addIngredient(Tags.Items.GEMS_EMERALD).build(consumer);

		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ECBlocks.CRYSTAL_ORE), ECItems.INERT_CRYSTAL, 0.5F, 200).addCriterion("has_crystal_ore", hasItem(ECBlocks.CRYSTAL_ORE)).build(consumer);
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ECBlocks.CRYSTAL_ORE), ECItems.INERT_CRYSTAL, 0.5F, 100).addCriterion("has_crystal_ore", hasItem(ECBlocks.CRYSTAL_ORE)).build(consumer,
				ElementalCraft.createRL("inertcrystal_from_blasting"));
		
		AirMillGrindingRecipeBuilder.grindingRecipe(Items.GRAVEL).withIngredient(Tags.Items.COBBLESTONE).build(consumer);
		AirMillGrindingRecipeBuilder.grindingRecipe(Items.SAND).withIngredient(Tags.Items.GRAVEL).build(consumer);

	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(block.getRegistryName(), ResourcePackType.SERVER_DATA, ".json", "recipes");
	}

	private void registerSlabsStairsWalls(Consumer<IFinishedRecipe> consumer) {
		ForgeRegistries.BLOCKS.forEach(block -> {
			if (ElementalCraft.MODID.equals(block.getRegistryName().getNamespace()) && !exists(block) && (block instanceof SlabBlock || block instanceof StairsBlock || block instanceof WallBlock)) {
				String name = block.getRegistryName().getPath();
				String sourceName = name.substring(0, name.length() - (block instanceof StairsBlock ? 7 : 5));
				IItemProvider source = ForgeRegistries.ITEMS.getValue(ElementalCraft.createRL(sourceName));
				ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shapedRecipe(block, block instanceof StairsBlock ? 4 : 6).key('#', source);

				if (block instanceof SlabBlock) {
					shaped.patternLine("###");
				} else if (block instanceof StairsBlock) {
					shaped.patternLine("#  ").patternLine("## ").patternLine("###");
				} else if (block instanceof WallBlock) {
					shaped.patternLine("###").patternLine("###");
				}
				shaped.addCriterion("has_" + sourceName, hasItem(source)).build(consumer);
				SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source), block, block instanceof SlabBlock ? 2 : 1).addCriterion("has_" + sourceName, hasItem(source)).build(consumer,
						ElementalCraft.createRL(name + "_from_" + sourceName + "_stonecutting"));
			}
		});
	}

	private void createNuggetIngotBlock(IItemProvider nugget, ITag<Item> nuggetTag, IItemProvider ingot, ITag<Item> ingotTag, IItemProvider block, ITag<Item> blockTag,
			Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ingot).key('#', nuggetTag).patternLine("###").patternLine("###").patternLine("###").addCriterion(has(nugget), hasItem(nuggetTag)).build(consumer,
				ElementalCraft.createRL(from(nugget, ingot)));
		ShapelessRecipeBuilder.shapelessRecipe(nugget, 9).addIngredient(ingotTag).addCriterion(has(ingot), hasItem(ingotTag)).build(consumer, from(ingot, nugget));
		ShapedRecipeBuilder.shapedRecipe(block).key('#', ingotTag).patternLine("###").patternLine("###").patternLine("###").addCriterion(has(ingot), hasItem(ingotTag)).build(consumer,
				from(ingot, block));
		ShapelessRecipeBuilder.shapelessRecipe(ingot, 9).addIngredient(blockTag).addCriterion(has(block), hasItem(blockTag)).build(consumer, from(block, ingot));
	}

	private void createStorageBlock(IItemProvider item, IItemProvider block, Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(block).key('#', item).patternLine("###").patternLine("###").patternLine("###").addCriterion(has(item), hasItem(item)).build(consumer,
				ElementalCraft.createRL(from(item, block)));
		ShapelessRecipeBuilder.shapelessRecipe(item, 9).addIngredient(block).addCriterion(has(block), hasItem(block)).build(consumer, from(block, item));
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL, 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result, IItemProvider crystal, int count) {
		return ShapedRecipeBuilder.shapedRecipe(result, count).key('c', crystal).addCriterion(has(crystal), hasItem(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL, 1).key('w', ECBlocks.WHITE_ROCK).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(IItemProvider result, IItemProvider crystal) {
		return prepareInstrumentRecipe(result, crystal, 1).key('w', ECBlocks.WHITE_ROCK).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.WHITE_ROCK));
	}

	private String from(IItemProvider from, IItemProvider to) {
		return to.asItem().getRegistryName().getPath() + "_from_" + from.asItem().getRegistryName().getPath();
	}

	private String has(IItemProvider item) {
		return "has_" + item.asItem().getRegistryName().getPath();
	}
}
