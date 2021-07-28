package sirttas.elementalcraft.particle.element.source;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

@OnlyIn(Dist.CLIENT)
public class SourceExhaustedParticle extends SourceParticle {

	public static final SpriteParticleRegistration<ElementTypeParticleData> FACTORY = s -> (data, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> 
			new SourceExhaustedParticle(worldIn, new Vec3(x, y, z), s, data.getElementType());
	
	private SourceExhaustedParticle(ClientLevel worldIn, Vec3 coord, SpriteSet sprite, ElementType type) {
		super(worldIn, coord, sprite, type);
		usingDefaultSize();
	}
}