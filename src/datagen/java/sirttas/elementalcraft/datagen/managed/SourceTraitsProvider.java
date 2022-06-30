package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.block.source.trait.value.FixedSourceTraitValueProvider;
import sirttas.elementalcraft.block.source.trait.value.RangeBasedSourceTraitValueProvider;
import sirttas.elementalcraft.block.source.trait.value.StepsSourceTraitValueProvider;
import sirttas.elementalcraft.data.predicate.block.RangeFromSpawnPredicate;

import javax.annotation.Nonnull;

public class SourceTraitsProvider extends AbstractManagedDataBuilderProvider<SourceTrait, SourceTrait.Builder> {


	public SourceTraitsProvider(DataGenerator generator) {
		super(generator, ElementalCraftApi.SOURCE_TRAIT_MANAGER, SourceTrait.Builder.ENCODER);
	}

	@Override
	public void collectBuilders() {
		builder(SourceTraits.ELEMENT_CAPACITY).order(0).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.element_capacity", 500000, 1000000, 10000));
		builder(SourceTraits.RECOVER_RATE).order(1).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.recover_rate", 50, 200, 10000));
		builder(SourceTraits.DIURNAL_NOCTURNAL).order(2).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.nocturnal.3", 1, -0.5F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.nocturnal.2", 5, -0.25F, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.nocturnal.1", 25, -0.1F)
				.step("source_trait.elementalcraft.diurnal.1", 25, 0.1F)
				.step("source_trait.elementalcraft.diurnal.2", 5, 0.25F, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.diurnal.3", 1, 0.5F, new RangeFromSpawnPredicate(1000))
				.chance(0.5F)
				.predicate(new RangeFromSpawnPredicate(100))
				.build());
		builder(SourceTraits.GENEROSITY).order(3).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.selfish.3", 1, -0.2F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.selfish.2", 5, -0.1F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.selfish.1", 25, -0.05F)
				.step("source_trait.elementalcraft.generous.1", 25, 0.05F)
				.step("source_trait.elementalcraft.generous.2", 5, 0.1F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.generous.3", 1, 0.2F, new RangeFromSpawnPredicate(2500))
				.chance(0.5F)
				.predicate(new RangeFromSpawnPredicate(500))
				.build());
		builder(SourceTraits.THRIFTINESS).order(4).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.wasteful.3", 1, -0.2F, new RangeFromSpawnPredicate(5000))
				.step("source_trait.elementalcraft.wasteful.2", 5, -0.1F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.wasteful.1", 25, -0.05F)
				.step("source_trait.elementalcraft.thrifty.1", 25, 0.05F)
				.step("source_trait.elementalcraft.thrifty.2", 5, 0.1F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.thrifty.3", 1, 0.2F, new RangeFromSpawnPredicate(5000))
				.chance(0.2F)
				.predicate(new RangeFromSpawnPredicate(1000))
				.build());
		builder(SourceTraits.FLEETING).order(5).value(new FixedSourceTraitValueProvider("source_trait.elementalcraft.fleeting", 5F));
	}
	
	private SourceTrait.Builder builder(ResourceKey<SourceTrait> key) {
		var builder = SourceTrait.builder();
		
		add(key, builder);
		return builder; 
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
