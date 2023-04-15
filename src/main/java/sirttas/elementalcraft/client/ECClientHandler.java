package sirttas.elementalcraft.client;

import net.minecraft.client.resources.model.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;
import sirttas.elementalcraft.block.source.SourceRendererHelper;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.container.menu.screen.ECScreens;
import sirttas.elementalcraft.gui.GuiHandler;
import sirttas.elementalcraft.gui.tooltip.ElementGaugeClientTooltip;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.curios.CuriosConstants;
import sirttas.elementalcraft.spell.airshield.AirShieldSpellRenderer;

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

	@SubscribeEvent
	public static void stitchTextures(TextureStitchEvent.Pre event) {
		addSprite(event, SolarSynthesizerRenderer.BEAM);
		addSprite(event, AirShieldSpellRenderer.BACKGROUND);
		addSprite(event, AirShieldSpellRenderer.BLADE);
		addSprite(event, SourceDisplacementPlateRenderer.SOURCE_DISPLACEMENT);
		addSprite(event, SourceDisplacementPlateRenderer.CIRCLE);
		addSprite(event, SourceRendererHelper.OUTER);
		addSprite(event, SourceRendererHelper.MIDDLE);
		addSprite(event, GuiHandler.TRANSLOCATION_ANCHOR_MARKER);
		if (ECinteractions.isCuriosActive()) {
			event.addSprite(CuriosConstants.EMPTY_ELEMENT_HOLDER_SLOT);
		}
	}
	
	private static void addSprite(TextureStitchEvent.Pre event, Material sprite) {
		if (event.getAtlas().location().equals(sprite.atlasLocation())) {
			event.addSprite(sprite.texture());
		}
	}
	
}
