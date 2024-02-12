package sirttas.elementalcraft.jewel.attribute;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.NeoForgeMod;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class DolphinJewel extends AttributeJewel {

    public static final String NAME = "dolphin";

    public DolphinJewel() {
        super(ElementType.WATER, 50, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(NeoForgeMod.SWIM_SPEED.value(), new AttributeModifier(UUID.fromString("a1cedb96-34ca-42ef-a9ea-c45868b2c790"), "Swim speed modifier", 0.05, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        });
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return entity.isInWater() && entity.isSprinting() && super.isActive(entity, elementStorage);
    }

    @Override
    protected Component getAttributesTitle() {
        return Component.translatable("tooltip.elementalcraft.while_swimming").withStyle(ChatFormatting.GRAY);
    }

}
