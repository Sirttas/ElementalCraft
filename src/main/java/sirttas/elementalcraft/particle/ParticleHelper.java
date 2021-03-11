package sirttas.elementalcraft.particle;

import java.util.Random;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

public class ParticleHelper {
	
	private ParticleHelper() {}
	
	public static void createSourceParticle(ElementType type, World world, Vector3d pos, Random rand) {
		double x = pos.getX() + (rand.nextDouble() * 2 - 1) / 16;
		double y = pos.getY() - 3D / 16;
		double z = pos.getZ() + (rand.nextDouble() * 2 - 1) / 16;

		world.addParticle(new ElementTypeParticleData(ECParticles.SOURCE, type), x, y, z, 0F, 0F, 0F);
	}
	
	public static void createExhaustedSourceParticle(ElementType type, World world, Vector3d pos, Random rand) {
		double x = pos.getX() + (rand.nextDouble() * 6 - 3) / 16;
		double y = pos.getY() - 3D / 16;
		double z = pos.getZ() + (rand.nextDouble() * 6 - 3) / 16;

		world.addParticle(new ElementTypeParticleData(ECParticles.SOURCE_EXHAUSTED, type), x, y, z, 0F, 0F, 0F);
	}

	public static void createCraftingParticle(ElementType type, World world, Vector3d pos, Random rand) {
		double x = pos.getX() + (rand.nextDouble() * 2 - 1) / 16;
		double y = pos.getY() - 3D / 16;
		double z = pos.getZ() + (rand.nextDouble() * 2 - 1) / 16;

		IntStream.range(0, 8 + rand.nextInt(5))
				.forEach(i -> world.addParticle(new ElementTypeParticleData(ECParticles.ELEMENT_CRAFTING, type != ElementType.NONE ? type : ElementType.random(rand)), x, y, z, 0F, 0F, 0F));
	}

	public static void createElementFlowParticle(ElementType type, World world, Vector3d pos, Direction direction, int scale, Random rand) {
		createElementFlowParticle(type, world, pos, Vector3d.copy(direction.getOpposite().getDirectionVec()).scale(scale == 0 ? 1 : scale), rand);
	}

	public static void createElementFlowParticle(ElementType type, World world, Vector3d pos, Vector3d flow, Random rand) {
		double x = pos.getX() + (rand.nextDouble() * 6 - 3) / 16;
		double y = pos.getY() - 3D / 16;
		double z = pos.getZ() + (rand.nextDouble() * 6 - 3) / 16;

		world.addParticle(new ElementTypeParticleData(ECParticles.ELEMENT_FLOW, type), x, y, z, flow.getX(), flow.getY(), flow.getZ());
	}

	public static void createEnderParticle(World world, Vector3d pos, int count, Random rand) {
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

	public static void createItemBreakParticle(World world, Vector3d pos, Random rand, ItemStack stack, int count) {
		for (int i = 0; i < count; ++i) {
			Vector3d speed = new Vector3d(0, rand.nextDouble() * 0.1 + 0.1, 0);
			Vector3d loc = pos.add(0, (rand.nextDouble() * 0.2 - 0.2), 0);

			if (world instanceof ServerWorld) {
				((ServerWorld) world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), loc.x, loc.y, loc.z, 1, speed.x, speed.y + 0.05D, speed.z, 0.0D);
			} else {
				world.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), loc.x, loc.y, loc.z, speed.x, speed.y + 0.05D, speed.z);
			}
		}

	}
}
