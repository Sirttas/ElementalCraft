package sirttas.elementalcraft.spell;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeEffectInstance;
import sirttas.elementalcraft.item.ECItem;

public class EffectSpell extends Spell {

	private final List<IForgeEffectInstance> effects;

	public EffectSpell(IForgeEffectInstance... effects) {
		this.effects = ImmutableList.copyOf(effects);
	}

	private ActionResultType applyEffect(Entity target) {
		if (target instanceof LivingEntity) {
			effects.forEach(e -> ((LivingEntity) target).addEffect(new EffectInstance(e.getEffectInstance())));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		return applyEffect(target);
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		return applyEffect(sender);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(List<ITextComponent> tooltip) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();

		if (!effects.isEmpty()) {
			for (IForgeEffectInstance forgeEffect : effects) {
				EffectInstance effectinstance = forgeEffect.getEffectInstance();
				IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(effectinstance.getDescriptionId());
				Effect effect = effectinstance.getEffect();

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
					iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent,
							new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
				}

				if (effectinstance.getDuration() > 20) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withDuration", iformattabletextcomponent, EffectUtils.formatDuration(effectinstance, 1));
				}

				tooltip.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}
		ECItem.addAttributeMultimapToTooltip(tooltip, multimap, new TranslationTextComponent("tooltip.elementalcraft.spell_effect_on_use").withStyle(TextFormatting.DARK_PURPLE));
	}

	public final List<IForgeEffectInstance> getEffects() {
		return effects;
	}
}
