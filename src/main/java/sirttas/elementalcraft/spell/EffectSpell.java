package sirttas.elementalcraft.spell;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.item.ECItem;

import javax.annotation.Nonnull;
import java.util.List;

public class EffectSpell extends Spell {

	private final List<MobEffectInstance> effects;

	public EffectSpell(ResourceKey<Spell> key, MobEffectInstance... effects) {
		super(key);
		this.effects = ImmutableList.copyOf(effects);
	}

	private InteractionResult applyEffect(Entity target) {
		if (target instanceof LivingEntity) {
			effects.forEach(e -> ((LivingEntity) target).addEffect(new MobEffectInstance(e)));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResult castOnEntity(@Nonnull Entity caster, @Nonnull Entity target) {
		return applyEffect(target);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		return applyEffect(caster);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(List<Component> tooltip) {
		Multimap<Attribute, AttributeModifier> multiMap = HashMultimap.create();

		if (!effects.isEmpty()) {
			for (MobEffectInstance effectInstance : effects) {
				var mutableComponent = Component.translatable(effectInstance.getDescriptionId());
				var effect = effectInstance.getEffect();
				var amplifier = effectInstance.getAmplifier();

				var map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (var entry : map.entrySet()) {
						var attributemodifier = entry.getValue();
						var attributeModifier1 =  entry.getValue().create(amplifier);

						multiMap.put(entry.getKey(), attributeModifier1);
					}
				}

				if (amplifier > 0) {
					mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + amplifier));
				}

				if (effectInstance.getDuration() > 20) {
					mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(effectInstance, 1, 20));
				}

				tooltip.add(mutableComponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}
		ECItem.addAttributeMultiMapToTooltip(tooltip, multiMap, Component.translatable("tooltip.elementalcraft.spell_effect_on_use").withStyle(ChatFormatting.DARK_PURPLE));
	}

	public final List<MobEffectInstance> getEffects() {
		return effects;
	}
}
