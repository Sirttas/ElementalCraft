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
	public final IntValue lavaShrineRange;
	public final IntValue lavaShrineConsumeAmount;
	public final DoubleValue lavaShrineChance;
	public final IntValue oreShrineRange;
	public final IntValue oreShrineConsumeAmount;
	public final DoubleValue oreShrineChance;
	public final IntValue tankMaxAmount;
	public final IntValue extractorExtractionAmount;
	public final IntValue improvedExtractorExtractionAmount;
	public final IntValue fireFurnaceConsumeAmount;
	public final IntValue pipeTransferAmount;

	public final BooleanValue binderRecipeMatchOrder;

	public final IntValue sourceSpawnChance;

	static {
		Pair<ECConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ECConfig::new);

		SPEC = pair.getRight();
		CONFIG = pair.getLeft();
	}

	public ECConfig(ForgeConfigSpec.Builder builder) {
		builder.comment("ElementalCraft config")
				.push("elementalcraft");

		swordAirInfusionSpeedBonus = builder.comment("The bonus to sword attack speed provided by the air infusion.").defineInRange("swordAirInfusionSpeedBonus", 0.8D, 0D, 10D);
		leggingsAirInfusionSpeedBonus = builder.comment("The bonus to movement speed provided by the air infusion.").defineInRange("leggingsAirInfusionSpeedBonus", 0.01D, 0D, 1D);

		shrineMaxAmount = builder.comment("The max element amount a shrine can hold.").defineInRange("shrineMaxAmount", 1000, 0, 100000000);
		firePylonRange = builder.comment("The range of the Fire Pylon.").defineInRange("firePylonRange", 10, 0, 100);
		firePylonConsumeAmount = builder.comment("The amount of element consumed by the Fire Pylon.").defineInRange("firePylonConsumeAmount", 5, 0, 100);
		vacuumShrineRange = builder.comment("The range of the Vacuum Shrine.").defineInRange("vacuumShrineRange", 10, 0, 100);
		vacuumShrineConsumeAmount = builder.comment("The amount of element consumed by the Vacuum Shrine.").defineInRange("vacuumShrineConsumeAmount", 1, 0, 100);
		vacuumShrinePullSpeed = builder.comment("The pull speed of the Vacuum Shrine.").defineInRange("vacuumShrinePullSpeed", 0.1D, 0D, 5D);
		growthShrineRange = builder.comment("The range of the Growth Shrine.").defineInRange("growthShrineRange", 4, 0, 100);
		growthShrineConsumeAmount = builder.comment("The amount of element consumed by the Growth Shrine.").defineInRange("growthShrineConsumeAmount", 5, 0, 100);
		growthShrineChance = builder.comment("The chance of the Growth Shrine to tick.").defineInRange("growthShrineChance", 0.02D, 0D, 1D);
		lavaShrineRange = builder.comment("The range of the Lava Shrine.").defineInRange("lavaShrineRange", 1, 0, 100);
		lavaShrineConsumeAmount = builder.comment("The amount of element consumed by the Lava Shrine.").defineInRange("lavaShrineConsumeAmount", 5000, 0, 100000);
		lavaShrineChance = builder.comment("The chance of the Lava Shrine to tick.").defineInRange("lavaShrineChance", 0.02D, 0D, 1D);
		oreShrineRange = builder.comment("The range of the Ore Shrine.").defineInRange("oreShrineRange", 6, 0, 100);
		oreShrineConsumeAmount = builder.comment("The amount of element consumed by the Ore Shrine.").defineInRange("oreShrineConsumeAmount", 500, 0, 1000);
		oreShrineChance = builder.comment("The chance of the Lava Ore to tick.").defineInRange("oreShrineChance", 0.005D, 0D, 1D);
		tankMaxAmount = builder.comment("The max element amount a tank can hold.").defineInRange("tankMaxAmount", 100000, 0, 100000000);
		extractorExtractionAmount = builder.comment("The amount of element extracted by an extractor.").defineInRange("extractorExtractionAmount", 5, 0, 100);
		improvedExtractorExtractionAmount = builder.comment("The amount of element extracted by an improved extractor.").defineInRange("improvedExtractorExtractionAmount", 25, 0, 500);
		fireFurnaceConsumeAmount = builder.comment("The amount of element consumed by the Fire Furnace per tick.").defineInRange("fireFurnaceConsumeAmount", 10, 0, 100);
		pipeTransferAmount = builder.comment("The amount of element transferred by pipes.").defineInRange("pipeTransferAmount", 100, 0, 10000);

		binderRecipeMatchOrder = builder.comment("If or not binder recip require to be ordered.").define("binderRecipeMatchOrder", true);

		sourceSpawnChance = builder.comment("Chance to add a source in world (the small the more frequante).").defineInRange("sourceSpawnChance", 20, 1, 10000);

		builder.pop();
	}
}
