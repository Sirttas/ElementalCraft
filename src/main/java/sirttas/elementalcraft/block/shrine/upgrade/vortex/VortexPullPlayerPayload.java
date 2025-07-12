package sirttas.elementalcraft.block.shrine.upgrade.vortex;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.network.payload.PayloadHelper;

public record VortexPullPlayerPayload(
        Vec3 target,
        double speed
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<VortexPullPlayerPayload> TYPE = PayloadHelper.createType("vortex_pull_player");
    public static final StreamCodec<FriendlyByteBuf, VortexPullPlayerPayload> STREAM_CODEC = StreamCodec.of((b, p) -> p.write(b), VortexPullPlayerPayload::new);

    public VortexPullPlayerPayload(FriendlyByteBuf buf) {
        this(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readDouble());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(target.x);
        buf.writeDouble(target.y);
        buf.writeDouble(target.z);
        buf.writeDouble(speed);
    }

    @Override
    public @NotNull Type<VortexPullPlayerPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext payloadContext) {
        payloadContext.enqueueWork(() -> {
            var player = payloadContext.player();

           player.setDeltaMovement(target.subtract(player.position()).normalize().multiply(speed, speed, speed));
        });
    }
}
