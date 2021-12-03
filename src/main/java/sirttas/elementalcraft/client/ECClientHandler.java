package sirttas.elementalcraft.client;

import net.minecraft.client.resources.model.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.entity.renderer.ECRenderers;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.container.menu.screen.ECScreens;
import sirttas.elementalcraft.rune.Runes;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECClientHandler {

	private ECClientHandler() {}
	
	@SubscribeEvent
	public static void setupClient(FMLClientSetupEvent event) {
		ECRenderers.initRenderLayouts();
		ECScreens.initScreenFactories();
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		Runes.registerModels();
		ForgeModelBakery.addSpecialModel(ElementPipeRenderer.SIDE_LOCATION);
		ForgeModelBakery.addSpecialModel(ElementPipeRenderer.EXTRACT_LOCATION);
		ForgeModelBakery.addSpecialModel(ElementPipeRenderer.PRIORITY_LOCATION);
		ForgeModelBakery.addSpecialModel(SolarSynthesizerRenderer.LENSE_LOCATION);
		ForgeModelBakery.addSpecialModel(AirMillRenderer.BLADES_LOCATION);
		ForgeModelBakery.addSpecialModel(DiffuserRenderer.CUBE_LOCATION);
		ForgeModelBakery.addSpecialModel(AccelerationShrineUpgradeRenderer.CLOCK_LOCATION);
		ForgeModelBakery.addSpecialModel(SourceRenderer.STABILIZER_LOCATION);
	}
	
	@SubscribeEvent
	public static void stitchTextures(TextureStitchEvent.Pre event) {
		addSprite(event, SolarSynthesizerRenderer.BEAM);
	}
	
	private static void addSprite(TextureStitchEvent.Pre event, Material sprite) {
		if (event.getAtlas().location().equals(sprite.atlasLocation())) {
			event.addSprite(sprite.texture());
		}
	}
	
}
