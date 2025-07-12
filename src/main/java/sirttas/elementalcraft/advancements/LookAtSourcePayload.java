package sirttas.elementalcraft.advancements;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.network.payload.PayloadHelper;
import sirttas.elementalcraft.tag.ECTags;

public record LookAtSourcePayload(
        BlockHitResult hitResult
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LookAtSourcePayload> TYPE = PayloadHelper.createType("look_at_source");
    public static final StreamCodec<FriendlyByteBuf, LookAtSourcePayload> STREAM_CODEC = StreamCodec.of((b, p) -> p.write(b), LookAtSourcePayload::new);

    public LookAtSourcePayload(FriendlyByteBuf buf) {
        this(buf.readBlockHitResult());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockHitResult(hitResult);
    }

    @Override
    public @NotNull Type<LookAtSourcePayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext payloadContext) {
        payloadContext.enqueueWork(() -> {
            if (payloadContext.player() instanceof ServerPlayer player) {
                var level = player.level();
                var state = level.getBlockState(hitResult.getBlockPos());

                if (state.is(ECTags.Blocks.SOURCES)) {
                    ECCriteriaTriggers.LOOK_AT_SOURCE.get().trigger(player);
                    player.setData(ECDataAttachments.HAS_SEEN_SOURCE, true);
                }
            }
        });
    }
}
