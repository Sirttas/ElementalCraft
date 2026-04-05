package sirttas.elementalcraft.api.source.trait.value;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StepsSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "steps";
	public static final MapCodec<StepsSourceTraitValueProvider> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			Step.CODEC.listOf().fieldOf(ECNames.STEPS).forGetter(p -> p.steps)
	).apply(builder, StepsSourceTraitValueProvider::new));
	
	private final List<Step> steps;

	private final Codec<ISourceTraitValue> valueCodec;
	private final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ISourceTraitValue> valueStreamCodec;

	public static Builder builder(String translationPrefix) {
		return new Builder(translationPrefix);
	}

	private StepsSourceTraitValueProvider(List<Step> steps) {
		this.steps = ImmutableList.copyOf(steps);
		this.valueCodec = Codec.STRING.xmap(this::findStep, StepsSourceTraitValueProvider::getStringValue);
		this.valueStreamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, StepsSourceTraitValueProvider::getStringValue, this::findStep);
	}

    private static String getStringValue(ISourceTraitValue value) {
        if (!(value instanceof Step step)) {
            throw new IllegalArgumentException("Source trait value must be of type Step");
        }
        return step.name();
    }

	@Override
	public ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos) {
		return rollStep(context.random(), context.luck(), steps.stream()
				.filter(s -> s.predicate().test(level, pos, null))
				.toList());
	}

	@Nullable
	@Override
	public ISourceTraitValue breed(SourceTraitRollContext context, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
		var random = context.random();
		var luck = context.luck();

		if (value1 instanceof Step step1 && value2 instanceof Step step2) {
			var b1 = step1.breedIndex();
			var b2 = step2.breedIndex();

			return rollStep(random, luck, getMin(b1, b2), getMax(b1, b2));
		} else if (value1 instanceof Step step) {
			return rollStep(random, luck, step);
		} else if (value2 instanceof Step step) {
			return rollStep(random, luck, step);
		}
		return rollStep(random, luck, -1, 1);
	}

	private int getMin(int b1, int b2) {
		var min = Math.min(b1, b2) - 1;

		if (steps.stream().anyMatch(s -> s.breedIndex() == min)) {
			return min;
		} else {
			return min - 1;
		}
	}

	private int getMax(int b1, int b2) {
		var max = Math.max(b1, b2) + 1;

		if (steps.stream().anyMatch(s -> s.breedIndex() == max)) {
			return max;
		} else {
			return max + 1;
		}
	}

	@Nullable
	private Step findStep(String name) {
		return steps.stream()
				.filter(s -> s.name().equals(name))
				.findFirst()
				.orElse(null);
	}

	@Nullable
	private Step rollStep(RandomSource random, float luck, Step step) {
		var index = step.breedIndex();

		return rollStep(random, luck, index - 1, index + 1);
	}

	@Nullable
	private Step rollStep(RandomSource random, float luck, int min, int max) {
		return rollStep(random, luck, steps.stream()
				.filter(s -> s.breedIndex() >= min && s.breedIndex() <= max)
				.toList());
	}

	@Nullable
	private Step rollStep(RandomSource random, float luck, List<Step> list) {
		var bound = list.stream()
				.mapToInt(s -> s.weight(luck))
				.sum();

		if (bound <= 0) {
			return null;
		}

		var roll = random.nextInt(bound);

		if (roll <= 0) {
			return null;
		}

		for (var step : list) {
			roll -= step.weight(luck);
			if (roll < 0) {
				return step;
			}
		}
		return null;
	}

	@Nonnull
	@Override
	public SourceTraitValueProviderType<StepsSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.STEPS.get();
	}

    @Override
	public Codec<ISourceTraitValue> valueCodec() {
		return valueCodec;
	}

	@Override
	public StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ISourceTraitValue> valueStreamCodec() {
		return valueStreamCodec;
	}

	public static class Builder implements ISourceTraitValueProviderBuilder {

		private final String translationPrefix;
		private final List<Step> steps;
		
		private Builder(String translationPrefix) {
            this.translationPrefix = translationPrefix;
            steps = new ArrayList<>();
		}

		public Builder step(String name, int weight, Map<SourceTrait.Type, Float> values, int breedIndex) {
			return step(name, weight, values, breedIndex, IBlockPosPredicate.any());
		}

		public Builder step(String name, String translationKey, int weight, Map<SourceTrait.Type, Float> values, int breedIndex) {
			return step(name, translationKey, weight, values, breedIndex, IBlockPosPredicate.any());
		}

		public Builder step(String name, int weight, float luckRatio, Map<SourceTrait.Type, Float> values, int breedIndex) {
			return step(name, weight, luckRatio, values, breedIndex, IBlockPosPredicate.any());
		}

		public Builder step(String name, String translationKey, int weight, float luckRatio, Map<SourceTrait.Type, Float> values, int breedIndex) {
			return step(name, translationKey, weight, luckRatio, values, breedIndex, IBlockPosPredicate.any());
		}

		public Builder step(String name, int weight, Map<SourceTrait.Type, Float> values, int breedIndex, IBlockPosPredicate predicate) {
			return step(name, weight, 1, values, breedIndex, predicate);
		}

		public Builder step(String name, String translationKey, int weight, Map<SourceTrait.Type, Float> values, int breedIndex, IBlockPosPredicate predicate) {
			return step(name, translationKey, weight, 1, values, breedIndex, predicate);
		}

		public Builder step(String name, int weight, float luckRatio, Map<SourceTrait.Type, Float> values, int breedIndex, IBlockPosPredicate predicate) {
			return step(name, translationPrefix + "." + name.replaceAll("_", "."), weight, luckRatio, values, breedIndex, predicate);
		}

		public Builder step(String name, String translationKey, int weight, float luckRatio, Map<SourceTrait.Type, Float> values, int breedIndex, IBlockPosPredicate predicate) {
			if (steps.stream().anyMatch(s -> s.name().equals(name))) {
				throw new IllegalArgumentException("Steps with the same name already exists!");
			}
			steps.add(new Step(name, translationKey, weight, luckRatio, values, breedIndex, predicate));
			return this;
		}
		
		@Override
		public StepsSourceTraitValueProvider build() {
			return new StepsSourceTraitValueProvider(steps);
		}
	}

	private record Step(
			String name,
			String translationKey,
			int weight,
			float luckRatio,
			Map<SourceTrait.Type, Float> values,
			int breedIndex,
			IBlockPosPredicate predicate
	) implements ISourceTraitValue {

		public static final Codec<Step> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				Codec.STRING.fieldOf(ECNames.NAME).forGetter(Step::name),
				Codec.STRING.fieldOf(ECNames.TRANSLATION_KEY).forGetter(Step::translationKey),
				Codec.INT.fieldOf(ECNames.WEIGHT).forGetter(Step::weight),
				Codec.FLOAT.optionalFieldOf(ECNames.LUCK_RATIO, 1f).forGetter(Step::luckRatio),
				SourceTrait.Type.VALUE_CODEC.fieldOf(ECNames.VALUES).forGetter(Step::values),
				Codec.INT.fieldOf(ECNames.BREED_INDEX).forGetter(Step::breedIndex),
				IBlockPosPredicate.CODEC.optionalFieldOf(ECNames.PREDICATE, IBlockPosPredicate.any()).forGetter(Step::predicate)
		).apply(builder, Step::new));

		@Override
		public float getValue(SourceTrait.Type type) {
			return values.getOrDefault(type, 1f);
		}

		@Override
		public Component getDescription() {
			return Component.translatable(translationKey);
		}

		public int weight(float luck) {
			return Math.max(0, Math.round(weight + (luckRatio * luck)));
		}
	}
}
