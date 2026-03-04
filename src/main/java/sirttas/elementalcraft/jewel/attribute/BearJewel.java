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
import sirttas.elementalcraft.entity.EntityHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BearJewel extends AttributeJewel {

    public static final String NAME = "bear";

    private static final Identifier ATTACK_DAMAGE_ID = ElementalCraftApi.createRL("bear_jewel_attack_damage");
    private static final Identifier ATTACK_KNOCKBACK_ID = ElementalCraftApi.createRL("bear_jewel_attack_knockback");

    public BearJewel() {
        super(ElementType.EARTH, 10, () -> {
            ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_ID, 6, AttributeModifier.Operation.ADD_VALUE));
            builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ATTACK_KNOCKBACK_ID, 2, AttributeModifier.Operation.ADD_VALUE));
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
