package sirttas.elementalcraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.stream.Stream;

public class EntityHelper {

	private EntityHelper() {}
	
	public static Stream<ItemStack> handStream(Player player) {
		return Stream.of(player.getMainHandItem(), player.getOffhandItem());
	}

	public static HitResult rayTrace(Entity entity) {
		double range = 5;

		if (entity instanceof LivingEntity livingEntity) {
			var reach = livingEntity.getAttribute(ForgeMod.REACH_DISTANCE.get());

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
		BlockHitResult blockResult = entity.level.clip(new ClipContext(eyePos, rayVector, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
		EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(entity.getLevel(), entity, eyePos, rayVector,
				entity.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D, 1.0D, 1.0D), e -> !e.isSpectator() && e.isPickable());

		return entityResult != null && entityResult.getLocation().subtract(eyePos).length() <= blockResult.getLocation().subtract(eyePos).length() ? entityResult : blockResult;
	}
	
	public static boolean isHostile(Entity entity) {
		return entity instanceof Mob && ((Mob) entity).isAggressive();
	}

	public static boolean spawnMob(ServerLevel level, BlockPos pos) {
		return WeightedRandomList.create(level.getChunkSource().getGenerator().getMobsAt(level.getBiome(pos), level.structureManager(), MobCategory.MONSTER, pos)
						.unwrap().stream()
						.filter(data -> canSpawn(level, pos, data))
						.toList())
				.getRandom(level.random)
				.map(data -> doSpawn(level, pos, data))
				.orElse(false);
	}

	private static boolean doSpawn(ServerLevel level, BlockPos pos, MobSpawnSettings.SpawnerData data) {
		var entity = data.type.create(level);

		if (entity instanceof Mob mob) {
			mob.moveTo(pos.getX(), pos.getY(), pos.getZ(), level.random.nextFloat() * 360.0F, 0.0F);
			if (!ForgeEventFactory.doSpecialSpawn(mob, (LevelAccessor)level, (float)entity.getX(), (float)entity.getY(), (float)entity.getZ(), null, MobSpawnType.SPAWNER)) {
				mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.SPAWNER, null, null);
			}
			level.addFreshEntityWithPassengers(mob);
			mob.spawnAnim();
			return true;
		}
		return false;
	}

	private static boolean canSpawn(ServerLevel level, BlockPos pos, MobSpawnSettings.SpawnerData data) {
		var entityType = data.type;

		if (entityType.canSummon()) {
			var placementType = SpawnPlacements.getPlacementType(entityType);

			if (!NaturalSpawner.isSpawnPositionOk(placementType, level, pos, entityType)) {
				return false;
			} else if (!SpawnPlacements.checkSpawnRules(entityType, level, MobSpawnType.SPAWNER, pos, level.random)) {
				return false;
			} else {
				return level.noCollision(entityType.getAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D));
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
}
