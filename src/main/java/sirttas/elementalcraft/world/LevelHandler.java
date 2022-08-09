package sirttas.elementalcraft.world;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class LevelHandler {

	private LevelHandler() {}
	
	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Level> event) {
		event.addCapability(ElementalCraft.createRL(ECNames.ELEMENT_STORAGE), LevelElementStorage.createProvider());
	}
}
