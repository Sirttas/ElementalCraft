package sirttas.elementalcraft.particle.element.source;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.AbstractECParticle;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

@OnlyIn(Dist.CLIENT)
public class ParticleSource extends AbstractECParticle {

	protected ParticleSource(ClientWorld worldIn, Vector3d coord, IAnimatedSprite sprite, ElementType type) {
		super(worldIn, coord);
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.prevPosX = coordX;
		this.prevPosY = coordY;
		this.prevPosZ = coordZ;
		this.posX = this.prevPosX;
		this.posY = this.prevPosY;
		this.posZ = this.prevPosZ;
		this.particleScale = 0.5F * (this.rand.nextFloat() * 0.2F + 0.5F);
		float f = this.rand.nextFloat() * 0.4F + 0.6F;
		this.particleRed = f * type.getRed();
		this.particleGreen = f * type.getGreen();
		this.particleBlue = f * type.getBlue();
		this.canCollide = false;
		this.maxAge = 300;
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
			float f1 = 1.0F - f;
			f1 = f1 * f1;
			f1 = f1 * f1;
			this.posX = this.coordX + this.motionX * f;
			this.posY = this.coordY + this.motionY * f;
			this.posZ = this.coordZ + this.motionZ * f;
			this.particleScale *= f - f1 * 1.2F;
		}
	}
	
	public static class Factory implements IParticleFactory<ElementTypeParticleData> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(ElementTypeParticleData data, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ParticleSource(worldIn, new Vector3d(x, y, z), this.spriteSet, data.getElementType());
		}
	}

}