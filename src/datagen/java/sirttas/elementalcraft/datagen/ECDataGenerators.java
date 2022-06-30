package sirttas.elementalcraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.datagen.loot.ECBlockLootProvider;
import sirttas.elementalcraft.datagen.loot.ECChestLootProvider;
import sirttas.elementalcraft.datagen.loot.ECEntityLootProvider;
import sirttas.elementalcraft.datagen.loot.ECInjectLootProvider;
import sirttas.elementalcraft.datagen.managed.RunesProvider;
import sirttas.elementalcraft.datagen.managed.ShrinePropertiesProvider;
import sirttas.elementalcraft.datagen.managed.ShrineUpgradeProvider;
import sirttas.elementalcraft.datagen.managed.SourceTraitsProvider;
import sirttas.elementalcraft.datagen.managed.SpellPropertiesProvider;
import sirttas.elementalcraft.datagen.managed.ToolInfusionProvider;
import sirttas.elementalcraft.datagen.recipe.ECRecipeProvider;
import sirttas.elementalcraft.datagen.tag.ECBiomeTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECBlockTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECItemTagsProvider;
import sirttas.elementalcraft.datagen.world.ECBiomeModifierProvider;
import sirttas.elementalcraft.datagen.world.ECFeaturesProvider;
import sirttas.elementalcraft.datagen.world.ECStructureSetsProvider;
import sirttas.elementalcraft.datagen.world.ECStructuresProvider;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECDataGenerators {

	private ECDataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();
		var includeServer = event.includeServer();
		var includeClient = event.includeClient();

		ECBlockTagsProvider blockTagsProvider = new ECBlockTagsProvider(generator, fileHelper);
		ECItemModelProvider itemModelProvider = new ECItemModelProvider(generator, fileHelper);

		generator.addProvider(includeServer, new ECBlockLootProvider(generator));
		generator.addProvider(includeServer, new ECChestLootProvider(generator));
		generator.addProvider(includeServer, new ECInjectLootProvider(generator));
		generator.addProvider(includeServer, new ECEntityLootProvider(generator));
		generator.addProvider(includeClient, new ECBlockStateProvider(generator, fileHelper));
		generator.addProvider(includeServer && includeClient, new RunesProvider(generator, itemModelProvider));
		generator.addProvider(includeClient, itemModelProvider);
		generator.addProvider(includeServer, blockTagsProvider);
		generator.addProvider(includeServer, new ECItemTagsProvider(generator, blockTagsProvider, fileHelper));
		generator.addProvider(includeServer, new ECBiomeTagsProvider(generator, fileHelper));
		generator.addProvider(includeServer, new ECRecipeProvider(generator, fileHelper));
		generator.addProvider(includeServer, new ECAdvancementProvider(generator));
		generator.addProvider(includeServer, new ShrineUpgradeProvider(generator));
		generator.addProvider(includeServer, new SpellPropertiesProvider(generator));
		generator.addProvider(includeServer, new ToolInfusionProvider(generator));
		generator.addProvider(includeServer, new SourceTraitsProvider(generator));
		generator.addProvider(includeServer, new ShrinePropertiesProvider(generator));
		generator.addProvider(includeServer, new ECFeaturesProvider(generator, fileHelper));
		generator.addProvider(includeServer, new ECBiomeModifierProvider(generator, fileHelper));
		generator.addProvider(includeServer, new ECStructuresProvider(generator, fileHelper));
		generator.addProvider(includeServer, new ECStructureSetsProvider(generator, fileHelper));
//		if (ECinteractions.isSilentGearActive()) {
//			generator.addProvider(includeServer, new ECSilentGearMaterialProvider(generator));
//		}
	}
}
