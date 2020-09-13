package sirttas.elementalcraft.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.data.loot.ECBlockLootProvider;
import sirttas.elementalcraft.data.loot.ECChestLootProvider;
import sirttas.elementalcraft.data.recipe.ECRecipeProvider;
import sirttas.elementalcraft.data.tag.ECBlockTagsProvider;
import sirttas.elementalcraft.data.tag.ECItemTagsProvider;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECDataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		if (evt.includeServer()) {
			evt.getGenerator().addProvider(new ECBlockLootProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new ECChestLootProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new ECBlockStateProvider(evt.getGenerator(), evt.getExistingFileHelper()));
			evt.getGenerator().addProvider(new ECItemModelProvider(evt.getGenerator(), evt.getExistingFileHelper()));
			evt.getGenerator().addProvider(new ECBlockTagsProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new ECItemTagsProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new ECRecipeProvider(evt.getGenerator(), evt.getExistingFileHelper()));
			evt.getGenerator().addProvider(new ECAdvancementProvider(evt.getGenerator()));
		}
	}

}
