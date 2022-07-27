package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.datafixers.Products.P4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

import javax.annotation.Nullable;
import java.util.List;

public class LinearSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "linear";
	public static final Codec<LinearSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> codec(builder)
	        .apply(builder, LinearSourceTraitValueProvider::new));
	
	protected final String translationKey;
	protected final float start;
	protected final float end;
	protected final List<SourceTrait.Type> types;

	public LinearSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float end) {
		this(translationKey, types, 0, end);
	}
	
	public LinearSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float start, float end) {
		this.translationKey = translationKey;
		this.start = start;
		this.end = end;
		this.types = types;
	}
	
    protected static <T extends LinearSourceTraitValueProvider>  P4<Mu<T>, String, List<SourceTrait.Type>, Float, Float> codec(Instance<T> builder) {
        return builder.group(
                Codec.STRING.fieldOf(ECNames.NAME).forGetter(p -> p.translationKey),
				SourceTrait.Type.CODEC.listOf().fieldOf(ECNames.BONUS_TYPE).forGetter(LinearSourceTraitValueProvider::getTypes),
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

	private List<SourceTrait.Type> getTypes() {
		return types;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos) {
		return createValue(start, end, level.random);
	}

	@Nullable
	@Override
	public ISourceTraitValue breed(SourceTrait trait, Level level, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
		if (value1 instanceof SourceTraitValue v1 && value2 instanceof SourceTraitValue v2) {
			return createValue(v1.value, v2.value, level.random);
		} else if (value1 instanceof SourceTraitValue v1) {
			return createValue(v1.value, roll(start, end, level.random), level.random);
		} else if (value2 instanceof SourceTraitValue v2) {
			return createValue(v2.value, roll(start, end, level.random), level.random);
		}
		return createValue(start, end, level.random);
	}

	private float roll(float s, float e, RandomSource rand) {
		if (s > e) {
			float tmp = s;

			s = e;
			e = tmp;
		}
		return s + rand.nextFloat() * (e - s);
	}

	private ISourceTraitValue createValue(float s, float e, RandomSource rand) {
		return createValue(roll(s, e, rand));
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
		public float getValue(SourceTrait.Type type) {
			if (types.contains(type)) {
				return value;
			}
			return 1;
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
