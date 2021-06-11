package sirttas.elementalcraft.network.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

public class ServerProxy implements IProxy {

	private MinecraftServer server;
	
	@Override
	public void registerHandlers() {
		MinecraftForge.EVENT_BUS.addListener(this::setupServer);
	}

	@Override
	public World getDefaultWorld() {
		return server.overworld();
	}
	
	private void setupServer(FMLServerAboutToStartEvent event) {
		server = event.getServer();
	}
}
