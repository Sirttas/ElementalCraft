package sirttas.elementalcraft.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ECConfig {

	public static final ECConfig CONFIG;
	public static final ForgeConfigSpec SPEC;

	public final DoubleValue swordAirInfusionSpeedBonus;
	public final DoubleValue leggingsAirInfusionSpeedBonus;

	public final IntValue shrineMaxAmount;
	public final IntValue firePylonRange;
	public final IntValue firePylonConsumeAmount;
	public final IntValue vacuumShrineRange;
	public final IntValue vacuumShrineConsumeAmount;
	public final DoubleValue vacuumShrinePullSpeed;
	public final IntValue growthShrineRange;
	public final IntValue growthShrineConsumeAmount;
	public final DoubleValue growthShrineChance;
	public final IntValue growthShrinePeriode;
	public final IntValue harvestShrineRange;
	public final IntValue harvestShrineConsumeAmount;
	public final DoubleValue harvestShrineChance;
	public final IntValue harvestShrinePeriode;
	public final IntValue lavaShrineRange;
	public final IntValue lavaShrineConsumeAmount;
	public final DoubleValue lavaShrineChance;
	public final IntValue lavaShrinePeriode;
	public final IntValue oreShrineRange;
	public final IntValue oreShrineConsumeAmount;
	public final IntValue oreShrinePeriode;
	public final IntValue overloadShrineConsumeAmount;
	public final IntValue overloadShrinePeriode;
	public final IntValue sweetShrineRange;
	public final IntValue sweetShrineConsumeAmount;
	public final IntValue sweetShrinePeriode;

	public final IntValue tankMaxAmount;
	public final IntValue tankSmallMaxAmount;
	public final IntValue extractorExtractionAmount;
	public final IntValue improvedExtractorExtractionAmount;
	public final IntValue fireFurnaceConsumeAmount;
	public final IntValue fireBlastFurnaceConsumeAmount;
	public final IntValue purifierConsumeAmount;
	public final IntValue purifierDuration;
	public final IntValue impairedPipeTransferAmount;
	public final IntValue pipeTransferAmount;
	public final IntValue improvedPipeTransferAmount;

	public final BooleanValue pureOreSmeltingRecipeInjection;
	public final BooleanValue binderRecipeMatchOrder;

	public final IntValue receptacleDurability;
	public final IntValue elementHolderMaxAmount;
	public final IntValue elementHolderExtractionAmount;

	public final IntValue sourceSpawnChance;
	public final IntValue sourceAltarDistance;

	static {
		Pair<ECConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ECConfig::new);

		SPEC = pair.getRight();
		CONFIG = pair.getLeft();
	}

	public ECConfig(ForgeConfigSpec.Builder builder) {
		builder.comment("ElementalCraft config").push("elementalcraft");

		builder.push("infusion");
		swordAirInfusionSpeedBonus = builder.comment("The bonus to sword attack speed provided by the air infusion.").defineInRange("swordAirInfusionSpeedBonus", 0.8D, 0D, 10D);
		leggingsAirInfusionSpeedBonus = builder.comment("The bonus to movement speed provided by the air infusion.").defineInRange("leggingsAirInfusionSpeedBonus", 0.01D, 0D, 1D);

		builder.pop().comment("Shrines config").push("shrines");
		shrineMaxAmount = builder.comment("The max element amount a shrine can hold.").defineInRange("shrineMaxAmount", 1000, 0, 100000000);
		builder.push("firePylon");
		firePylonRange = builder.comment("The range of the Fire Pylon.").defineInRange("firePylonRange", 10, 0, 100);
		firePylonConsumeAmount = builder.comment("The amount of element consumed by the Fire Pylon.").defineInRange("firePylonConsumeAmount", 5, 0, 100);
		builder.pop().push("vacuumShrine");
		vacuumShrineRange = builder.comment("The range of the Vacuum Shrine.").defineInRange("vacuumShrineRange", 10, 0, 100);
		vacuumShrineConsumeAmount = builder.comment("The amount of element consumed by the Vacuum Shrine.").defineInRange("vacuumShrineConsumeAmount", 1, 0, 100);
		vacuumShrinePullSpeed = builder.comment("The pull speed of the Vacuum Shrine.").defineInRange("vacuumShrinePullSpeed", 0.1D, 0D, 5D);
		builder.pop().push("growthShrine");
		growthShrineRange = builder.comment("The range of the Growth Shrine.").defineInRange("growthShrineRange", 4, 0, 100);
		growthShrineConsumeAmount = builder.comment("The amount of element consumed by the Growth Shrine.").defineInRange("growthShrineConsumeAmount", 5, 0, 100);
		growthShrineChance = builder.comment("The chance of the Growth Shrine to tick.").defineInRange("growthShrineChance", 0.02D, 0D, 1D);
		growthShrinePeriode = builder.comment("The nember of tick betwenn two GFrowth Shrine activations.").defineInRange("growthShrinePeriode", 20, 0, 2400);
		builder.pop().push("harvestShrine");
		harvestShrineRange = builder.comment("The range of the Harvest Shrine.").defineInRange("harvestShrineRange", 4, 0, 100);
		harvestShrineConsumeAmount = builder.comment("The amount of element consumed by the Harvest Shrine.").defineInRange("harvestShrineConsumeAmount", 5, 0, 100);
		harvestShrineChance = builder.comment("The chance of the Harvest Shrine to tick.").defineInRange("harvestShrineChance", 0.02D, 0D, 1D);
		harvestShrinePeriode = builder.comment("The nember of tick betwenn two Harvest Shrine activations.").defineInRange("harvestShrinePeriode", 20, 0, 2400);
		builder.pop().push("lavaShrine");
		lavaShrineRange = builder.comment("The range of the Lava Shrine.").defineInRange("lavaShrineRange", 1, 0, 100);
		lavaShrineConsumeAmount = builder.comment("The amount of element consumed by the Lava Shrine.").defineInRange("lavaShrineConsumeAmount", 5000, 0, 100000);
		lavaShrineChance = builder.comment("The chance of the Lava Shrine to tick.").defineInRange("lavaShrineChance", 0.02D, 0D, 1D);
		lavaShrinePeriode = builder.comment("The nember of tick betwenn two Lava Shrine activations.").defineInRange("lavaShrinePeriode", 20, 0, 2400);
		builder.pop().push("oreShrine");
		oreShrineRange = builder.comment("The range of the Ore Shrine.").defineInRange("oreShrineRange", 6, 0, 100);
		oreShrineConsumeAmount = builder.comment("The amount of element consumed by the Ore Shrine.").defineInRange("oreShrineConsumeAmount", 500, 0, 1000);
		oreShrinePeriode = builder.comment("The nember of tick betwenn two Ore Shrine activations.").defineInRange("oreShrinePeriode", 200, 0, 2400);
		builder.pop().push("overloadShrine");
		overloadShrineConsumeAmount = builder.comment("The amount of element consumed by the overload Shrine.").defineInRange("overloadShrineConsumeAmount", 500, 0, 1000);
		overloadShrinePeriode = builder.comment("The nember of tick betwenn two Overload Shrine activations.").defineInRange("overloadShrinePeriode", 3, 0, 2400);
		builder.pop().push("sweetShrine");
		sweetShrineRange = builder.comment("The range of the Sweet Shrine.").defineInRange("sweetShrineRange", 10, 0, 100);
		sweetShrineConsumeAmount = builder.comment("The amount of element consumed by the Sweet Shrine.").defineInRange("sweetShrineConsumeAmount", 100, 0, 1000);
		sweetShrinePeriode = builder.comment("The nember of tick betwenn two Sweet Shrine activations.").defineInRange("sweetShrinePeriode", 40, 0, 2400);

		builder.pop(2).comment("Instruments config").push("instruments").push("tank");
		tankSmallMaxAmount = builder.comment("The max element amount a small element container can hold.").defineInRange("tankSmallMaxAmount", 1000, 0, 100000000);
		tankMaxAmount = builder.comment("The max element amount an element container can hold.").defineInRange("tankMaxAmount", 100000, 0, 100000000);
		builder.pop().push("extractor");
		extractorExtractionAmount = builder.comment("The amount of element extracted by an extractor.").defineInRange("extractorExtractionAmount", 5, 0, 100);
		improvedExtractorExtractionAmount = builder.comment("The amount of element extracted by an improved extractor.").defineInRange("improvedExtractorExtractionAmount", 25, 0, 500);
		builder.pop().push("fireFurnace");
		fireFurnaceConsumeAmount = builder.comment("The amount of element consumed by the Fire Furnace per tick.").defineInRange("fireFurnaceConsumeAmount", 10, 0, 500);
		fireBlastFurnaceConsumeAmount = builder.comment("The amount of element consumed by the Fire Blast Furnace per tick.").defineInRange("fireBlastFurnaceConsumeAmount", 20, 0, 500);
		builder.pop().push("purifier");
		purifierConsumeAmount = builder.comment("The amount of element consumed by the Ore Purifier per tick.").defineInRange("purifierConsumeAmount", 25, 0, 1000);
		purifierDuration = builder.comment("The nember of tick for a Ore Purifier co procces one item.").defineInRange("purifierDuration", 100, 0, 2400);
		builder.pop().push("elementPipe");
		impairedPipeTransferAmount = builder.comment("The amount of element transferred by impaired pipes.").defineInRange("impairedPipeTransferAmount", 5, 0, 10000);
		pipeTransferAmount = builder.comment("The amount of element transferred by pipes.").defineInRange("pipeTransferAmount", 25, 0, 10000);
		improvedPipeTransferAmount = builder.comment("The amount of element transferred by improved pipes.").defineInRange("improvedPipeTransferAmount", 100, 0, 10000);

		builder.pop();
		pureOreSmeltingRecipeInjection = builder.comment("Set to false if you want to manualy manage processing of pure ore.").define("pureOreSmeltingRecipeInjection", true);
		binderRecipeMatchOrder = builder.comment("Define if or not binder recip require to be ordered.").define("binderRecipeMatchOrder", true);

		builder.pop().comment("Items config").push("items");
		receptacleDurability = builder.comment("Define source receptacle durablility (0 for unbreakable).").defineInRange("receptacleDurability", 20, 0, 1000);
		elementHolderMaxAmount = builder.comment("The max element amount an element holder can hold.").defineInRange("elementHolderMaxAmount", 10000, 0, 100000000);
		elementHolderExtractionAmount = builder.comment("The amount of element extracted by an element holder.").defineInRange("elementHolderExtractionAmount", 25, 0, 1000);

		builder.pop().comment("Worldgen config").push("worldgen");
		sourceSpawnChance = builder.comment("Chance to add a source in world (the small the more frequante).").defineInRange("sourceSpawnChance", 20, 1, 10000);
		sourceAltarDistance = builder.comment("CSource Altar genreration distance setting.").defineInRange("sourceAltarDistance", 16, 0, 100);

		builder.pop();
	}
}
