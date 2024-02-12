package sirttas.elementalcraft.spell;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public record ChangeSpellPayload(int i) implements CustomPacketPayload {

    public static final ResourceLocation ID = ElementalCraftApi.createRL("change_spell");

    public ChangeSpellPayload(FriendlyByteBuf buf) {
         this(buf.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(i);
    }


    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> ctx.player()
                .flatMap(player -> EntityHelper.handStream(player)
                .filter(stack -> stack.is(ECTags.Items.SPELL_CAST_TOOLS))
                .findFirst())
                .ifPresent(stack -> SpellHelper.setSelected(stack, i)));
    }


    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
    }
}
