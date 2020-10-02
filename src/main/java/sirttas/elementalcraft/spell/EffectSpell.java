package sirttas.elementalcraft.spell;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeEffectInstance;
import sirttas.elementalcraft.item.ItemEC;

public class EffectSpell extends Spell {

	private final List<IForgeEffectInstance> effects;

	public EffectSpell(Properties properties, IForgeEffectInstance... effects) {
		super(properties);
		this.effects = ImmutableList.copyOf(effects);
	}

	private ActionResultType applyEffect(Entity target) {
		if (target instanceof LivingEntity) {
			effects.forEach(e -> ((LivingEntity) target).addPotionEffect(e.getEffectInstance()));
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
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();

		if (!effects.isEmpty()) {
			for (IForgeEffectInstance forgeEffect : effects) {
				EffectInstance effectinstance = forgeEffect.getEffectInstance();
				ITextComponent itextcomponent = new TranslationTextComponent(effectinstance.getEffectName());
				Effect effect = effectinstance.getPotion();
				Map<IAttribute, AttributeModifier> map = effect.getAttributeModifierMap();

				if (!map.isEmpty()) {
					for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierAmount(effectinstance.getAmplifier(), attributemodifier),
								attributemodifier.getOperation());
						multimap.put(entry.getKey().getName(), attributemodifier1);
					}
				}

				if (effectinstance.getAmplifier() > 0) {
					itextcomponent.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
				}

				if (effectinstance.getDuration() > 20) {
					itextcomponent.appendText(" (").appendText(EffectUtils.getPotionDurationString(effectinstance, 1)).appendText(")");
				}

				tooltip.add(itextcomponent.applyTextStyle(effect.getEffectType().getColor()));
			}
		}
		ItemEC.addAttributeMultimapToTooltip(tooltip, multimap, new TranslationTextComponent("tooltip.elementalcraft.spell_effect_on_use").applyTextStyle(TextFormatting.DARK_PURPLE));
	}

	public final List<IForgeEffectInstance> getEffects() {
		return effects;
	}
}
