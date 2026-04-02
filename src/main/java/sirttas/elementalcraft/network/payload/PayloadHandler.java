package sirttas.elementalcraft.network.payload;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import sirttas.elementalcraft.advancements.LookAtSourcePayload;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorListPayload;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexPullPlayerPayload;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassPayload;
import sirttas.elementalcraft.item.spell.book.SpellBookPayload;
import sirttas.elementalcraft.jewel.handler.ActiveJewelsPayload;
import sirttas.elementalcraft.pureore.PureOreSyncPayload;
import sirttas.elementalcraft.spell.ChangeSpellPayload;
import sirttas.elementalcraft.spell.tick.SpellTickCooldownPayload;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class PayloadHandler {

	private static final String PROTOCOL_VERSION = "3";

	private PayloadHandler() {}

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		var registrar = event.registrar(ElementalCraftApi.MODID).versioned(PROTOCOL_VERSION);

		registrar.playToClient(SpellBookPayload.TYPE, SpellBookPayload.STREAM_CODEC, SpellBookPayload::handle);
		registrar.playToClient(SpellTickCooldownPayload.TYPE, SpellTickCooldownPayload.STREAM_CODEC, SpellTickCooldownPayload::handle);
		registrar.playToClient(SourceAnalysisGlassPayload.TYPE, SourceAnalysisGlassPayload.STREAM_CODEC, SourceAnalysisGlassPayload::handle);
		registrar.playToClient(ActiveJewelsPayload.TYPE, ActiveJewelsPayload.STREAM_CODEC, ActiveJewelsPayload::handle);
		registrar.playToClient(VortexPullPlayerPayload.TYPE, VortexPullPlayerPayload.STREAM_CODEC, VortexPullPlayerPayload::handle);
		registrar.playToClient(TranslocationAnchorListPayload.TYPE, TranslocationAnchorListPayload.STREAM_CODEC, TranslocationAnchorListPayload::handle);
		registrar.playToClient(PureOreSyncPayload.TYPE, PureOreSyncPayload.STREAM_CODEC, PureOreSyncPayload::handle);

		registrar.playToServer(ChangeSpellPayload.TYPE, ChangeSpellPayload.STREAM_CODEC, ChangeSpellPayload::handle);
		registrar.playToServer(LookAtSourcePayload.TYPE, LookAtSourcePayload.STREAM_CODEC, LookAtSourcePayload::handle);
	}
}
