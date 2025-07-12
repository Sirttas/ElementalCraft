package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.TooltipHelper;

public record AttributeToolInfusionEffect(
		EquipmentSlotGroup slotGroup,
		Holder<Attribute> attribute,
		AttributeModifier modifier
) implements IToolInfusionEffect {

	public static final String NAME = ECNames.ATTRIBUTE;
	public static final MapCodec<AttributeToolInfusionEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			EquipmentSlotGroup.CODEC.fieldOf(ECNames.SLOT_GROUP).forGetter(i -> i.slotGroup),
			BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf(ECNames.ATTRIBUTE).forGetter(i -> i.attribute),
			AttributeModifier.CODEC.fieldOf(ECNames.MODIFIER).forGetter(i -> i.modifier)
	).apply(builder, AttributeToolInfusionEffect::new));

	@Override
	public Component getDescription() {
		return TooltipHelper.getAttributeTooltip(attribute, modifier).withStyle(ChatFormatting.YELLOW);
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.ATTRIBUTE.get();
	}


}
