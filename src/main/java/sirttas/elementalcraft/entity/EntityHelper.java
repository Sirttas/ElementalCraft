package sirttas.elementalcraft.entity;

import java.util.stream.Stream;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class EntityHelper {

	private EntityHelper() {}
	
	public static Stream<ItemStack> handStream(PlayerEntity player) {
		return Stream.of(player.getMainHandItem(), player.getOffhandItem());

	}

	public static RayTraceResult rayTrace(Entity entity, double range) {
		Vector3d eyePos = entity.getEyePosition(1);
		Vector3d look = entity.getViewVector(1);
		Vector3d rayVector = eyePos.add(look.x * range, look.y * range, look.z * range);
		BlockRayTraceResult blockResult = entity.level.clip(new RayTraceContext(eyePos, rayVector, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, entity));
		EntityRayTraceResult entityResult = ProjectileHelper.getEntityHitResult(entity.getCommandSenderWorld(), entity, eyePos, rayVector,
				entity.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D, 1.0D, 1.0D), e -> !e.isSpectator() && e.isPickable());

		return entityResult != null && entityResult.getLocation().subtract(eyePos).length() <= blockResult.getLocation().subtract(eyePos).length() ? entityResult : blockResult;
	}

	@SuppressWarnings("resource")
	public static void swingArm(PlayerEntity player) {
		if (player.getCommandSenderWorld().isClientSide && player instanceof ClientPlayerEntity) {
			((ClientPlayerEntity) player).swing(Hand.MAIN_HAND);
		}
	}
}
