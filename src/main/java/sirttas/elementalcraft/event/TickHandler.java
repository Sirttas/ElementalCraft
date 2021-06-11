package sirttas.elementalcraft.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class TickHandler {
	
	private static long ticksInGame = 0;
	
	private TickHandler() {}
	
	@SubscribeEvent
	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isPaused()) {
			ticksInGame++;
		}
	}

	public static long getTicksInGame() {
		return ticksInGame;
	}
}
