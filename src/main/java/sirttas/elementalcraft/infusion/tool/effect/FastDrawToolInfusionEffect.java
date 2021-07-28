package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;

public class FastDrawToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = "fastdraw";
	public static final Codec<FastDrawToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.VALUE).forGetter(FastDrawToolInfusionEffect::getValue)
	).apply(builder, FastDrawToolInfusionEffect::new));
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final ToolInfusionEffectType<FastDrawToolInfusionEffect> TYPE = null;
	
	private final int value;
	
	public FastDrawToolInfusionEffect(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	@Override
	public Component getDescription() {
		return new TranslatableComponent("tooltip.elementalcraft.fastdraw_infused");
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return TYPE;
	}

}
