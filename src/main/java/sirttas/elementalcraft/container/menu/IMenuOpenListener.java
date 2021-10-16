package sirttas.elementalcraft.container.menu;

import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface IMenuOpenListener {
	
	void onOpen(Player player);
	
}
