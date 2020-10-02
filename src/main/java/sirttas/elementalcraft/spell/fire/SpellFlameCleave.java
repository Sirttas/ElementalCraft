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
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.Spell;

public class SpellFlameCleave extends Spell {

	public static final String NAME = "flame_cleave";

	public SpellFlameCleave() {
		super(Properties.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).cooldown(ECConfig.CONFIG.flameCleaveCooldown.get()).consumeAmount(ECConfig.CONFIG.flameCleaveConsumeAmount.get()));
	}


	@Override
	public ActionResultType castOnSelf(Entity sender) {
		World world = sender.getEntityWorld();
		Double range = ECConfig.CONFIG.flameCleaveRange.get();

		if (sender instanceof LivingEntity) {
			LivingEntity livingSender = (LivingEntity) sender;
			float damageBase = (float) livingSender.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
			float damageMult = (1 + EnchantmentHelper.getSweepingDamageRatio(livingSender));

			for (LivingEntity target : world.getEntitiesWithinAABB(LivingEntity.class, livingSender.getBoundingBox().grow(range + 1, 0.25D, range + 1))) {
				hitTarget(livingSender, target, damageBase, damageMult);
			}

			world.playSound(null, livingSender.getPosX(), livingSender.getPosY(), livingSender.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, livingSender.getSoundCategory(), 1.0F, 1.0F);
			if (livingSender instanceof PlayerEntity) {
				((PlayerEntity) livingSender).spawnSweepParticles();
			}
			world.playEvent(null, 2004, livingSender.getPosition(), 0);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}


	private void hitTarget(LivingEntity sender, LivingEntity target, float damageBase, float damageMult) {
		Double range = ECConfig.CONFIG.flameCleaveRange.get();

		if (target != sender && !sender.isOnSameTeam(target) && (!(target instanceof ArmorStandEntity) || !((ArmorStandEntity) target).hasMarker()) && sender.getDistanceSq(target) < range * range) {
			float damage = damageMult * (damageBase + EnchantmentHelper.getModifierForCreature(sender.getHeldItemMainhand(), target.getCreatureAttribute()));

			if (damage > 0) {
				target.applyKnockback(0.4F, sender.getPosX() - target.getPosX(), sender.getPosZ() - target.getPosZ());
				target.attackEntityFrom((sender instanceof PlayerEntity ? DamageSource.causePlayerDamage((PlayerEntity) sender) : DamageSource.causeMobDamage(sender)).setFireDamage(), damage);
				target.setFire(5);

				EnchantmentHelper.applyThornEnchantments(target, sender);
				EnchantmentHelper.applyArthropodEnchantments(sender, target);

				hitWithItem(sender, target);
			}
		}
	}


	private void hitWithItem(LivingEntity sender, LivingEntity target) {
		ItemStack stack = sender.getHeldItemMainhand();

		if (!stack.isEmpty() && sender instanceof PlayerEntity) {
			ItemStack copy = stack.copy();

			stack.getItem().hitEntity(stack, target, sender);
			if (stack.isEmpty()) {
				ForgeEventFactory.onPlayerDestroyItem((PlayerEntity) sender, copy, Hand.MAIN_HAND);
				sender.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
	}
}
