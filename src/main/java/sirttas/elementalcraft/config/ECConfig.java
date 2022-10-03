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
		public final IntValue airMillGrindstoneTransferSpeed;
		public final IntValue airMillGrindstoneMaxRunes;

		public final IntValue waterMillWoodSawTransferSpeed;
		public final IntValue waterMillWoodSawMaxRunes;
		public final IntValue purifierTransferSpeed;
		public final IntValue purifierMaxRunes;
		public final BooleanValue pureOreRecipeInjection;
		public final IntValue impairedPipeTransferAmount;
		public final IntValue pipeTransferAmount;
		public final IntValue improvedPipeTransferAmount;
		public final BooleanValue pipePathCache;
		public final IntValue sorterCooldown;
		public final IntValue sorterMaxItem;
		public final IntValue sorterMaxRunes;

		public final IntValue pureInfuserTransferSpeed;
		public final IntValue pureInfuserMaxRunes;
		public final IntValue pedestalCapacity;
		public final IntValue pedestalMaxRunes;
		public final IntValue sourceBreederTransferSpeed;
		public final IntValue sourceBreederMaxRunes;
		public final IntValue sourceBreederPedestalCapacity;
		public final IntValue sourceBreederPedestalMaxRunes;
		public final IntValue elementHolderCapacity;
		public final IntValue elementHolderTransferAmount;
		public final IntValue pureElementHolderCapacity;
		public final IntValue pureElementHolderTransferAmount;
		public final IntValue focusMaxSpell;
		public final IntValue spellBookMaxSpell;
		public final BooleanValue playersSpawnWithBook;
		public final IntValue shardElementAmount;

		public final BooleanValue disableSourceSpawn;
		public final IntValue sourceSpawnCount;

		public final BooleanValue spellConsumeOnFail;

		public final BooleanValue disableSourceExhaustion;

		public final IntValue manaSythesizerMaxRunes;
		public final IntValue manaSythesizerManaCapacity;
		public final DoubleValue manaElementRatio;
		public final IntValue manaSythesizerLenseElementMultiplier;

		public final IntValue mekanismPureOreInputMultiplier;
		public final IntValue mekanismPureOreOutputMultiplier;

		public Common(ForgeConfigSpec.Builder builder) {
			builder.comment("ElementalCraft config").push("elementalcraft");

			builder.comment("Instruments config").push("instruments").push("tank");
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
			fireFurnaceTransferSpeed = builder.comment("The max amount of element consumed by the fire furnace per tick.").defineInRange("fireFurnaceTransferSpeed", 10, 0, 1000);
			fireFurnaceMaxRunes = builder.comment("The max amount of runes on a fire furnace.").defineInRange("fireFurnaceMaxRunes", 1, 0, 10);
			fireFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the furnace recipe.").defineInRange("fireFurnaceElementAmount", 5, 0, 1000);
			builder.push("fireBlastFurnace");
			fireBlastFurnaceTransferSpeed = builder.comment("The max amount of element consumed by the fire blast furnace per tick.").defineInRange("fireBlastFurnaceTransferSpeed", 20, 0, 1000);
			fireBlastFurnaceMaxRunes = builder.comment("The max amount of runes on a fire blast furnace.").defineInRange("fireBlastFurnaceMaxRunes", 2, 0, 10);
			fireBlastFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the blast furnace recipe.").defineInRange("fireBlastFurnaceElementAmount", 10, 0, 1000);
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
			builder.pop().push("airMillGrindstone");
			airMillGrindstoneTransferSpeed = builder.comment("The max amount of element consumed by the Air Mill Grindstone per tick.").defineInRange("airMillGrindstoneTransferSpeed", 10, 0, 1000);
			airMillGrindstoneMaxRunes = builder.comment("The max amount of runes on an Air Mill Grindstone.").defineInRange("airMillGrindstoneMaxRunes", 3, 0, 10);
			builder.pop().push("waterMillWoodSaw");
			waterMillWoodSawTransferSpeed = builder.comment("The max amount of element consumed by the Water Mill Wood Saw per tick.").defineInRange("waterMillWoodSawTransferSpeed", 10, 0, 1000);
			waterMillWoodSawMaxRunes = builder.comment("The max amount of runes on an Water Mill Wood Saw.").defineInRange("waterMillWoodSawMaxRunes", 3, 0, 10);
			builder.pop().push("purifier");
			purifierTransferSpeed = builder.comment("The max amount of element consumed by the Ore Purifier per tick.").defineInRange("purifierTransferSpeed", 25, 0, 1000);
			purifierMaxRunes = builder.comment("The max amount of runes on an purifier.").defineInRange("purifierMaxRunes", 3, 0, 10);
			builder.push("pureOre");
			pureOreRecipeInjection = builder.comment("Set to false if you want to manually manage processing of pure ore.").define("pureOreRecipeInjection", true);
			builder.pop(2).push("elementPipe");
			impairedPipeTransferAmount = builder.comment("The amount of element transferred by impaired pipes.").defineInRange("impairedPipeTransferAmount", 5, 0, 10000);
			pipeTransferAmount = builder.comment("The amount of element transferred by pipes.").defineInRange("pipeTransferAmount", 25, 0, 10000);
			improvedPipeTransferAmount = builder.comment("The amount of element transferred by improved pipes.").defineInRange("improvedPipeTransferAmount", 100, 0, 10000);
			pipePathCache = builder.comment("Cache the last path used by the pipe to increase performances.").define("pipePathCache", true);
			builder.pop().push("sorter");
			sorterCooldown = builder.comment("The amount of tick between two ordered sorter item transfer.").defineInRange("sorterCooldown", 10, 0, 100);
			sorterMaxItem = builder.comment("The max amount of items an order sorter can filter.").defineInRange("sorterMaxItem", 15, 0, 100);
			sorterMaxRunes = builder.comment("The max amount of runes an order sorter can have.").defineInRange("sorterMaxRunes", 3, 0, 10);

			builder.pop(2).comment("Pure Infuser and pedestals config").push("pureInfuser");
			pureInfuserTransferSpeed = builder.comment("The max amount of element consumed by the pure infuser per tick.").defineInRange("pureInfuserTransferSpeed", 100, 0, 1000);
			pureInfuserMaxRunes = builder.comment("The max amount of runes on a pure infuser.").defineInRange("pureInfuserMaxRunes", 3, 0, 10);
			builder.push("pedestals");
			pedestalMaxRunes = builder.comment("The max amount of runes on a pedestal.").defineInRange("pedestalMaxRunes", 1, 0, 10);
			pedestalCapacity = builder.comment("The element capacity of a pedestal.").defineInRange("pedestalCapacity", 10000, 0, 100000000);
			builder.pop(2).comment("Source Breeder and pedestals config").push("sourceBreeder");
			sourceBreederTransferSpeed = builder.comment("The max amount of element consumed by the source breeder per tick.").defineInRange("sourceBreederTransferSpeed", 500, 0, 1000);
			sourceBreederMaxRunes = builder.comment("The max amount of runes on a source breeder.").defineInRange("sourceBreederMaxRunes", 3, 0, 10);
			builder.push("pedestals");
			sourceBreederPedestalMaxRunes = builder.comment("The max amount of runes on a source breeder pedestal.").defineInRange("sourceBreederPedestalMaxRunes", 1, 0, 10);
			sourceBreederPedestalCapacity = builder.comment("The element capacity of a source breeder pedestal.").defineInRange("sourceBreederPedestalCapacity", 100000, 0, 100000000);

			builder.pop(2).comment("Items config").push("items");
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

			builder.pop().comment("Spell config").push("spell");
			spellConsumeOnFail = builder.comment("Define if a spell will be cast (and destroyed) or not if you dont have enought element.").define("spellConsumeOnFail", false);


			builder.pop().comment("Source config").push("source");
			disableSourceExhaustion = builder.comment("set to true to make sources infinite.").define("disableSourceExhaustion", false);
			builder.pop().comment("Config of sources around").push("spawn");
			disableSourceSpawn = builder.comment("Disable creation of sources.").define("disableSourceSpawn", false);
			sourceSpawnCount = builder.comment("number of sources at spawn per element type.").defineInRange("sourceSpawnCount", 2, 1, 20);

			builder.pop(2).comment("mod interaction config").push("interaction");
			builder.push("botania").push("manaSythesizer");
			manaSythesizerMaxRunes = builder.comment("The max amount of runes on a Mana Sythesizer.").defineInRange("manaSythesizerMaxRunes", 2, 0, 10);
			manaSythesizerManaCapacity = builder.comment("The mana capacity of the Mana Sythesizer.").defineInRange("manaSythesizerManaCapacity", 10000, 0, 1000000);
			manaSythesizerLenseElementMultiplier = builder.comment("the multiplier of lense in the Mana Sythesizer (based on 1500)").defineInRange("manaSythesizerLenseElementMultiplier", 50, 0, 100);
			manaElementRatio = builder.comment("The amount of element 1 mana is worth.").defineInRange("manaElementRatio", 0.1, 0, 100);
			builder.pop(2).push("mekanism");
			mekanismPureOreInputMultiplier = builder.comment("The amount multiplier when using pure ore in mekanism.").defineInRange("mekanismPureOreInputMultiplier",5, 0, 20);
			mekanismPureOreOutputMultiplier = builder.comment("The amount multiplier when using pure ore in mekanism.").defineInRange("mekanismPureOreOutputMultiplier",3, 0, 20);

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
