package sirttas.elementalcraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

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

		public final IntValue shrinesCapacity;
		public final IntValue firePylonRange;
		public final IntValue firePylonConsumeAmount;
		public final DoubleValue firePylonDamage;
		public final IntValue vacuumShrineRange;
		public final IntValue vacuumShrineConsumeAmount;
		public final DoubleValue vacuumShrinePullSpeed;
		public final IntValue growthShrineRange;
		public final IntValue growthShrineConsumeAmount;
		public final DoubleValue growthShrinePeriod;
		public final IntValue harvestShrineRange;
		public final IntValue harvestShrineConsumeAmount;
		public final DoubleValue harvestShrinePeriod;
		public final IntValue lavaShrineRange;
		public final IntValue lavaShrineConsumeAmount;
		public final DoubleValue lavaShrinePeriod;
		public final IntValue oreShrineRange;
		public final IntValue oreShrineConsumeAmount;
		public final DoubleValue oreShrinePeriod;
		public final IntValue overloadShrineConsumeAmount;
		public final DoubleValue overloadShrinePeriod;
		public final IntValue sweetShrineRange;
		public final IntValue sweetShrineConsumeAmount;
		public final DoubleValue sweetShrinePeriod;
		public final IntValue sweetShrineFood;
		public final DoubleValue sweetShrineSaturation;
		public final DoubleValue spawningShrinePeriod;
		public final IntValue spawningShrineConsumeAmount;
		public final IntValue spawningShrineRange;
		public final IntValue enderLockShrineRange;
		public final IntValue enderLockShrineConsumeAmount;
		public final IntValue breedingShrineRange;
		public final IntValue breedingShrineConsumeAmount;
		public final DoubleValue breedingShrinePeriod;
		public final IntValue groveShrineRange;
		public final IntValue groveShrineConsumeAmount;
		public final DoubleValue groveShrinePeriod;
		public final IntValue springShrineConsumeAmount;
		public final DoubleValue springShrinePeriod;
		public final DoubleValue springShrineFilling;
		public final IntValue buddingShrineConsumeAmount;
		public final DoubleValue buddingShrinePeriod;

		public final IntValue tankCapacity;
		public final IntValue tankSmallCapacity;
		public final IntValue reservoirCapacity;
		public final IntValue extractorExtractionAmount;
		public final IntValue extractorMaxRunes;
		public final IntValue improvedExtractorExtractionAmount;
		public final IntValue improvedExtractorMaxRunes;
		public final IntValue evaporatorExtractionAmount;
		public final IntValue evaporatorMaxRunes;
		public final IntValue solarSythesizerMaxRunes;
		public final IntValue solarSythesizerLenseElementMultiplier;
		public final IntValue diffuserDiffusionAmount;
		public final IntValue diffuserMaxRunes;
		public final IntValue diffuserRange;
		public final IntValue fireFurnaceTransferSpeed;
		public final IntValue fireFurnaceMaxRunes;
		public final IntValue fireFurnaceElementAmount;
		public final IntValue fireBlastFurnaceTransferSpeed;
		public final IntValue fireBlastFurnaceMaxRunes;
		public final IntValue fireBlastFurnaceElementAmount;
		public final IntValue infuserTransferSpeed;
		public final IntValue infuserMaxRunes;
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
		public final IntValue purifierTransferSpeed;
		public final IntValue purifierMaxRunes;
		public final IntValue purifierDuration;
		public final IntValue purifierBaseCost;
		public final BooleanValue pureOreRecipeInjection;
		public final BooleanValue pureOreSmeltingRecipe;
		public final BooleanValue pureOreBlastingRecipe;
		public final BooleanValue pureOreCampFireRecipe;
		public final IntValue pureOreOresInput;
		public final IntValue pureOreOresOutput;
		public final DoubleValue pureOreOresLuckRatio;
		public final IntValue pureOreRawMaterialsInput;
		public final IntValue pureOreRawMaterialsOutput;
		public final DoubleValue pureOreRawMaterialsLuckRatio;
		public final IntValue impairedPipeTransferAmount;
		public final IntValue pipeTransferAmount;
		public final IntValue improvedPipeTransferAmount;
		public final BooleanValue pipePathCache;
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
		public final IntValue pureElementHolderCapacity;
		public final IntValue pureElementHolderTransferAmount;
		public final IntValue focusMaxSpell;
		public final IntValue spellBookMaxSpell;
		public final BooleanValue playersSpawnWithBook;
		public final IntValue shardElementAmount;
		public final IntValue chiselDurability;
		
		public final BooleanValue spellConsumeOnFail;

		public final BooleanValue disableWorldGen;
		public final BooleanValue disableInertCrystal;
		public final IntValue inertCrystalCount;
		public final IntValue inertCrystalSize;
		public final IntValue inertCrystalYMax;
		public final BooleanValue disableSourceSpawn;
		public final IntValue sourceSpawnChance;
		public final IntValue sourceSpawnCount;
		public final IntValue oceanSourceSpawnChance;
		public final IntValue randomSourceSpawnChance;
		public final IntValue sourceAltarDistance;

		public final BooleanValue disableSourceExhaustion;

		public final BooleanValue botaniaInteracionEnabled;
		public final IntValue manaSythesizerMaxRunes;
		public final IntValue manaSythesizerManaCapacity;
		public final DoubleValue manaElementRatio;
		public final IntValue manaSythesizerLenseElementMultiplier;

		public final BooleanValue mekanismInteracionEnabled;
		public final IntValue mekanismPureOreInputMultiplier;
		public final IntValue mekanismPureOreOutputMultiplier;
		public final BooleanValue mekanismPureOreDissolutionRecipe;
		public final BooleanValue mekanismPureOreInjectingRecipe;
		public final BooleanValue mekanismPureOrePurifyingRecipe;
		public final BooleanValue mekanismPureOreEnrichingRecipe;
		public final BooleanValue mekanismPureOreCrushingRecipe;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.comment("ElementalCraft config").push("elementalcraft");

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
			growthShrinePeriod = builder.comment("The number of tick between two Growth Shrine activations.").defineInRange("growthShrinePeriod", 20D, 0, 2400);
			builder.pop().push("harvestShrine");
			harvestShrineRange = builder.comment("The range of the Harvest Shrine.").defineInRange("harvestShrineRange", 4, 0, 100);
			harvestShrineConsumeAmount = builder.comment("The amount of element consumed by the Harvest Shrine.").defineInRange("harvestShrineConsumeAmount", 100, 0, 1000);
			harvestShrinePeriod = builder.comment("The nember of tick between two Harvest Shrine activations.").defineInRange("harvestShrinePeriod", 20D, 0, 2400);
			builder.pop().push("lavaShrine");
			lavaShrineRange = builder.comment("The range of the Lava Shrine.").defineInRange("lavaShrineRange", 1, 0, 100);
			lavaShrineConsumeAmount = builder.comment("The amount of element consumed by the Lava Shrine.").defineInRange("lavaShrineConsumeAmount", 5000, 0, 100000);
			lavaShrinePeriod = builder.comment("The nember of tick between two Lava Shrine activations.").defineInRange("lavaShrinePeriod", 1200D, 0, 4800);
			builder.pop().push("oreShrine");
			oreShrineRange = builder.comment("The range of the Ore Shrine.").defineInRange("oreShrineRange", 12, 0, 100);
			oreShrineConsumeAmount = builder.comment("The amount of element consumed by the Ore Shrine.").defineInRange("oreShrineConsumeAmount", 2000, 0, 10000);
			oreShrinePeriod = builder.comment("The nember of tick between two Ore Shrine activations.").defineInRange("oreShrinePeriod", 200D, 0, 2400);
			builder.pop().push("overloadShrine");
			overloadShrineConsumeAmount = builder.comment("The amount of element consumed by the overload Shrine.").defineInRange("overloadShrineConsumeAmount", 10, 0, 1000);
			overloadShrinePeriod = builder.comment("The nember of tick between two Overload Shrine activations.").defineInRange("overloadShrinePeriod", 3D, 0, 2400);
			builder.pop().push("sweetShrine");
			sweetShrineRange = builder.comment("The range of the Sweet Shrine.").defineInRange("sweetShrineRange", 10, 0, 100);
			sweetShrineConsumeAmount = builder.comment("The amount of element consumed by the Sweet Shrine.").defineInRange("sweetShrineConsumeAmount", 500, 0, 10000);
			sweetShrinePeriod = builder.comment("The nember of tick between two Sweet Shrine activations.").defineInRange("sweetShrinePeriod", 40D, 0, 2400);
			sweetShrineFood = builder.comment("The food given by the sheet shrine.").defineInRange("sweetShrineFood", 1, 1, 10);
			sweetShrineSaturation = builder.comment("The saturation given by the sheet shrine.").defineInRange("sweetShrineSaturation", 0.1D, 0, 10);
			builder.pop().push("spawningShrine");
			spawningShrineRange = builder.comment("The range of the spawning Shrine.").defineInRange("spawningShrineRange", 4, 0, 100);
			spawningShrineConsumeAmount = builder.comment("The amount of element consumed by the spawning Shrine.").defineInRange("spawningShrineConsumeAmount", 2000, 0, 10000);
			spawningShrinePeriod = builder.comment("The nember of tick between two Sweet spawning activations.").defineInRange("spawningShrinePeriod", 100D, 0, 2400);
			builder.pop().push("enderLock");
			enderLockShrineRange = builder.comment("The range of the Ender Lock Shrine.").defineInRange("enderLockShrineRange", 10, 0, 100);
			enderLockShrineConsumeAmount = builder.comment("The amount of element consumed by the Ender Lock Shrine.").defineInRange("enderLockShrineConsumeAmount", 500, 0, 10000);
			builder.pop().push("breedingShrine");
			breedingShrineRange = builder.comment("The range of the breeding Shrine.").defineInRange("breedingShrineRange", 10, 0, 100);
			breedingShrineConsumeAmount = builder.comment("The amount of element consumed by the breeding Shrine.").defineInRange("breedingShrineConsumeAmount", 2000, 0, 10000);
			breedingShrinePeriod = builder.comment("The nember of tick between two breeding Shrine activations.").defineInRange("breedingShrinePeriod", 200D, 0, 2400);
			builder.pop().push("groveShrine");
			groveShrineRange = builder.comment("The range of the wild grove Shrine.").defineInRange("groveShrineRange", 5, 0, 100);
			groveShrineConsumeAmount = builder.comment("The amount of element consumed by the wild grove Shrine.").defineInRange("groveShrineConsumeAmount", 500, 0, 10000);
			groveShrinePeriod = builder.comment("The nember of tick between two wild grove Shrine activations.").defineInRange("groveShrinePeriod", 200D, 0, 2400);
			builder.pop().push("springShrine");
			springShrineConsumeAmount = builder.comment("The amount of element consumed by the spring Shrine.").defineInRange("springShrineConsumeAmount", 5, 0, 10000);
			springShrinePeriod = builder.comment("The nember of tick between two spring Shrine activations.").defineInRange("springShrinePeriod", 5D, 0, 2400);
			springShrineFilling = builder.comment("The amound of water filling a tank with a filling shrine upgrades.").defineInRange("springShrineFilling", 100D, 0, 10000);
			builder.pop().push("buddingShrine");
			buddingShrineConsumeAmount = builder.comment("The amount of element consumed by the budding Shrine.").defineInRange("buddingShrineConsumeAmount", 2000, 0, 10000);
			buddingShrinePeriod = builder.comment("The nember of tick between two budding Shrine activations.").defineInRange("buddingShrinePeriod", 600D, 0, 4800);

			
			builder.pop(2).comment("Instruments config").push("instruments").push("tank");
			tankSmallCapacity = builder.comment("The element capacity of a small element container.").defineInRange("tankSmallCapacity", 1000, 0, 100000000);
			tankCapacity = builder.comment("The element capacity of a element container.").defineInRange("tankCapacity", 100000, 0, 100000000);
			reservoirCapacity = builder.comment("The element capacity of a element reservoir.").defineInRange("reservoirCapacity", 5000000, 0, 100000000);
			builder.pop().push("extractor");
			extractorExtractionAmount = builder.comment("The amount of element extracted by an extractor.").defineInRange("extractorExtractionAmount", 5, 0, 100);
			extractorMaxRunes = builder.comment("The max amount ofrunes on an extractor.").defineInRange("extractorMaxRunes", 1, 0, 10);
			improvedExtractorExtractionAmount = builder.comment("The amount of element extracted by an improved extractor.").defineInRange("improvedExtractorExtractionAmount", 25, 0, 500);
			improvedExtractorMaxRunes = builder.comment("The max amount of runes on an improved extracto.").defineInRange("improvedExtractoMaxRunes", 3, 0, 10);
			builder.pop().push("evaporator");
			evaporatorExtractionAmount = builder.comment("The amount of element extracted by an evaporator.").defineInRange("evaporatorExtractionAmount", 1, 0, 100);
			evaporatorMaxRunes = builder.comment("The max amount of runes on an evaporator.").defineInRange("evaporatorMaxRunes", 1, 0, 10);
			builder.pop().push("solarSythesizer");
			solarSythesizerMaxRunes = builder.comment("The max amount of runes on a Solar Sythesizer.").defineInRange("solarSythesizerMaxRunes", 2, 0, 10);
			solarSythesizerLenseElementMultiplier = builder.comment("the multiplier of lense in the Solar Sythesizer (based on 1500)").defineInRange("solarSythesizerLenseElementMultiplier", 10, 0, 100);
			builder.pop().push("diffuser");
			diffuserDiffusionAmount = builder.comment("The amount of element transfered by a diffuser.").defineInRange("diffuserDiffusionAmount", 5, 0, 100);
			diffuserMaxRunes = builder.comment("The max amount of runes on a diffuser.").defineInRange("diffuserMaxRunes", 3, 0, 10);
			diffuserRange = builder.comment("The range of a diffuser.").defineInRange("diffuserRange", 10, 0, 100);
			builder.pop().push("fireFurnace");
			fireFurnaceTransferSpeed = builder.comment("The max amount of element consumed by the fire furnace per tick.").defineInRange("fireFurnaceTransferSpeed", 20, 0, 1000);
			fireFurnaceMaxRunes = builder.comment("The max amount of runes on a fire furnace.").defineInRange("fireFurnaceMaxRunes", 1, 0, 10);
			fireFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the furnace recipe.").defineInRange("fireFurnaceElementAmount", 10, 0, 1000);
			builder.push("fireBlastFurnace");
			fireBlastFurnaceTransferSpeed = builder.comment("The max amount of element consumed by the fire blast furnace per tick.").defineInRange("fireBlastFurnaceTransferSpeed", 40, 0, 1000);
			fireBlastFurnaceMaxRunes = builder.comment("The max amount of runes on a fire blast furnace.").defineInRange("fireBlastFurnaceMaxRunes", 2, 0, 10);
			fireBlastFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the blast furnace recipe.").defineInRange("fireBlastFurnaceElementAmount", 20, 0, 1000);
			builder.pop(2).push("infuser");
			infuserTransferSpeed = builder.comment("The max amount of element consumed by the infuser per tick.").defineInRange("infuserTransferSpeed", 10, 0, 1000);
			infuserMaxRunes = builder.comment("The max amount of runes on an infuser.").defineInRange("infuserMaxRunes", 1, 0, 10);
			builder.pop().push("binder");
			binderTransferSpeed = builder.comment("The max amount of element consumed by the binder per tick.").defineInRange("binderTransferSpeed", 25, 0, 1000);
			binderMaxRunes = builder.comment("The max amount of runes on an binder.").defineInRange("binderMaxRunes", 2, 0, 10);
			binderRecipeMatchOrder = builder.comment("Define if or not binder recip require to be ordered.").define("binderRecipeMatchOrder", true);
			builder.push("improved");
			improvedBinderTransferSpeed = builder.comment("The max amount of element consumed by the improved binder per tick.").defineInRange("improvedBinderTransferSpeed", 50, 0, 1000);
			improvedBinderMaxRunes = builder.comment("The max amount of runes on an improved binder.").defineInRange("improvedBinderMaxRunes", 3, 0, 10);
			builder.pop(2).push("crystallizer");
			crystallizerTransferSpeed = builder.comment("The max amount of element consumed by the gem crystallizer per tick.").defineInRange("crystallizerTransferSpeed", 25, 0, 1000);
			crystallizerMaxRunes = builder.comment("The max amount of runes on an crystallizer.").defineInRange("crystallizerMaxRunes", 3, 0, 10);
			crystallizerLuckRatio = builder.comment("The ratio of each luck rune on a crystallizer.").defineInRange("crystallizerLuckRatio", 3D, 0D, 10D);
			builder.pop().push("inscriber");
			inscriberTransferSpeed = builder.comment("The max amount of element consumed by the gem inscriber per tick.").defineInRange("inscriberTransferSpeed", 1000, 0, 10000);
			inscriberMaxRunes = builder.comment("The max amount of runes on an inscriber.").defineInRange("inscriberMaxRunes", 3, 0, 10);
			builder.pop().push("airMill");
			airMillTransferSpeed = builder.comment("The max amount of element consumed by the Air Mill Grindstone per tick.").defineInRange("airMillTransferSpeed", 10, 0, 1000);
			airMillMaxRunes = builder.comment("The max amount of runes on an Air Mill Grindstone.").defineInRange("airMillMaxRunes", 3, 0, 10);
			builder.pop().push("purifier");
			purifierTransferSpeed = builder.comment("The max amount of element consumed by the Ore Purifier per tick.").defineInRange("purifierTransferSpeed", 25, 0, 1000);
			purifierMaxRunes = builder.comment("The max amount of runes on an purifier.").defineInRange("purifierMaxRunes", 3, 0, 10);
			purifierDuration = builder.comment("The number of tick for a Ore Purifier to process one item.").defineInRange("purifierDuration", 100, 0, 2400);
			purifierBaseCost = builder.comment("The base cost of a purifier recipe.").defineInRange("purifierBaseCost", 2500, 0, 10000);
			builder.push("pureOre");
			pureOreRecipeInjection = builder.comment("Set to false if you want to manually manage processing of pure ore.").define("pureOreRecipeInjection", true);
			pureOreSmeltingRecipe = builder.comment("Set to false if you want pure ore to not use smelting recipes.").define("pureOreSmeltingRecipe", true);
			pureOreBlastingRecipe = builder.comment("Set to false if you want pure ore to not use blasting recipes.").define("pureOreBlastingRecipe", true);
			pureOreCampFireRecipe = builder.comment("Set to false if you want pure ore to not use camp fire recipes.").define("pureOreCampFireRecipe", true);
			builder.push("ores");
			pureOreOresInput = builder.comment("The number of input pure ores by a purifier. Using ore blocks.").defineInRange("pureOreOresInput", 1, 1, 20);
			pureOreOresOutput = builder.comment("The number of output pure ores by a purifier. Using ore blocks.").defineInRange("pureOreOresOutput", 2, 1, 20);
			pureOreOresLuckRatio = builder.comment("The ratio of each luck rune on a purifier.  Using ore blocks.").defineInRange("pureOreOresLuckRatio", 5D, 0D, 10D);
			builder.pop().push("rawMaterials");
			pureOreRawMaterialsInput = builder.comment("The number of input pure ores by a purifier. Using raw materials.").defineInRange("pureOreRawMaterialsInput", 3, 1, 20);
			pureOreRawMaterialsOutput = builder.comment("The number of output pure ores by a purifier. Using raw materials.").defineInRange("pureOreRawMaterialsOutput", 4, 1, 20);
			pureOreRawMaterialsLuckRatio = builder.comment("The ratio of each luck rune on a purifier.  Using raw materials.").defineInRange("pureOreRawMaterialsLuckRatio", 2D, 0D, 10D);
			builder.pop(3).push("elementPipe");
			impairedPipeTransferAmount = builder.comment("The amount of element transferred by impaired pipes.").defineInRange("impairedPipeTransferAmount", 5, 0, 10000);
			pipeTransferAmount = builder.comment("The amount of element transferred by pipes.").defineInRange("pipeTransferAmount", 25, 0, 10000);
			improvedPipeTransferAmount = builder.comment("The amount of element transferred by improved pipes.").defineInRange("improvedPipeTransferAmount", 100, 0, 10000);
			pipePathCache = builder.comment("Cache the last path used by the pipe to increase performances.").define("pipePathCache", true);
			builder.pop().push("sorter");
			sorterCooldown = builder.comment("The amount of tick between two ordered sorter item transfer.").defineInRange("sorterCooldown", 10, 0, 100);
			sorterMaxItem = builder.comment("The max amount of items an order sorter can filter.").defineInRange("sorterMaxItem", 15, 0, 100);

			builder.pop(2).comment("Pure Infuser and pedestals config").push("pureInfuser");
			pureInfuserTransferSpeed = builder.comment("The max amount of element consumed by the pure infuser per tick.").defineInRange("pureInfuserTransferSpeed", 100, 0, 1000);
			pureInfuserMaxRunes = builder.comment("The max amount of runes on a pure infuser.").defineInRange("pureInfuserMaxRunes", 3, 0, 10);
			builder.push("pedestals");
			pedestalMaxRunes = builder.comment("The max amount of runes on a pedestal.").defineInRange("pedestalMaxRunes", 1, 0, 10);
			pedestalCapacity = builder.comment("The element capacity of a pedestal.").defineInRange("pedestalCapacity", 10000, 0, 100000000);

			builder.pop(2).comment("Items config").push("items");
			receptacleDurability = builder.comment("Define source receptacle durability (0 for unbreakable).").defineInRange("receptacleDurability", 5, 0, 1000);
			improvedReceptacleDurability = builder.comment("Define improved source receptacle durability (0 for unbreakable).").defineInRange("improvedReceptacleDurability", 20, 0, 1000);
			receptacleEnchantable = builder.comment("Define if or not receptacles can be enchanted.").define("receptacleEnchantable", false);
			builder.push("elementHolder");
			elementHolderCapacity = builder.comment("The element capacity of an element holder.").defineInRange("elementHolderCapacity", 10000, 0, 100000000);
			elementHolderTransferAmount = builder.comment("The amount of element transferred by an element holder.").defineInRange("elementHolderTransferAmount", 25, 0, 1000);
			builder.push("pure");
			pureElementHolderCapacity = builder.comment("The element capacity of a pure element holder.").defineInRange("pureElementHolderCapacity", 100000, 0, 100000000);
			pureElementHolderTransferAmount = builder.comment("The amount of element transferred by a pure element holder.").defineInRange("pureElementHolderTransferAmount", 100, 0, 1000);
			builder.pop(2);
			focusMaxSpell = builder.comment("The max number of spells on a focus.").defineInRange("focusMaxSpell", 10, 1, 20);
			spellBookMaxSpell = builder.comment("The max number of spells on an elementalist grimoire.").defineInRange("spellBookMaxSpell", 100, 1, 1000);
			playersSpawnWithBook = builder.comment("Players start the game with an elementopedia in their inventory.").define("playersSpawnWithBook", false);
			shardElementAmount = builder.comment("The amount of element contained in a single shard.").defineInRange("shardElementAmount", 250, 0, 1000);
			chiselDurability = builder.comment("Define chisel durablility (0 for unbreakable).").defineInRange("chiselDurability", 250, 0, 1000);

			builder.pop().comment("Spell config").push("spell");
			spellConsumeOnFail = builder.comment("Define if a spell will be cast (and destroyed) or not if you dont have enought element.").define("spellConsumeOnFail", false);

			builder.pop().comment("Worldgen config").push("worldgen");
			disableWorldGen = builder.comment("Disable all elementalcraft world gen.").define("disableWorldGen", false);
			builder.push("inertCrystal");
			disableInertCrystal = builder.comment("Disable creation of inertCrystals.").define("disableInertCrystal", false);
			inertCrystalCount = builder.comment("Number of inert crystal vein.").defineInRange("inertCrystalCount", 10, 1, 100);
			inertCrystalSize = builder.comment("Size of inert crystal vein.").defineInRange("inertCrystalSize", 9, 1, 100);
			inertCrystalYMax = builder.comment("max Y level of inert crystal.").defineInRange("inertCrystalYMax", 96, 1, 256);
			builder.pop();
			disableSourceSpawn = builder.comment("Disable creation of sources.").define("disableSourceSpawn", false);
			sourceSpawnChance = builder.comment("Chance to add a source in world (the small the more frequent).").defineInRange("sourceSpawnChance", 30, 1, 10000);
			sourceSpawnCount = builder.comment("number of sources at spawn per element type.").defineInRange("sourceSpawnCount", 2, 1, 20);
			oceanSourceSpawnChance = builder.comment("Chance to add a source in an ocean biome (the small the more frequent).").defineInRange("oceanSourceSpawnChance", 150, 1, 10000);
			randomSourceSpawnChance = builder.comment("Chance to add a source in world ignoring biome element type (the small the more frequent).").defineInRange("randomSourceSpawnChance", 300, 1,
					10000);
			sourceAltarDistance = builder.comment("CSource Altar genreration distance setting.").defineInRange("sourceAltarDistance", 64, 0, 100);

			builder.pop().comment("Source config").push("source");
			disableSourceExhaustion = builder.comment("set to true to make sources infinite.").define("disableSourceExhaustion", false);

			builder.pop().comment("mod interaction config").push("interaction");
			builder.push("botania");
			botaniaInteracionEnabled = builder.comment("Enable interaction with botania.").define("botaniaInteracionEnabled", true);
			builder.push("manaSythesizer");
			manaSythesizerMaxRunes = builder.comment("The max amount of runes on a Mana Sythesizer.").defineInRange("manaSythesizerMaxRunes", 2, 0, 10);
			manaSythesizerManaCapacity = builder.comment("The mana capacity of the Mana Sythesizer.").defineInRange("manaSythesizerManaCapacity", 10000, 0, 1000000);
			manaSythesizerLenseElementMultiplier = builder.comment("the multiplier of lense in the Mana Sythesizer (based on 1500)").defineInRange("manaSythesizerLenseElementMultiplier", 50, 0, 100);
			manaElementRatio = builder.comment("The amount of element 1 mana is worth.").defineInRange("manaElementRatio", 0.1, 0, 100);
			builder.pop(2).push("mekanism");
			mekanismInteracionEnabled = builder.comment("Enable interaction with mekanism.").define("mekanismInteracionEnabled", true);
			mekanismPureOreInputMultiplier = builder.comment("The amount multiplier when using pure ore in mekanism.").defineInRange("mekanismPureOreInputMultiplier",5, 0, 20);
			mekanismPureOreOutputMultiplier = builder.comment("The amount multiplier when using pure ore in mekanism.").defineInRange("mekanismPureOreOutputMultiplier",3, 0, 20);
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
		public final BooleanValue renderShrineUpgradeShadow;
		public final BooleanValue renderInstrumentShadow;
		public final BooleanValue fastParticleEffects;

		public final IntValue gaugeOffsetX;
		public final IntValue gaugeOffsetY;

		public Client(ForgeConfigSpec.Builder builder) {
			builder.comment("ElementalCraft client config").push("elementalcraft-client");

			shrineRangeDisplayDuration = builder.comment("The duration of shrine range display.").defineInRange("shrineRangeDisplayDuration", 600, 0, 10000);
			fastParticleEffects = builder.comment("Set to true if you want to reduce quality of particles for beter performances.").define("fastParticleEffects", false);

			builder.push("shadowBlocks");
			renderPedestalShadow = builder.comment("Display a shadow where pedestals can be placed.").define("renderPedestalShadow", true);
			renderShrineUpgradeShadow = builder.comment("Display a shadow where shrine upgrades can be placed.").define("renderShrineUpgradeShadow", true);
			renderInstrumentShadow = builder.comment("Display a shadow where instruments can be placed.").define("renderInstrumentShadow", true);

			builder.pop().push("gauge");
			usePaleElementGauge = builder.comment("Use pale element gauges.").define("usePaleElementGauge", false);
			gaugeOffsetX = builder.comment("the offset of the gauge on the X axis.").defineInRange("gaugeOffsetX", 0, -10000, 10000);
			gaugeOffsetY = builder.comment("the offset of the gauge on the Y axis.").defineInRange("gaugeOffsetY", 0, -10000, 10000);
			builder.pop();
		}
	}
}
