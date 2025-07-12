package sirttas.elementalcraft.jewel.attribute;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.entity.EntityHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TigerJewel extends AttributeJewel {

    public static final String NAME = "tiger";

    private static final ResourceLocation ATTACK_SPEED_ID = ElementalCraftApi.createRL("tiger_jewel_attack_speed");

    public TigerJewel() {
        super(ElementType.AIR, 10, () -> {
            ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_ID, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            return builder.build();
        });
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return EntityHelper.isFighting(entity) && super.isActive(entity, elementStorage);
    }

    @Override
    protected Component getAttributesTitle() {
        return Component.translatable("tooltip.elementalcraft.while_fighting").withStyle(ChatFormatting.GRAY);
    }

}
