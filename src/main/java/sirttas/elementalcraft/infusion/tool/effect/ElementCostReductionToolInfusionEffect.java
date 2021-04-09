package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;

public class ElementCostReductionToolInfusionEffect implements IToolInfusionEffect, IElementTypeProvider {

	public static final String NAME = "element_cost_reduction";
	public static final Codec<ElementCostReductionToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.forGetter(ElementCostReductionToolInfusionEffect::getElementType),
			Codec.FLOAT.fieldOf(ECNames.VALUE).forGetter(ElementCostReductionToolInfusionEffect::getValue)
	).apply(builder, ElementCostReductionToolInfusionEffect::new));
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final ToolInfusionEffectType<ElementCostReductionToolInfusionEffect> TYPE = null;
	
	private final ElementType elementType;
	private final float value;

	public ElementCostReductionToolInfusionEffect(ElementType elementType, float value) {
		this.elementType = elementType;
		this.value = value;
	}

	@Override
	public ITextComponent getDescription() {
		return new TranslationTextComponent("tooltip.elementalcraft.element_cost_reduction_infusion", elementType.getDisplayName());
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return TYPE;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
	
	public float getValue() {
		return value;
	}

}
