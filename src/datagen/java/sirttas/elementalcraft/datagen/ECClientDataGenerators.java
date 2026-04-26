package sirttas.elementalcraft.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.datagen.interaction.DatagenInteraction;
import sirttas.elementalcraft.datagen.language.ECEnglishLanguageProvider;
import sirttas.elementalcraft.datagen.language.ECFrenchLanguageProvider;
import sirttas.elementalcraft.datagen.model.BuddingShrinePlateModelGenerator;
import sirttas.elementalcraft.datagen.model.ECBlockStateModelGenerator;
import sirttas.elementalcraft.datagen.model.ECItemModelGenerator;
import sirttas.elementalcraft.datagen.model.ECModelProvider;
import sirttas.elementalcraft.jewel.Jewels;

import java.util.List;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ECClientDataGenerators {

	private ECClientDataGenerators() {}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		PipeUpgradeTypes.setup();
		Jewels.setup();

		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var lookupProvider = event.getLookupProvider();

		event.addProvider(new ECEnglishLanguageProvider(output));
		event.addProvider(new ECFrenchLanguageProvider(output));
		event.addProvider(new ECSpriteSourceProvider(output, lookupProvider));
		event.addProvider(new ECModelProvider(output, List.of(
				ECBlockStateModelGenerator.FACTORY,
				ECItemModelGenerator.FACTORY,
				BuddingShrinePlateModelGenerator.FACTORY)));

		DatagenInteraction.get().getClientProviders(output, lookupProvider).forEach(event::addProvider);
	}
}
