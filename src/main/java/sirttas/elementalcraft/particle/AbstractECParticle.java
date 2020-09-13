package sirttas.elementalcraft.particle;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;

public abstract class AbstractECParticle extends SpriteTexturedParticle {

	protected final double coordX;
	protected final double coordY;
	protected final double coordZ;

	public AbstractECParticle(ClientWorld world, Vector3d coord) {
		super(world, coord.getX(), coord.getY(), coord.getZ());
		this.coordX = coord.getX();
		this.coordY = coord.getY();
		this.coordZ = coord.getZ();
	}


	@Override
	public IParticleRenderType getRenderType() {
		return ECParticles.EC_RENDER;
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	@Override
	public int getBrightnessForRender(float partialTick) {
		int i = super.getBrightnessForRender(partialTick);
		float f = (float) this.age / (float) this.maxAge;
		f = f * f;
		f = f * f;
		int j = i & 255;
		int k = i >> 16 & 255;
		k = k + (int) (f * 15.0F * 16.0F);
		if (k > 240) {
			k = 240;
		}
	
		return j | k << 16;
	}

}