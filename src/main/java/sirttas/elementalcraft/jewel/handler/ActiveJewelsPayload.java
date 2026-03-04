package sirttas.elementalcraft.jewel.handler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.network.payload.PayloadHelper;

import java.util.List;

public record ActiveJewelsPayload(List<Identifier> jewels) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ActiveJewelsPayload> TYPE = PayloadHelper.createType("active_jewels");
    public static final StreamCodec<FriendlyByteBuf, ActiveJewelsPayload> STREAM_CODEC = StreamCodec.of((b, p) -> p.write(b), ActiveJewelsPayload::new);

    public ActiveJewelsPayload(IJewelHandler jewelHandler) {
        this(jewelHandler.getActiveJewels().stream()
                .map(Jewel::getKey)
                .toList());
    }

    public ActiveJewelsPayload(FriendlyByteBuf buf) {
        this(buf.readList(FriendlyByteBuf::readIdentifier));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(jewels, FriendlyByteBuf::writeIdentifier);
    }

    @Override
    public @NotNull Type<ActiveJewelsPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext payloadContext) {
        payloadContext.enqueueWork(() -> {
            var player = payloadContext.player();

            if (player.getCapability(IJewelHandler.CAPABILITY) instanceof ClientJewelHandler handler) {
                handler.setActiveJewels(jewels.stream()
                        .map(Jewels.REGISTRY::get)
                        .toList());
            }
        });
    }
}
