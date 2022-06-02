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

public class TranslocationSpell extends Spell {

	public static final String NAME = "translocation";

	public TranslocationSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private void teleport(Entity sender, Vec3 newPos) {
		sender.setPos(newPos.x, newPos.y, newPos.z);
	}
	
	@Override
	public InteractionResult castOnSelf(Entity sender) {
		Level world = sender.getCommandSenderWorld();
		Vec3 look = sender.getLookAngle();
		Vec3 tagetPos = sender.position().add(new Vec3(look.x(), 0, look.z()).normalize().scale(this.getRange(sender)));
		Vec3 newPos = new Vec3(tagetPos.x(), getHeight(world, sender, tagetPos), tagetPos.z());
		
		if (world.getChunk(((int) Math.round(newPos.x)) >> 4, ((int) Math.round(newPos.z)) >> 4) == null) {
			return InteractionResult.FAIL;
		} else if (MinecraftForge.EVENT_BUS.post(new Event(sender, newPos.x, newPos.y, newPos.z))) {
			return InteractionResult.SUCCESS;
		} else if (sender instanceof LivingEntity livingSender) {
			teleport(sender, newPos);
			livingSender.getCommandSenderWorld().playSound(null, livingSender.xo, livingSender.yo, livingSender.zo, SoundEvents.ENDERMAN_TELEPORT,
					livingSender.getSoundSource(), 1.0F, 1.0F);
			livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		} else {
			teleport(sender, newPos);
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
