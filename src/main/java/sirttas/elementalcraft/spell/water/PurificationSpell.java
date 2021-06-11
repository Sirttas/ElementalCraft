package sirttas.elementalcraft.spell.water;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import sirttas.elementalcraft.spell.Spell;

public class PurificationSpell extends Spell {

	public static final String NAME = "purification";

	@SuppressWarnings("resource")
	private ActionResultType cureEffects(Entity target) {
		if (target instanceof LivingEntity) {
			if (!target.getCommandSenderWorld().isClientSide) {
				LivingEntity livingTarget = (LivingEntity) target;
				Iterator<EffectInstance> itr = livingTarget.getActiveEffects().iterator();

				while (itr.hasNext()) {
					EffectInstance effect = itr.next();

					if (!effect.getCurativeItems().isEmpty() && effect.getEffect().getCategory() == EffectType.HARMFUL
							&& !MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionRemoveEvent(livingTarget, effect))) {
						livingTarget.onEffectRemoved(effect);
						itr.remove();
						livingTarget.updateEffectVisibility();
					}
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		return cureEffects(target);
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		return cureEffects(sender);
	}
}
