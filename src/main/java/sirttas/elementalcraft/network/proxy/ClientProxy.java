package sirttas.elementalcraft.network.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.entity.renderer.ECRenderers;
import sirttas.elementalcraft.block.instrument.mill.AirMillRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerRenderer;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.inventory.container.screen.ECScreens;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.particle.ECParticles;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.spell.SpellTickManager;

public class ClientProxy implements IProxy {

	private final Minecraft minecraft;
	
	public ClientProxy() {
		minecraft = Minecraft.getInstance();
	}
	
	@Override
	public void registerHandlers() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::setupClient);
		modBus.addListener(ECItems::replaceModels);
		modBus.addListener(this::registerModels);
		modBus.addListener(this::stitchTextures);
		modBus.addListener(ECItems::registerItemColors);
		modBus.addListener(ECParticles::registerFactories);

		MinecraftForge.EVENT_BUS.addListener(SpellTickManager::clientTick);
	}


	@Override
	public Level getDefaultWorld() {
		return minecraft.level;
	}
	
	@Override
	public Player getDefaultPlayer() {
		return minecraft.player;
	}
	
	private void setupClient(FMLClientSetupEvent event) {
		ECRenderers.initRenderLayouts();
		ECEntities.registerRenderers();
		ECScreens.initScreenFactories();
	}

	public void registerModels(ModelRegistryEvent event) {
		Runes.registerModels();
		ModelLoader.addSpecialModel(ElementPipeRenderer.SIDE_LOCATION);
		ModelLoader.addSpecialModel(ElementPipeRenderer.EXTRACT_LOCATION);
		ModelLoader.addSpecialModel(ElementPipeRenderer.PRIORITY_LOCATION);
		ModelLoader.addSpecialModel(SolarSynthesizerRenderer.LENSE_LOCATION);
		ModelLoader.addSpecialModel(AirMillRenderer.BLADES_LOCATION);
		ModelLoader.addSpecialModel(DiffuserRenderer.CUBE_LOCATION);
		ModelLoader.addSpecialModel(AccelerationShrineUpgradeRenderer.CLOCK_LOCATION);
	}
	
	public void stitchTextures(TextureStitchEvent.Pre event) {
		addSprite(event, SolarSynthesizerRenderer.BEAM);
	}
	
	private void addSprite(TextureStitchEvent.Pre event, Material sprite) {
		if (event.getMap().location().equals(sprite.atlasLocation())) {
			event.addSprite(sprite.texture());
		}
	}
}

