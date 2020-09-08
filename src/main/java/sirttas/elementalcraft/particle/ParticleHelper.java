package sirttas.elementalcraft.particle;

import java.util.Random;

import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockEC;

@OnlyIn(Dist.CLIENT)
public class ParticleHelper {
	public static void createSourceParticle(ElementType type, World world, Vector3d pos, Random rand) {
		double x = pos.getX() + (7 + rand.nextDouble() * 2) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 5 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (7 + rand.nextDouble() * 2) * BlockEC.BIT_SIZE;

		world.addParticle(ParticleSource.createData(type), x, y, z, 0F, 0F, 0F);
	}

	public static void createElementFlowParticle(ElementType type, World world, Vector3d pos, Direction direction, Random rand) {
		double x = pos.getX() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 5 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
		Vector3i flow = direction.getOpposite().getDirectionVec();

		world.addParticle(ParticleElementFlow.createData(type), x, y, z, flow.getX(), flow.getY(), flow.getZ());
	}
}
