package sirttas.elementalcraft.block.source.trait.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

public class StepsSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "steps";
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final SourceTraitValueProviderType<StepsSourceTraitValueProvider> TYPE = null;
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
		var list = steps.stream()
				.filter(s -> s.predicate().test(level, pos))
				.collect(Collectors.toList());
		int roll = level.random.nextInt(list.stream().mapToInt(Step::weight).sum());
		
		
		for (var step : list) {
			roll -= step.weight();
			if (roll < 0) {
				return step;
			}
		}
		return null;
	}
	
	@Override
	public SourceTraitValueProviderType<? extends ISourceTraitValueProvider> getType() {
		return TYPE;
	}

	@Override
	public ISourceTraitValue load(Tag tag) {
		return tag instanceof IntTag intTag && intTag.getAsInt() < steps.size() ? steps.get(intTag.getAsInt()) : null;
	}

	@Override
	public Tag save(ISourceTraitValue value) {
		return value instanceof Step step ? IntTag.valueOf(steps.indexOf(step)) : null;
	}
	
	public static class Builder {
		
		private final List<Step> steps;
		
		private Builder() {
			steps = new ArrayList<>();
		}
		
		public Builder step(String translationKey, int weight, float value) {
			return step(translationKey, weight, value, IBlockPosPredicate.any());
		}
		
		public Builder step(String translationKey, int weight, float value, IBlockPosPredicate predicate) {
			steps.add(new Step(translationKey, weight, value, predicate));
			return this;
		}
		
		public StepsSourceTraitValueProvider build() {
			return new StepsSourceTraitValueProvider(steps);
		}
	}
	
	private record Step(String translationKey, int weight, float value, IBlockPosPredicate predicate) implements ISourceTraitValue {
		
		public static final Codec<Step> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				Codec.STRING.fieldOf(ECNames.NAME).forGetter(Step::translationKey),
				Codec.INT.fieldOf(ECNames.WEIGHT).forGetter(Step::weight),
				Codec.FLOAT.fieldOf(ECNames.VALUE).forGetter(Step::value),
				IBlockPosPredicate.CODEC.optionalFieldOf(ECNames.PREDICATE, IBlockPosPredicate.any()).forGetter(Step::predicate)
		).apply(builder, Step::new));

		@Override
		public float getValue() {
			return value;
		}

		@Override
		public Component getDescription() {
			return new TranslatableComponent(translationKey);
		}
	}
}
