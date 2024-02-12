package sirttas.elementalcraft.block.shrine.upgrade.vortex;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;

public record VortexPullPlayerPayload(
        Vec3 target,
        double speed
) implements CustomPacketPayload {

    public static final ResourceLocation ID = ElementalCraftApi.createRL("vortex_pull_player");

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
    @Nonnull
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> ctx.player()
                .ifPresent(player -> player.setDeltaMovement(target.subtract(player.position()).normalize().multiply(speed, speed, speed))));
    }
}
