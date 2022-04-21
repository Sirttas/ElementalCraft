package sirttas.elementalcraft.infusion.tool;

import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ToolInfusionHandler {

	private ToolInfusionHandler() {}

	@SubscribeEvent
	public static void addInfusionAttributes(ItemAttributeModifierEvent event) {
		ToolInfusionHelper.getInfusionAttribute(event.getItemStack(), event.getSlotType()).forEach(event::addModifier);
	}

}
