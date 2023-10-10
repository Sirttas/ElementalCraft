package sirttas.elementalcraft.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.datagen.interaction.ECSilentGearMaterialProvider;
import sirttas.elementalcraft.datagen.loot.ECLootTableProvider;
import sirttas.elementalcraft.datagen.managed.ECRemapKeysProvider;
import sirttas.elementalcraft.datagen.managed.RunesProvider;
import sirttas.elementalcraft.datagen.managed.SourceTraitsProvider;
import sirttas.elementalcraft.datagen.managed.SpellPropertiesProvider;
import sirttas.elementalcraft.datagen.managed.ToolInfusionProvider;
import sirttas.elementalcraft.datagen.managed.pure.ore.loader.loader.PureOreLoaderProvider;
import sirttas.elementalcraft.datagen.managed.shrine.ShrinePropertiesProvider;
import sirttas.elementalcraft.datagen.managed.shrine.ShrineUpgradeProvider;
import sirttas.elementalcraft.datagen.recipe.ECRecipeProvider;
import sirttas.elementalcraft.datagen.registry.ECTrimMaterialProvider;
import sirttas.elementalcraft.datagen.registry.world.ECBiomeModifierProvider;
import sirttas.elementalcraft.datagen.registry.world.ECFeaturesProvider;
import sirttas.elementalcraft.datagen.registry.world.ECStructureSetsProvider;
import sirttas.elementalcraft.datagen.registry.world.ECStructuresProvider;
import sirttas.elementalcraft.datagen.tag.ECBiomeTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECBlockTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECItemTagsProvider;
import sirttas.elementalcraft.interaction.ECinteractions;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECDataGenerators {

	private ECDataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		PipeUpgradeTypes.setup();

		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var fileHelper = event.getExistingFileHelper();
		var includeServer = event.includeServer();
		var includeClient = event.includeClient();

		var itemModelProvider = new ECItemModelProvider(output, fileHelper);
		var datapackProvider = generator.addProvider(includeServer, new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), new RegistrySetBuilder()
				.add(Registries.PLACED_FEATURE, new ECFeaturesProvider())
				.add(Registries.STRUCTURE, new ECStructuresProvider())
				.add(Registries.STRUCTURE_SET, new ECStructureSetsProvider())
				.add(ForgeRegistries.Keys.BIOME_MODIFIERS, new ECBiomeModifierProvider())
				.add(Registries.TRIM_MATERIAL, new ECTrimMaterialProvider()),
				Set.of(ElementalCraftApi.MODID)));
		var registries = datapackProvider.getRegistryProvider();

		generator.addProvider(includeClient, new ECSpriteSourceProvider(output, fileHelper));
		generator.addProvider(includeServer, new ECLootTableProvider(output));
		generator.addProvider(includeClient, new ECBlockStateProvider(output, fileHelper));
		generator.addProvider(includeClient, itemModelProvider);
		var blockTagsProvider = generator.addProvider(includeServer, new ECBlockTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECItemTagsProvider(output, registries, blockTagsProvider.contentsGetter(), fileHelper));
		generator.addProvider(includeServer, new ECBiomeTagsProvider(output, registries, fileHelper));
		generator.addProvider(includeServer, new ECRecipeProvider(output, fileHelper));
		generator.addProvider(includeServer, new ForgeAdvancementProvider(output, registries, fileHelper, List.of(new ECAdvancementProvider())));
		generator.addProvider(includeServer, new RunesProvider(output, registries, itemModelProvider));
		generator.addProvider(includeServer, new ShrineUpgradeProvider(output, registries));
		generator.addProvider(includeServer, new SpellPropertiesProvider(output, registries));
		generator.addProvider(includeServer, new ToolInfusionProvider(output, registries));
		generator.addProvider(includeServer, new SourceTraitsProvider(output, registries));
		generator.addProvider(includeServer, new ShrinePropertiesProvider(output, registries));
		generator.addProvider(includeServer, new PureOreLoaderProvider(output, registries));
		generator.addProvider(includeServer, new ECRemapKeysProvider(output, registries));
		if (ECinteractions.isSilentGearActive()) {
			generator.addProvider(includeServer, new ECSilentGearMaterialProvider(generator));
		}
	}

	public static String getPipeTexture(ElementPipeBlock.PipeType type) {
		return switch (type) {
			case IMPAIRED -> "iron";
			case STANDARD -> "brass";
			case IMPROVED -> "pure_iron";
			case CREATIVE -> "creative_iron";
		};
	}

}
