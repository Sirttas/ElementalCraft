package sirttas.elementalcraft.spell.water;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.ISelfCastedSpell;
import sirttas.elementalcraft.spell.Spell;

public class SpellPurification extends Spell implements ISelfCastedSpell, IEntityCastedSpell {

	public static final String NAME = "purification";


	public SpellPurification() {
		super(Properties.create(Spell.Type.MIXED).elementType(ElementType.WATER).cooldown(ECConfig.CONFIG.purificationCooldown.get()).consumeAmount(ECConfig.CONFIG.purificationConsumeAmount.get()));
	}

	@SuppressWarnings("resource")
	private ActionResultType cureEffects(Entity target) {
		if (target instanceof LivingEntity) {
			if (!target.getEntityWorld().isRemote) {
				LivingEntity livingTarget = (LivingEntity) target;
				Iterator<EffectInstance> itr = livingTarget.getActivePotionEffects().iterator();

				while (itr.hasNext()) {
					EffectInstance effect = itr.next();

					if (!effect.getCurativeItems().isEmpty() && effect.getPotion().getEffectType() == EffectType.HARMFUL
							&& !MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionRemoveEvent(livingTarget, effect))) {
						livingTarget.onFinishedPotionEffect(effect);
						itr.remove();
						livingTarget.markPotionsDirty();
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
