package sirttas.elementalcraft.spell;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nonnull;

public class AoeSpell extends Spell {

	protected AoeSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public @Nonnull SpellCastResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
		float range = getRange(caster);

		if (caster instanceof LivingEntity livingSender) {
			var attribute = livingSender.getAttribute(Attributes.ATTACK_DAMAGE);
			float damageBase = attribute != null ? (float) attribute.getValue() : 1;
			float damageMultiplier = (1F + (float) livingSender.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO)) * getStrength();

			for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, livingSender.getBoundingBox().inflate(range + 1, 0.25D, range + 1))) {
				hitTarget(level, livingSender, target, damageBase * damageMultiplier);
			}

			level.playSound(null, livingSender.getX(), livingSender.getY(), livingSender.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, livingSender.getSoundSource(), 1.0F, 1.0F);
			livingSender.swing(InteractionHand.MAIN_HAND);
			if (livingSender instanceof Player playerSender) {
				playerSender.sweepAttack();
				playerSender.resetAttackStrengthTicker();
			}
			return SpellCastResult.SUCCESS;
		}
		return SpellCastResult.PASS;
	}


	private void hitTarget(Level level, LivingEntity sender, LivingEntity target, float damage) {
		var range = getRange(sender);

		if (target != sender && !sender.isAlliedTo(target) && (!(target instanceof ArmorStand stand) || !stand.isMarker()) && sender.distanceToSqr(target) < range * range) {
			var sources = level.damageSources();
			var damageSource = sender instanceof Player player ? sources.playerAttack(player) : sources.mobAttack(sender);

			damage = getEnchantedDamage(level, sender, target, damage, damageSource);

			if (damage > 0) {
				target.knockback(0.4F, sender.getX() - target.getX(), sender.getZ() - target.getZ());
				target.hurt(damageSource, damage);
				onHit(sender, target, damage);

				if (level instanceof ServerLevel serverlevel) {
					EnchantmentHelper.doPostAttackEffects(serverlevel, target, damageSource);
				}
				hitWithItem(sender, target);
			}
		}
	}

	private float getEnchantedDamage(@Nonnull Level level, LivingEntity sender, Entity target, float damage, DamageSource damageSource) {
		if (level instanceof ServerLevel serverLevel) {
			return EnchantmentHelper.modifyDamage(serverLevel, sender.getWeaponItem(), target, damageSource, damage);
		}
		return damage;
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
				EventHooks.onPlayerDestroyItem(player, copy, InteractionHand.MAIN_HAND);
				sender.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
	}
}
