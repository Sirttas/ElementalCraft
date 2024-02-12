package sirttas.elementalcraft.spell.water;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

	@SuppressWarnings("resource")
	private InteractionResult cureEffects(Entity target) {
		if (target instanceof LivingEntity livingTarget) {
			if (!target.level().isClientSide) {
				Iterator<MobEffectInstance> itr = livingTarget.getActiveEffects().iterator();

				while (itr.hasNext()) {
					var effect = itr.next();
					var cures = effect.getCures(); // TODO create cure for purification spell

					if (!cures.isEmpty() && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL && NeoForge.EVENT_BUS.post(new MobEffectEvent.Remove(livingTarget, effect, cures.iterator().next())).isCanceled()) {
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
	public InteractionResult castOnEntity(@Nonnull Entity caster, @Nonnull Entity target) {
		return cureEffects(target);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		return cureEffects(caster);
	}
}
