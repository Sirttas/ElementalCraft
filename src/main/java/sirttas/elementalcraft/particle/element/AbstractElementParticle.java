package sirttas.elementalcraft.particle.element;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.AbstractECParticle;

public abstract class AbstractElementParticle extends AbstractECParticle {

	protected AbstractElementParticle(ClientWorld worldIn, Vector3d coord, ElementType type) {
		super(worldIn, coord);
		float f = this.random.nextFloat() * 0.4F + 0.6F;
		
		this.rCol = f * type.getRed();
		this.gCol = f * type.getGreen();
		this.bCol = f * type.getBlue();
		this.hasPhysics = false;
	}
	
	protected void usingDefaultSize() {
		this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
	}
}
