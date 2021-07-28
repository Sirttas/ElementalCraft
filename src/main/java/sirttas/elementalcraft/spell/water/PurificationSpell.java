package sirttas.elementalcraft.spell.water;

import java.util.Iterator;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import sirttas.elementalcraft.spell.Spell;

public class PurificationSpell extends Spell {

	public static final String NAME = "purification";

	@SuppressWarnings("resource")
	private InteractionResult cureEffects(Entity target) {
		if (target instanceof LivingEntity) {
			if (!target.getCommandSenderWorld().isClientSide) {
				LivingEntity livingTarget = (LivingEntity) target;
				Iterator<MobEffectInstance> itr = livingTarget.getActiveEffects().iterator();

				while (itr.hasNext()) {
					MobEffectInstance effect = itr.next();

					if (!effect.getCurativeItems().isEmpty() && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL
							&& !MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionRemoveEvent(livingTarget, effect))) {
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

	@Override
	public InteractionResult castOnEntity(Entity sender, Entity target) {
		return cureEffects(target);
	}

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		return cureEffects(sender);
	}
}
