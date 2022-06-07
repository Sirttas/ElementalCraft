package sirttas.elementalcraft.spell.air;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import java.util.Comparator;

public class EnderStrikeSpell extends Spell {

	public static final String NAME = "ender_strike";

	public EnderStrikeSpell(ResourceKey<Spell> key) {
		super(key);
	}

	@Nonnull
	@Override
	public InteractionResult castOnEntity(@Nonnull Entity caster, @Nonnull Entity target) {
		Vec3 newPos = target.position().add(target.getLookAngle().reverse().normalize());

		if (MinecraftForge.EVENT_BUS.post(new Event(caster, newPos.x, newPos.y + 0.5F, newPos.z))) {
			return InteractionResult.SUCCESS;
		}
		if (caster instanceof LivingEntity livingSender && livingSender.randomTeleport(newPos.x, newPos.y + 0.5F, newPos.z, true)) {
			livingSender.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
			livingSender.getLevel().playSound(null, livingSender.xo, livingSender.yo, livingSender.zo, SoundEvents.ENDERMAN_TELEPORT, livingSender.getSoundSource(), 1.0F, 1.0F);
			livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			livingSender.swing(InteractionHand.MAIN_HAND);
			if (livingSender instanceof Player playerSender) {
				playerSender.attack(target);
				playerSender.resetAttackStrengthTicker();
			} else {
				livingSender.doHurtTarget(target);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Vec3 pos = caster.position();

		return caster.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos.add(1, 1, 1)).inflate(getRange(caster))).stream()
				.filter(Enemy.class::isInstance).min(Comparator.comparingDouble(e -> pos.distanceTo(e.position()))).map(e -> castOnEntity(caster, e)).orElse(InteractionResult.PASS);
	}
	
	public static class Event extends EntityTeleportEvent {

		public Event(Entity entity, double targetX, double targetY, double targetZ) {
			super(entity, targetX, targetY, targetZ);
		}
	}
}
