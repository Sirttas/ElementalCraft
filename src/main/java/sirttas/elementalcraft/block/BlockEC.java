package sirttas.elementalcraft.block;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.DistExecutor;
import sirttas.elementalcraft.property.ECProperties;

public class BlockEC extends Block implements IBlockEC {

	public static final float BIT_SIZE = 0.0625f; // 1/16

	public BlockEC() {
		this(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
	}

	public BlockEC(AbstractBlock.Properties properties) {
		super(properties);
	}

	public static <T> Optional<T> getTileEntityAs(@Nonnull IBlockReader world, BlockPos pos, Class<T> clazz) {
		return Optional.ofNullable(world.getTileEntity(pos)).filter(clazz::isInstance).map(clazz::cast);
	}

	protected RayTraceResult rayTrace(IBlockReader world, Entity entity) {
		return DistExecutor.unsafeRunForDist(() -> () -> Minecraft.getInstance().objectMouseOver, () -> () -> {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				float f = player.rotationPitch;
				float f1 = player.rotationYaw;
				Vector3d vec3d = player.getEyePosition(1.0F);
				float f2 = MathHelper.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
				float f3 = MathHelper.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
				float f4 = -MathHelper.cos(-f * ((float) Math.PI / 180F));
				float f5 = MathHelper.sin(-f * ((float) Math.PI / 180F));
				float f6 = f3 * f4;
				float f7 = f2 * f4;
				double d0 = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
				Vector3d vec3d1 = vec3d.add(f6 * d0, f5 * d0, f7 * d0);

				return world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));
			}
			return null;
		});
	}

	protected static boolean doesVectorColide(AxisAlignedBB bb, Vector3d vec) {
		return vec.x >= bb.minX && vec.y >= bb.minY && vec.z >= bb.minZ && vec.x <= bb.maxX && vec.y <= bb.maxY && vec.z <= bb.maxZ;
	}
}
