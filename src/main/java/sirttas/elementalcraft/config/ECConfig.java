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

	public static class Common {
		public final DoubleValue swordAirInfusionSpeedBonus;
		public final DoubleValue leggingsAirInfusionSpeedBonus;
		public final DoubleValue chestplateAirInfusionDodgeChance;

		public final IntValue shrinesCapacity;
		public final IntValue firePylonRange;
		public final IntValue firePylonConsumeAmount;
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
		public final IntValue enderLockShrineRange;
		public final IntValue enderLockShrineConsumeAmount;

		public final IntValue tankCapacity;
		public final IntValue tankSmallCapacity;
		public final IntValue extractorExtractionAmount;
		public final IntValue improvedExtractorExtractionAmount;
		public final IntValue fireFurnaceConsumeAmount;
		public final IntValue fireBlastFurnaceConsumeAmount;
		public final BooleanValue binderRecipeMatchOrder;
		public final IntValue purifierConsumeAmount;
		public final IntValue purifierDuration;
		public final BooleanValue pureOreRecipeInjection;
		public final BooleanValue pureOreSmeltingRecipe;
		public final BooleanValue pureOreBlastingRecipe;
		public final BooleanValue pureOreCampFireRecipe;
		public final IntValue impairedPipeTransferAmount;
		public final IntValue pipeTransferAmount;
		public final IntValue improvedPipeTransferAmount;

		public final IntValue receptacleDurability;
		public final BooleanValue receptacleEnchantable;
		public final IntValue elementHolderCapacity;
		public final IntValue elementHolderTransferAmount;
		public final IntValue focusMaxSpell;

		public final BooleanValue disableWorldGen;
		public final IntValue sourceSpawnChance;
		public final IntValue sourceAltarDistance;
		public final BooleanValue playersSpawnWithBook;

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
			builder.pop().push("enderLock");
			enderLockShrineRange = builder.comment("The range of the Ender Lock Shrine.").defineInRange("enderLockShrineRange", 10, 0, 100);
			enderLockShrineConsumeAmount = builder.comment("The amount of element consumed by the Ender Lock Shrine.").defineInRange("enderLockShrineConsumeAmount", 500, 0, 10000);

			builder.pop(2).comment("Instruments config").push("instruments").push("tank");
			tankSmallCapacity = builder.comment("The element capacity of a small element container.").defineInRange("tankSmallCapacity", 1000, 0, 100000000);
			tankCapacity = builder.comment("The element capacity of a element container.").defineInRange("tankCapacity", 100000, 0, 100000000);
			builder.pop().push("extractor");
			extractorExtractionAmount = builder.comment("The amount of element extracted by an extractor.").defineInRange("extractorExtractionAmount", 5, 0, 100);
			improvedExtractorExtractionAmount = builder.comment("The amount of element extracted by an improved extractor.").defineInRange("improvedExtractorExtractionAmount", 25, 0, 500);
			builder.pop().push("fireFurnace");
			fireFurnaceConsumeAmount = builder.comment("The amount of element consumed by the Fire Furnace per tick.").defineInRange("fireFurnaceConsumeAmount", 10, 0, 500);
			fireBlastFurnaceConsumeAmount = builder.comment("The amount of element consumed by the Fire Blast Furnace per tick.").defineInRange("fireBlastFurnaceConsumeAmount", 20, 0, 500);
			builder.pop().push("binder");
			binderRecipeMatchOrder = builder.comment("Define if or not binder recip require to be ordered.").define("binderRecipeMatchOrder", true);
			builder.pop().push("purifier");
			purifierConsumeAmount = builder.comment("The amount of element consumed by the Ore Purifier per tick.").defineInRange("purifierConsumeAmount", 25, 0, 1000);
			purifierDuration = builder.comment("The nember of tick for a Ore Purifier co procces one item.").defineInRange("purifierDuration", 100, 0, 2400);
			builder.push("pureOre");
			pureOreRecipeInjection = builder.comment("Set to false if you want to manualy manage processing of pure ore.").define("pureOreRecipeInjection", true);
			pureOreSmeltingRecipe = builder.comment("Set to false if you want pure ore to not use smelting recipes.").define("pureOreSmeltingRecipe", true);
			pureOreBlastingRecipe = builder.comment("Set to false if you want pure ore to not use blasting recipes.").define("pureOreBlastingRecipe", true);
			pureOreCampFireRecipe = builder.comment("Set to false if you want pure ore to not use camp fire recipes.").define("pureOreCampFireRecipe", true);
			builder.pop(2).push("elementPipe");
			impairedPipeTransferAmount = builder.comment("The amount of element transferred by impaired pipes.").defineInRange("impairedPipeTransferAmount", 5, 0, 10000);
			pipeTransferAmount = builder.comment("The amount of element transferred by pipes.").defineInRange("pipeTransferAmount", 25, 0, 10000);
			improvedPipeTransferAmount = builder.comment("The amount of element transferred by improved pipes.").defineInRange("improvedPipeTransferAmount", 100, 0, 10000);

			builder.pop(2).comment("Items config").push("items");
			receptacleDurability = builder.comment("Define source receptacle durablility (0 for unbreakable).").defineInRange("receptacleDurability", 20, 0, 1000);
			receptacleEnchantable = builder.comment("Define if or not receptacles can be enchanted.").define("receptacleEnchantable", false);
			elementHolderCapacity = builder.comment("The element capacity of an element holder.").defineInRange("elementHolderCapacity", 10000, 0, 100000000);
			elementHolderTransferAmount = builder.comment("The amount of element transfered by an element holder.").defineInRange("elementHolderTransferAmount", 25, 0, 1000);
			focusMaxSpell = builder.comment("The max number of spells on a focus.").defineInRange("focusMaxSpell", 10, 1, 20);
			playersSpawnWithBook = builder.comment("Players start the game with an elementopedia in their inventory.").define("playersSpawnWithBook", false);
			
			builder.pop().comment("Worldgen config").push("worldgen");
			disableWorldGen = builder.comment("Disable all elementalcraft world gen.").define("disableWorldGen", false);
			sourceSpawnChance = builder.comment("Chance to add a source in world (the small the more frequante).").defineInRange("sourceSpawnChance", 20, 1, 10000);
			sourceAltarDistance = builder.comment("CSource Altar genreration distance setting.").defineInRange("sourceAltarDistance", 16, 0, 100);

			builder.pop();
		}
	}

	public static class Client {

		public final BooleanValue reloadJEIAfterPureOreGen;
		public final BooleanValue usePaleElementGauge;
		public final IntValue shrineRangeDisplayDuration;

		public final IntValue gaugeOffsetX;
		public final IntValue gaugeOffsetY;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.comment("ElementalCraft client config").push("elementalcraft-client");

			reloadJEIAfterPureOreGen = builder.comment("Reload JEI if it was loaded before Pure Ore generation.").define("reloadJEIAfterPureOreGen", true);
			shrineRangeDisplayDuration = builder.comment("The duration of shrine range display.").defineInRange("shrineRangeDisplayDuration", 600, 0, 10000);

			builder.push("gauge");
			usePaleElementGauge = builder.comment("Use pale element gauges.").define("usePaleElementGauge", false);
			gaugeOffsetX = builder.comment("the offset of the gauge on the X axis.").defineInRange("gaugeOffsetX", 0, -10000, 10000);
			gaugeOffsetY = builder.comment("the offset of the gauge on the Y axis.").defineInRange("gaugeOffsetY", 0, -10000, 10000);
			builder.pop();
		}
	}
}
