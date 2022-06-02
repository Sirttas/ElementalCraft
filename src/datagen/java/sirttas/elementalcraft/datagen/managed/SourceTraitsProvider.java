package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.DataGenerator;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
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
		builder(ECNames.ELEMENT_CAPACITY).order(0).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.element_capacity", 500000, 1000000, 1000));
		builder(ECNames.RECOVER_RATE).order(1).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.recover_rate", 50, 200, 1000));
		builder("diurnal_nocturnal").order(2).chance(0.5F).predicate(new RangeFromSpawnPredicate(100)).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.nocturnal.3", 1, -0.5F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.nocturnal.2", 5, -0.25F, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.nocturnal.1", 25, -0.1F)
				.step("source_trait.elementalcraft.diurnal.1", 25, 0.1F)
				.step("source_trait.elementalcraft.diurnal.2", 5, 0.25F, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.diurnal.3", 1, 0.5F, new RangeFromSpawnPredicate(1000))
				.build());
		builder("generosity").order(3).chance(0.5F).predicate(new RangeFromSpawnPredicate(500)).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.selfish.3", 1, -0.2F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.selfish.2", 5, -0.1F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.selfish.1", 25, -0.05F)
				.step("source_trait.elementalcraft.generous.1", 25, 0.05F)
				.step("source_trait.elementalcraft.generous.2", 5, 0.1F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.generous.3", 1, 0.2F, new RangeFromSpawnPredicate(2500))
				.build());
		builder("thriftiness").order(4).chance(0.2F).predicate(new RangeFromSpawnPredicate(1000)).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.wasteful.3", 1, -0.2F, new RangeFromSpawnPredicate(5000))
				.step("source_trait.elementalcraft.wasteful.2", 5, -0.1F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.wasteful.1", 25, -0.05F)
				.step("source_trait.elementalcraft.thrifty.1", 25, 0.05F)
				.step("source_trait.elementalcraft.thrifty.2", 5, 0.1F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.thrifty.3", 1, 0.2F, new RangeFromSpawnPredicate(5000))
				.build());
	}
	
	private SourceTrait.Builder builder(String name) {
		var builder = SourceTrait.builder();
		
		add(ElementalCraft.createRL(name), builder);
		return builder; 
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
