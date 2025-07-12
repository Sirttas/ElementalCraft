package sirttas.elementalcraft.event;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class TickHandler {
	
	private static long ticksInGame = 0;
	
	private TickHandler() {}
	
	@SubscribeEvent
	public static void clientTickEnd(ClientTickEvent.Post event) {
		if (!Minecraft.getInstance().isPaused()) {
			ticksInGame++;
		}
	}

	public static long getTicksInGame() {
		return ticksInGame;
	}
}
