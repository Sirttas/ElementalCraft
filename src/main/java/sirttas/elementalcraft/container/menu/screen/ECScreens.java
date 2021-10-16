package sirttas.elementalcraft.container.menu.screen;

import net.minecraft.client.gui.screens.MenuScreens;
import sirttas.elementalcraft.block.spelldesk.SpellDeskScreen;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassScreen;
import sirttas.elementalcraft.item.spell.book.SpellBookScreen;

public class ECScreens {

	private ECScreens() {}
	
	public static void initScreenFactories() {
		MenuScreens.register(ECMenus.SPELL_BOOK, SpellBookScreen::new);
		MenuScreens.register(ECMenus.SPELL_DESK, SpellDeskScreen::new);
		MenuScreens.register(ECMenus.SOURCE_ANALYSIS_GLASS, SourceAnalysisGlassScreen::new);
	}
}
