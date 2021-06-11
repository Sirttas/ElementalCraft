package sirttas.elementalcraft.spell.air;

import java.util.Comparator;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.spell.Spell;

public class EnderStrikeSpell extends Spell {

	public static final String NAME = "ender_strike";

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		if (sender instanceof LivingEntity) {
			LivingEntity livingSender = (LivingEntity) sender;
			Vector3d newPos = target.position().add(target.getLookAngle().reverse().normalize());

			if (MinecraftForge.EVENT_BUS.post(new EnderTeleportEvent(livingSender, newPos.x, newPos.y + 0.5F, newPos.z, 0))) {
				return ActionResultType.SUCCESS;
			}
			if (livingSender.randomTeleport(newPos.x, newPos.y + 0.5F, newPos.z, true)) {
				livingSender.lookAt(EntityAnchorArgument.Type.EYES, target.position());
				livingSender.getCommandSenderWorld().playSound(null, livingSender.xo, livingSender.yo, livingSender.zo, SoundEvents.ENDERMAN_TELEPORT,
						livingSender.getSoundSource(), 1.0F, 1.0F);
				livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
				if (livingSender instanceof PlayerEntity) {
					PlayerEntity playerSender = (PlayerEntity) livingSender;

					playerSender.attack(target);
					playerSender.resetAttackStrengthTicker();
					EntityHelper.swingArm(playerSender);
				} else {
					livingSender.doHurtTarget(target);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Vector3d pos = sender.position();

		return sender.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).inflate(getRange(sender))).stream()
				.filter(IMob.class::isInstance).sorted(Comparator.comparingDouble(e -> pos.distanceTo(e.position()))).findFirst().map(e -> castOnEntity(sender, e)).orElse(ActionResultType.PASS);
	}
}
