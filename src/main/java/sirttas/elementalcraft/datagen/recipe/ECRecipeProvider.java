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

		ShapedRecipeBuilder.shapedRecipe(ECItems.containedCrystal).key('g', Tags.Items.NUGGETS_GOLD).key('c', ECItems.inertCrystal).patternLine(" g ").patternLine("gcg").patternLine(" g ")
				.addCriterion(HAS_INERTCRYSTAL, hasItem(ECItems.inertCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.shrineBase).key('w', ECBlocks.whiteRock).key('c', ECItems.inertCrystal).key('p', ECBlocks.elementPipe).patternLine(" p ").patternLine("pcp")
				.patternLine("www").addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.emptyReceptacle).key('c', ECItems.pureCrystal).key('d', Tags.Items.GEMS_DIAMOND).key('g', Tags.Items.INGOTS_GOLD)
				.key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine("gig").patternLine("dcd").patternLine("gig").addCriterion(HAS_PURECRYSTAL, hasItem(ECItems.pureCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.chisel).key('h', ECItems.hardenedHandle).key('d', Tags.Items.GEMS_DIAMOND).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine(" i ")
				.patternLine(" di").patternLine("h  ").addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.shrineUpgradeCore).key('c', ECItems.containedCrystal).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('r', Tags.Items.DUSTS_REDSTONE).patternLine("rir")
				.patternLine("ici").patternLine("rir").addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.scrollPaper).addIngredient(ECItems.airCrystal).addIngredient(Items.PAPER).addIngredient(Items.INK_SAC)
				.addCriterion("has_air_silk", hasItem(ECItems.airSilk)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.fireElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall)
				.key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('c', ECItems.fireCrystal).patternLine("geg").patternLine("iti").patternLine("gcg")
				.addCriterion("has_firecrystal", hasItem(ECItems.fireCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.waterElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall)
				.key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('c', ECItems.waterCrystal).patternLine("geg").patternLine("iti").patternLine("gcg")
				.addCriterion("has_watercrystal", hasItem(ECItems.waterCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.earthElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall)
				.key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('c', ECItems.earthCrystal).patternLine("geg").patternLine("iti").patternLine("gcg")
				.addCriterion("has_earthcrystal", hasItem(ECItems.earthCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.airElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall)
				.key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('c', ECItems.airCrystal).patternLine("geg").patternLine("iti").patternLine("gcg")
				.addCriterion("has_aircrystal", hasItem(ECItems.airCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.focus).key('d', Tags.Items.GEMS_DIAMOND).key('c', ECItems.containedCrystal).key('s', ECItems.hardenedHandle).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.patternLine(" ic").patternLine(" si").patternLine("d  ").addCriterion(HAS_CONTAINEDCRYSTAL, hasItem(ECItems.containedCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.spellBook).key('c', ECItems.pureCrystal).key('s', ECItems.airSilk).key('l', Tags.Items.LEATHER).key('p', ECItems.scrollPaper).patternLine("slp")
				.patternLine("clp").patternLine("slp").addCriterion(HAS_PURECRYSTAL, hasItem(ECItems.pureCrystal)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECBlocks.burntGlassPane, 16).key('#', ECBlocks.burntGlass).patternLine("###").patternLine("###").addCriterion("has_burnt_glass", hasItem(ECBlocks.burntGlass))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.whiteRockFence, 16).key('#', ECBlocks.whiteRock).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).patternLine("#i#").patternLine("#i#")
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.whiteRockBrick, 4).key('#', ECBlocks.whiteRock).patternLine("##").patternLine("##").addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock))
				.build(consumer);
		SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(ECBlocks.whiteRock), ECBlocks.whiteRockBrick).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer,
				ElementalCraft.createRL("whiterock_brick_from_whiterock_stonecutting"));

		createNuggetIngotBlock(ECItems.drenchedIronNugget, ECTags.Items.NUGGETS_DRENCHED_IRON, ECItems.drenchedIronIngot, ECTags.Items.INGOTS_DRENCHED_IRON, ECItems.drenchedIronBlock,
				ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON, consumer);
		createNuggetIngotBlock(ECItems.swiftAlloyNugget, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECItems.swiftAlloyIngot, ECTags.Items.INGOTS_SWIFT_ALLOY, ECItems.swiftAlloyBlock,
				ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY, consumer);
		createNuggetIngotBlock(ECItems.fireiteNugget, ECTags.Items.NUGGETS_FIREITE, ECItems.fireiteIngot, ECTags.Items.INGOTS_FIREITE, ECItems.fireiteBlock, ECTags.Items.STORAGE_BLOCKS_FIREITE,
				consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.powerfulFireShard).key('#', ECItems.fireShard).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_fire_shard", hasItem(ECItems.fireShard)).build(consumer, ElementalCraft.createRL("powerful_fire_shard_from_fire_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.fireShard, 9).addIngredient(ECItems.powerfulFireShard).addCriterion("has_powerful_fire_shard", hasItem(ECItems.powerfulFireShard))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.powerfulWaterShard).key('#', ECItems.waterShard).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_water_shard", hasItem(ECItems.waterShard)).build(consumer, ElementalCraft.createRL("powerful_water_shard_from_water_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.waterShard, 9).addIngredient(ECItems.powerfulWaterShard).addCriterion("has_powerful_water_shard", hasItem(ECItems.powerfulWaterShard))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.powerfulEarthShard).key('#', ECItems.earthShard).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_earth_shard", hasItem(ECItems.earthShard)).build(consumer, ElementalCraft.createRL("powerful_earth_shard_from_earth_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.earthShard, 9).addIngredient(ECItems.powerfulEarthShard).addCriterion("has_powerful_earth_shard", hasItem(ECItems.powerfulEarthShard))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.powerfulAirShard).key('#', ECItems.airShard).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_air_shard", hasItem(ECItems.airShard)).build(consumer, ElementalCraft.createRL("powerful_air_shard_from_air_shards"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.airShard, 9).addIngredient(ECItems.powerfulAirShard).addCriterion("has_powerful_air_shard", hasItem(ECItems.powerfulAirShard)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECBlocks.tankSmall).key('g', Tags.Items.GLASS).key('p', ECBlocks.impairedElementPipe).patternLine(" p ").patternLine("pgp").patternLine(" p ")
				.addCriterion(HAS_CONTAINEDCRYSTAL, hasItem(ECItems.containedCrystal)).build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.tankSmall).addIngredient(ECBlocks.tankSmall).addCriterion("has_tank_small", hasItem(ECBlocks.tankSmall)).build(consumer, "tank_small_emptying");
		prepareWhiterockInstrumentRecipe(ECBlocks.tank).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('g', ECBlocks.burntGlass).key('p', ECBlocks.elementPipe).patternLine("ici").patternLine("pgp")
				.patternLine("www").build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.tank).addIngredient(ECBlocks.tank).addCriterion("has_tank", hasItem(ECBlocks.tank)).build(consumer, "tank_emptying");
		prepareInstrumentRecipe(ECBlocks.extractor).key('i', Tags.Items.INGOTS_IRON).patternLine(" c ").patternLine(" i ").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.improvedExtractor, ECItems.pureCrystal).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('e', ECBlocks.extractor).patternLine(" e ").patternLine("eie")
				.patternLine("wcw").build(consumer);
		prepareInstrumentRecipe(ECBlocks.evaporator).key('i', Tags.Items.INGOTS_IRON).key('g', Tags.Items.GLASS).key('d', Tags.Items.GEMS_DIAMOND).patternLine("idi").patternLine("igi")
				.patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.infuser).key('i', Tags.Items.INGOTS_IRON).key('n', Tags.Items.NUGGETS_IRON).patternLine("n n").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.binder).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).patternLine("i i").patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.improvedBinder, ECItems.pureCrystal).key('f', ECTags.Items.INGOTS_FIREITE).key('d', Tags.Items.GEMS_DIAMOND).key('b', ECItems.binder)
				.key('i', ECItems.infuser).patternLine("did").patternLine("fbf").patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.crystallizer).key('i', ECItems.swiftAlloyIngot).patternLine("iwi").patternLine("i i").patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.inscriber).key('i', ECItems.swiftAlloyIngot).key('d', Tags.Items.GEMS_DIAMOND).patternLine(" wi").patternLine("wdi").patternLine("wcw")
				.build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.pureInfuser).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('n', ECBlocks.infuser).patternLine("wnw").patternLine("ici").patternLine("www")
				.build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.fireFurnace, ECItems.fireCrystal).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('f', Blocks.FURNACE).patternLine("www").patternLine("wfw")
				.patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.fireBlastFurnace, ECItems.fireCrystal).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('F', Blocks.BLAST_FURNACE).key('g', ECBlocks.burntGlass)
				.patternLine("www").patternLine("gFg").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.purifier, ECItems.pureCrystal).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('e', ECTags.Items.FINE_EARTH_GEMS).key('g', Tags.Items.INGOTS_GOLD)
				.patternLine("gig").patternLine("wew").patternLine("ici").build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.instrumentRetriever).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).key('h', Blocks.HOPPER).key('d', Blocks.DISPENSER).key('w', ECBlocks.whiteRock)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).patternLine("iw ").patternLine("hdi").patternLine("iw ").build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.spellDesk).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).key('l', Blocks.LECTERN).key('w', ECBlocks.whiteRock)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).patternLine("wlw").patternLine(" i ").patternLine(" w ").build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.accelerationShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('i', Items.CLOCK).key('w', ECBlocks.whiteRock).key('c', ECItems.pureCrystal)
				.key('r', Tags.Items.DUSTS_REDSTONE).patternLine("rir").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.rangeShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('g', Tags.Items.DUSTS_GLOWSTONE).key('w', ECBlocks.whiteRock)
				.key('c', ECItems.earthCrystal).patternLine("ggg").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.capacityShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('g', ECItems.burntGlass).key('b', Items.BUCKET).key('w', ECBlocks.whiteRock)
				.key('c', ECItems.waterCrystal).patternLine("gbg").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.efficiencyShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('g', Tags.Items.INGOTS_GOLD).key('d', Tags.Items.GEMS_DIAMOND)
				.key('w', ECBlocks.whiteRock).key('c', ECItems.fireCrystal).patternLine("gdg").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.strengthShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('g', Tags.Items.DUSTS_GLOWSTONE).key('r', Tags.Items.RODS_BLAZE)
				.key('w', ECBlocks.whiteRock).key('c', ECItems.fireCrystal).patternLine("grg").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.optimizationShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('f', ECTags.Items.INGOTS_FIREITE).key('d', Tags.Items.GEMS_DIAMOND)
				.key('w', ECBlocks.whiteRock).key('c', ECItems.pureCrystal).patternLine("dfd").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.fortuneShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('l', Tags.Items.GEMS_LAPIS).key('w', ECBlocks.whiteRock).key('c', ECItems.waterCrystal)
				.patternLine("lll").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.silkTouchShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('s', ECItems.airSilk).key('w', ECBlocks.whiteRock).key('c', ECItems.pureCrystal)
				.patternLine("sss").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.plantingShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('s', Tags.Items.SEEDS).key('h', Items.DIAMOND_HOE).key('w', ECBlocks.whiteRock)
				.key('c', ECItems.earthCrystal).patternLine("shs").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.bonelseeGrowthShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('b', Items.BONE_BLOCK).key('d', Tags.Items.GEMS_DIAMOND)
				.key('w', ECBlocks.whiteRock).key('c', ECItems.pureCrystal).patternLine("bdb").patternLine("wCw").patternLine(" c ")
				.addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.pickupShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('e', Items.ENDER_EYE).key('h', Items.HOPPER).key('w', ECBlocks.whiteRock)
				.key('c', ECItems.pureCrystal).patternLine("ehe").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.nectarShrineUpgrade).key('C', ECItems.shrineUpgradeCore).key('h', Items.HONEY_BLOCK).key('s', Items.SUGAR).key('w', ECBlocks.whiteRock)
				.key('c', ECItems.waterCrystal).patternLine("shs").patternLine("wCw").patternLine(" c ").addCriterion(HAS_SHRINE_UPGRADE_CORE, hasItem(ECItems.shrineUpgradeCore)).build(consumer);

		prepareInstrumentRecipe(ECBlocks.impairedElementPipe, ECItems.containedCrystal, 4).key('i', Tags.Items.INGOTS_IRON).patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.elementPipe, ECItems.containedCrystal, 4).key('i', ECTags.Items.INGOTS_DRENCHED_IRON).patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.improvedElementPipe, ECItems.containedCrystal, 4).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY).patternLine("ici").build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.elementPipe).addIngredient(ECBlocks.impairedElementPipe).addIngredient(Ingredient.fromTag(ECTags.Items.NUGGETS_DRENCHED_IRON), 5)
				.addCriterion(HAS_DRENCHED_IRON_NUGGET, hasItem(ECTags.Items.NUGGETS_DRENCHED_IRON)).build(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.elementPipe, 4).addIngredient(ECBlocks.impairedElementPipe, 4).addIngredient(Ingredient.fromTag(ECTags.Items.INGOTS_DRENCHED_IRON), 2)
				.addCriterion(HAS_DRENCHED_IRON_INGOT, hasItem(ECTags.Items.INGOTS_DRENCHED_IRON)).build(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe).addIngredient(ECBlocks.elementPipe).addIngredient(Ingredient.fromTag(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.addCriterion(HAS_SWIFT_ALLOY_NUGGET, hasItem(ECTags.Items.NUGGETS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe, 4).addIngredient(ECBlocks.elementPipe, 4).addIngredient(Ingredient.fromTag(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe).addIngredient(ECBlocks.impairedElementPipe).addIngredient(Ingredient.fromTag(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.addCriterion(HAS_SWIFT_ALLOY_NUGGET, hasItem(ECTags.Items.NUGGETS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe, 4).addIngredient(ECBlocks.impairedElementPipe, 4).addIngredient(Ingredient.fromTag(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.addCriterion(HAS_SWIFT_ALLOY_INGOT, hasItem(ECTags.Items.INGOTS_SWIFT_ALLOY)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_ingot"));

		ShapedRecipeBuilder.shapedRecipe(ECItems.minorRuneSlate, 4).patternLine("www").patternLine("wiw").patternLine("www").key('w', ECBlocks.whiteRock).key('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.runeSlate, 4).patternLine("www").patternLine("wiw").patternLine("www").key('w', ECBlocks.whiteRock).key('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.majorRuneSlate, 4).patternLine("www").patternLine("wiw").patternLine("www").key('w', ECBlocks.whiteRock).key('i', ECTags.Items.INGOTS_FIREITE)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.fireCrystal, ElementType.FIRE).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.waterCrystal, ElementType.WATER).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.airCrystal, ElementType.AIR).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.earthCrystal, ElementType.EARTH).build(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(Items.STONE), ECItems.whiteRock, ElementType.EARTH).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.INGOTS_IRON), ECItems.drenchedIronIngot, ElementType.WATER).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.GLASS), ECBlocks.burntGlass, ElementType.FIRE).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.STRING), ECItems.airSilk, ElementType.AIR).withElementAmount(500).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.firePylon, ElementType.FIRE).addIngredient(ECItems.shrineBase).addIngredient(ECItems.fireCrystal).addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Tags.Items.INGOTS_GOLD).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.vacuumShrine, ElementType.AIR).addIngredient(ECItems.shrineBase).addIngredient(ECItems.airCrystal).addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.growthShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.earthCrystal)
				.addIngredient(Items.WHEAT_SEEDS).addIngredient(Items.BONE_MEAL).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.lavaShrine, ElementType.FIRE).addIngredient(ECItems.firePylon).addIngredient(ECItems.fireCrystal).addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.addIngredient(Blocks.OBSIDIAN).addIngredient(Items.LAVA_BUCKET).addIngredient(Items.BLAZE_ROD).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.harvestShrine, ElementType.EARTH).addIngredient(ECItems.growthShrine).addIngredient(ECItems.earthCrystal)
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS).addIngredient(Items.DIAMOND_HOE).addIngredient(Items.DIAMOND_AXE).addIngredient(Items.SHEARS).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.oreShrine, ElementType.EARTH).addIngredient(ECItems.shrineBase).addIngredient(ECItems.earthCrystal).addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.addIngredient(Items.DIAMOND_PICKAXE).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.overloadShrine, ElementType.AIR).addIngredient(ECItems.shrineBase).addIngredient(ECItems.airCrystal).addIngredient(ECItems.pureCrystal)
				.addIngredient(Items.CLOCK).addIngredient(Items.ENDER_EYE).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.sweetShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.earthCrystal)
				.addIngredient(Items.SUGAR).addIngredient(Items.HONEY_BOTTLE).addIngredient(Items.MILK_BUCKET).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.enderLockShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Items.ENDER_EYE).addIngredient(Items.DRAGON_BREATH).addIngredient(Items.OBSIDIAN).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.breedingShrine, ElementType.EARTH).addIngredient(ECItems.shrineBase).addIngredient(ECItems.earthCrystal).addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Tags.Items.CROPS).addIngredient(Tags.Items.LEATHER).addIngredient(Items.MILK_BUCKET).addIngredient(Tags.Items.GEMS_DIAMOND).withElementAmount(5000).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.firePedestal, ElementType.FIRE).addIngredient(ECItems.infuser).addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.whiteRock).addIngredient(ECItems.whiteRock).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.waterPedestal, ElementType.WATER).addIngredient(ECItems.infuser).addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.whiteRock).addIngredient(ECItems.whiteRock).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.earthPedestal, ElementType.EARTH).addIngredient(ECItems.infuser).addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.whiteRock).addIngredient(ECItems.whiteRock).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.airPedestal, ElementType.AIR).addIngredient(ECItems.infuser).addIngredient(ECTags.Items.FINE_AIR_GEMS).addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECItems.whiteRock).addIngredient(ECItems.whiteRock).withElementAmount(30000).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.swiftAlloyIngot, ElementType.AIR).addIngredient(Tags.Items.INGOTS_GOLD).addIngredient(ECTags.Items.INGOTS_DRENCHED_IRON)
				.addIngredient(Tags.Items.DUSTS_REDSTONE).addIngredient(ECItems.airCrystal).withElementAmount(1250).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.fireiteIngot, ElementType.FIRE).addIngredient(Tags.Items.INGOTS_NETHERITE).addIngredient(ECItems.pureCrystal).withElementAmount(30000)
				.build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.hardenedHandle, ElementType.EARTH).addIngredient(Items.STICK).addIngredient(ECBlocks.whiteRock).addIngredient(ECItems.airSilk)
				.addIngredient(ECItems.earthCrystal).withElementAmount(1250).build(consumer);

		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE).setGem(ECTags.Items.INPUT_FIRE_GEMS).setCrystal(ECItems.fireCrystal).setShard(ECTags.Items.FIRE_SHARDS)
				.addOutput(ECItems.crudeFireGem, 15).addOutput(ECItems.fineFireGem, 4).addOutput(ECItems.pristineFireGem, 1).build(consumer, "fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER).setGem(ECTags.Items.INPUT_WATER_GEMS).setCrystal(ECItems.waterCrystal).setShard(ECTags.Items.WATER_SHARDS)
				.addOutput(ECItems.crudeWaterGem, 15).addOutput(ECItems.fineWaterGem, 4).addOutput(ECItems.pristineWaterGem, 1).build(consumer, "water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH).setGem(ECTags.Items.INPUT_EARTH_GEMS).setCrystal(ECItems.earthCrystal).setShard(ECTags.Items.EARTH_SHARDS)
				.addOutput(ECItems.crudeEarthGem, 15).addOutput(ECItems.fineEarthGem, 4).addOutput(ECItems.pristineEarthGem, 1).build(consumer, "earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR).setGem(ECTags.Items.INPUT_AIR_GEMS).setCrystal(ECItems.airCrystal).setShard(ECTags.Items.AIR_SHARDS)
				.addOutput(ECItems.crudeAirGem, 15).addOutput(ECItems.fineAirGem, 4).addOutput(ECItems.pristineAirGem, 1).build(consumer, "air_gem");

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureCrystal).setIngredient(Tags.Items.GEMS_DIAMOND).setIngredient(ElementType.WATER, ECItems.waterCrystal)
				.setIngredient(ElementType.FIRE, ECItems.fireCrystal).setIngredient(ElementType.EARTH, ECItems.earthCrystal).setIngredient(ElementType.AIR, ECItems.airCrystal).build(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureRock).setIngredient(Items.OBSIDIAN).setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, ECTags.Items.INGOTS_FIREITE).setIngredient(ElementType.EARTH, ECItems.whiteRock).setIngredient(ElementType.AIR, Items.PURPUR_BLOCK).build(consumer);

		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("manx"), ElementType.AIR).withElementAmount(2000).setSlate(ECItems.minorRuneSlate).addIngredient(ECTags.Items.CRUDE_AIR_GEMS)
				.addIngredient(Items.SUGAR).addIngredient(Items.SUGAR).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("jita"), ElementType.AIR).setSlate(ECItems.runeSlate).addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Tags.Items.STRING).addIngredient(Tags.Items.STRING).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tano"), ElementType.AIR).withElementAmount(10000).setSlate(ECItems.majorRuneSlate)
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS).addIngredient(ECItems.airSilk).addIngredient(ECItems.airSilk).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("fus"), ElementType.FIRE).withElementAmount(2000).setSlate(ECItems.minorRuneSlate)
				.addIngredient(ECTags.Items.CRUDE_FIRE_GEMS).addIngredient(ItemTags.COALS).addIngredient(ItemTags.COALS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("wii"), ElementType.FIRE).setSlate(ECItems.runeSlate).addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(Items.BLAZE_ROD).addIngredient(Items.BLAZE_ROD).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("zod"), ElementType.FIRE).withElementAmount(10000).setSlate(ECItems.majorRuneSlate)
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS).addIngredient(Tags.Items.STORAGE_BLOCKS_COAL).addIngredient(Tags.Items.STORAGE_BLOCKS_COAL).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("claptrap"), ElementType.WATER).withElementAmount(2000).setSlate(ECItems.minorRuneSlate)
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS).addIngredient(Tags.Items.GEMS_LAPIS).addIngredient(Tags.Items.GEMS_LAPIS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("bombadil"), ElementType.WATER).setSlate(ECItems.runeSlate).addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS).addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tzeentch"), ElementType.WATER).withElementAmount(10000).setSlate(ECItems.majorRuneSlate)
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS).addIngredient(Tags.Items.GEMS_EMERALD).addIngredient(Tags.Items.GEMS_EMERALD).build(consumer);

		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ECBlocks.crystalOre), ECItems.inertCrystal, 0.5F, 200).addCriterion("has_crystal_ore", hasItem(ECBlocks.crystalOre)).build(consumer);
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ECBlocks.crystalOre), ECItems.inertCrystal, 0.5F, 100).addCriterion("has_crystal_ore", hasItem(ECBlocks.crystalOre)).build(consumer,
				ElementalCraft.createRL("inertcrystal_from_blasting"));

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

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.containedCrystal, 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result, IItemProvider crystal, int count) {
		return ShapedRecipeBuilder.shapedRecipe(result, count).key('c', crystal).addCriterion(has(crystal), hasItem(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.containedCrystal, 1).key('w', ECBlocks.whiteRock).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(IItemProvider result, IItemProvider crystal) {
		return prepareInstrumentRecipe(result, crystal, 1).key('w', ECBlocks.whiteRock).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock));
	}

	private String from(IItemProvider from, IItemProvider to) {
		return to.asItem().getRegistryName().getPath() + "_from_" + from.asItem().getRegistryName().getPath();
	}

	private String has(IItemProvider item) {
		return "has_" + item.asItem().getRegistryName().getPath();
	}
}
