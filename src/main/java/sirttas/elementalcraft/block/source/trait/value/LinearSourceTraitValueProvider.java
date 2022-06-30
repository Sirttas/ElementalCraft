package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.ChatFormatting;
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

public class LinearSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "linear";
	public static final Codec<LinearSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> codec(builder)
	        .apply(builder, LinearSourceTraitValueProvider::new));
	
	protected final String translationKey;
	protected final float start;
	protected final float end;
	
	public LinearSourceTraitValueProvider(String translationKey, float end) {
		this(translationKey, 0, end);
	}
	
	public LinearSourceTraitValueProvider(String translationKey, float start, float end) {
		this.translationKey = translationKey;
		this.start = start;
		this.end = end;
	}
	
    protected static <T extends LinearSourceTraitValueProvider>  P3<Mu<T>, String, Float, Float> codec(Instance<T> builder) {
        return builder.group(
                Codec.STRING.fieldOf(ECNames.NAME).forGetter(p -> p.translationKey),
                Codec.FLOAT.optionalFieldOf(ECNames.START, 0F).forGetter(LinearSourceTraitValueProvider::getStart),
                Codec.FLOAT.fieldOf(ECNames.END).forGetter(LinearSourceTraitValueProvider::getEnd)
        );
    }
    
	
	public float getStart() {
		return start;
	}
	public float getEnd() {
		return end;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos) {
		return createValue(start + level.random.nextFloat() * (end - start));
	}
	
	@Override
	public @NotNull SourceTraitValueProviderType<? extends LinearSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.LINEAR.get();
	}
	
	@Override
	public ISourceTraitValue load(Tag tag) {
		return tag instanceof FloatTag floatTag ? createValue(floatTag.getAsFloat()) : null;
	}

	@Override
	public Tag save(ISourceTraitValue value) {
		return value instanceof SourceTraitValue sourceTraitValue ? FloatTag.valueOf(sourceTraitValue.value) : null;
	}

	protected ISourceTraitValue createValue(float value) {
	    return new SourceTraitValue(value);
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

		public float getRatio() {
			var e = end - start;

			if (e == 0) {
				return 0;
			}
			return (value - start) / e;
		}

		@Override
		public Component getDescription() {
			var ratio = getRatio();

			return Component.translatable("source_trait.elementalcraft.linear", Component.translatable(translationKey), Math.round(ratio * 100F)).withStyle(getFormatting(ratio));
		}

		private ChatFormatting getFormatting(float ratio) {
			if (ratio < 0.25F) {
				return ChatFormatting.RED;
			} else if (ratio < 0.5F) {
				return ChatFormatting.YELLOW;
			}
			return ChatFormatting.GREEN;
		}
	}
}
