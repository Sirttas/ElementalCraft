package sirttas.elementalcraft.jewel.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import sirttas.elementalcraft.jewel.Jewels;

import java.util.List;
import java.util.function.Supplier;

public record ActiveJewelsMessage(List<ResourceLocation> jewels) {

    public static ActiveJewelsMessage create(IJewelHandler jewelHandler) {
        return new ActiveJewelsMessage(jewelHandler.getActiveJewels().stream()
                .map(ForgeRegistryEntry::getRegistryName)
                .toList());
    }

    public static ActiveJewelsMessage decode(FriendlyByteBuf buf) {
        return new ActiveJewelsMessage(buf.readList(FriendlyByteBuf::readResourceLocation));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(jewels, FriendlyByteBuf::writeResourceLocation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                var player = Minecraft.getInstance().player;

                if (player != null) {
                    player.getCapability(IJewelHandler.JEWEL_HANDLER_CAPABILITY)
                            .filter(ClientJewelHandler.class::isInstance)
                            .map(ClientJewelHandler.class::cast)
                            .ifPresent(handler -> handler.setActiveJewels(jewels.stream()
                                    .map(Jewels.REGISTRY.get()::getValue)
                                    .toList()));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
