package sirttas.elementalcraft.api.infusion.tool.effect;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;

public interface IToolInfusionEffect {
	
	Codec<IToolInfusionEffect> CODEC = CodecHelper.getRegistryCodec(ElementalCraftApi.TOOL_INFUSION_EFFECT_TYPE_REGISTRY_KEY).dispatch(IToolInfusionEffect::getType, ToolInfusionEffectType::codec);
	
	@OnlyIn(Dist.CLIENT)
	Component getDescription();
	
	ToolInfusionEffectType<? extends IToolInfusionEffect> getType();
	


	
}
