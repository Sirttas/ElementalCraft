package sirttas.elementalcraft.particle.element;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;

@OnlyIn(Dist.CLIENT)
public class ElementFlowParticle extends AbstractElementParticle {

	private ElementFlowParticle(ClientLevel level, Vec3 coord, Vec3 speed, TextureAtlasSprite sprite, ElementType type) {
		super(level, coord, sprite, type);
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

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<@NotNull ElementParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull ElementParticleType elementParticleType, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5, @NotNull RandomSource randomSource) {
            return new ElementFlowParticle(clientLevel, new Vec3(v, v1, v2), new Vec3(v3, v4, v5), sprites.get(randomSource), elementParticleType.getElementType());
        }
    }
}
