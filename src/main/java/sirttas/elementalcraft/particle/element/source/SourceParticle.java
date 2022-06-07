package sirttas.elementalcraft.particle.element.source;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.AbstractElementParticle;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

@OnlyIn(Dist.CLIENT)
public class SourceParticle extends AbstractElementParticle {

	public static final SpriteParticleRegistration<ElementTypeParticleData> FACTORY = s -> (data, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> new SourceParticle(worldIn, new Vec3(x, y, z), s, data.getElementType());
	
	protected SourceParticle(ClientLevel worldIn, Vec3 coord, SpriteSet sprite, ElementType type) {
		super(worldIn, coord, type);
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
		this.xo = coordX;
		this.yo = coordY;
		this.zo = coordZ;
		this.x = this.xo;
		this.y = this.yo;
		this.z = this.zo;
		this.quadSize = 0.5F * (this.random.nextFloat() * 0.2F + 0.5F);
		this.hasPhysics = false;
		this.lifetime = 300;
		this.pickSprite(sprite);
	}

	@Override
	public void tick() {
		if (checkLife()) {
			float f = (float) this.age / (float) this.lifetime;
			f = 1.0F - f;
			float f1 = 1.0F - f;
			f1 = f1 * f1;
			f1 = f1 * f1;
			this.x = this.coordX + this.xd * f;
			this.y = this.coordY + this.yd * f;
			this.z = this.coordZ + this.zd * f;
			this.quadSize *= f - f1 * 1.2F;
		}
	}
}
