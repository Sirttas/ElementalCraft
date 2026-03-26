package sirttas.elementalcraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class EntityHelper {

	private EntityHelper() {}
	
	public static Stream<ItemStack> handStream(LivingEntity entity) {
		return Stream.of(entity.getMainHandItem(), entity.getOffhandItem());
	}

	public static HitResult rayTrace(Entity entity) {
		double range = 5;

		if (entity instanceof LivingEntity livingEntity) {
			var reach = livingEntity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);

			if (reach != null) {
				range = reach.getValue();
			}
		}
		return rayTrace(entity, range);
	}

	public static HitResult rayTrace(Entity entity, double range) {
		Vec3 eyePos = entity.getEyePosition(1);
		Vec3 look = entity.getViewVector(1);
		Vec3 rayVector = eyePos.add(look.x * range, look.y * range, look.z * range);
		BlockHitResult blockResult = entity.level().clip(new ClipContext(eyePos, rayVector, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
		EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(entity.level(), entity, eyePos, rayVector,
				entity.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D, 1.0D, 1.0D), e -> !e.isSpectator() && e.isPickable(), ProjectileUtil.computeMargin(entity));

		return entityResult != null && entityResult.getLocation().subtract(eyePos).length() <= blockResult.getLocation().subtract(eyePos).length() ? entityResult : blockResult;
	}
	
	public static boolean isHostile(Entity entity) {
		return entity instanceof Enemy;
	}

	public static boolean spawnMob(ServerLevel level, BlockPos pos) {
		return WeightedList.of(level.getChunkSource().getGenerator().getMobsAt(level.getBiome(pos), level.structureManager(), MobCategory.MONSTER, pos)
						.unwrap().stream()
						.filter(data -> canSpawn(level, pos, data.value()))
						.toList())
				.getRandom(level.getRandom())
				.map(data -> doSpawn(level, pos, data))
				.orElse(false);
	}

	private static boolean doSpawn(ServerLevel level, BlockPos pos, MobSpawnSettings.SpawnerData data) {
		var entity = data.type().create(level, EntitySpawnReason.SPAWNER);

		if (entity instanceof Mob mob) {
			mob.snapTo(pos.getX(), pos.getY(), pos.getZ(), level.getRandom().nextFloat() * 360.0F, 0.0F);

			EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.SPAWNER, null);
			level.addFreshEntityWithPassengers(mob);
			mob.spawnAnim();
			return true;
		}
		return false;
	}

	private static boolean canSpawn(ServerLevel level, BlockPos pos, MobSpawnSettings.SpawnerData data) {
		var entityType = data.type();

		if (entityType.canSummon()) {
			var placementType = SpawnPlacements.getPlacementType(entityType);

			if (!placementType.isSpawnPositionOk(level, pos, entityType)) {
				return false;
			} else if (!SpawnPlacements.checkSpawnRules(entityType, level, EntitySpawnReason.SPAWNER, pos, level.getRandom())) {
				return false;
			} else {
				return level.noCollision(entityType.getSpawnAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D));
			}
		}
		return false;
	}

	public static boolean isFighting(Entity entity) {
		return isFighting(entity, 20);
	}

	public static boolean isFighting(Entity entity, int ticks) {
		return entity instanceof LivingEntity livingEntity && livingEntity.attackStrengthTicker < ticks;
	}

	public static void dropAtFeet(@NotNull Level level, @NotNull Entity entity, @NotNull ItemStack stack) {
		level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY() + 0.25, entity.getZ(), stack));
	}
}
