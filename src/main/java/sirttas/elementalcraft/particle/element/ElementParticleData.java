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

public record ElementParticleData(
		ParticleType<@NotNull ElementParticleData> type,
		ElementType elementType
) implements ParticleOptions, IElementTypeProvider {

	@Nonnull
    @Override
	public ParticleType<@NotNull ElementParticleData> getType() {
		return type;
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}

	public static ParticleType<@NotNull ElementParticleData> createParticleType(boolean overrideLimiter) {
		return new ParticleType<>(overrideLimiter) {

			private final MapCodec<ElementParticleData> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
					ElementType.forGetter(ElementParticleData::getElementType)
			).apply(instance, t -> new ElementParticleData(this, t)));
			private final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ElementParticleData> streamCodec = ElementType.STREAM_CODEC
					.map(t -> new ElementParticleData(this, t), ElementParticleData::getElementType)
					.mapStream(Function.identity());

			@Nonnull
            @Override
			public MapCodec<ElementParticleData> codec() {
				return codec;
			}

			@Nonnull
			@Override
			public StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ElementParticleData> streamCodec() {
				return streamCodec;
			}
		};
	}
}
