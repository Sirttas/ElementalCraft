package sirttas.elementalcraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.container.ElementContainerBlockItem;
import sirttas.elementalcraft.gui.tooltip.ElementGaugeClientTooltip;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ECClientHandler {

	private ECClientHandler() {}

	@SubscribeEvent
	public static void registerTooltipImages(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ElementGaugeTooltip.class, ElementGaugeClientTooltip::new);
		event.register(ElementContainerBlockItem.Tooltip.class, ElementContainerBlockItem.ClientTooltip::new);
	}
	
}
