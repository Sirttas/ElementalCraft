package sirttas.elementalcraft.spell.air;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class TranslocationSpell extends Spell {

	public static final String NAME = "translocation";

	public TranslocationSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private void teleport(Entity caster, Vec3 newPos) {
		caster.setPos(newPos.x, newPos.y, newPos.z);
	}
	
	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Level level = caster.getLevel();
		Vec3 look = caster.getLookAngle();
		Vec3 targetPos = caster.position().add(new Vec3(look.x(), 0, look.z()).normalize().scale(this.getRange(caster)));
		Vec3 newPos = new Vec3(targetPos.x(), getHeight(level, caster, targetPos), targetPos.z());

		if (newPos.y >= level.dimensionType().logicalHeight() || newPos.y < level.getSeaLevel()) {
			return InteractionResult.PASS;
		}

		level.getChunk(((int) Math.round(newPos.x / 16)), ((int) Math.round(newPos.z / 16)));

		if (MinecraftForge.EVENT_BUS.post(new Event(caster, newPos.x, newPos.y, newPos.z))) {
			return InteractionResult.SUCCESS;
		}
		ParticleHelper.createEnderParticle(level, caster.position(), 3, level.random);
		ParticleHelper.createEnderParticle(level, newPos, 3, level.random);
		this.delay(caster, 10, () -> {
			if (caster instanceof LivingEntity livingSender) {
				teleport(caster, newPos);
				level.playSound(null, livingSender.xo, livingSender.yo, livingSender.zo, SoundEvents.ENDERMAN_TELEPORT, livingSender.getSoundSource(), 1.0F, 1.0F);
				livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			} else {
				teleport(caster, newPos);
			}
		});
		return InteractionResult.SUCCESS;
	}

	private double getHeight(Level world, Entity sender, Vec3 targetPos) {
		double height = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(targetPos)).getY() + 1D;
		
		if (!sender.isOnGround()) {
			return Math.max(height, sender.getY());
		}
		return height;
	}
	
	public static class Event extends EntityTeleportEvent {

		public Event(Entity entity, double targetX, double targetY, double targetZ) {
			super(entity, targetX, targetY, targetZ);
		}
	}
}
