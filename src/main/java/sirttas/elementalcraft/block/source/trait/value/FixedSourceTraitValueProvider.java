package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

public class FixedSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "fixed";
	public static final Codec<FixedSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.STRING.fieldOf(ECNames.NAME).forGetter(p -> p.translationKey),
			Codec.FLOAT.fieldOf(ECNames.VALUE).forGetter(FixedSourceTraitValueProvider::getValue)
	).apply(builder, FixedSourceTraitValueProvider::new));
	
	private final String translationKey;
	private final float value;
	
	
	public FixedSourceTraitValueProvider(String translationKey, float value) {
		this.translationKey = translationKey;
		this.value = value;
	}
	
	public float getValue() {
		return value;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos) {
		return new SourceTraitValue(value);
	}
	
	@Override
	public @NotNull SourceTraitValueProviderType<FixedSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.FIXED.get();
	}
	
	@Override
	public ISourceTraitValue load(Tag tag) {
		return tag instanceof FloatTag floatTag ? new SourceTraitValue(floatTag.getAsFloat()) : null;
	}

	@Override
	public Tag save(ISourceTraitValue value) {
		return value instanceof SourceTraitValue sourceTraitValue ? FloatTag.valueOf(sourceTraitValue.value) : null;
	}

	private class SourceTraitValue implements ISourceTraitValue {
		
		private final float value;
		
		private SourceTraitValue(float value) {
			this.value = value;
		}
		
		@Override
		public float getValue() {
			return value;
		}

		@Override
		public Component getDescription() {
			return Component.translatable(translationKey);
		}
	}
}
