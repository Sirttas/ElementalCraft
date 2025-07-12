package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.network.payload.IMenuPayload;
import sirttas.elementalcraft.network.payload.PayloadHelper;

import java.util.Map;

public record SourceAnalysisGlassPayload(
		Map<Holder<SourceTrait>, ISourceTraitValue> traits
) implements IMenuPayload<SourceAnalysisGlassMenu> {

	public static final CustomPacketPayload.Type<SourceAnalysisGlassPayload> TYPE = PayloadHelper.createType("source_analysis_glass");
	public static final StreamCodec<FriendlyByteBuf, SourceAnalysisGlassPayload> STREAM_CODEC = StreamCodec.of((b, p) -> p.write(b), SourceAnalysisGlassPayload::new);

	public SourceAnalysisGlassPayload(FriendlyByteBuf buf) {
		this(SourceTraitHelper.loadTraits(buf.readNbt()));
	}

	@Override
	public Class<? extends SourceAnalysisGlassMenu> getMenuType() {
		return SourceAnalysisGlassMenu.class;
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeNbt(SourceTraitHelper.saveTraits(traits));

	}

	@Override
	public @NotNull Type<SourceAnalysisGlassPayload> type() {
		return TYPE;
	}

	@Override
	public void handleOnMenu(IPayloadContext payloadContext, SourceAnalysisGlassMenu menu) {
		menu.setTraits(traits);
	}
}
