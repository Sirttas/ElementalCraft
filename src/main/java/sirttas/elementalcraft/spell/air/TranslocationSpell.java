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
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class TranslocationSpell extends Spell {

	public static final String NAME = "translocation";

	public TranslocationSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private void teleport(Entity sender, Vec3 newPos) {
		sender.setPos(newPos.x, newPos.y, newPos.z);
	}
	
	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Level world = caster.getLevel();
		Vec3 look = caster.getLookAngle();
		Vec3 tagetPos = caster.position().add(new Vec3(look.x(), 0, look.z()).normalize().scale(this.getRange(caster)));
		Vec3 newPos = new Vec3(tagetPos.x(), getHeight(world, caster, tagetPos), tagetPos.z());
		
		if (world.getChunk(((int) Math.round(newPos.x)) >> 4, ((int) Math.round(newPos.z)) >> 4) == null) {
			return InteractionResult.FAIL;
		} else if (MinecraftForge.EVENT_BUS.post(new Event(caster, newPos.x, newPos.y, newPos.z))) {
			return InteractionResult.SUCCESS;
		} else if (caster instanceof LivingEntity livingSender) {
			teleport(caster, newPos);
			livingSender.getLevel().playSound(null, livingSender.xo, livingSender.yo, livingSender.zo, SoundEvents.ENDERMAN_TELEPORT,
					livingSender.getSoundSource(), 1.0F, 1.0F);
			livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		} else {
			teleport(caster, newPos);
		}
		return InteractionResult.SUCCESS;
	}

	private double getHeight(Level world, Entity sender, Vec3 tagetPos) {
		double height = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(tagetPos)).getY() + 1D;
		
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
