package sirttas.elementalcraft.jewel.attribute;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LeopardJewel extends AttributeJewel {

    public static final String NAME = "leopard";

    private static final Identifier MOVEMENT_SPEED_ID = ElementalCraftApi.identifier("leopard_jewel_movement_speed");

    public LeopardJewel() {
        super(ElementType.AIR, 5, () -> {
            ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_ID, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            return builder.build();
        });
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return !entity.isInWater() && entity.isSprinting() && super.isActive(entity, elementStorage);
    }

    @Override
    protected Component getAttributesTitle() {
        return Component.translatable("tooltip.elementalcraft.while_sprinting").withStyle(ChatFormatting.GRAY);
    }

}
