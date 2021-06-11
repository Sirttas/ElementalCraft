package sirttas.elementalcraft.world;

import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class WorldHandler {

	private WorldHandler() {}
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<World> event) {
		event.addCapability(ElementalCraft.createRL(ECNames.ELEMENT_STORAGE), WorldElementStorage.createProvider());
	}
}
