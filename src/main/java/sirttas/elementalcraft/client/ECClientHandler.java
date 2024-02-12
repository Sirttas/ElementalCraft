package sirttas.elementalcraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;
import sirttas.elementalcraft.container.menu.screen.ECScreens;
import sirttas.elementalcraft.gui.tooltip.ElementGaugeClientTooltip;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECClientHandler {

	private ECClientHandler() {}
	
	@SubscribeEvent
	public static void setupClient(FMLClientSetupEvent event) {
		ECScreens.initScreenFactories();
	}

	@SubscribeEvent
	public static void registerTooltipImages(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ElementGaugeTooltip.class, ElementGaugeClientTooltip::new);
		event.register(AbstractElementContainerBlock.Tooltip.class, AbstractElementContainerBlock.ClientTooltip::new);
	}
	
}
