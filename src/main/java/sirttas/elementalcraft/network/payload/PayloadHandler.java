package sirttas.elementalcraft.network.payload;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorListPayload;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexPullPlayerPayload;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassPayload;
import sirttas.elementalcraft.item.spell.book.SpellBookPayload;
import sirttas.elementalcraft.jewel.handler.ActiveJewelsPayload;
import sirttas.elementalcraft.spell.ChangeSpellPayload;
import sirttas.elementalcraft.spell.tick.SpellTickCooldownPayload;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PayloadHandler {

	private static final String PROTOCOL_VERSION = "1";

	private PayloadHandler() {}

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlerEvent event) {
		var registrar = event.registrar(ElementalCraftApi.MODID).versioned(PROTOCOL_VERSION);

		registrar.play(ChangeSpellPayload.ID, ChangeSpellPayload::new, ChangeSpellPayload::handle);
		registrar.play(SpellBookPayload.ID, SpellBookPayload::new, SpellBookPayload::handle);
		registrar.play(SpellTickCooldownPayload.ID, SpellTickCooldownPayload::new, SpellTickCooldownPayload::handle);
		registrar.play(SourceAnalysisGlassPayload.ID, SourceAnalysisGlassPayload::new, SourceAnalysisGlassPayload::handle);
		registrar.play(ActiveJewelsPayload.ID, ActiveJewelsPayload::new, ActiveJewelsPayload::handle);
		registrar.play(VortexPullPlayerPayload.ID, VortexPullPlayerPayload::new, VortexPullPlayerPayload::handle);
		registrar.play(TranslocationAnchorListPayload.ID, TranslocationAnchorListPayload::new, TranslocationAnchorListPayload::handle);
	}
}
