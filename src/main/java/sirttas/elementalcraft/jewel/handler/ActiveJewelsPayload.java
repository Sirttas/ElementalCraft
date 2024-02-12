package sirttas.elementalcraft.jewel.handler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;

import javax.annotation.Nonnull;
import java.util.List;

public record ActiveJewelsPayload(List<ResourceLocation> jewels) implements CustomPacketPayload {

    public static final ResourceLocation ID = ElementalCraftApi.createRL("active_jewels");

    public ActiveJewelsPayload(IJewelHandler jewelHandler) {
        this(jewelHandler.getActiveJewels().stream()
                .map(Jewel::getKey)
                .toList());
    }

    public ActiveJewelsPayload(FriendlyByteBuf buf) {
        this(buf.readList(FriendlyByteBuf::readResourceLocation));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(jewels, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> ctx.player()
                .map(player -> player.getCapability(IJewelHandler.CAPABILITY))
                .filter(ClientJewelHandler.class::isInstance)
                .map(ClientJewelHandler.class::cast)
                .ifPresent(handler -> handler.setActiveJewels(jewels.stream()
                        .map(Jewels.REGISTRY::get)
                        .toList())));
    }

}
