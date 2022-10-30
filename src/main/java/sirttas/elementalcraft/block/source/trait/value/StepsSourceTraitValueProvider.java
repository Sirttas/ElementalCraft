package sirttas.elementalcraft.block.source.trait.value;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StepsSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "steps";
	public static final Codec<StepsSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Step.CODEC.listOf().fieldOf(ECNames.STEPS).forGetter(p -> p.steps)
	).apply(builder, StepsSourceTraitValueProvider::new));
	
	private final List<Step> steps;
	
	public static Builder builder() {
		return new Builder();
	}
	
	private StepsSourceTraitValueProvider(Collection<Step> steps) {
		this.steps = ImmutableList.copyOf(steps);
	}
	
	@Override
	public ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos) {
		return roll(level, steps.stream()
				.filter(s -> s.predicate().test(level, pos))
				.toList());
	}

	@Nullable
	private Step roll(Level level, List<Step> list) {
		int roll = level.random.nextInt(list.stream().mapToInt(Step::weight).sum());


		for (var step : list) {
			roll -= step.weight();
			if (roll < 0) {
				return step;
			}
		}
		return null;
	}

	@Nullable
	@Override
	public ISourceTraitValue breed(SourceTrait trait, Level level, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
		if (value1 instanceof Step step1 && value2 instanceof Step step2) {
			var b1 = step1.breedIndex();
			var b2 = step2.breedIndex();

			return createStep(level, getMin(b1, b2), getMax(b1, b2));
		} else if (value1 instanceof Step step) {
			return createStep(level, step);
		} else if (value2 instanceof Step step) {
			return createStep(level, step);
		}
		return roll(level, steps);
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
	private Step createStep(Level level, Step step) {
		var index = step.breedIndex();

		return createStep(level, index - 1, index + 1);
	}

	@Nullable
	private Step createStep(Level level, int min, int max) {
		return roll(level, steps.stream()
				.filter(s -> s.breedIndex() >= min && s.breedIndex() <= max)
				.toList());
	}

	@Nonnull
	@Override
	public SourceTraitValueProviderType<StepsSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.STEPS.get();
	}

	@Override
	public ISourceTraitValue load(Tag tag) {
		return tag instanceof IntTag intTag && intTag.getAsInt() >= 0 && intTag.getAsInt() < steps.size() ? steps.get(intTag.getAsInt()) : null;
	}

	@Override
	public Tag save(ISourceTraitValue value) {
		return value instanceof Step step ? IntTag.valueOf(steps.indexOf(step)) : null;
	}
	
	public static class Builder implements ISourceTraitValueProviderBuilder {
		
		private final List<Step> steps;
		
		private Builder() {
			steps = new ArrayList<>();
		}
		
		public Builder step(String translationKey, int weight, Map<SourceTrait.Type, Float> values, int breedIndex) {
			return step(translationKey, weight, values, breedIndex, IBlockPosPredicate.any());
		}
		
		public Builder step(String translationKey, int weight, Map<SourceTrait.Type, Float> values, int breedIndex, IBlockPosPredicate predicate) {
			steps.add(new Step(translationKey, weight, values, breedIndex, predicate));
			return this;
		}
		
		@Override
		public StepsSourceTraitValueProvider build() {
			return new StepsSourceTraitValueProvider(steps);
		}
	}

	private record Step(
			String translationKey,
			int weight,
			Map<SourceTrait.Type, Float> values,
			int breedIndex,
			IBlockPosPredicate predicate
	) implements ISourceTraitValue {

		public static final Codec<Step> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				Codec.STRING.fieldOf(ECNames.NAME).forGetter(Step::translationKey),
				Codec.INT.fieldOf(ECNames.WEIGHT).forGetter(Step::weight),
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
	}
}
