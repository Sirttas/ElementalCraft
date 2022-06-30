package sirttas.elementalcraft.api.infusion.tool;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Products.P1;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.name.ECNames;

import java.util.Collections;
import java.util.List;

public class ToolInfusion implements IElementTypeProvider {
	
	public static final String NAME = "tool_infusions";
	public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;
	public static final ToolInfusion NONE = new ToolInfusion(ElementType.NONE, Collections.emptyList());
	public static final Codec<ToolInfusion> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.forGetter(ToolInfusion::getElementType),
			IToolInfusionEffect.CODEC.listOf().fieldOf(ECNames.EFFECTS).forGetter(ToolInfusion::getEffects)
	).apply(builder, ToolInfusion::new));
	
	private ResourceLocation id;
	private final ElementType elementType;
	private final List<IToolInfusionEffect> effects;
	
	public ToolInfusion(ElementType elementType, List<IToolInfusionEffect> effects) {
		this.elementType = elementType;
		this.effects = effects;
	}
	
	protected static <T extends ToolInfusion> P1<Mu<T>, ElementType> codec(Instance<T> builder) {
		return builder.group(ElementType.forGetter(ToolInfusion::getElementType));
	}
	
	@OnlyIn(Dist.CLIENT)
	public List<Component> getTooltipInformation() {
		List<Component> tooltip = Lists.newArrayList();

		if (effects.size() == 1) {
			tooltip.add(Component.translatable("tooltip.elementalcraft.infused.single", elementType.getDisplayName(), effects.get(0).getDescription()).withStyle(ChatFormatting.YELLOW));
		} else {
			tooltip.add(Component.translatable("tooltip.elementalcraft.infused", elementType.getDisplayName()).withStyle(ChatFormatting.YELLOW));
			effects.stream().map(e -> Component.literal(" ").append(e.getDescription()).withStyle(ChatFormatting.YELLOW)).forEach(tooltip::add);
		}
		return tooltip;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	public ResourceLocation getId() {
		return id;
	}

	public void setId(ResourceLocation id) {
		this.id = id;
	}

	public List<IToolInfusionEffect> getEffects() {
		return effects;
	}

	
}
