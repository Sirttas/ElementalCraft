package sirttas.elementalcraft.spell.fire;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;

public class FireBallSpell extends Spell {

	public static final String NAME = "fireball";

	public FireBallSpell(ResourceKey<@NotNull Spell> key) {
		super(key);
	}

	@Override
	public @Nonnull SpellCastResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
		if (!(caster instanceof LivingEntity livingEntity)) {
			return SpellCastResult.PASS;
		}

		Vec3 vec3d = livingEntity.getLookAngle();
        LargeFireball fireball = new LargeFireball(
				level,
				livingEntity,
				new Vec3(vec3d.x * 100, vec3d.y * 100, vec3d.z * 100),
				Math.round(getStrength()));

		fireball.setPos(livingEntity.getX() + vec3d.x * 4.0D, livingEntity.getY(0.5D) + 0.5D, fireball.getZ() + vec3d.z * 4.0D);
		level.addFreshEntity(fireball);

		return SpellCastResult.SUCCESS;
	}
}
