package sirttas.elementalcraft.particle.element;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.particle.AbstractECParticle;

@OnlyIn(Dist.CLIENT)
public class ParticleElementFlow extends AbstractECParticle {


	private ParticleElementFlow(World worldIn, Vec3d coord, Vec3d speed, IAnimatedSprite sprite, ElementType type) {
		super(worldIn, coord);
		this.motionX = speed.getX();
		this.motionY = speed.getY();
		this.motionZ = speed.getZ();
		this.prevPosX = coordX + motionX;
		this.prevPosY = coordY + motionY;
		this.prevPosZ = coordZ + motionZ;
		this.posX = this.prevPosX;
		this.posY = this.prevPosY;
		this.posZ = this.prevPosZ;
		this.particleScale = 0.1F * (this.rand.nextFloat() * 0.5F + 0.2F);
		float f = this.rand.nextFloat() * 0.4F + 0.6F;
		this.particleRed = f * type.getRed();
		this.particleGreen = f * type.getGreen();
		this.particleBlue = f * type.getBlue();
		this.canCollide = false;
		this.maxAge = (int) ((this.rand.nextInt(10) + 30) * speed.length());
		this.selectSpriteRandomly(sprite);
	}

	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.age++ >= this.maxAge) {
			this.setExpired();
		} else {
			float f = (float) this.age / (float) this.maxAge;
			f = 1.0F - f;
			this.posX = this.coordX + this.motionX * f;
			this.posY = this.coordY + this.motionY * f;
			this.posZ = this.coordZ + this.motionZ * f;
		}
	}

	public static class Factory implements IParticleFactory<ElementTypeParticleData> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(ElementTypeParticleData data, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ParticleElementFlow(worldIn, new Vec3d(x, y, z), new Vec3d(xSpeed, ySpeed, zSpeed), this.spriteSet, data.getElementType());
		}
	}

}