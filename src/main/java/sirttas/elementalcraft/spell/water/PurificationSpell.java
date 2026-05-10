package sirttas.elementalcraft.spell.water;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import java.util.stream.Collectors;

public class PurificationSpell extends Spell {

	public static final String NAME = "purification";

	public PurificationSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	private SpellCastResult cureEffects(Level level, Entity target) {
		if (!(target instanceof LivingEntity livingTarget) || level.isClientSide()) {
            return SpellCastResult.PASS;
        }

        var effectsToRemove = livingTarget.getActiveEffects().stream()
                .map(MobEffectInstance::getEffect)
                .filter(effect -> effect.value().getCategory() == MobEffectCategory.HARMFUL)
                .collect(Collectors.toSet());

        if (effectsToRemove.isEmpty()) {
            return SpellCastResult.PASS;
        }

        for (var effect : effectsToRemove) {
            livingTarget.removeEffect(effect);
        }
        livingTarget.updateEffectVisibility();
        return SpellCastResult.SUCCESS;
	}

	@Override
	public SpellCastResult castOnEntity(Level level, Entity caster, Entity target) {
		return cureEffects(level, target);
	}

	@Override
	public SpellCastResult castOnSelf(Level level, Entity caster) {
		return cureEffects(level, caster);
	}
}
