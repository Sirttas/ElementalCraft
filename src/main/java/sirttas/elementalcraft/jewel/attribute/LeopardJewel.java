package sirttas.elementalcraft.jewel.attribute;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import sirttas.elementalcraft.api.element.ElementType;

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
    public boolean isActive(Entity entity) {
        return !entity.isInWater() && entity.isSprinting() && super.isActive(entity);
    }

    @Override
    protected Component getAttributesTitle() {
        return new TranslatableComponent("tooltip.elementalcraft.while_sprinting").withStyle(ChatFormatting.GRAY);
    }

    public static class DolphinJewel extends AttributeJewel {

        public static final String NAME = "dolphin";

        public DolphinJewel() {
            super(ElementType.WATER, 50, () -> {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

                builder.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(UUID.fromString("a1cedb96-34ca-42ef-a9ea-c45868b2c790"), "Swim speed modifier", 0.05, AttributeModifier.Operation.MULTIPLY_TOTAL));
                return builder.build();
            });
        }

        @Override
        public boolean isActive(Entity entity) {
            return entity.isInWater() && entity.isSprinting() && super.isActive(entity);
        }

        @Override
        protected Component getAttributesTitle() {
            return new TranslatableComponent("tooltip.elementalcraft.while_swimming").withStyle(ChatFormatting.GRAY);
        }

    }
}
