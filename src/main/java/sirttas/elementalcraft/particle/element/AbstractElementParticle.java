package sirttas.elementalcraft.particle.element;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.AbstractECParticle;

public abstract class AbstractElementParticle extends AbstractECParticle {

	private static final SingleQuadParticle.Layer LAYER = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.TRANSLUCENT_PARTICLE); // TODO RenderPipeline

	protected AbstractElementParticle(ClientLevel level, Vec3 coord, TextureAtlasSprite sprite, ElementType type) {
		super(level, coord, sprite);
		float f = this.random.nextFloat() * 0.3F + 0.7F;

		this.rCol = f * type.getRed();
		this.gCol = f * type.getGreen();
		this.bCol = f * type.getBlue();
		this.hasPhysics = false;
	}


    @Override
    protected @NotNull Layer getLayer() {
        return LAYER;
    }

	protected void usingDefaultSize() {
		this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
	}
}
