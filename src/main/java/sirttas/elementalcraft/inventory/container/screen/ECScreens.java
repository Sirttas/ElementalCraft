package sirttas.elementalcraft.inventory.container.screen;

import net.minecraft.client.gui.screens.MenuScreens;
import sirttas.elementalcraft.block.spelldesk.SpellDeskScreen;
import sirttas.elementalcraft.inventory.container.ECContainers;
import sirttas.elementalcraft.item.spell.book.SpellBookScreen;

public class ECScreens {

	private ECScreens() {}
	
	public static void initScreenFactories() {
		MenuScreens.register(ECContainers.SPELL_BOOK, SpellBookScreen::new);
		MenuScreens.register(ECContainers.SPELL_DESK, SpellDeskScreen::new);
	}
}
