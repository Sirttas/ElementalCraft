package sirttas.elementalcraft.api.infusion.tool.effect;

import com.mojang.serialization.Codec;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.dpanvil.api.codec.CodecHelper;

public interface IToolInfusionEffect {
	
	public static final Codec<IToolInfusionEffect> CODEC = CodecHelper.getRegistryCodec(() -> ToolInfusionEffectType.REGISTRY).dispatch(IToolInfusionEffect::getType, ToolInfusionEffectType::getCodec);
	
	@OnlyIn(Dist.CLIENT)
	ITextComponent getDescription();
	
	ToolInfusionEffectType<? extends IToolInfusionEffect> getType();
	


	
}
	