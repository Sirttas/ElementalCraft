package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;

public class AutoSmeltToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = "autosmelt";
	public static final Codec<AutoSmeltToolInfusionEffect> CODEC = Codec.unit(AutoSmeltToolInfusionEffect::new);
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final ToolInfusionEffectType<AutoSmeltToolInfusionEffect> TYPE = null;
	

	@Override
	public ITextComponent getDescription() {
		return new TranslationTextComponent("tooltip.elementalcraft.autosmelt_infused");
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return TYPE;
	}

}
