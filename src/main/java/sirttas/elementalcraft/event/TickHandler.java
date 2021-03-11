package sirttas.elementalcraft.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class TickHandler {
	
	private static long ticksInGame = 0;
	
	private TickHandler() {}
	
	@SubscribeEvent
	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isGamePaused()) {
			ticksInGame++;
		}
	}

	public static long getTicksInGame() {
		return ticksInGame;
	}
}
