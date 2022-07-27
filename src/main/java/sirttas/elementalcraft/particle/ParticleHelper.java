package sirttas.elementalcraft.particle;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

import java.util.stream.IntStream;

public class ParticleHelper {
	
	private ParticleHelper() {}
	
	public static void createSourceParticle(ElementType type, Level world, Vec3 pos, RandomSource rand) {
		double x = pos.x() + (rand.nextDouble() * 2 - 1) / 16;
		double y = pos.y() - 3D / 16;
		double z = pos.z() + (rand.nextDouble() * 2 - 1) / 16;

		world.addParticle(new ElementTypeParticleData(ECParticles.SOURCE.get(), type), x, y, z, 0F, 0F, 0F);
	}

	public static void createCraftingParticle(ElementType type, Level world, Vec3 pos, RandomSource rand) {
		double x = pos.x() + (rand.nextDouble() * 2 - 1) / 16;
		double y = pos.y() - 3D / 16;
		double z = pos.z() + (rand.nextDouble() * 2 - 1) / 16;

		IntStream.range(0, 8 + rand.nextInt(5))
				.forEach(i -> world.addParticle(new ElementTypeParticleData(ECParticles.ELEMENT_CRAFTING.get(), type != ElementType.NONE ? type : ElementType.random(rand)), x, y, z, 0F, 0F, 0F));
	}

	public static void createElementFlowParticle(ElementType type, Level level, Vec3 end, Direction direction, float scale, RandomSource rand) {
		createElementFlowParticle(type, level, end, Vec3.atLowerCornerOf(direction.getOpposite().getNormal()).scale(scale <= 0 ? 1F : scale), new Vec3(3,3,3), rand);
	}

	public static void createElementFlowParticle(ElementType type, Level level, Vec3 start, Vec3 end, RandomSource rand) {
		createElementFlowParticle(type, level, end, start.subtract(end), new Vec3(1, 1, 1), rand);
	}

	private static void createElementFlowParticle(ElementType type, Level level, Vec3 end, Vec3 flow, Vec3 radius, RandomSource rand) {
		double x = end.x() + (((2 * rand.nextDouble()) - 1) * radius.x()) / 16;
		double y = end.y() + (((2 * rand.nextDouble()) - 1) * radius.y()) / 16;
		double z = end.z() + (((2 * rand.nextDouble()) - 1) * radius.z()) / 16;

		level.addParticle(new ElementTypeParticleData(ECParticles.ELEMENT_FLOW.get(), type), x, y, z, flow.x(), flow.y(), flow.z());
	}


	public static void createEnderParticle(Level world, Vec3 pos, int count, RandomSource rand) {
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

	public static void createItemBreakParticle(Level world, Vec3 pos, RandomSource rand, ItemStack stack, int count) {
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
