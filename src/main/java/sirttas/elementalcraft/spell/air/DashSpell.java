package sirttas.elementalcraft.spell.air;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;

public class DashSpell extends Spell {

	public static final String NAME = "dash";

	public DashSpell(ResourceKey<Spell> key) {
		super(key);
	}


	@Override
	public @Nonnull SpellCastResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
		var range = getRange(caster);

		if (caster.isPassenger()) {
			return SpellCastResult.PASS;
		}

		if (caster instanceof LivingEntity livingEntity && isFlying(livingEntity)) {
			this.effect(caster, livingEntity.getRandom().nextInt(10, 20) + (int) Math.ceil(range), instance -> {
				if (isFlying(livingEntity)) {
					Vec3 lookAngle = livingEntity.getLookAngle();
					Vec3 movement = livingEntity.getDeltaMovement();

					livingEntity.setDeltaMovement(movement.add(
							lookAngle.x * 0.1D + (lookAngle.x * 1.5D - movement.x) * 0.5D,
							lookAngle.y * 0.1D + (lookAngle.y * 1.5D - movement.y) * 0.5D,
							lookAngle.z * 0.1D + (lookAngle.z * 1.5D - movement.z) * 0.5D
					));
				} else {
					instance.stop();
				}
			});
		} else {
			caster.setDeltaMovement(caster.getDeltaMovement().add(caster.getLookAngle().normalize().scale(range)));
		}
		return SpellCastResult.SUCCESS;
	}

	private boolean isFlying(LivingEntity livingEntity) {
		return livingEntity.isFallFlying() || (livingEntity instanceof FlyingAnimal flyingAnimal && flyingAnimal.isFlying());
	}
}
