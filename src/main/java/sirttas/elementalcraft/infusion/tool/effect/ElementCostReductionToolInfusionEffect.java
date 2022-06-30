package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;

public record ElementCostReductionToolInfusionEffect(
		ElementType elementType,
		float value
) implements IToolInfusionEffect, IElementTypeProvider {

	public static final String NAME = "element_cost_reduction";
	public static final Codec<ElementCostReductionToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.forGetter(ElementCostReductionToolInfusionEffect::getElementType),
			Codec.FLOAT.fieldOf(ECNames.VALUE).forGetter(ElementCostReductionToolInfusionEffect::value)
	).apply(builder, ElementCostReductionToolInfusionEffect::new));

	@Override
	public Component getDescription() {
		return Component.translatable("tooltip.elementalcraft.element_cost_reduction_infusion", elementType.getDisplayName());
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.ELEMENT_COST_REDUCTION.get();
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
}
