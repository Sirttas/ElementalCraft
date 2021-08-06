package sirttas.elementalcraft.client;

import net.minecraft.client.resources.model.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.entity.renderer.ECRenderers;
import sirttas.elementalcraft.block.instrument.mill.AirMillRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerRenderer;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.inventory.container.screen.ECScreens;
import sirttas.elementalcraft.rune.Runes;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ECClientHandler {

	private ECClientHandler() {}
	
	@SubscribeEvent
	public static void setupClient(FMLClientSetupEvent event) {
		ECRenderers.initRenderLayouts();
		ECEntities.registerRenderers();
		ECScreens.initScreenFactories();
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		Runes.registerModels();
		ModelLoader.addSpecialModel(ElementPipeRenderer.SIDE_LOCATION);
		ModelLoader.addSpecialModel(ElementPipeRenderer.EXTRACT_LOCATION);
		ModelLoader.addSpecialModel(ElementPipeRenderer.PRIORITY_LOCATION);
		ModelLoader.addSpecialModel(SolarSynthesizerRenderer.LENSE_LOCATION);
		ModelLoader.addSpecialModel(AirMillRenderer.BLADES_LOCATION);
		ModelLoader.addSpecialModel(DiffuserRenderer.CUBE_LOCATION);
		ModelLoader.addSpecialModel(AccelerationShrineUpgradeRenderer.CLOCK_LOCATION);
	}
	
	@SubscribeEvent
	public static void stitchTextures(TextureStitchEvent.Pre event) {
		addSprite(event, SolarSynthesizerRenderer.BEAM);
	}
	
	private static void addSprite(TextureStitchEvent.Pre event, Material sprite) {
		if (event.getMap().location().equals(sprite.atlasLocation())) {
			event.addSprite(sprite.texture());
		}
	}
	
}
