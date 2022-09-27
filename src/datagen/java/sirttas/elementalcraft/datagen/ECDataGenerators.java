package sirttas.elementalcraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.datagen.interaction.ECSilentGearMaterialProvider;
import sirttas.elementalcraft.datagen.loot.ECLootTableProvider;
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
import sirttas.elementalcraft.interaction.ECinteractions;

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

			generator.addProvider(new ECLootTableProvider(generator));
			generator.addProvider(new ECBlockStateProvider(generator, fileHelper));
			generator.addProvider(new RunesProvider(generator, itemModelProvider));
			generator.addProvider(itemModelProvider);
			generator.addProvider(blockTagsProvider);
			generator.addProvider(new ECItemTagsProvider(generator, blockTagsProvider, fileHelper));
			generator.addProvider(new ECBiomeTagsProvider(generator, fileHelper));
			generator.addProvider(new ECRecipeProvider(generator, fileHelper));
			generator.addProvider(new ECAdvancementProvider(generator));
			generator.addProvider(new ShrineUpgradeProvider(generator));
			generator.addProvider(new SpellPropertiesProvider(generator));
			generator.addProvider(new ToolInfusionProvider(generator));
			generator.addProvider(new SourceTraitsProvider(generator));
			generator.addProvider(new ShrinePropertiesProvider(generator));
			if (ECinteractions.isSilentGearActive()) {
				generator.addProvider(new ECSilentGearMaterialProvider(generator));
			}
		}
	}

}
