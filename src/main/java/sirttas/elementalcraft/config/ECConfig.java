package sirttas.elementalcraft.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECConfig {

	public static final Server SERVER;
	public static final ModConfigSpec SERVER_SPEC;

	public static final Client CLIENT;
	public static final ModConfigSpec CLIENT_SPEC;

	static {
		Pair<Server, ModConfigSpec> serverPair = new ModConfigSpec.Builder().configure(Server::new);
		Pair<Client, ModConfigSpec> clientPair = new ModConfigSpec.Builder().configure(Client::new);

		SERVER_SPEC = serverPair.getRight();
		SERVER = serverPair.getLeft();
		CLIENT_SPEC = clientPair.getRight();
		CLIENT = clientPair.getLeft();
	}

	private ECConfig() {}
	
	public static class Server {

		public final IntValue rudimentaryExtractorExtractionAmount;
		public final IntValue rudimentaryExtractorMaxRunes;
		public final IntValue extractorExtractionAmount;
		public final IntValue extractorMaxRunes;
		public final IntValue improvedExtractorExtractionAmount;
		public final IntValue improvedExtractorMaxRunes;
		public final IntValue diffuserDiffusionAmount;
		public final IntValue diffuserMaxRunes;
		public final IntValue fireFurnaceElementAmount;
		public final IntValue fireBlastFurnaceElementAmount;
		public final BooleanValue binderRecipeMatchOrder;
		public final IntValue enchantmentLiquefierElementAmount;
		public final DoubleValue enchantmentLiquefierBreakChance;
		public final BooleanValue pureOreRecipeInjection;
		public final IntValue rudimentaryPipeTransferAmount;
		public final IntValue pipeTransferAmount;
		public final IntValue improvedPipeTransferAmount;
		public final BooleanValue pipePathCache;
		public final IntValue elementBeamRange;
		public final IntValue elementBeamMaxRunes;
		public final DoubleValue elementPumpMultiplier;
		public final DoubleValue elementPumpWaste;
		public final IntValue elementPumpMaxRunes;
		public final IntValue sorterCooldown;
		public final IntValue sorterMaxItem;
		public final IntValue sorterMaxRunes;

		public final IntValue pedestalCapacity;
		public final IntValue pedestalMaxRunes;
		public final IntValue sourceBreedingBaseCost;
		public final IntValue sourceBreederPedestalCapacity;
		public final IntValue sourceBreederPedestalMaxRunes;
		public final IntValue fortuneShrineUpgradeLevel;
		public final IntValue greaterFortuneShrineUpgradeLevel;
		public final IntValue elementHolderCapacity;
		public final IntValue elementHolderTransferAmount;
		public final IntValue pureElementHolderCapacity;
		public final IntValue pureElementHolderTransferAmount;
		public final IntValue focusMaxSpell;
		public final IntValue spellBookMaxSpell;
		public final IntValue shardElementAmount;

		public final BooleanValue spellConsumeOnFail;

		public final IntValue mekanismPureOreInputMultiplier;
		public final IntValue mekanismPureOreOutputMultiplier;

		public Server(ModConfigSpec.Builder builder) {
			builder.comment("ElementalCraft config")
					.comment("Note: ElementalCraft favor setting config for blocks and stacks through custom datatypes found inside data/elementalcraft/elementalcraft/ (https://github.com/Sirttas/ElementalCraft/tree/1.21/src/generated/resources/data/elementalcraft/elementalcraft)")
					.push(ElementalCraftApi.MODID);

			builder.push("extractor");
			rudimentaryExtractorExtractionAmount = builder.comment("The amount of element extracted by a rudimentary extractor.").defineInRange("rudimentaryExtractorExtractionAmount", 5, 0, 100);
			rudimentaryExtractorMaxRunes = builder.comment("The max amount of runes on a rudimentary extractor.").defineInRange("rudimentaryExtractorMaxRunes", 1, 0, 10);
			extractorExtractionAmount = builder.comment("The amount of element extracted by an extractor.").defineInRange("extractorExtractionAmount", 25, 0, 100);
			extractorMaxRunes = builder.comment("The max amount of runes on an extractor.").defineInRange("extractorMaxRunes", 2, 0, 10);
			improvedExtractorExtractionAmount = builder.comment("The amount of element extracted by an improved extractor.").defineInRange("improvedExtractorExtractionAmount", 100, 0, 500);
			improvedExtractorMaxRunes = builder.comment("The max amount of runes on an improved extractor.").defineInRange("improvedExtractoMaxRunes", 3, 0, 10);
			builder.pop().push("diffuser");
			diffuserDiffusionAmount = builder.comment("The amount of element transfered by a diffuser.").defineInRange("diffuserDiffusionAmount", 5, 0, 100);
			diffuserMaxRunes = builder.comment("The max amount of runes on a diffuser.").defineInRange("diffuserMaxRunes", 3, 0, 10);
			builder.pop().push("fireFurnace");
			fireFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the furnace recipe.").defineInRange("fireFurnaceElementAmount", 5, 0, 1000);
			builder.push("fireBlastFurnace");
			fireBlastFurnaceElementAmount = builder.comment("The amount multiplied by the cooking time of the blast furnace recipe.").defineInRange("fireBlastFurnaceElementAmount", 10, 0, 1000);
			builder.pop().push("binder");
			binderRecipeMatchOrder = builder.comment("Define if or not binder recipe require to be ordered.").define("binderRecipeMatchOrder", true);
			builder.pop().push("enchantmentLiquefier");
			enchantmentLiquefierElementAmount = builder.comment("The cost of liquefying echantments.").defineInRange("enchantmentLiquefierElementAmount", 5000, 0, 100000000);
			enchantmentLiquefierBreakChance = builder.comment("The chance of breaking echantments.").defineInRange("enchantmentLiquefierBreakChance", 2.5, 0, 100);
			builder.pop().push("purifier")
					.push("pureOre");
			pureOreRecipeInjection = builder.comment("Set to false if you want to manually manage processing of pure ore.").define("pureOreRecipeInjection", true);
			builder.pop(2).push("elementPipe");
			rudimentaryPipeTransferAmount = builder.comment("The amount of element transferred by rudimentary pipes.").defineInRange("rudimentaryPipeTransferAmount", 25, 0, 10000);
			pipeTransferAmount = builder.comment("The amount of element transferred by pipes.").defineInRange("pipeTransferAmount", 100, 0, 10000);
			improvedPipeTransferAmount = builder.comment("The amount of element transferred by improved pipes.").defineInRange("improvedPipeTransferAmount", 500, 0, 10000);
			pipePathCache = builder.comment("Cache the last path used by the pipe to increase performances.").define("pipePathCache", true);
			builder.push("upgrade")
					.push("elementBeam");
			elementBeamRange = builder.comment("The ranges of the element beam.").defineInRange("elementBeamRange", 10, 0, 100);
			elementBeamMaxRunes = builder.comment("The max amount of runes an element beam can have.").defineInRange("elementBeamMaxRunes", 1, 0, 10);
			builder.pop().push("elementPump");
			elementPumpMultiplier = builder.comment("The amount of element pumped by the element pump.").defineInRange("elementPumpMultiplier", 5D, 0, 100);
			elementPumpWaste = builder.comment("The amount of element wasted by the element pump.").defineInRange("elementPumpWaste", 0.1D, 0, 1);
			elementPumpMaxRunes = builder.comment("The max amount of runes an element pump can have.").defineInRange("elementPumpMaxRunes", 3, 0, 10);
			builder.pop(3).push("sorter");
			sorterCooldown = builder.comment("The amount of tick between two ordered sorter item transfer.").defineInRange("sorterCooldown", 10, 0, 100);
			sorterMaxItem = builder.comment("The max amount of stacks an order sorter can filter.").defineInRange("sorterMaxItem", 15, 0, 100);
			sorterMaxRunes = builder.comment("The max amount of runes an order sorter can have.").defineInRange("sorterMaxRunes", 3, 0, 10);

			builder.pop(2).comment("Pure Infuser and pedestals config").push("pureInfuser")
					.push("pedestals");
			pedestalMaxRunes = builder.comment("The max amount of runes on a pedestal.").defineInRange("pedestalMaxRunes", 1, 0, 10);
			pedestalCapacity = builder.comment("The element capacity of a pedestal.").defineInRange("pedestalCapacity", 10000, 0, 100000000);

			builder.pop(2).comment("Shrines config").push("shrines")
					.comment("Shrine upgrades config").push("upgrades");
			fortuneShrineUpgradeLevel = builder.comment("The fortune level of the fortune shrine upgrade.").defineInRange("fortuneShrineUpgradeLevel", 1, 1, 10);
			greaterFortuneShrineUpgradeLevel = builder.comment("The fortune level of the fortune shrine upgrade.").defineInRange("greaterFortuneShrineUpgradeLevel", 3, 1, 10);

			builder.pop(2).comment("Items config").push("stacks")
					.push("elementHolder");
			elementHolderCapacity = builder.comment("The element capacity of an element holder.").defineInRange("elementHolderCapacity", 10000, 0, 100000000);
			elementHolderTransferAmount = builder.comment("The amount of element transferred by an element holder.").defineInRange("elementHolderTransferAmount", 25, 0, 1000);
			builder.push("pure");
			pureElementHolderCapacity = builder.comment("The element capacity of a pure element holder.").defineInRange("pureElementHolderCapacity", 100000, 0, 100000000);
			pureElementHolderTransferAmount = builder.comment("The amount of element transferred by a pure element holder.").defineInRange("pureElementHolderTransferAmount", 100, 0, 1000);
			builder.pop(2);
			focusMaxSpell = builder.comment("The max number of spells on a focus.").defineInRange("focusMaxSpell", 10, 1, 20);
			spellBookMaxSpell = builder.comment("The max number of spells on an elementalist grimoire.").defineInRange("spellBookMaxSpell", 100, 1, 1000);
			shardElementAmount = builder.comment("The amount of element contained in a single shard.").defineInRange("shardElementAmount", 250, 0, 1000);

			builder.pop().comment("Spell config").push("spell");
			spellConsumeOnFail = builder.comment("Define if a spell will be cast (and destroyed) or not if you dont have enought element.").define("spellConsumeOnFail", false);

			builder.pop().comment("Source config").push("source")
					.comment("Source Breeder and pedestals config").push("breeder");
			sourceBreedingBaseCost = builder.comment("The base cost of breeding sources.").defineInRange("sourceBreedingBaseCost", 100000, 0, 100000000);
			builder.push("pedestals");
			sourceBreederPedestalMaxRunes = builder.comment("The max amount of runes on a source breeder pedestal.").defineInRange("sourceBreederPedestalMaxRunes", 1, 0, 10);
			sourceBreederPedestalCapacity = builder.comment("The element capacity of a source breeder pedestal.").defineInRange("sourceBreederPedestalCapacity", 100000, 0, 100000000);

			builder.pop(3).comment("mod interaction config").push("interaction")
					.push("mekanism");
			mekanismPureOreInputMultiplier = builder.comment("The amount multiplier when using pure ore in mekanism.").defineInRange("mekanismPureOreInputMultiplier",5, 0, 20);
			mekanismPureOreOutputMultiplier = builder.comment("The amount multiplier when using pure ore in mekanism.").defineInRange("mekanismPureOreOutputMultiplier",3, 0, 20);
			builder.pop(2);
		}
	}

	public static class Client {

		public final BooleanValue usePaleElementGauge;
		public final IntValue rangeDisplayDuration;
		public final BooleanValue renderPedestalShadow;
		public final BooleanValue renderShrineUpgradeShadow;
		public final BooleanValue renderInstrumentShadow;

		public final IntValue gaugeOffsetX;
		public final IntValue gaugeOffsetY;

		public Client(ModConfigSpec.Builder builder) {
			builder.comment("ElementalCraft client config").push("elementalcraft-client");

			rangeDisplayDuration = builder.comment("The duration of block ranges display.").defineInRange("rangeDisplayDuration", 600, 0, 10000);
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
