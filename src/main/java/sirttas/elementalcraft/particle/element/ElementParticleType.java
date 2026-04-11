package sirttas.elementalcraft.particle.element;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import javax.annotation.Nonnull;
import java.util.function.Function;

public record ElementParticleType(
		ParticleType<@NotNull ElementParticleType> type,
		ElementType elementType
) implements ParticleOptions, IElementTypeProvider {

	@Nonnull
    @Override
	public ParticleType<@NotNull ElementParticleType> getType() {
		return type;
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}

	public static ParticleType<@NotNull ElementParticleType> createParticleType(boolean overrideLimiter) {
		return new ParticleType<>(overrideLimiter) {

			private final MapCodec<ElementParticleType> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
					ElementType.forGetter(ElementParticleType::getElementType)
			).apply(instance, t -> new ElementParticleType(this, t)));
			private final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ElementParticleType> streamCodec = ElementType.STREAM_CODEC
					.map(t -> new ElementParticleType(this, t), ElementParticleType::getElementType)
					.mapStream(Function.identity());

			@Nonnull
            @Override
			public MapCodec<ElementParticleType> codec() {
				return codec;
			}

			@Nonnull
			@Override
			public StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ElementParticleType> streamCodec() {
				return streamCodec;
			}
		};
	}
}
