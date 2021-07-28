package sirttas.elementalcraft.particle;

import java.util.Random;
import java.util.stream.IntStream;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

public class ParticleHelper {
	
	private ParticleHelper() {}
	
	public static void createSourceParticle(ElementType type, Level world, Vec3 pos, Random rand) {
		double x = pos.x() + (rand.nextDouble() * 2 - 1) / 16;
		double y = pos.y() - 3D / 16;
		double z = pos.z() + (rand.nextDouble() * 2 - 1) / 16;

		world.addParticle(new ElementTypeParticleData(ECParticles.SOURCE, type), x, y, z, 0F, 0F, 0F);
	}
	
	public static void createExhaustedSourceParticle(ElementType type, Level world, Vec3 pos, Random rand) {
		double x = pos.x() + (rand.nextDouble() * 6 - 3) / 16;
		double y = pos.y() - 3D / 16;
		double z = pos.z() + (rand.nextDouble() * 6 - 3) / 16;

		world.addParticle(new ElementTypeParticleData(ECParticles.SOURCE_EXHAUSTED, type), x, y, z, 0F, 0F, 0F);
	}

	public static void createCraftingParticle(ElementType type, Level world, Vec3 pos, Random rand) {
		double x = pos.x() + (rand.nextDouble() * 2 - 1) / 16;
		double y = pos.y() - 3D / 16;
		double z = pos.z() + (rand.nextDouble() * 2 - 1) / 16;

		IntStream.range(0, 8 + rand.nextInt(5))
				.forEach(i -> world.addParticle(new ElementTypeParticleData(ECParticles.ELEMENT_CRAFTING, type != ElementType.NONE ? type : ElementType.random(rand)), x, y, z, 0F, 0F, 0F));
	}

	public static void createElementFlowParticle(ElementType type, Level world, Vec3 pos, Direction direction, int scale, Random rand) {
		createElementFlowParticle(type, world, pos, Vec3.atLowerCornerOf(direction.getOpposite().getNormal()).scale(scale == 0 ? 1 : scale), rand);
	}

	public static void createElementFlowParticle(ElementType type, Level world, Vec3 pos, Vec3 flow, Random rand) {
		double x = pos.x() + (rand.nextDouble() * 6 - 3) / 16;
		double y = pos.y() - 3D / 16;
		double z = pos.z() + (rand.nextDouble() * 6 - 3) / 16;

		world.addParticle(new ElementTypeParticleData(ECParticles.ELEMENT_FLOW, type), x, y, z, flow.x(), flow.y(), flow.z());
	}

	public static void createEnderParticle(Level world, Vec3 pos, int count, Random rand) {
		for (int i = 0; i < count; ++i) {
			int j = rand.nextInt(2) * 2 - 1;
			int k = rand.nextInt(2) * 2 - 1;
			double d0 = pos.x() + 0.5D + 0.25D * j;
			double d1 = pos.y() + rand.nextFloat();
			double d2 = pos.z() + 0.5D + 0.25D * k;
			double d3 = rand.nextFloat() * j;
			double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
			double d5 = rand.nextFloat() * k;
			world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		}
	}

	public static void createItemBreakParticle(Level world, Vec3 pos, Random rand, ItemStack stack, int count) {
		for (int i = 0; i < count; ++i) {
			Vec3 speed = new Vec3(0, rand.nextDouble() * 0.1 + 0.1, 0);
			Vec3 loc = pos.add(0, (rand.nextDouble() * 0.2 - 0.2), 0);

			if (world instanceof ServerLevel) {
				((ServerLevel) world).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), loc.x, loc.y, loc.z, 1, speed.x, speed.y + 0.05D, speed.z, 0.0D);
			} else {
				world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), loc.x, loc.y, loc.z, speed.x, speed.y + 0.05D, speed.z);
			}
		}

	}
}
