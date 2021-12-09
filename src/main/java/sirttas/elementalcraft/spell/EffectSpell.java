package sirttas.elementalcraft.spell;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.item.ECItem;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EffectSpell extends Spell {

	private final List<MobEffectInstance> effects;

	public EffectSpell(MobEffectInstance... effects) {
		this.effects = ImmutableList.copyOf(effects);
	}

	private InteractionResult applyEffect(Entity target) {
		if (target instanceof LivingEntity) {
			effects.forEach(e -> ((LivingEntity) target).addEffect(new MobEffectInstance(e)));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult castOnEntity(Entity sender, Entity target) {
		return applyEffect(target);
	}

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		return applyEffect(sender);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(List<Component> tooltip) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();

		if (!effects.isEmpty()) {
			for (MobEffectInstance effectinstance : effects) {
				MutableComponent iformattabletextcomponent = new TranslatableComponent(effectinstance.getDescriptionId());
				MobEffect effect = effectinstance.getEffect();

				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(effectinstance.getAmplifier(), attributemodifier),
								attributemodifier.getOperation());
						multimap.put(entry.getKey(), attributemodifier1);
					}
				}

				if (effectinstance.getAmplifier() > 0) {
					iformattabletextcomponent = new TranslatableComponent("potion.withAmplifier", iformattabletextcomponent,
							new TranslatableComponent("potion.potency." + effectinstance.getAmplifier()));
				}

				if (effectinstance.getDuration() > 20) {
					iformattabletextcomponent = new TranslatableComponent("potion.withDuration", iformattabletextcomponent, MobEffectUtil.formatDuration(effectinstance, 1));
				}

				tooltip.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}
		ECItem.addAttributeMultiMapToTooltip(tooltip, multimap, new TranslatableComponent("tooltip.elementalcraft.spell_effect_on_use").withStyle(ChatFormatting.DARK_PURPLE));
	}

	public final List<MobEffectInstance> getEffects() {
		return effects;
	}
}
