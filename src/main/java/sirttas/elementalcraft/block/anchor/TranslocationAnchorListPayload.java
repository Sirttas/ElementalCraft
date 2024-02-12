package sirttas.elementalcraft.block.anchor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public record TranslocationAnchorListPayload(List<BlockPos> list) implements CustomPacketPayload {

    public static final ResourceLocation ID = ElementalCraftApi.createRL("translocation_anchor_list");

    public TranslocationAnchorListPayload(FriendlyByteBuf buf) {
        this(buf.readList(b -> BlockPos.of(b.readLong())));
    }

    public static TranslocationAnchorListPayload create(Level level) {
        var anchorList = TranslocationAnchorList.get(level);

        if (anchorList != null) {
            return new TranslocationAnchorListPayload(anchorList.getAnchors());
        }
        return new TranslocationAnchorListPayload(Collections.emptyList());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(list, (b, p) -> b.writeLong(p.asLong()));
    }

    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            TranslocationAnchorList.CLIENT_LIST.clear();
            TranslocationAnchorList.CLIENT_LIST.addAll(list);
        });
    }
}
