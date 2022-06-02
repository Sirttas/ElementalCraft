package sirttas.elementalcraft.spell.fire;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.spell.Spell;

public class FireBallSpell extends Spell {

	public static final String NAME = "fireball";

	public FireBallSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Override
	public InteractionResult castOnSelf(Entity sender) {
		Vec3 vec3d = sender.getLookAngle();
		LargeFireball fireballentity = new LargeFireball(sender.level, (LivingEntity) sender, vec3d.x * 100, vec3d.y * 100, vec3d.z * 100, 1); // TODO config

		fireballentity.setPos(sender.getX() + vec3d.x * 4.0D, sender.getY(0.5D) + 0.5D, fireballentity.getZ() + vec3d.z * 4.0D);
		sender.level.addFreshEntity(fireballentity);

		return InteractionResult.SUCCESS;
	}
}
