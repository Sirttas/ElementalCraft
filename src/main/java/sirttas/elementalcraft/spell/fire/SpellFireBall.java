package sirttas.elementalcraft.spell.fire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.Spell;

public class SpellFireBall extends Spell {

	public static final String NAME = "fireball";

	public SpellFireBall() {
		super(Properties.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).cooldown(ECConfig.CONFIG.fireBallCooldown.get()).consumeAmount(ECConfig.CONFIG.fireBallConsumeAmount.get()));
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Vector3d vec3d = sender.getLookVec();
		FireballEntity fireballentity = new FireballEntity(sender.world, (LivingEntity) sender, vec3d.x * 100, vec3d.y * 100, vec3d.z * 100);

		fireballentity.setPosition(sender.getPosX() + vec3d.x * 4.0D, sender.getPosYHeight(0.5D) + 0.5D, fireballentity.getPosZ() + vec3d.z * 4.0D);
		sender.world.addEntity(fireballentity);

		return ActionResultType.SUCCESS;
	}
}
