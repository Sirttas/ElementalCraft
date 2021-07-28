package sirttas.elementalcraft.spell.fire;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.spell.Spell;

public class FlameCleaveSpell extends Spell {

	public static final String NAME = "flame_cleave";

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		Level world = sender.getCommandSenderWorld();
		float range = getRange(sender);

		if (sender instanceof LivingEntity) {
			LivingEntity livingSender = (LivingEntity) sender;
			float damageBase = (float) livingSender.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
			float damageMult = (1 + EnchantmentHelper.getSweepingDamageRatio(livingSender));

			for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, livingSender.getBoundingBox().inflate(range + 1, 0.25D, range + 1))) {
				hitTarget(livingSender, target, damageBase, damageMult);
			}

			world.playSound(null, livingSender.getX(), livingSender.getY(), livingSender.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, livingSender.getSoundSource(), 1.0F, 1.0F);
			if (livingSender instanceof Player) {
				Player playerSender = (Player) livingSender;

				playerSender.sweepAttack();
				playerSender.resetAttackStrengthTicker();
				EntityHelper.swingArm(playerSender);
			}
			world.levelEvent(null, 2004, livingSender.blockPosition(), 0);

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}


	private void hitTarget(LivingEntity sender, LivingEntity target, float damageBase, float damageMult) {
		float range = getRange(sender);

		if (target != sender && !sender.isAlliedTo(target) && (!(target instanceof ArmorStand) || !((ArmorStand) target).isMarker()) && sender.distanceToSqr(target) < range * range) {
			float damage = damageMult * (damageBase + EnchantmentHelper.getDamageBonus(sender.getMainHandItem(), target.getMobType()));

			if (damage > 0) {
				target.knockback(0.4F, sender.getX() - target.getX(), sender.getZ() - target.getZ());
				target.hurt(sender instanceof Player ? DamageSource.playerAttack((Player) sender) : DamageSource.mobAttack(sender), damage);
				target.setSecondsOnFire(5);

				EnchantmentHelper.doPostHurtEffects(target, sender);
				EnchantmentHelper.doPostDamageEffects(sender, target);

				hitWithItem(sender, target);
			}
		}
	}


	private void hitWithItem(LivingEntity sender, LivingEntity target) {
		ItemStack stack = sender.getMainHandItem();

		if (!stack.isEmpty() && sender instanceof Player) {
			ItemStack copy = stack.copy();

			stack.getItem().hurtEnemy(stack, target, sender);
			if (stack.isEmpty()) {
				ForgeEventFactory.onPlayerDestroyItem((Player) sender, copy, InteractionHand.MAIN_HAND);
				sender.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
	}
}
