package sirttas.elementalcraft.spell.air;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import java.util.Comparator;

public class EnderStrikeSpell extends Spell {

	public static final String NAME = "ender_strike";

	public EnderStrikeSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	@Override
	public SpellCastResult castOnEntity(Level level, Entity caster, Entity target) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return SpellCastResult.PASS;
        }

		Vec3 newPos = target.position().add(target.getLookAngle().reverse().normalize());

		if (NeoForge.EVENT_BUS.post(new Event(caster, serverLevel, newPos.x, newPos.y + 0.5F, newPos.z)).isCanceled()) {
			return SpellCastResult.SUCCESS;
		}
		if (caster instanceof LivingEntity livingSender) {
			livingSender.teleportTo(newPos.x, newPos.y + 0.5, newPos.z);
			livingSender.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
			level.playSound(null, livingSender.xo, livingSender.yo, livingSender.zo, SoundEvents.ENDERMAN_TELEPORT, livingSender.getSoundSource(), 1.0F, 1.0F);
			livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			livingSender.swing(InteractionHand.MAIN_HAND);
			if (livingSender instanceof Player playerSender) {
				playerSender.attack(target);
				playerSender.resetAttackStrengthTicker();
			} else {
				livingSender.doHurtTarget(serverLevel, target);
			}
			return SpellCastResult.SUCCESS;
		}
		return SpellCastResult.PASS;
	}

	@Override
	public SpellCastResult castOnSelf(Level level, Entity caster) {
		Vec3 pos = caster.position();

		return level.getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos.add(1, 1, 1)).inflate(getRange(caster))).stream()
				.filter(Enemy.class::isInstance)
				.min(Comparator.comparingDouble(e -> pos.distanceTo(e.position())))
				.map(e -> castOnEntity(level, caster, e))
				.orElse(SpellCastResult.PASS);
	}
	
	public static class Event extends EntityTeleportEvent {

		public Event(Entity entity, ServerLevel targetLevel, double targetX, double targetY, double targetZ) {
			super(entity, targetLevel, targetX, targetY, targetZ);
		}
	}
}
