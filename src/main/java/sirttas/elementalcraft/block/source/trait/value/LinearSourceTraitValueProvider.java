package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.datafixers.Products;
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
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;
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
	protected final float luckRatio;
	protected final List<SourceTrait.Type> types;

	public LinearSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float end) {
		this(translationKey, types, 0, end);
	}

	public LinearSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float start, float end) {
		this(translationKey, types, start, end, 1);
	}

	public LinearSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float start, float end, float luckRatio) {
		this.translationKey = translationKey;
		this.start = start;
		this.end = end;
		this.types = types;
		this.luckRatio = luckRatio;
	}
	
    protected static <T extends LinearSourceTraitValueProvider> Products.P5<Mu<T>, String, List<SourceTrait.Type>, Float, Float, Float> codec(Instance<T> builder) {
        return builder.group(
                Codec.STRING.fieldOf(ECNames.NAME).forGetter(p -> p.translationKey),
				SourceTrait.Type.CODEC.listOf().fieldOf(ECNames.BONUS_TYPE).forGetter(LinearSourceTraitValueProvider::getTypes),
                Codec.FLOAT.optionalFieldOf(ECNames.START, 0F).forGetter(LinearSourceTraitValueProvider::getStart),
                Codec.FLOAT.fieldOf(ECNames.END).forGetter(LinearSourceTraitValueProvider::getEnd),
                Codec.FLOAT.fieldOf(ECNames.LUCK_RATIO).forGetter(LinearSourceTraitValueProvider::getEnd)
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
	public ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos) {
		return createValue(start, end, context.random(), context.luck());
	}

	@Nullable
	@Override
	public ISourceTraitValue breed(SourceTraitRollContext context, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
		var random = context.random();
		var luck = context.luck();

		if (value1 instanceof SourceTraitValue v1 && value2 instanceof SourceTraitValue v2) {
			return createValue(v1.value, v2.value, random, luck);
		} else if (value1 instanceof SourceTraitValue v1) {
			return createValue(v1.value, roll(start, end, random, luck), random, luck);
		} else if (value2 instanceof SourceTraitValue v2) {
			return createValue(v2.value, roll(start, end, random, luck), random, luck);
		}
		return createValue(start, end, random, luck);
	}

	private float roll(float s, float e, RandomSource rand, float luck) {
		if (s > e) {
			return roll(e, s, rand, luck);
		}

		var newStart = s + (luck * luckRatio);

		return newStart + rand.nextFloat() * (e - newStart);
	}

	private ISourceTraitValue createValue(float s, float e, RandomSource rand, float luck) {
		return createValue(roll(s, e, rand, luck));
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
