package sirttas.elementalcraft.inventory.container.screen;

import net.minecraft.client.gui.ScreenManager;
import sirttas.elementalcraft.inventory.container.ECContainers;
import sirttas.elementalcraft.item.spell.book.SpellBookScreen;

public class ECScreens {

	private ECScreens() {}
	
	public static void initScreenFactories() {
		ScreenManager.registerFactory(ECContainers.SPELL_BOOK, SpellBookScreen::new);
	}
}
