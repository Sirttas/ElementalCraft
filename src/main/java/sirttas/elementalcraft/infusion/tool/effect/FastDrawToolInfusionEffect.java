package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;

public record FastDrawToolInfusionEffect(int value) implements IToolInfusionEffect {

	public static final String NAME = "fastdraw";
	public static final Codec<FastDrawToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.VALUE).forGetter(FastDrawToolInfusionEffect::value)
	).apply(builder, FastDrawToolInfusionEffect::new));


	@Override
	public Component getDescription() {
		return Component.translatable("tooltip.elementalcraft.fastdraw_infused");
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.FAST_DRAW.get();
	}

}
