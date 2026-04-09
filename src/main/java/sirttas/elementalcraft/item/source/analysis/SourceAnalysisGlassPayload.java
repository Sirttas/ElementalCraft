package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.network.payload.IMenuPayload;
import sirttas.elementalcraft.network.payload.PayloadHelper;

import java.util.Map;

public record SourceAnalysisGlassPayload(
		Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits
) implements IMenuPayload<SourceAnalysisGlassMenu> {

	public static final CustomPacketPayload.Type<@NotNull SourceAnalysisGlassPayload> TYPE = PayloadHelper.createType("source_analysis_glass");
	public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SourceAnalysisGlassPayload> STREAM_CODEC = StreamCodec.composite(SourceTrait.VALUE_MAP_STREAM_CODEC, p -> p.traits, SourceAnalysisGlassPayload::new);

	@Override
	public Class<? extends SourceAnalysisGlassMenu> getMenuType() {
		return SourceAnalysisGlassMenu.class;
	}

	@Override
	public @NotNull Type<@NotNull SourceAnalysisGlassPayload> type() {
		return TYPE;
	}

	@Override
	public void handleOnMenu(IPayloadContext payloadContext, SourceAnalysisGlassMenu menu) {
		menu.setTraits(traits);
	}
}
