package sirttas.elementalcraft.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class TickHandler {
	
	private static int ticksInGame = 0;
	
	@SubscribeEvent
	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && !Minecraft.getInstance().isGamePaused()) {
			ticksInGame++;
		}
	}

	public static int getTicksInGame() {
		return ticksInGame;
	}
}
