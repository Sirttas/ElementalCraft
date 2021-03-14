package sirttas.elementalcraft.particle.element.source;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

@OnlyIn(Dist.CLIENT)
public class ParticleSourceExhausted extends ParticleSource {

	private ParticleSourceExhausted(ClientWorld worldIn, Vector3d coord, IAnimatedSprite sprite, ElementType type) {
		super(worldIn, coord, sprite, type);
		this.particleScale = 0.1F * (this.rand.nextFloat() * 0.5F + 0.2F);
	}

	public static class Factory implements IParticleFactory<ElementTypeParticleData> {
		
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(ElementTypeParticleData data, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ParticleSourceExhausted(worldIn, new Vector3d(x, y, z), this.spriteSet, data.getElementType());
		}
	}

}