package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;

public class EnchantmentToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = ECNames.ENCHANTMENT;
	public static final Codec<EnchantmentToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codecs.ENCHANTMENT.fieldOf(ECNames.ENCHANTMENT).forGetter(EnchantmentToolInfusionEffect::getEnchantment),
			Codec.INT.optionalFieldOf(ECNames.LEVEL, 1).forGetter(EnchantmentToolInfusionEffect::getLevel)
	).apply(builder, EnchantmentToolInfusionEffect::new));
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final ToolInfusionEffectType<EnchantmentToolInfusionEffect> TYPE = null;
	
	private final Enchantment enchantment;
	private final int level;

	public EnchantmentToolInfusionEffect(Enchantment enchantment) {
		this(enchantment, 1);
	}
	
	public EnchantmentToolInfusionEffect(Enchantment enchantment, int level) {
		this.enchantment = enchantment;
		this.level = level;
	}

	@Override
	public ITextComponent getDescription() {
		return new TranslationTextComponent("tooltip.elementalcraft.enchantment_infused", new TranslationTextComponent(enchantment.getName()), level);
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return TYPE;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public int getLevel() {
		return level;
	}

}
