package sirttas.elementalcraft.block.shrine.upgrade.unidirectional.vortex;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record VortexPullPlayerMessage(
        Vec3 target,
        double speed
) {

    public static VortexPullPlayerMessage decode(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double speed = buf.readDouble();

        return new VortexPullPlayerMessage(new Vec3(x, y, z), speed);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(target.x);
        buf.writeDouble(target.y);
        buf.writeDouble(target.z);
        buf.writeDouble(speed);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                var player = Minecraft.getInstance().player;

                if (player != null) {
                    player.setDeltaMovement(target.subtract(player.position()).normalize().multiply(speed, speed, speed));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
