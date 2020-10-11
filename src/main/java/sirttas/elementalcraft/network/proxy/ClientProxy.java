package sirttas.elementalcraft.network.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.block.tile.renderer.ECRenderers;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.particle.ECParticles;
import sirttas.elementalcraft.spell.SpellTickManager;

public class ClientProxy implements IProxy {

	@Override
	public void registerHandlers() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::setupClient);
		modBus.addListener(ECItems::registerItemColors);
		modBus.addListener(ECParticles::registerFactories);

		MinecraftForge.EVENT_BUS.addListener(SpellTickManager::clientTick);
	}

	private void setupClient(FMLClientSetupEvent event) {
		ECRenderers.initRenderLayouts();
		ECEntities.registerRenderers();
	}
}
