package sirttas.elementalcraft.spell.fire;

import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.spell.Spell;

public class InfernoSpell extends Spell {

	public static final String NAME = "inferno";

	public InfernoSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		Level world = sender.getCommandSenderWorld();
		float range = getRange(sender);
		Vec3 look = sender.getLookAngle().normalize();

		if (sender instanceof LivingEntity livingSender) {
			for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, sender.getBoundingBox().expandTowards(look.scale(range + 1)).inflate(1.0D, 0.25D, 1.0D))) {
				if (target != sender && !sender.isAlliedTo(target) && (!(target instanceof ArmorStand) || !((ArmorStand) target).isMarker())
						&& sender.distanceToSqr(target) < range * range && getAngle(sender, target) <= 30) {
					target.hurt((sender instanceof Player ? DamageSource.playerAttack((Player) sender) : DamageSource.mobAttack(livingSender)).setIsFire(), 2);
					target.setSecondsOnFire(1);
				}
			}
			for (int i = 0; i < range; i += 1) {
				Vec3 scaledLook = look.scale(i);
				
				world.levelEvent(null, 2004, livingSender.blockPosition().offset(new Vec3i(scaledLook.x, scaledLook.y, scaledLook.z)), 0);
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	private double getAngle(Entity sender, Entity target) {
		Vec3 vec1 = sender.getLookAngle().normalize();
		Vec3 vec2 = target.position().subtract(sender.position()).normalize();
		
		return Math.acos(vec1.dot(vec2)) * (180 / Math.PI);
		
	}

}
