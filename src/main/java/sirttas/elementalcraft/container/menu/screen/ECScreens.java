package sirttas.elementalcraft.container.menu.screen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.spelldesk.SpellDeskScreen;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassScreen;
import sirttas.elementalcraft.item.spell.book.SpellBookScreen;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECScreens {

	private ECScreens() {}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(ECMenus.SPELL_BOOK.get(), SpellBookScreen::new);
		event.register(ECMenus.SPELL_DESK.get(), SpellDeskScreen::new);
		event.register(ECMenus.SOURCE_ANALYSIS_GLASS.get(), SourceAnalysisGlassScreen::new);
	}
}
