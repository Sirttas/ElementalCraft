package sirttas.elementalcraft.jewel.attribute;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class LeopardJewel extends AttributeJewel {

    public static final String NAME = "leopard";

    public LeopardJewel() {
        super(ElementType.AIR, 5, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("38fd9b47-779f-4b82-b7ee-b6592307d6fc"), "Movement speed modifier", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        });
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return !entity.isInWater() && entity.isSprinting() && super.isActive(entity, elementStorage);
    }

    @Override
    protected Component getAttributesTitle() {
        return new TranslatableComponent("tooltip.elementalcraft.while_sprinting").withStyle(ChatFormatting.GRAY);
    }

}
