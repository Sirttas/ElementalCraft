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
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.Spell;

public class SpellInferno extends Spell {

	public static final String NAME = "inferno";

	public SpellInferno() {
		super(Properties.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).consumeAmount(ECConfig.CONFIG.infernoConsumeAmount.get()).cooldown(ECConfig.CONFIG.infernoCooldown.get())
				.useDuration(ECConfig.CONFIG.infernoDuration.get()));
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		World world = sender.getEntityWorld();
		Double range = ECConfig.CONFIG.infernoRange.get();
		Vector3d look = sender.getLookVec().normalize();

		if (sender instanceof LivingEntity) {
			LivingEntity livingSender = (LivingEntity) sender;

			for (LivingEntity target : world.getEntitiesWithinAABB(LivingEntity.class, sender.getBoundingBox().expand(look.scale(range + 1)).grow(1.0D, 0.25D, 1.0D))) {
				if (target != sender && !sender.isOnSameTeam(target) && (!(target instanceof ArmorStandEntity) || !((ArmorStandEntity) target).hasMarker())
						&& sender.getDistanceSq(target) < range * range && getAngle(sender, target) <= 30) {
					target.attackEntityFrom((sender instanceof PlayerEntity ? DamageSource.causePlayerDamage((PlayerEntity) sender) : DamageSource.causeMobDamage(livingSender)).setFireDamage(), 2);
					target.setFire(1);
				}
			}
			for (int i = 0; i < range; i += 1) {
				Vector3d scaledLook = look.scale(i);
				
				world.playEvent(null, 2004, livingSender.getPosition().add(new Vector3i(scaledLook.x, scaledLook.y, scaledLook.z)), 0);
			}
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}

	private double getAngle(Entity sender, Entity target) {
		Vector3d vec1 = sender.getLookVec().normalize();
		Vector3d vec2 = target.getPositionVec().subtract(sender.getPositionVec()).normalize();
		
		return Math.acos(vec1.dotProduct(vec2)) * (180 / Math.PI);
		
	}

}
