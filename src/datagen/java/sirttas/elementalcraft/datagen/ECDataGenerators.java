package sirttas.elementalcraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.datagen.loot.ECBlockLootProvider;
import sirttas.elementalcraft.datagen.loot.ECChestLootProvider;
import sirttas.elementalcraft.datagen.loot.ECInjectLootProvider;
import sirttas.elementalcraft.datagen.managed.RunesProvider;
import sirttas.elementalcraft.datagen.managed.ShrineUpgradeProvider;
import sirttas.elementalcraft.datagen.managed.SpellPropertiesProvider;
import sirttas.elementalcraft.datagen.managed.ToolInfusionProvider;
import sirttas.elementalcraft.datagen.recipe.ECRecipeProvider;
import sirttas.elementalcraft.datagen.tag.ECBlockTagsProvider;
import sirttas.elementalcraft.datagen.tag.ECItemTagsProvider;
import sirttas.elementalcraft.datagen.tag.RuneTagsProvider;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECDataGenerators {

	private ECDataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();
		ExistingFileHelper fileHelper = evt.getExistingFileHelper();

		if (evt.includeServer()) {
			ECBlockTagsProvider blockTagsProvider = new ECBlockTagsProvider(generator, fileHelper);
			ECItemModelProvider itemModelProvider = new ECItemModelProvider(generator, fileHelper);

			generator.addProvider(new ECBlockLootProvider(generator));
			generator.addProvider(new ECChestLootProvider(generator));
			generator.addProvider(new ECInjectLootProvider(generator));
			generator.addProvider(new ECBlockStateProvider(generator, fileHelper));
			generator.addProvider(new RunesProvider(generator, itemModelProvider));
			generator.addProvider(itemModelProvider);
			generator.addProvider(blockTagsProvider);
			generator.addProvider(new ECItemTagsProvider(generator, blockTagsProvider, fileHelper));
			generator.addProvider(new RuneTagsProvider(generator, fileHelper));
			generator.addProvider(new ECRecipeProvider(generator, fileHelper));
			generator.addProvider(new ECAdvancementProvider(generator));
			generator.addProvider(new ShrineUpgradeProvider(generator));
			generator.addProvider(new SpellPropertiesProvider(generator));
			generator.addProvider(new ToolInfusionProvider(generator));
		}
	}

}
