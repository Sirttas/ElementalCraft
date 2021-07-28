package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;

public class AutoSmeltToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = "autosmelt";
	public static final Codec<AutoSmeltToolInfusionEffect> CODEC = Codec.unit(AutoSmeltToolInfusionEffect::new);
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final ToolInfusionEffectType<AutoSmeltToolInfusionEffect> TYPE = null;
	

	@Override
	public Component getDescription() {
		return new TranslatableComponent("tooltip.elementalcraft.autosmelt_infused");
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return TYPE;
	}

}
