package sirttas.elementalcraft.network.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {
	default void registerHandlers() {}

	World getDefaultWorld();
	
	default PlayerEntity getDefaultPlayer() {
		return null;
	}
}
