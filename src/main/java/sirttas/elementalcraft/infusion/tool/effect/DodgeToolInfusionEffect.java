package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;

public class DodgeToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = "dodge";
	public static final Codec<DodgeToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.DOUBLE.fieldOf(ECNames.VALUE).forGetter(DodgeToolInfusionEffect::getValue)
	).apply(builder, DodgeToolInfusionEffect::new));
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final ToolInfusionEffectType<DodgeToolInfusionEffect> TYPE = null;
	
	private final double value;
	
	public DodgeToolInfusionEffect(double value) {;
		this.value = value;
	}

	public double getValue() {
		return value;
	}
	
	@Override
	public ITextComponent getDescription() {
		return new TranslationTextComponent("tooltip.elementalcraft.dodge_infused");
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return TYPE;
	}

}
