package sirttas.elementalcraft.network.proxy;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.block.tile.renderer.ECRenderers;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.item.pureore.PureOreHelper;

public class ClientProxy implements IProxy {

	@Override
	public void registerHandlers() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		MinecraftForge.EVENT_BUS.addListener(this::clientLoggin);
	}

	private void setupClient(FMLClientSetupEvent event) {
		ECRenderers.initRenderLayouts();
		ECEntities.registerRenderers();
	}

	private void clientLoggin(ClientPlayerNetworkEvent.LoggedInEvent event) {
		PureOreHelper.generatePureOres(event.getPlayer().getEntityWorld().getRecipeManager());
	}
}
