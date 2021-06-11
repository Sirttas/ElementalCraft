package sirttas.elementalcraft.spell.fire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import sirttas.elementalcraft.spell.Spell;

public class InfernoSpell extends Spell {

	public static final String NAME = "inferno";

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		World world = sender.getCommandSenderWorld();
		float range = getRange(sender);
		Vector3d look = sender.getLookAngle().normalize();

		if (sender instanceof LivingEntity) {
			LivingEntity livingSender = (LivingEntity) sender;

			for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, sender.getBoundingBox().expandTowards(look.scale(range + 1)).inflate(1.0D, 0.25D, 1.0D))) {
				if (target != sender && !sender.isAlliedTo(target) && (!(target instanceof ArmorStandEntity) || !((ArmorStandEntity) target).isMarker())
						&& sender.distanceToSqr(target) < range * range && getAngle(sender, target) <= 30) {
					target.hurt((sender instanceof PlayerEntity ? DamageSource.playerAttack((PlayerEntity) sender) : DamageSource.mobAttack(livingSender)).setIsFire(), 2);
					target.setSecondsOnFire(1);
				}
			}
			for (int i = 0; i < range; i += 1) {
				Vector3d scaledLook = look.scale(i);
				
				world.levelEvent(null, 2004, livingSender.blockPosition().offset(new Vector3i(scaledLook.x, scaledLook.y, scaledLook.z)), 0);
			}
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}

	private double getAngle(Entity sender, Entity target) {
		Vector3d vec1 = sender.getLookAngle().normalize();
		Vector3d vec2 = target.position().subtract(sender.position()).normalize();
		
		return Math.acos(vec1.dot(vec2)) * (180 / Math.PI);
		
	}

}
