package sirttas.elementalcraft.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sirttas.elementalcraft.ElementalCraftInteraction;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.datagen.advancement.ECAdvancementGenerator;
import sirttas.elementalcraft.datagen.advancement.ECPickupAdvancementGenerator;
import sirttas.elementalcraft.datagen.interaction.ECSilentGearMaterialProvider;
import sirttas.elementalcraft.datagen.interaction.patchouli.BookDataProvider;
import sirttas.elementalcraft.datagen.language.ECEnglishLanguageProvider;
import sirttas.elementalcraft.datagen.language.ECFrenchLanguageProvider;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;
import sirttas.elementalcraft.datagen.loot.ECLootTableProvider;
import sirttas.elementalcraft.datagen.managed.BudTypeProvider;
import sirttas.elementalcraft.datagen.managed.ECRemapKeysProvider;
import sirttas.elementalcraft.datagen.managed.RangesProvider;
import sirttas.elementalcraft.datagen.managed.RunesProvider;
import sirttas.elementalcraft.datagen.managed.ShrineUpgradeProvider;
import sirttas.elementalcraft.datagen.managed.SourceTraitsProvider;
import sirttas.elementalcraft.datagen.managed.SpellPropertiesProvider;
import sirttas.elementalcraft.datagen.managed.ToolInfusionProvider;
import sirttas.elementalcraft.datagen.managed.block.entity.properties.ConfigurableBlockEntityPropertiesProvider;
import sirttas.elementalcraft.datagen.managed.pure.ore.loader.PureOreLoaderProvider;
import sirttas.elementalcraft.datagen.recipe.ECRecipeProvider;
import sirttas.elementalcraft.datagen.registry.ECDamageTypeProvider;
import sirttas.elementalcraft.datagen.registry.ECTrimMaterialProvider;
import sirttas.elementalcraft.datagen.registry.world.ECBiomeModifierProvider;
import sirttas.elementalcraft.datagen.registry.world.ECFeaturesProvider;
import sirttas.elementalcraft.datagen.registry.world.ECStructureSetsProvider;
import sirttas.elementalcraft.datagen.registry.world.ECStructuresProvider;
import sirttas.elementalcraft.datagen.tag.ECBiomeTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECBlockTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECDamageTypeTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECGameEventTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECItemTagsProvider;
import sirttas.elementalcraft.jewel.Jewels;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECDataGenerators {

	private ECDataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		PipeUpgradeTypes.setup();
		Jewels.setup();

		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var fileHelper = event.getExistingFileHelper();
		var includeServer = event.includeServer();
		var includeClient = event.includeClient();
		var lookupProvider = event.getLookupProvider();

		var itemModelProvider = new ECItemModelProvider(output, fileHelper);
		var registriesProvider = generator.addProvider(includeServer, new DatapackBuiltinEntriesProvider(output, lookupProvider, new RegistrySetBuilder()
				.add(Registries.PLACED_FEATURE, new ECFeaturesProvider())
				.add(Registries.STRUCTURE, new ECStructuresProvider())
				.add(Registries.STRUCTURE_SET, new ECStructureSetsProvider())
				.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, new ECBiomeModifierProvider())
				.add(Registries.TRIM_MATERIAL, new ECTrimMaterialProvider())
				.add(Registries.DAMAGE_TYPE, new ECDamageTypeProvider()),
				Set.of(ElementalCraftApi.MODID)));
		var registries = registriesProvider.getRegistryProvider();

		var translationKeyValidator = new TranslationKeyValidator(List.of(
				generator.addProvider(includeClient, new ECEnglishLanguageProvider(output)),
				generator.addProvider(includeClient, new ECFrenchLanguageProvider(output))));
		generator.addProvider(includeClient, new ECSpriteSourceProvider(output, lookupProvider, fileHelper));
		generator.addProvider(includeServer, new ECLootTableProvider(output, registries));
		generator.addProvider(includeClient, new ECBlockStateProvider(output, fileHelper));
		generator.addProvider(includeClient, itemModelProvider);
        generator.addProvider(includeClient, new ECBlockModelProvider(output, fileHelper));
		var blockTagsProvider = generator.addProvider(includeServer, new ECBlockTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECItemTagsProvider(output, registries, blockTagsProvider.contentsGetter(), fileHelper));
		generator.addProvider(includeServer, new ECBiomeTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECDamageTypeTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECGameEventTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECRecipeProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new AdvancementProvider(output, registries, fileHelper, List.of(
				new ECAdvancementGenerator(translationKeyValidator),
				new ECPickupAdvancementGenerator(translationKeyValidator))));
		generator.addProvider(includeServer, new ECDataMapProvider(output, registries));
		generator.addProvider(includeServer, new RangesProvider(output, registries));
		generator.addProvider(includeServer, new RunesProvider(output, registries, itemModelProvider));
		generator.addProvider(includeServer, new ShrineUpgradeProvider(output, registries));
		generator.addProvider(includeServer, new SpellPropertiesProvider(output, registries));
		generator.addProvider(includeServer, new ToolInfusionProvider(output, registries));
		generator.addProvider(includeServer, new SourceTraitsProvider(output, registries));
		generator.addProvider(includeServer, new ConfigurableBlockEntityPropertiesProvider(output, registries));
		generator.addProvider(includeServer, new PureOreLoaderProvider(output, registries));
		generator.addProvider(includeServer, new BudTypeProvider(output, registries));
		generator.addProvider(includeServer, new ECRemapKeysProvider(output, registries));
		generator.addProvider(includeServer && includeClient, new BookDataProvider(output, registries, fileHelper, translationKeyValidator));
		if (ElementalCraftInteraction.isSilentGearActive()) {
			generator.addProvider(includeServer, new ECSilentGearMaterialProvider(generator));
		}
	}

	public static String getPipeTexture(ElementPipeBlock.PipeType type) {
		return switch (type) {
			case RUDIMENTARY -> "iron";
			case STANDARD -> "brass";
			case IMPROVED -> "pure_iron";
			case CREATIVE -> "creative_iron";
		};
	}

}
