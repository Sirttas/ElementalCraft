package sirttas.elementalcraft.spell;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.network.payload.PayloadHelper;
import sirttas.elementalcraft.tag.ECTags;

public record ChangeSpellPayload(int i) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ChangeSpellPayload> TYPE = PayloadHelper.createType("change_spell");
    public static final StreamCodec<FriendlyByteBuf, ChangeSpellPayload> STREAM_CODEC = StreamCodec.of((b, p) -> p.write(b), ChangeSpellPayload::new);

    public ChangeSpellPayload(FriendlyByteBuf buf) {
         this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(i);
    }

    @Override
    public Type<ChangeSpellPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext payloadContext) {
        payloadContext.enqueueWork(() -> EntityHelper.handStream(payloadContext.player())
                .filter(stack -> stack.is(ECTags.Items.SPELL_CAST_TOOLS))
                .findFirst()
                .ifPresent(stack -> SpellHelper.setSelected(stack, i)));
    }
}
