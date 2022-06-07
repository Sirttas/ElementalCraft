package sirttas.elementalcraft.spell;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;

public class AoeSpell extends Spell {

	protected AoeSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Level level = caster.getLevel();
		float range = getRange(caster);

		if (caster instanceof LivingEntity livingSender) {
			var attribute = livingSender.getAttribute(Attributes.ATTACK_DAMAGE);
			float damageBase = attribute != null ? (float) attribute.getValue() : 1;
			float damageMultiplier = (1 + EnchantmentHelper.getSweepingDamageRatio(livingSender));

			for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, livingSender.getBoundingBox().inflate(range + 1, 0.25D, range + 1))) {
				hitTarget(livingSender, target, damageBase, damageMultiplier);
			}

			level.playSound(null, livingSender.getX(), livingSender.getY(), livingSender.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, livingSender.getSoundSource(), 1.0F, 1.0F);
			livingSender.swing(InteractionHand.MAIN_HAND);
			if (livingSender instanceof Player playerSender) {
				playerSender.sweepAttack();
				playerSender.resetAttackStrengthTicker();
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}


	private void hitTarget(LivingEntity sender, LivingEntity target, float damageBase, float damageMultiplier) {
		float range = getRange(sender);

		if (target != sender && !sender.isAlliedTo(target) && (!(target instanceof ArmorStand) || !((ArmorStand) target).isMarker()) && sender.distanceToSqr(target) < range * range) {
			float damage = damageMultiplier * (damageBase + EnchantmentHelper.getDamageBonus(sender.getMainHandItem(), target.getMobType()));

			if (damage > 0) {
				target.knockback(0.4F, sender.getX() - target.getX(), sender.getZ() - target.getZ());
				target.hurt(sender instanceof Player ? DamageSource.playerAttack((Player) sender) : DamageSource.mobAttack(sender), damage);
				onHit(sender, target, damage);

				EnchantmentHelper.doPostHurtEffects(target, sender);
				EnchantmentHelper.doPostDamageEffects(sender, target);
				hitWithItem(sender, target);
			}
		}
	}

	protected void onHit(LivingEntity sender, LivingEntity target, float damage) {
		// for override
	}

	private void hitWithItem(LivingEntity sender, LivingEntity target) {
		ItemStack stack = sender.getMainHandItem();

		if (!stack.isEmpty() && sender instanceof Player player) {
			ItemStack copy = stack.copy();

			stack.getItem().hurtEnemy(stack, target, player);
			if (stack.isEmpty()) {
				ForgeEventFactory.onPlayerDestroyItem(player, copy, InteractionHand.MAIN_HAND);
				sender.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
	}
}
