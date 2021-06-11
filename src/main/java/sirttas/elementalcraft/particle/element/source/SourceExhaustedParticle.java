package sirttas.elementalcraft.particle.element.source;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.ParticleManager.IParticleMetaFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;

@OnlyIn(Dist.CLIENT)
public class SourceExhaustedParticle extends SourceParticle {

	public static final IParticleMetaFactory<ElementTypeParticleData> FACTORY = s -> (data, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> 
			new SourceExhaustedParticle(worldIn, new Vector3d(x, y, z), s, data.getElementType());
	
	private SourceExhaustedParticle(ClientWorld worldIn, Vector3d coord, IAnimatedSprite sprite, ElementType type) {
		super(worldIn, coord, sprite, type);
		usingDefaultSize();
	}
}