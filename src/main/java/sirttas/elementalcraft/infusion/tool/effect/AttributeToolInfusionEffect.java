package sirttas.elementalcraft.infusion.tool.effect;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItem;

import java.util.List;

public record AttributeToolInfusionEffect(
		List<EquipmentSlot> slots,
		Attribute attribute,
		AttributeModifier modifier
) implements IToolInfusionEffect {

	public static final String NAME = ECNames.ATTRIBUTE;
	public static final Codec<AttributeToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codecs.EQUIPMENT_SLOT_TYPE.listOf().fieldOf(ECNames.SLOT).forGetter(i -> i.slots),
			Codecs.ATTRIBUTE.fieldOf(ECNames.ATTRIBUTE).forGetter(i -> i.attribute),
			Codecs.ATTRIBUTE_MODIFIER.fieldOf(ECNames.MODIFIER).forGetter(i -> i.modifier)
	).apply(builder, AttributeToolInfusionEffect::new));

	public AttributeToolInfusionEffect(List<EquipmentSlot> slots, Attribute attribute, AttributeModifier modifier) {
		this.slots = ImmutableList.copyOf(slots);
		this.attribute = attribute;
		this.modifier = modifier;
	}

	@Override
	public Component getDescription() {
		return ECItem.getAttributeTooltip(attribute, modifier).withStyle(ChatFormatting.YELLOW);
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.ATTRIBUTE.get();
	}


}
