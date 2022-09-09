package sirttas.elementalcraft.client;

import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.vortex.VortexShrineUpgradeRenderer;
import sirttas.elementalcraft.block.source.ISourceRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.container.menu.screen.ECScreens;
import sirttas.elementalcraft.gui.GuiHandler;
import sirttas.elementalcraft.gui.tooltip.ElementGaugeClientTooltip;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.curios.CuriosConstants;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.spell.airshield.AirShieldSpellRenderer;

import java.util.function.Consumer;

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
	public static void registerModels(ModelEvent.RegisterAdditional event) {
		var addModel = addModel(event::register);

		Runes.registerModels(addModel);
		Jewels.registerModels(addModel);
		event.register(ElementPipeRenderer.SIDE_LOCATION);
		event.register(ElementPipeRenderer.EXTRACT_LOCATION);
		event.register(ElementPipeRenderer.PRIORITY_LOCATION);
		event.register(SolarSynthesizerRenderer.LENSE_LOCATION);
		event.register(AirMillGrindstoneRenderer.BLADES_LOCATION);
		event.register(DiffuserRenderer.CUBE_LOCATION);
		event.register(AccelerationShrineUpgradeRenderer.CLOCK_LOCATION);
		event.register(VortexShrineUpgradeRenderer.RING_LOCATION);
		event.register(SourceRenderer.STABILIZER_LOCATION);
	}

	@OnlyIn(Dist.CLIENT)
	private static Consumer<ResourceLocation> addModel(Consumer<ResourceLocation> consumer) {
		return m -> {
			String path = StringUtils.removeStart(StringUtils.removeEnd(m.getPath(), ".json"), "models/item/");

			consumer.accept(new ModelResourceLocation(new ResourceLocation(m.getNamespace(), path), "inventory"));
		};
	}

	@SubscribeEvent
	public static void stitchTextures(TextureStitchEvent.Pre event) {
		addSprite(event, SolarSynthesizerRenderer.BEAM);
		addSprite(event, AirShieldSpellRenderer.BACKGROUND);
		addSprite(event, AirShieldSpellRenderer.BLADE);
		addSprite(event, SourceDisplacementPlateRenderer.SOURCE_DISPLACEMENT);
		addSprite(event, SourceDisplacementPlateRenderer.CIRCLE);
		addSprite(event, ISourceRenderer.OUTER);
		addSprite(event, ISourceRenderer.MIDDLE);
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
