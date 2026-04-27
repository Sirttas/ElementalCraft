package sirttas.elementalcraft.particle.element.source;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.particle.element.AbstractElementParticle;
import sirttas.elementalcraft.particle.element.ElementParticleType;

public class SourceParticle extends AbstractElementParticle {
	
	protected SourceParticle(ClientLevel level, Vec3 coord, TextureAtlasSprite sprite, ElementType type) {
		super(level, coord, sprite, type);
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

    public static class Provider implements ParticleProvider<@NotNull ElementParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull ElementParticleType elementParticleType, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5, @NotNull RandomSource randomSource) {
            return new SourceParticle(clientLevel, new Vec3(v, v1, v2), sprites.get(randomSource), elementParticleType.getElementType());
        }
    }
}
