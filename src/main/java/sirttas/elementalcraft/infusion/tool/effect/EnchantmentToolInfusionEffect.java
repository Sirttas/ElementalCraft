package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.enchantment.Enchantment;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;

public class EnchantmentToolInfusionEffect implements IToolInfusionEffect {

	public static final String NAME = ECNames.ENCHANTMENT;
	public static final MapCodec<EnchantmentToolInfusionEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			RegistryFixedCodec.create(Registries.ENCHANTMENT).fieldOf(ECNames.ENCHANTMENT).forGetter(EnchantmentToolInfusionEffect::getEnchantment),
			Codec.INT.optionalFieldOf(ECNames.LEVEL, 1).forGetter(EnchantmentToolInfusionEffect::getLevel)
	).apply(builder, EnchantmentToolInfusionEffect::new));

	private final Holder<Enchantment> enchantment;
	private final int level;

	public EnchantmentToolInfusionEffect(Holder<Enchantment> enchantment) {
		this(enchantment, 1);
	}
	
	public EnchantmentToolInfusionEffect(Holder<Enchantment> enchantment, int level) {
		this.enchantment = enchantment;
		this.level = level;
	}

	@Override
	public Component getDescription() {
		return Component.translatable("tooltip.elementalcraft.enchantment_infused", enchantment.value().description(), level);
	}

	@Override
	public ToolInfusionEffectType<? extends IToolInfusionEffect> getType() {
		return ToolInfusionEffectTypes.ENCHANTMENT.get();
	}

	public Holder<Enchantment> getEnchantment() {
		return enchantment;
	}

	public int getLevel() {
		return level;
	}

}
