package sirttas.elementalcraft.entity;

import java.util.stream.Stream;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class EntityHelper {

	public static Stream<ItemStack> handStream(PlayerEntity player) {
		return Stream.of(player.getHeldItemMainhand(), player.getHeldItemOffhand());

	}

	public static RayTraceResult rayTrace(Entity entity, double range) {
		Vector3d eyePos = entity.getEyePosition(1);
		Vector3d look = entity.getLook(1);
		Vector3d rayVector = eyePos.add(look.x * range, look.y * range, look.z * range);
		BlockRayTraceResult blockResult = entity.world.rayTraceBlocks(new RayTraceContext(eyePos, rayVector, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, entity));
		EntityRayTraceResult entityResult = ProjectileHelper.rayTraceEntities(entity, eyePos, rayVector, entity.getBoundingBox().expand(look.scale(range)).grow(1.0D, 1.0D, 1.0D),
				e -> !e.isSpectator() && e.canBeCollidedWith(), 0);

		return entityResult != null && entityResult.getHitVec().subtract(eyePos).length() <= blockResult.getHitVec().subtract(eyePos).length() ? entityResult : blockResult;
	}

}
