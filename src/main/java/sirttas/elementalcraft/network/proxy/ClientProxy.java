package sirttas.elementalcraft.network.proxy;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.block.tile.renderer.ECRenderers;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.pureore.PureOreHelper;
import sirttas.elementalcraft.spell.SpellTickManager;

public class ClientProxy implements IProxy {

	@Override
	public void registerHandlers() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ECItems::registerItemColors);

		MinecraftForge.EVENT_BUS.addListener(this::clientLoggin);
		MinecraftForge.EVENT_BUS.addListener(SpellTickManager::clientTick);
	}

	private void setupClient(FMLClientSetupEvent event) {
		ECRenderers.initRenderLayouts();
		ECEntities.registerRenderers();
	}

	private void clientLoggin(ClientPlayerNetworkEvent.LoggedInEvent event) {
		PureOreHelper.generatePureOres(event.getPlayer().getEntityWorld().getRecipeManager());
	}
}
