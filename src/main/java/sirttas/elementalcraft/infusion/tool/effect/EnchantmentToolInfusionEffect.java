package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;

public class EnchantmentToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = ECNames.ENCHANTMENT;
	public static final Codec<EnchantmentToolInfusionEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codecs.ENCHANTMENT.fieldOf(ECNames.ENCHANTMENT).forGetter(EnchantmentToolInfusionEffect::getEnchantment),
			Codec.INT.optionalFieldOf(ECNames.LEVEL, 1).forGetter(EnchantmentToolInfusionEffect::getLevel)
	).apply(builder, EnchantmentToolInfusionEffect::new));

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
	public Component getDescription() {
		return Component.translatable("tooltip.elementalcraft.enchantment_infused", Component.translatable(enchantment.getDescriptionId()), level);
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.ENCHANTMENT.get();
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public int getLevel() {
		return level;
	}

}
