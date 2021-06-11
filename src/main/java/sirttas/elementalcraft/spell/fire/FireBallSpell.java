package sirttas.elementalcraft.spell.fire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.spell.Spell;

public class FireBallSpell extends Spell {

	public static final String NAME = "fireball";

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Vector3d vec3d = sender.getLookAngle();
		FireballEntity fireballentity = new FireballEntity(sender.level, (LivingEntity) sender, vec3d.x * 100, vec3d.y * 100, vec3d.z * 100);

		fireballentity.setPos(sender.getX() + vec3d.x * 4.0D, sender.getY(0.5D) + 0.5D, fireballentity.getZ() + vec3d.z * 4.0D);
		sender.level.addFreshEntity(fireballentity);

		return ActionResultType.SUCCESS;
	}
}
