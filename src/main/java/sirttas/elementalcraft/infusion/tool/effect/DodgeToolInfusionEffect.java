package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;

public record DodgeToolInfusionEffect(double value) implements IToolInfusionEffect {

	public static final String NAME = "dodge";
	public static final Codec<DodgeToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.DOUBLE.fieldOf(ECNames.VALUE).forGetter(DodgeToolInfusionEffect::value)
	).apply(builder, DodgeToolInfusionEffect::new));


	@Override
	public Component getDescription() {
		return Component.translatable("tooltip.elementalcraft.dodge_infused");
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.DODGE.get();
	}

}
