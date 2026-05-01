package sirttas.elementalcraft.gui;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.tick.SpellCooldownItemDecorator;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class GuiHandler {

    private static final Identifier GAUGE_LAYER = ElementalCraftApi.createRL("gauge");
    private static final Identifier TRANSLOCATION_ANCHOR_MARKER_LAYER = ElementalCraftApi.createRL("translocation_anchor_marker");
    private static final Identifier SINGLE_TRANSLOCATION_ANCHOR_MARKER_LAYER = ElementalCraftApi.createRL("single_translocation_anchor_marker");

	private GuiHandler() {}

	@SubscribeEvent
	public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
		var spellCooldown = new SpellCooldownItemDecorator();

		event.register(ECItems.SCROLL.get(), spellCooldown);
		event.register(ECItems.FOCUS.get(), spellCooldown);
		event.register(ECItems.STAFF.get(), spellCooldown);
	}

	@SubscribeEvent
	public static void onDrawScreenPost(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, GAUGE_LAYER, ElementGaugeGui::drawGauge);
		event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, TRANSLOCATION_ANCHOR_MARKER_LAYER, TranslocationAnchorGui::drawAnchors);
		event.registerBelow(TRANSLOCATION_ANCHOR_MARKER_LAYER, SINGLE_TRANSLOCATION_ANCHOR_MARKER_LAYER, TranslocationAnchorGui::drawAnchor);
	}
}
