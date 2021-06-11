package sirttas.elementalcraft.spell.fire;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.spell.Spell;

public class FlameCleaveSpell extends Spell {

	public static final String NAME = "flame_cleave";

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		World world = sender.getCommandSenderWorld();
		float range = getRange(sender);

		if (sender instanceof LivingEntity) {
			LivingEntity livingSender = (LivingEntity) sender;
			float damageBase = (float) livingSender.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
			float damageMult = (1 + EnchantmentHelper.getSweepingDamageRatio(livingSender));

			for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, livingSender.getBoundingBox().inflate(range + 1, 0.25D, range + 1))) {
				hitTarget(livingSender, target, damageBase, damageMult);
			}

			world.playSound(null, livingSender.getX(), livingSender.getY(), livingSender.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, livingSender.getSoundSource(), 1.0F, 1.0F);
			if (livingSender instanceof PlayerEntity) {
				PlayerEntity playerSender = (PlayerEntity) livingSender;

				playerSender.sweepAttack();
				playerSender.resetAttackStrengthTicker();
				EntityHelper.swingArm(playerSender);
			}
			world.levelEvent(null, 2004, livingSender.blockPosition(), 0);

			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}


	private void hitTarget(LivingEntity sender, LivingEntity target, float damageBase, float damageMult) {
		float range = getRange(sender);

		if (target != sender && !sender.isAlliedTo(target) && (!(target instanceof ArmorStandEntity) || !((ArmorStandEntity) target).isMarker()) && sender.distanceToSqr(target) < range * range) {
			float damage = damageMult * (damageBase + EnchantmentHelper.getDamageBonus(sender.getMainHandItem(), target.getMobType()));

			if (damage > 0) {
				target.knockback(0.4F, sender.getX() - target.getX(), sender.getZ() - target.getZ());
				target.hurt(sender instanceof PlayerEntity ? DamageSource.playerAttack((PlayerEntity) sender) : DamageSource.mobAttack(sender), damage);
				target.setSecondsOnFire(5);

				EnchantmentHelper.doPostHurtEffects(target, sender);
				EnchantmentHelper.doPostDamageEffects(sender, target);

				hitWithItem(sender, target);
			}
		}
	}


	private void hitWithItem(LivingEntity sender, LivingEntity target) {
		ItemStack stack = sender.getMainHandItem();

		if (!stack.isEmpty() && sender instanceof PlayerEntity) {
			ItemStack copy = stack.copy();

			stack.getItem().hurtEnemy(stack, target, sender);
			if (stack.isEmpty()) {
				ForgeEventFactory.onPlayerDestroyItem((PlayerEntity) sender, copy, Hand.MAIN_HAND);
				sender.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
	}
}
