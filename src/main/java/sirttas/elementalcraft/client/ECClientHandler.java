package sirttas.elementalcraft.client;

import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.entity.renderer.ECRenderers;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.vortex.VortexShrineUpgradeRenderer;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.container.menu.screen.ECScreens;
import sirttas.elementalcraft.gui.tooltip.ElementGaugeClientTooltip;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.rune.Runes;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECClientHandler {

	private ECClientHandler() {}
	
	@SubscribeEvent
	public static void setupClient(FMLClientSetupEvent event) {
		ECRenderers.initRenderLayouts();
		ECScreens.initScreenFactories();
		registerTooltipImages();
	}

	private static void registerTooltipImages() {
		MinecraftForgeClient.registerTooltipComponentFactory(ElementGaugeTooltip.class, ElementGaugeClientTooltip::new);
		MinecraftForgeClient.registerTooltipComponentFactory(AbstractElementContainerBlock.Tooltip.class, AbstractElementContainerBlock.ClientTooltip::new);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		Runes.registerModels(ECClientHandler::addModel);
		Jewels.registerModels(ECClientHandler::addModel);
		ForgeModelBakery.addSpecialModel(ElementPipeRenderer.SIDE_LOCATION);
		ForgeModelBakery.addSpecialModel(ElementPipeRenderer.EXTRACT_LOCATION);
		ForgeModelBakery.addSpecialModel(ElementPipeRenderer.PRIORITY_LOCATION);
		ForgeModelBakery.addSpecialModel(SolarSynthesizerRenderer.LENSE_LOCATION);
		ForgeModelBakery.addSpecialModel(AirMillRenderer.BLADES_LOCATION);
		ForgeModelBakery.addSpecialModel(DiffuserRenderer.CUBE_LOCATION);
		ForgeModelBakery.addSpecialModel(AccelerationShrineUpgradeRenderer.CLOCK_LOCATION);
		ForgeModelBakery.addSpecialModel(VortexShrineUpgradeRenderer.RING_LOCATION);
		ForgeModelBakery.addSpecialModel(SourceRenderer.STABILIZER_LOCATION);
	}

	@OnlyIn(Dist.CLIENT)
	private static void addModel(ResourceLocation model) {
		String path = StringUtils.removeStart(StringUtils.removeEnd(model.getPath(), ".json"), "models/item/");

		ForgeModelBakery.addSpecialModel(new ModelResourceLocation(new ResourceLocation(model.getNamespace(), path), "inventory"));
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
