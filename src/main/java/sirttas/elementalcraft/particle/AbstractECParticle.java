package sirttas.elementalcraft.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractECParticle extends SingleQuadParticle {

	protected final double coordX;
	protected final double coordY;
	protected final double coordZ;
	
	protected AbstractECParticle(ClientLevel level, Vec3 coord, TextureAtlasSprite sprite) {
		super(level, coord.x(), coord.y(), coord.z(), sprite);
		this.coordX = coord.x();
		this.coordY = coord.y();
		this.coordZ = coord.z();
	}

	protected boolean checkLife() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
			return false;
		}
		return true;
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().move(x, y, z));
		this.setLocationFromBoundingbox();
	}
}
