package sirttas.elementalcraft.block.anchor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public record TranslocationAnchorListMessage(List<BlockPos> list) {

    public static TranslocationAnchorListMessage create(Level level) {
        var anchorList = TranslocationAnchorList.get(level);

        if (anchorList != null) {
            return new TranslocationAnchorListMessage(anchorList.getAnchors());
        }
        return new TranslocationAnchorListMessage(Collections.emptyList());
    }

    public static TranslocationAnchorListMessage decode(FriendlyByteBuf buf) {
        return new TranslocationAnchorListMessage(buf.readList(b -> BlockPos.of(b.readLong())));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(list, (b, p) -> b.writeLong(p.asLong()));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                TranslocationAnchorList.CLIENT_LIST.clear();
                TranslocationAnchorList.CLIENT_LIST.addAll(list);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
