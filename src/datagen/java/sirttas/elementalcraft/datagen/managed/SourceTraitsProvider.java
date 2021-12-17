package sirttas.elementalcraft.datagen.managed;

import java.io.IOException;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import sirttas.dpanvil.api.data.AbstractManagedDataProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.block.source.trait.value.RangeBasedSourceTraitValueProvider;
import sirttas.elementalcraft.block.source.trait.value.StepsSourceTraitValueProvider;
import sirttas.elementalcraft.data.predicate.block.RangeFromSpawnPredicate;

import javax.annotation.Nonnull;

public class SourceTraitsProvider extends AbstractManagedDataProvider<SourceTrait> {


	public SourceTraitsProvider(DataGenerator generator) {
		super(generator, ElementalCraftApi.SOURCE_TRAIT_MANAGER);
	}

	@Override
	public void run(@Nonnull HashCache cache) throws IOException {
		save(cache, SourceTrait.builder().order(0).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.element_capacity", 500000, 1000000, 1000)), ECNames.ELEMENT_CAPACITY);
		save(cache, SourceTrait.builder().order(1).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.recover_rate", 50, 200, 1000)), ECNames.RECOVER_RATE);
		save(cache, SourceTrait.builder().order(2).chance(0.5F).predicate(new RangeFromSpawnPredicate(100)).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.nocturnal.3", 1, -0.5F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.nocturnal.2", 5, -0.25F, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.nocturnal.1", 25, -0.1F)
				.step("source_trait.elementalcraft.diurnal.1", 25, 0.1F)
				.step("source_trait.elementalcraft.diurnal.2", 5, 0.25F, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.diurnal.3", 1, 0.5F, new RangeFromSpawnPredicate(1000))
				.build()), "diurnal_nocturnal");
		save(cache, SourceTrait.builder().order(3).chance(0.5F).predicate(new RangeFromSpawnPredicate(500)).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.selfish.3", 1, -0.2F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.selfish.2", 5, -0.1F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.selfish.1", 25, -0.05F)
				.step("source_trait.elementalcraft.generous.1", 25, 0.05F)
				.step("source_trait.elementalcraft.generous.2", 5, 0.1F, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.generous.3", 1, 0.2F, new RangeFromSpawnPredicate(2500))
				.build()), "generosity");
		save(cache, SourceTrait.builder().order(4).chance(0.2F).predicate(new RangeFromSpawnPredicate(1000)).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.wasteful.3", 1, -0.2F, new RangeFromSpawnPredicate(5000))
				.step("source_trait.elementalcraft.wasteful.2", 5, -0.1F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.wasteful.1", 25, -0.05F)
				.step("source_trait.elementalcraft.thrifty.1", 25, 0.05F)
				.step("source_trait.elementalcraft.thrifty.2", 5, 0.1F, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.thrifty.3", 1, 0.2F, new RangeFromSpawnPredicate(5000))
				.build()), "thriftiness");
	}

	protected void save(HashCache cache, SourceTrait.Builder builder, String name) throws IOException {
		save(cache, builder.toJson(), ElementalCraft.createRL(name));
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
