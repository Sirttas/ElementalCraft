package sirttas.elementalcraft.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.datagen.advancement.ECAdvancementGenerator;
import sirttas.elementalcraft.datagen.advancement.ECPickupAdvancementGenerator;
import sirttas.elementalcraft.datagen.interaction.DatagenInteraction;
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
import sirttas.elementalcraft.datagen.model.BuddingShrinePlateModelGenerator;
import sirttas.elementalcraft.datagen.model.ECBlockStateModelGenerator;
import sirttas.elementalcraft.datagen.model.ECItemModelGenerator;
import sirttas.elementalcraft.datagen.model.ECModelProvider;
import sirttas.elementalcraft.datagen.model.PipeUpgradeModelGenerator;
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

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ECDataGenerators {

	private ECDataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		PipeUpgradeTypes.setup();
		Jewels.setup();

		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var lookupProvider = event.getLookupProvider();

		var registriesProvider = event.addProvider(new DatapackBuiltinEntriesProvider(output, lookupProvider, new RegistrySetBuilder()
				.add(Registries.PLACED_FEATURE, new ECFeaturesProvider())
				.add(Registries.STRUCTURE, new ECStructuresProvider())
				.add(Registries.STRUCTURE_SET, new ECStructureSetsProvider())
				.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, new ECBiomeModifierProvider())
				.add(Registries.TRIM_MATERIAL, new ECTrimMaterialProvider())
				.add(Registries.DAMAGE_TYPE, new ECDamageTypeProvider()),
				Set.of(ElementalCraftApi.MODID)));
		var registries = registriesProvider.getRegistryProvider();

		var translationKeyValidator = new TranslationKeyValidator(List.of(
				event.addProvider(new ECEnglishLanguageProvider(output)),
				event.addProvider(new ECFrenchLanguageProvider(output))));
		event.addProvider(new ECSpriteSourceProvider(output, lookupProvider));
		event.addProvider(new ECLootTableProvider(output, registries));
		event.addProvider(new ECModelProvider(output, List.of(
				ECBlockStateModelGenerator.FACTORY,
				ECItemModelGenerator.FACTORY,
				PipeUpgradeModelGenerator.FACTORY,
				BuddingShrinePlateModelGenerator.FACTORY)));
		event.addProvider(new ECBlockTagsProvider(output, registries));
		event.addProvider(new ECItemTagsProvider(output, registries));
		event.addProvider(new ECBiomeTagsProvider(output, registries));
		event.addProvider(new ECDamageTypeTagsProvider(output, registries));
		event.addProvider(new ECGameEventTagsProvider(output, registries));
		event.addProvider(new ECRecipeProvider.Runner(output, registries));
		event.addProvider(new AdvancementProvider(output, registries, List.of(
				new ECAdvancementGenerator(translationKeyValidator),
				new ECPickupAdvancementGenerator(translationKeyValidator))));
		event.addProvider(new ECDataMapProvider(output, registries));
		event.addProvider(new RangesProvider(output, registries));
		event.addProvider(new RunesProvider(output, registries));
		event.addProvider(new ShrineUpgradeProvider(output, registries));
		event.addProvider(new SpellPropertiesProvider(output, registries));
		event.addProvider(new ToolInfusionProvider(output, registries));
		event.addProvider(new SourceTraitsProvider(output, registries));
		event.addProvider(new ConfigurableBlockEntityPropertiesProvider(output, registries));
		event.addProvider(new PureOreLoaderProvider(output, registries));
		event.addProvider(new BudTypeProvider(output, registries));
		event.addProvider(new ECRemapKeysProvider(output, registries));
		event.addProvider(new BookDataProvider(output, registries, translationKeyValidator));

		DatagenInteraction.get().getProviders(output, registries).forEach(event::addProvider);
	}
}
