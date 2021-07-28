package sirttas.elementalcraft.entity;

import java.util.stream.Stream;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityHelper {

	private EntityHelper() {}
	
	public static Stream<ItemStack> handStream(Player player) {
		return Stream.of(player.getMainHandItem(), player.getOffhandItem());

	}

	public static HitResult rayTrace(Entity entity, double range) {
		Vec3 eyePos = entity.getEyePosition(1);
		Vec3 look = entity.getViewVector(1);
		Vec3 rayVector = eyePos.add(look.x * range, look.y * range, look.z * range);
		BlockHitResult blockResult = entity.level.clip(new ClipContext(eyePos, rayVector, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
		EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(entity.getCommandSenderWorld(), entity, eyePos, rayVector,
				entity.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D, 1.0D, 1.0D), e -> !e.isSpectator() && e.isPickable());

		return entityResult != null && entityResult.getLocation().subtract(eyePos).length() <= blockResult.getLocation().subtract(eyePos).length() ? entityResult : blockResult;
	}

	@SuppressWarnings("resource")
	public static void swingArm(Player player) {
		if (player.getCommandSenderWorld().isClientSide && player instanceof LocalPlayer) {
			((LocalPlayer) player).swing(InteractionHand.MAIN_HAND);
		}
	}
	
	public static boolean isHostile(Entity entity) {
		return entity instanceof Mob && ((Mob) entity).isAggressive();
	}
}
