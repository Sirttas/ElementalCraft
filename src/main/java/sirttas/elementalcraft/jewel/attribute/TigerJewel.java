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
import sirttas.elementalcraft.entity.EntityHelper;

import java.util.UUID;

public class TigerJewel extends AttributeJewel {

    public static final String NAME = "tiger";

    public TigerJewel() {
        super(ElementType.AIR, 10, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("3053b337-f27d-4263-9e81-7f86e4e09303"), "Attack speed modifier", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        });
    }

    @Override
    public boolean isActive(Entity entity) {
        return EntityHelper.isFighting(entity) && super.isActive(entity);
    }

    @Override
    protected Component getAttributesTitle() {
        return new TranslatableComponent("tooltip.elementalcraft.while_fighting").withStyle(ChatFormatting.GRAY);
    }

}
