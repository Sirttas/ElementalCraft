package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;

import javax.annotation.Nullable;
import java.util.Map;

public record FixedSourceTraitValueProvider(
		String translationKey,
		Map<SourceTrait.Type, Float> values
) implements ISourceTraitValueProvider {

	public static final String NAME = "fixed";
	public static final Codec<FixedSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.STRING.fieldOf(ECNames.NAME).forGetter(FixedSourceTraitValueProvider::translationKey),
			SourceTrait.Type.VALUE_CODEC.fieldOf(ECNames.VALUES).forGetter(FixedSourceTraitValueProvider::values)
	).apply(builder, FixedSourceTraitValueProvider::new));

	
	@Override
	public ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos) {
		return new SourceTraitValue();
	}

	@Override
	public ISourceTraitValue breed(SourceTraitRollContext context, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
		return new SourceTraitValue();
	}

	@Override
	public @NotNull SourceTraitValueProviderType<FixedSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.FIXED.get();
	}
	
	@Override
	public ISourceTraitValue load(Tag tag) {
		return new SourceTraitValue();
	}

	@Override
	public Tag save(ISourceTraitValue value) {
		return ByteTag.ONE;
	}

	private class SourceTraitValue implements ISourceTraitValue {
		
		@Override
		public float getValue(SourceTrait.Type type) {
			return values.getOrDefault(type, 1f);
		}

		@Override
		public Component getDescription() {
			return Component.translatable(translationKey);
		}
	}
}
