package sirttas.elementalcraft.network.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;

public class ServerProxy implements IProxy {

	private MinecraftServer server;
	
	@Override
	public void registerHandlers() {
		MinecraftForge.EVENT_BUS.addListener(this::setupServer);
	}

	@Override
	public Level getDefaultWorld() {
		return server.overworld();
	}
	
	private void setupServer(FMLServerAboutToStartEvent event) {
		server = event.getServer();
	}
}
