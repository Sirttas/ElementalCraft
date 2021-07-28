package sirttas.elementalcraft.network.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IProxy {
	default void registerHandlers() {}

	Level getDefaultWorld();
	
	default Player getDefaultPlayer() {
		return null;
	}
}
