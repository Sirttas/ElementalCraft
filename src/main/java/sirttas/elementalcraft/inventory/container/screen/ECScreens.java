package sirttas.elementalcraft.inventory.container.screen;

import net.minecraft.client.gui.ScreenManager;
import sirttas.elementalcraft.inventory.container.ECContainers;
import sirttas.elementalcraft.item.spell.book.SpellBookScreen;

public class ECScreens {

	public static void initScreenFactoriess() {
		ScreenManager.registerFactory(ECContainers.spellBook, SpellBookScreen::new);
	}
}
