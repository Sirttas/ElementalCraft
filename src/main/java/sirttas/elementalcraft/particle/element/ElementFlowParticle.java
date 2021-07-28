package sirttas.elementalcraft.particle.element;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;

@OnlyIn(Dist.CLIENT)
public class ElementFlowParticle extends AbstractElementParticle {

	public static final SpriteParticleRegistration<ElementTypeParticleData> FACTORY = s -> (data, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> 
			new ElementFlowParticle(worldIn, new Vec3(x, y, z), new Vec3(xSpeed, ySpeed, zSpeed), s, data.getElementType());
	
	private ElementFlowParticle(ClientLevel worldIn, Vec3 coord, Vec3 speed, SpriteSet sprite, ElementType type) {
		super(worldIn, coord, type);
		this.xd = speed.x();
		this.yd = speed.y();
		this.zd = speed.z();
		this.xo = coordX + xd;
		this.yo = coordY + yd;
		this.zo = coordZ + zd;
		this.x = this.xo;
		this.y = this.yo;
		this.z = this.zo;
		usingDefaultSize();
		this.lifetime = (int) ((this.random.nextInt(10) + 30) * speed.length());
		this.pickSprite(sprite);
	}

	@Override
	public void tick() {
		if (checkLife()) {
			float f = (float) this.age / (float) this.lifetime;
			f = 1.0F - f;
			this.x = this.coordX + this.xd * f;
			this.y = this.coordY + this.yd * f;
			this.z = this.coordZ + this.zd * f;
		}
	}
}