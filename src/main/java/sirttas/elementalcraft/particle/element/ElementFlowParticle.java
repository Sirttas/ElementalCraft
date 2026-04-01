package sirttas.elementalcraft.particle.element;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

@OnlyIn(Dist.CLIENT)
public class ElementFlowParticle extends AbstractElementParticle {

	public static final ParticleResources.SpriteParticleRegistration<@NotNull ElementParticleData> FACTORY = s -> (data, level, x, y, z, xSpeed, ySpeed, zSpeed) -> new ElementFlowParticle(level, new Vec3(x, y, z), new Vec3(xSpeed, ySpeed, zSpeed), s, data.getElementType());
	
	private ElementFlowParticle(ClientLevel level, Vec3 coord, Vec3 speed, SpriteSet sprite, ElementType type) {
		super(level, coord, type);
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
