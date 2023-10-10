package sirttas.elementalcraft.spell.fire;

import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class InfernoSpell extends Spell {

	public static final String NAME = "inferno";

	public InfernoSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Level world = caster.level();
		float range = getRange(caster);
		Vec3 look = caster.getLookAngle().normalize();

		if (caster instanceof LivingEntity livingSender) {
			for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().expandTowards(look.scale(range + 1)).inflate(1.0D, 0.25D, 1.0D))) {
				if (target != caster && !caster.isAlliedTo(target) && (!(target instanceof ArmorStand stand) || !stand.isMarker())
						&& caster.distanceToSqr(target) < range * range && getAngle(caster, target) <= 30) {
					var sources = caster.level().damageSources();

					target.hurt(caster instanceof Player player ? sources.playerAttack(player) : sources.mobAttack(livingSender), 2);
					target.setSecondsOnFire(1);
				}
			}
			for (int i = 0; i < range; i += 1) {
				Vec3 scaledLook = look.scale(i);
				
				world.levelEvent(null, 2004, livingSender.blockPosition().offset(new Vec3i((int) Math.round(scaledLook.x), (int) Math.round(scaledLook.y), (int) Math.round(scaledLook.z))), 0);
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
