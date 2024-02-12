package sirttas.elementalcraft.api.infusion.tool.effect;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.registry.ElementalCraftRegistries;

public interface IToolInfusionEffect {

	Codec<IToolInfusionEffect> CODEC = ElementalCraftRegistries.TOOL_INFUSION_EFFECT_TYPE.byNameCodec().dispatch(IToolInfusionEffect::getType, ToolInfusionEffectType::codec);
	
	@OnlyIn(Dist.CLIENT)
	Component getDescription();
	
	ToolInfusionEffectType<? extends IToolInfusionEffect> getType();
	


	
}
