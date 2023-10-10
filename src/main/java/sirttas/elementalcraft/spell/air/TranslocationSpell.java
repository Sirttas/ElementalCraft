package sirttas.elementalcraft.spell.air;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorList;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import java.util.List;

public class TranslocationSpell extends Spell {

	public static final String NAME = "translocation";

	public TranslocationSpell(ResourceKey<Spell> key) {
		super(key);
	}

    public static boolean isTranslocation(ItemStack stack) {
        return SpellHelper.getSpell(stack) == Spells.TRANSLOCATION.get();
    }

	public static boolean holdsTranslocation(Player player) {
		return isTranslocation(player.getMainHandItem()) || isTranslocation(player.getOffhandItem());
	}

	public static BlockPos getTargetAnchor(Entity caster, List<BlockPos> anchors) {
		var playerPos = caster.getEyePosition();
		var playerLook = caster.getLookAngle().normalize();
		BlockPos target = null;
		var angle = 0.0;

		for (BlockPos pos : anchors) {
			var a = getAngle(pos, playerPos, playerLook);

			if (a > 0 && a < Math.PI / 24 && (a < angle || target == null)) {
				angle = a;
				target = pos;
			}
		}

		return target;
	}

	private static double getAngle(BlockPos pos, Vec3 playerPos, Vec3 playerLook) {
		return Math.acos(playerPos.vectorTo(Vec3.atBottomCenterOf(pos)).normalize().dot(playerLook));
	}


	private void teleport(Entity caster, Vec3 newPos) {
		caster.teleportTo(newPos.x, newPos.y, newPos.z);
	}
	
	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Level level = caster.level();
		Vec3 look = caster.getLookAngle();
		Vec3 newPos = getNewPos(caster, level, look);

		if (newPos == null) {
			return InteractionResult.PASS;
		}

		if (!level.isClientSide) {
			level.getChunk(((int) Math.round(newPos.x / 16)), ((int) Math.round(newPos.z / 16)));

			if (MinecraftForge.EVENT_BUS.post(new Event(caster, newPos.x, newPos.y, newPos.z))) {
				return InteractionResult.SUCCESS;
			}
			ParticleHelper.createEnderParticle(level, caster.position(), 3, level.random);
			ParticleHelper.createEnderParticle(level, newPos, 3, level.random);
			this.delay(caster, 10, () -> {
				if (caster instanceof LivingEntity livingSender) {
					var oldPos = caster.position();

					teleport(caster, newPos);
					level.playSound(null, oldPos.x, oldPos.y, oldPos.z, SoundEvents.ENDERMAN_TELEPORT, livingSender.getSoundSource(), 1.0F, 1.0F);
					livingSender.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
				} else {
					teleport(caster, newPos);
				}
			});
		}
		return InteractionResult.SUCCESS;
	}

	private Vec3 getNewPos(@Nonnull Entity caster, Level level, Vec3 look) {
		var list = TranslocationAnchorList.get(level);

		if (list != null) {
			var target = getTargetAnchor(caster, list.getAnchors());

			if (target != null) {
				return Vec3.atCenterOf(target);
			}
		}

		var targetPos = caster.position().add(new Vec3(look.x(), 0, look.z()).normalize().scale(this.getRange(caster)));
		var newPos = new Vec3(targetPos.x(), getHeight(level, caster, targetPos), targetPos.z());

		if (newPos.y >= level.dimensionType().logicalHeight() || newPos.y < level.getSeaLevel()) {
			return null;
		}
		return newPos;
	}

	private double getHeight(Level world, Entity sender, Vec3 targetPos) {
		double height = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlockPos.containing(targetPos)).getY() + 1D;
		
		if (!sender.onGround()) {
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
