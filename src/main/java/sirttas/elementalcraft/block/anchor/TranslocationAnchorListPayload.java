package sirttas.elementalcraft.block.anchor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.network.payload.PayloadHelper;

import java.util.Collections;
import java.util.List;

public record TranslocationAnchorListPayload(List<BlockPos> list) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<@NotNull TranslocationAnchorListPayload> TYPE = PayloadHelper.createType("translocation_anchor_list");
    public static final StreamCodec<@NotNull FriendlyByteBuf, @NotNull TranslocationAnchorListPayload> STREAM_CODEC = StreamCodec.of((b, p) -> p.write(b), TranslocationAnchorListPayload::new);

    public TranslocationAnchorListPayload(FriendlyByteBuf buf) {
        this(buf.readList(b -> BlockPos.of(b.readLong())));
    }

    public static TranslocationAnchorListPayload create(Level level) {
        var anchors = TranslocationAnchors.get(level);

        if (anchors != null) {
            return new TranslocationAnchorListPayload(List.copyOf(anchors.getAnchors()));
        }
        return new TranslocationAnchorListPayload(Collections.emptyList());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(list, (b, p) -> b.writeLong(p.asLong()));
    }

    @Override
    public @NotNull Type<@NotNull TranslocationAnchorListPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext payloadContext) {
        payloadContext.enqueueWork(() -> {
            TranslocationAnchors.CLIENT_SET.clear();
            TranslocationAnchors.CLIENT_SET.addAll(list);
        });
    }
}
