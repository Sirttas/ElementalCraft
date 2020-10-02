package sirttas.elementalcraft.particle;

import java.util.Random;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.particle.element.ParticleElementCrafting;
import sirttas.elementalcraft.particle.element.ParticleElementFlow;
import sirttas.elementalcraft.particle.element.ParticleSource;

@OnlyIn(Dist.CLIENT)
public class ParticleHelper {
	public static void createSourceParticle(ElementType type, World world, Vec3d pos, Random rand) {
		double x = pos.getX() + (7 + rand.nextDouble() * 2) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 5 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (7 + rand.nextDouble() * 2) * BlockEC.BIT_SIZE;

		world.addParticle(ParticleSource.createData(type), x, y, z, 0F, 0F, 0F);
	}

	public static void createCraftingParticle(ElementType type, World world, Vec3d pos, Random rand) {
		double x = pos.getX() + (7 + rand.nextDouble() * 2) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 5 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (7 + rand.nextDouble() * 2) * BlockEC.BIT_SIZE;

		IntStream.range(0, 8 + rand.nextInt(5)).forEach(i -> world.addParticle(ParticleElementCrafting.createData(type != ElementType.NONE ? type : ElementType.random(rand)), x, y, z, 0F, 0F, 0F));
	}

	public static void createElementFlowParticle(ElementType type, World world, Vec3d pos, Direction direction, float scale, Random rand) {
		double x = pos.getX() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 5 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
		Vec3d flow = new Vec3d(direction.getOpposite().getDirectionVec()).scale(scale == 0 ? 1 : scale);

		world.addParticle(ParticleElementFlow.createData(type), x, y, z, flow.getX(), flow.getY(), flow.getZ());
	}

	public static void createEnderParticle(World world, Vec3d pos, int count, Random rand) {
		for (int i = 0; i < count; ++i) {
			int j = rand.nextInt(2) * 2 - 1;
			int k = rand.nextInt(2) * 2 - 1;
			double d0 = pos.getX() + 0.5D + 0.25D * j;
			double d1 = pos.getY() + rand.nextFloat();
			double d2 = pos.getZ() + 0.5D + 0.25D * k;
			double d3 = rand.nextFloat() * j;
			double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
			double d5 = rand.nextFloat() * k;
			world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		}
	}

	public static void createItemBreakParticle(World world, Vec3d pos, Random rand, ItemStack stack, int count) {
		for (int i = 0; i < count; ++i) {
			Vec3d speed = new Vec3d(0, rand.nextDouble() * 0.1 + 0.1, 0);
			Vec3d loc = pos.add(0, (rand.nextDouble() * 0.2 - 0.2), 0);

			if (world instanceof ServerWorld) {
				((ServerWorld) world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), loc.x, loc.y, loc.z, 1, speed.x, speed.y + 0.05D, speed.z, 0.0D);
			} else {
				world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), loc.x, loc.y, loc.z, speed.x, speed.y + 0.05D, speed.z);
			}
		}

	}
}
