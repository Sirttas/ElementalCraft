package sirttas.elementalcraft.spell.water;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class PurificationSpell extends Spell {

	public static final String NAME = "purification";

	public PurificationSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private InteractionResult cureEffects(@Nonnull Level level, Entity target) {
		if (target instanceof LivingEntity livingTarget) {
			if (!level.isClientSide) {
				Iterator<MobEffectInstance> itr = livingTarget.getActiveEffects().iterator();

				while (itr.hasNext()) {
					var effect = itr.next();
					var cures = effect.getCures(); // TODO create cure for purification spell

					if (!cures.isEmpty() && effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL && NeoForge.EVENT_BUS.post(new MobEffectEvent.Remove(livingTarget, effect, cures.iterator().next())).isCanceled()) {
						livingTarget.onEffectRemoved(effect);
						itr.remove();
						livingTarget.updateEffectVisibility();
					}
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResult castOnEntity(@Nonnull Level level, @Nonnull Entity caster, @Nonnull Entity target) {
		return cureEffects(level, target);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
		return cureEffects(level, caster);
	}
}
