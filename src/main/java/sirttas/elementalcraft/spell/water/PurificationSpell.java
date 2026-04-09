package sirttas.elementalcraft.spell.water;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class PurificationSpell extends Spell {

	public static final String NAME = "purification";

	public PurificationSpell(ResourceKey<@NotNull Spell> key) {
		super(key);
	}

	private SpellCastResult cureEffects(@Nonnull Level level, Entity target) {
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

	@Nonnull
	@Override
	public SpellCastResult castOnEntity(@Nonnull Level level, @Nonnull Entity caster, @Nonnull Entity target) {
		return cureEffects(level, target);
	}

	@Override
	public @Nonnull SpellCastResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
		return cureEffects(level, caster);
	}
}
