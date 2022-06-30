package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;

public class AutoSmeltToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = "autosmelt";
	public static final Codec<AutoSmeltToolInfusionEffect> CODEC = Codec.unit(AutoSmeltToolInfusionEffect::new);

	@Override
	public Component getDescription() {
		return Component.translatable("tooltip.elementalcraft.autosmelt_infused");
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.AUTO_SMELT.get();
	}

}
