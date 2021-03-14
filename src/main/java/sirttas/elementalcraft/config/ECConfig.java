package sirttas.elementalcraft.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ECConfig {

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	public static final Client CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		Pair<Common, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(Common::new);
		Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);

		COMMON_SPEC = serverPair.getRight();
		COMMON = serverPair.getLeft();
		CLIENT_SPEC = clientPair.getRight();
		CLIENT = clientPair.getLeft();
	}

	private ECConfig() {}
	
	public static class Common {
		public final DoubleValue swordAirInfusionSpeedBonus;
		public final DoubleValue leggingsAirInfusionSpeedBonus;
		public final DoubleValue chestplateAirInfusionDodgeChance;

		public final IntValue shrinesCapacity;
		public final IntValue firePylonRange;
		public final IntValue firePylonConsumeAmount;
		public final DoubleValue firePylonDamage;
		public final IntValue vacuumShrineRange;
		public final IntValue vacuumShrineConsumeAmount;
		public final DoubleValue vacuumShrinePullSpeed;
		public final IntValue growthShrineRange;
		public final IntValue growthShrineConsumeAmount;
		public final DoubleValue growthShrinePeriode;
		public final IntValue harvestShrineRange;
		public final IntValue harvestShrineConsumeAmount;
		public final DoubleValue harvestShrinePeriode;
		public final IntValue lavaShrineRange;
		public final IntValue lavaShrineConsumeAmount;
		public final DoubleValue lavaShrinePeriode;
		public final IntValue oreShrineRange;
		public final IntValue oreShrineConsumeAmount;
		public final DoubleValue oreShrinePeriode;
		public final IntValue overloadShrineConsumeAmount;
		public final DoubleValue overloadShrinePeriode;
		public final IntValue sweetShrineRange;
		public final IntValue sweetShrineConsumeAmount;
		public final DoubleValue sweetShrinePeriode;
		public final IntValue sweetShrineFood;
		public final DoubleValue sweetShrineSaturation;
		public final IntValue enderLockShrineRange;
		public final IntValue enderLockShrineConsumeAmount;
		public final IntValue breedingShrineRange;
		public final IntValue breedingShrineConsumeAmount;
		public final DoubleValue breedingShrinePeriode;
		public final IntValue groveShrineRange;
		public final IntValue groveShrineConsumeAmount;
		public final DoubleValue groveShrinePeriode;

		public final IntValue tankCapacity;
		public final IntValue tankSmallCapacity;
		public final IntValue extractorExtractionAmount;
		public final IntValue extractorMaxRunes;
		public final IntValue improvedExtractorExtractionAmount;
		public final IntValue improvedExtractorMaxRunes;
		public final IntValue evaporatorExtractionAmount;
		public final IntValue evaporatorMaxRunes;
		public final IntValue fireFurnaceTransferSpeed;
		public final IntValue fireFurnaceMaxRunes;
		public final IntValue fireFurnaceElementAmount;
		public final IntValue fireBlastFurnaceTransferSpeed;
		public final IntValue fireBlastFurnaceMaxRunes;
		public final IntValue fireBlastFurnaceElementAmount;
		public final IntValue infuserTransferSpeed;
		public final IntValue infuserMaxRunes;
		public final IntValue toolInfustionBaseCost;
		public final IntValue binderTransferSpeed;
		public final IntValue binderMaxRunes;
		public final IntValue improvedBinderTransferSpeed;
		public final IntValue improvedBinderMaxRunes;
		public final BooleanValue binderRecipeMatchOrder;
		public final IntValue crystallizerTransferSpeed;
		public final IntValue crystallizerMaxRunes;
		public final DoubleValue crystallizerLuckRatio;
		public final IntValue inscriberTransferSpeed;
		public final IntValue inscriberMaxRunes;
		public final IntValue airMillTransferSpeed;
		public final IntValue airMillMaxRunes;
		public final DoubleValue airMillLuckRatio;
		public final IntValue purifierTransferSpeed;
		public final IntValue purifierMaxRunes;
		public final IntValue purifierDuration;
		public final DoubleValue purifierLuckRatio;
		public final IntValue purifierBaseCost;
		public final IntValue pureOreMultiplier;
		public final BooleanValue pureOreRecipeInjection;
		public final BooleanValue pureOreSmeltingRecipe;
		public final BooleanValue pureOreBlastingRecipe;
		public final BooleanValue pureOreCampFireRecipe;
		public final IntValue impairedPipeTransferAmount;
		public final IntValue pipeTransferAmount;
		public final IntValue improvedPipeTransferAmount;
		public final IntValue sorterCooldown;
		public final IntValue sorterMaxItem;

		public final IntValue pureInfuserTransferSpeed;
		public final IntValue pureInfuserMaxRunes;
		public final IntValue pedestalCapacity;
		public final IntValue pedestalMaxRunes;

		public final IntValue receptacleDurability;
		public final IntValue improvedReceptacleDurability;
		public final BooleanValue receptacleEnchantable;
		public final IntValue elementHolderCapacity;
		public final IntValue elementHolderTransferAmount;
		public final IntValue focusMaxSpell;
		public final IntValue spellBookMaxSpell;
		public final BooleanValue playersSpawnWithBook;
		public final IntValue shardElementAmount;
		public final IntValue chiselDurability;
		public final IntValue lenseElementMultiplier;

		public final BooleanValue disableWorldGen;
		public final BooleanValue disableInertCrystal;
		public final IntValue inertCrystalCount;
		public final IntValue inertCrystalSize;
		public final IntValue inertCrystalYMax;
		public final IntValue sourceSpawnChance;
		public final IntValue oceanSourceSpawnChance;
		public final IntValue randomSourceSpawnChance;
		public final IntValue sourceAltarDistance;

		public final BooleanValue disableSourceExhaustion;
		public final IntValue sourceCapacityMin;
		public final IntValue sourceCapacityMax;
		public final IntValue sourceRecoverRate;

		public final BooleanValue mekanismInteracionEnabled;
		public final DoubleValue mekanismPureOreDimishingAmount;
		public final BooleanValue mekanismPureOreDissolutionRecipe;
		public final BooleanValue mekanismPureOreInjectingRecipe;
		public final BooleanValue mekanismPureOrePurifyingRecipe;
		public final BooleanValue mekanismPureOreEnrichingRecipe;
		public final BooleanValue mekanismPureOreCrushingRecipe;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.comment("ElementalCraft config").push("elementalcraft");

			builder.push("infusion");
			swordAirInfusionSpeedBonus = builder.comment("The bonus to sword attack speed provided by the air infusion.").defineInRange("swordAirInfusionSpeedBonus", 0.8D, 0D, 10D);
			leggingsAirInfusionSpeedBonus = builder.comment("The bonus to movement speed provided by the air infusion on leggings.").defineInRange("leggingsAirInfusionSpeedBonus", 0.01D, 0D, 1D);
			chestplateAirInfusionDodgeChance = builder.comment("The dodge chance provided by the air infusion on chestplate.").defineInRange("chestplateAirInfusionDodgeChance", 0.2D, 0D, 1D);

			
			builder.pop().comment("Shrines config").push("shrines");
			shrinesCapacity = builder.comment("The element capacity of shrines.").defineInRange("shrinesCapacity", 10000, 0, 100000000);
			builder.push("firePylon");
			firePylonRange = builder.comment("The range of the Fire Pylon.").defineInRange("firePylonRange", 10, 0, 100);
			firePylonConsumeAmount = builder.comment("The amount of element consumed by the Fire Pylon.").defineInRange("firePylonConsumeAmount", 5, 0, 100);
			firePylonDamage = builder.comment("The damage of the Fire Pylon.").defineInRange("firePylonDamage", 2D, 0, 20);
			builder.pop().push("vacuumShrine");
			vacuumShrineRange = builder.comment("The range of the Vacuum Shrine.").defineInRange("vacuumShrineRange", 10, 0, 100);
			vacuumShrineConsumeAmount = builder.comment("The amount of element consumed by the Vacuum Shrine.").defineInRange("vacuumShrineConsumeAmount", 1, 0, 100);
			vacuumShrinePullSpeed = builder.comment("The pull speed of the Vacuum Shrine.").defineInRange("vacuumShrinePullSpeed", 0.1D, 0D, 5D);
			builder.pop().push("growthShrine");
			growthShrineRange = builder.comment("The range of the Growth Shrine.").defineInRange("growthShrineRange", 4, 0, 100);
			growthShrineConsumeAmount = builder.comment("The amount of element consumed by the Growth Shrine.").defineInRange("growthShrineConsumeAmount", 50, 0, 1000);
			growthShrinePeriode = builder.comment("The nember of tick betwenn two GFrowth Shrine activations.").defineInRange("growthShrinePeriode", 20D, 0, 2400);
			builder.pop().push("harvestShrine");
			harvestShrineRange = builder.comment("The range of the Harvest Shrine.").defineInRange("harvestShrineRange", 4, 0, 100);
			harvestShrineConsumeAmount = builder.comment("The amount of element consumed by the Harvest Shrine.").defineInRange("harvestShrineConsumeAmount", 100, 0, 1000);
			harvestShrinePeriode = builder.comment("The nember of tick betwenn two Harvest Shrine activations.").defineInRange("harvestShrinePeriode", 20D, 0, 2400);
			builder.pop().push("lavaShrine");
			lavaShrineRange = builder.comment("The range of the Lava Shrine.").defineInRange("lavaShrineRange", 1, 0, 100);
			lavaShrineConsumeAmount = builder.comment("The amount of element consumed by the Lava Shrine.").defineInRange("lavaShrineConsumeAmount", 5000, 0, 100000);
			lavaShrinePeriode = builder.comment("The nember of tick betwenn two Lava Shrine activations.").defineInRange("lavaShrinePeriode", 1200D, 0, 4800);
			builder.pop().push("oreShrine");
			oreShrineRange = builder.comment("The range of the Ore Shrine.").defineInRange("oreShrineRange", 6, 0, 100);
			oreShrineConsumeAmount = builder.comment("The amount of element consumed by the Ore Shrine.").defineInRange("oreShrineConsumeAmount", 2000, 0, 10000);
			oreShrinePeriode = builder.comment("The nember of tick betwenn two Ore Shrine activations.").defineInRange("oreShrinePeriode", 200D, 0, 2400);
			builder.pop().push("overloadShrine");
			overloadShrineConsumeAmount = builder.comment("The amount of element consumed by the overload Shrine.").defineInRange("overloadShrineConsumeAmount", 100, 0, 1000);
			overloadShrinePeriode = builder.comment("The nember of tick betwenn two Overload Shrine activations.").defineInRange("overloadShrinePeriode", 3D, 0, 2400);
			builder.pop().push("sweetShrine");
			sweetShrineRange = builder.comment("The range of the Sweet Shrine.").defineInRange("sweetShrineRange", 10, 0, 100);
			sweetShrineConsumeAmount = builder.comment("The amount of element consumed by the Sweet Shrine.").defineInRange("sweetShrineConsumeAmount", 500, 0, 10000);
			sweetShrinePeriode = builder.comment("The nember of tick betwenn two Sweet Shrine activations.").defineInRange("sweetShrinePeriode", 40D, 0, 2400);
			sweetShrineFood = builder.comment("The food given by the sheet shrine.").defineInRange("sweetShrineFood", 1, 1, 10);
			sweetShrineSaturation = builder.comment("The saturation given by the sheet shrine.").defineInRange("sweetShrineSaturation", 0.1D, 0, 10);
			builder.pop().push("enderLock");
			enderLockShrineRange = builder.comment("The range of the Ender Lock Shrine.").defineInRange("enderLockShrineRange", 10, 0, 100);
			enderLockShrineConsumeAmount = builder.comment("The amount of element consumed by the Ender Lock Shrine.").defineInRange("enderLockShrineConsumeAmount", 500, 0, 10000);
			builder.pop().push("breedingShrine");
			breedingShrineRange = builder.comment("The range of the breeding Shrine.").defineInRange("breedingShrineRange", 10, 0, 100);
			breedingShrineConsumeAmount = builder.comment("The amount of element consumed by the breeding Shrine.").defineInRange("breedingShrineConsumeAmount", 2000, 0, 10000);
			breedingShrinePeriode = builder.comment("The nember of tick betwenn two breeding Shrine activations.").defineInRange("breedingShrinePeriode", 200D, 0, 2400);
			builder.pop().push("groveShrine");
			groveShrineRange = builder.comment("The range of the wild grove Shrine.").defineInRange("groveShrineRange", 5, 0, 100);
			groveShrineConsumeAmount = builder.comment("The amount of element consumed by the wild grove Shrine.").defineInRange("groveShrineConsumeAmount", 500, 0, 10000);
			groveShrinePeriode = builder.comment("The nember of tick betwenn two wild grove Shrine activations.").defineInRange("groveShrinePeriode", 200D, 0, 2400);

			builder.pop(2).comment("Instruments config").push("instruments").push("tank");
			tankSmallCapacity = builder.comment("The element capacity of a small element container.").defineInRange("tankSmallCapacity", 1000, 0, 100000000);
			tankCapacity = builder.comment("The element capacity of a element container.").defineInRange("tankCapacity", 100000, 0, 100000000);
			builder.pop().push("extractor");
			extractorExtractionAmount = builder.comment("The amount of element extracted by an extractor.").defineInRange("extractorExtractionAmount", 5, 0, 100);
			extractorMaxRunes = builder.comment("The max amount of on an extractor.").defineInRange("extractorMaxRunes", 1, 0, 10);
			improvedExtractorExtractionAmount = builder.comment("The amount of element extracted by an improved extractor.").defineInRange("improvedExtractorExtractionAmount", 25, 0, 500);
			improvedExtractorMaxRunes = builder.comment("The max amount of on an improved extracto.").defineInRange("improvedExtractoMaxRunes", 3, 0, 10);
			builder.pop().push("evaporator");
			evaporatorExtractionAmount = builder.comment("The amount of element extracted by an evaporator.").defineInRange("evaporatorExtractionAmount", 1, 0, 100);
			evaporatorMaxRunes = builder.comment("The max amount of on an evaporator.").defineInRange("evaporatorMaxRunes", 1, 0, 10);
			builder.pop().push("fireFurnace");
			fireFurnaceTransferSpeed = builder.comment("The max amount of element consumed by the fire furnace per tick.").defineInRange("fireFurnaceTransferSpeed", 20, 0, 1000);
			fireFurnaceMaxRunes = builder.comment("The max amount of on a fire furnace.").defineInRange("fireFurnaceMaxRunes", 1, 0, 10);
			fireFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the furnace recipe.").defineInRange("fireFurnaceElementAmount", 10, 0, 1000);
			builder.push("fireBlastFurnace");
			fireBlastFurnaceTransferSpeed = builder.comment("The max amount of element consumed by the fire blast furnace per tick.").defineInRange("fireBlastFurnaceTransferSpeed", 40, 0, 1000);
			fireBlastFurnaceMaxRunes = builder.comment("The max amount of on a fire blast furnace.").defineInRange("fireBlastFurnaceMaxRunes", 2, 0, 10);
			fireBlastFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the blast furnace recipe.").defineInRange("fireBlastFurnaceElementAmount", 20, 0, 1000);
			builder.pop(2).push("infuser");
			infuserTransferSpeed = builder.comment("The max amount of element consumed by the infuser per tick.").defineInRange("infuserTransferSpeed", 10, 0, 1000);
			infuserMaxRunes = builder.comment("The max amount of on an infuser.").defineInRange("infuserMaxRunes", 1, 0, 10);
			toolInfustionBaseCost = builder.comment("The base cost of a tool infusion recipe.").defineInRange("toolInfustionBaseCost", 1000, 0, 10000);
			builder.pop().push("binder");
			binderTransferSpeed = builder.comment("The max amount of element consumed by the binder per tick.").defineInRange("binderTransferSpeed", 25, 0, 1000);
			binderMaxRunes = builder.comment("The max amount of on an binder.").defineInRange("binderMaxRunes", 2, 0, 10);
			binderRecipeMatchOrder = builder.comment("Define if or not binder recip require to be ordered.").define("binderRecipeMatchOrder", true);
			builder.push("improved");
			improvedBinderTransferSpeed = builder.comment("The max amount of element consumed by the improved binder per tick.").defineInRange("improvedBinderTransferSpeed", 50, 0, 1000);
			improvedBinderMaxRunes = builder.comment("The max amount of on an improved binder.").defineInRange("improvedBinderMaxRunes", 3, 0, 10);
			builder.pop(2).push("crystallizer");
			crystallizerTransferSpeed = builder.comment("The max amount of element consumed by the gem crystallizer per tick.").defineInRange("crystallizerTransferSpeed", 25, 0, 1000);
			crystallizerMaxRunes = builder.comment("The max amount of on an crystallizer.").defineInRange("crystallizerMaxRunes", 3, 0, 10);
			crystallizerLuckRatio = builder.comment("The ratio of each luck rune on a crystallizer.").defineInRange("crystallizerLuckRatio", 3D, 0D, 10D);
			builder.pop().push("inscriber");
			inscriberTransferSpeed = builder.comment("The max amount of element consumed by the gem inscriber per tick.").defineInRange("inscriberTransferSpeed", 1000, 0, 10000);
			inscriberMaxRunes = builder.comment("The max amount of on an inscriber.").defineInRange("inscriberMaxRunes", 3, 0, 10);
			builder.pop().push("airMill");
			airMillTransferSpeed = builder.comment("The max amount of element consumed by the Air Mill Grindstone per tick.").defineInRange("airMillTransferSpeed", 10, 0, 1000);
			airMillMaxRunes = builder.comment("The max amount of on an Air Mill Grindstone.").defineInRange("airMillMaxRunes", 3, 0, 10);
			airMillLuckRatio = builder.comment("The ratio of each luck rune on a Air Mill Grindstone.").defineInRange("airMillLuckRatio", 2D, 0D, 10D);
			builder.pop().push("purifier");
			purifierTransferSpeed = builder.comment("The max amount of element consumed by the Ore Purifier per tick.").defineInRange("purifierTransferSpeed", 25, 0, 1000);
			purifierMaxRunes = builder.comment("The max amount of on an purifier.").defineInRange("purifierMaxRunes", 3, 0, 10);
			purifierDuration = builder.comment("The nember of tick for a Ore Purifier to procces one item.").defineInRange("purifierDuration", 100, 0, 2400);
			purifierLuckRatio = builder.comment("The ratio of each luck rune on a purifier.").defineInRange("purifierLuckRatio", 3D, 0D, 10D);
			purifierBaseCost = builder.comment("The base cost of a piurifier recipe.").defineInRange("purifierBaseCost", 2500, 0, 10000);
			builder.push("pureOre");
			pureOreMultiplier = builder.comment("The number of output pure ores by a purifier.").defineInRange("pureOreMultiplier", 2, 1, 20);
			pureOreRecipeInjection = builder.comment("Set to false if you want to manualy manage processing of pure ore.").define("pureOreRecipeInjection", true);
			pureOreSmeltingRecipe = builder.comment("Set to false if you want pure ore to not use smelting recipes.").define("pureOreSmeltingRecipe", true);
			pureOreBlastingRecipe = builder.comment("Set to false if you want pure ore to not use blasting recipes.").define("pureOreBlastingRecipe", true);
			pureOreCampFireRecipe = builder.comment("Set to false if you want pure ore to not use camp fire recipes.").define("pureOreCampFireRecipe", true);
			builder.pop(2).push("elementPipe");
			impairedPipeTransferAmount = builder.comment("The amount of element transferred by impaired pipes.").defineInRange("impairedPipeTransferAmount", 5, 0, 10000);
			pipeTransferAmount = builder.comment("The amount of element transferred by pipes.").defineInRange("pipeTransferAmount", 25, 0, 10000);
			improvedPipeTransferAmount = builder.comment("The amount of element transferred by improved pipes.").defineInRange("improvedPipeTransferAmount", 100, 0, 10000);
			builder.pop().push("sorter");
			sorterCooldown = builder.comment("The amount of tick between two ordered sorter item transpher.").defineInRange("sorterCooldown", 10, 0, 100);
			sorterMaxItem = builder.comment("The max amount of items an order sorter can filter.").defineInRange("sorterMaxItem", 15, 0, 100);

			builder.pop(2).comment("Pure Infusser and pedestals config").push("pureInfuser");
			pureInfuserTransferSpeed = builder.comment("The max amount of element consumed by the pure infuser per tick.").defineInRange("pureInfuserTransferSpeed", 100, 0, 1000);
			pureInfuserMaxRunes = builder.comment("The max amount of on a pure infuser.").defineInRange("pureInfuserMaxRunes", 3, 0, 10);
			builder.push("pedestals");
			pedestalMaxRunes = builder.comment("The max amount of on a pedestal.").defineInRange("pedestalMaxRunes", 1, 0, 10);
			pedestalCapacity = builder.comment("The element capacity of a pedestal.").defineInRange("pedestalCapacity", 10000, 0, 100000000);

			builder.pop(2).comment("Items config").push("items");
			receptacleDurability = builder.comment("Define source receptacle durablility (0 for unbreakable).").defineInRange("receptacleDurability", 5, 0, 1000);
			improvedReceptacleDurability = builder.comment("Define improved source receptacle durablility (0 for unbreakable).").defineInRange("improvedReceptacleDurability", 20, 0, 1000);
			receptacleEnchantable = builder.comment("Define if or not receptacles can be enchanted.").define("receptacleEnchantable", false);
			elementHolderCapacity = builder.comment("The element capacity of an element holder.").defineInRange("elementHolderCapacity", 10000, 0, 100000000);
			elementHolderTransferAmount = builder.comment("The amount of element transfered by an element holder.").defineInRange("elementHolderTransferAmount", 25, 0, 1000);
			focusMaxSpell = builder.comment("The max number of spells on a focus.").defineInRange("focusMaxSpell", 10, 1, 20);
			spellBookMaxSpell = builder.comment("The max number of spells on an elementalist grimoire.").defineInRange("spellBookMaxSpell", 100, 1, 1000);
			playersSpawnWithBook = builder.comment("Players start the game with an elementopedia in their inventory.").define("playersSpawnWithBook", false);
			shardElementAmount = builder.comment("The amount of element contained in a single shard.").defineInRange("shardElementAmount", 250, 0, 1000);
			chiselDurability = builder.comment("Define chisel durablility (0 for unbreakable).").defineInRange("chiselDurability", 250, 0, 1000);
			lenseElementMultiplier = builder.comment("the multiplier of lense (based on 1000)").defineInRange("lenseElementMultiplier", 100, 0, 100);
			
			builder.pop().comment("Worldgen config").push("worldgen");
			disableWorldGen = builder.comment("Disable all elementalcraft world gen.").define("disableWorldGen", false);
			builder.push("inertCrystal");
			disableInertCrystal = builder.comment("Disable creation of inertCrystals.").define("disableInertCrystal", false);
			inertCrystalCount = builder.comment("Number of inert crystal vein.").defineInRange("inertCrystalCount", 10, 1, 100);
			inertCrystalSize = builder.comment("Size of inert crystal vein.").defineInRange("inertCrystalSize", 9, 1, 100);
			inertCrystalYMax = builder.comment("max Y level of inert crystal.").defineInRange("inertCrystalYMax", 64, 1, 256);
			builder.pop();
			sourceSpawnChance = builder.comment("Chance to add a source in world (the small the more frequante).").defineInRange("sourceSpawnChance", 30, 1, 10000);
			oceanSourceSpawnChance = builder.comment("Chance to add a source in an ocean biome (the small the more frequante).").defineInRange("oceanSourceSpawnChance", 150, 1, 10000);
			randomSourceSpawnChance = builder.comment("Chance to add a source in world ingoring biome elemen type (the small the more frequante).").defineInRange("randomSourceSpawnChance", 300, 1,
					10000);
			sourceAltarDistance = builder.comment("CSource Altar genreration distance setting.").defineInRange("sourceAltarDistance", 64, 0, 100);

			builder.pop().comment("Source config").push("source");
			disableSourceExhaustion = builder.comment("set to true to make sources infinite.").define("disableSourceExhaustion", false);
			sourceCapacityMin = builder.comment("The minimum element capacity of a source.").defineInRange("sourceCapacityMin", 500000, 0, 10000000);
			sourceCapacityMax = builder.comment("The maximum element capacity of a source.").defineInRange("sourceCapacityMax", 1000000, 0, 10000000);
			sourceRecoverRate = builder.comment("The element a source can generate per tick.").defineInRange("sourceRecoverRate", 5, 0, 100);

			builder.pop().comment("mod interaction config").push("interaction").push("mekanism");
			mekanismInteracionEnabled = builder.comment("Enable interaction with mekanism.").define("mekanismInteracionEnabled", true);
			mekanismPureOreDimishingAmount = builder.comment("The dimishing amount multiplier when using pure ore in mekanism. it prevent an exploit.").defineInRange("mekanismPureOreDimishingAmount",
					0.75D, 0, 1);
			mekanismPureOreDissolutionRecipe = builder.comment("Set to false if you want pure ore to not use mekanism dissolution recipes.").define("mekanismPureOreDissolutionRecipe", true);
			mekanismPureOreInjectingRecipe = builder.comment("Set to false if you want pure ore to not use mekanism injecting recipes.").define("mekanismPureOreInjectingRecipe", true);
			mekanismPureOrePurifyingRecipe = builder.comment("Set to false if you want pure ore to not use mekanism purifying recipes.").define("mekanismPureOrePurifyingRecipe", true);
			mekanismPureOreEnrichingRecipe = builder.comment("Set to false if you want pure ore to not use mekanism enriching recipes.").define("mekanismPureOreEnrichingRecipe", true);
			mekanismPureOreCrushingRecipe = builder.comment("Set to false if you want pure ore to not use mekanism crushing recipes.").define("mekanismPureOreCrushingRecipe", true);

			builder.pop(2);
		}
	}

	public static class Client {

		public final BooleanValue usePaleElementGauge;
		public final IntValue shrineRangeDisplayDuration;
		public final BooleanValue renderPedestalShadow;
		public final BooleanValue fastParticleEffects;

		public final IntValue gaugeOffsetX;
		public final IntValue gaugeOffsetY;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.comment("ElementalCraft client config").push("elementalcraft-client");

			shrineRangeDisplayDuration = builder.comment("The duration of shrine range display.").defineInRange("shrineRangeDisplayDuration", 600, 0, 10000);
			renderPedestalShadow = builder.comment("Display a shadow where pedestals can be placed.").define("renderPedestalShadow", true);
			fastParticleEffects = builder.comment("Set to true if you want to reduce quality of particles for beter performances.").define("fastParticleEffects", false);

			builder.push("gauge");
			usePaleElementGauge = builder.comment("Use pale element gauges.").define("usePaleElementGauge", false);
			gaugeOffsetX = builder.comment("the offset of the gauge on the X axis.").defineInRange("gaugeOffsetX", 0, -10000, 10000);
			gaugeOffsetY = builder.comment("the offset of the gauge on the Y axis.").defineInRange("gaugeOffsetY", 0, -10000, 10000);
			builder.pop();
		}
	}
}
