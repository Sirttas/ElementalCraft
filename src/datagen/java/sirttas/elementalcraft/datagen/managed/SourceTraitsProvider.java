package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.block.source.trait.value.FixedSourceTraitValueProvider;
import sirttas.elementalcraft.block.source.trait.value.RangeBasedSourceTraitValueProvider;
import sirttas.elementalcraft.block.source.trait.value.StepsSourceTraitValueProvider;
import sirttas.elementalcraft.data.predicate.block.RangeFromSpawnPredicate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class SourceTraitsProvider extends AbstractManagedDataBuilderProvider<SourceTrait, SourceTrait.Builder> {


	public SourceTraitsProvider(DataGenerator generator) {
		super(generator, ElementalCraftApi.SOURCE_TRAIT_MANAGER, SourceTrait.Builder.ENCODER);
	}

	@Override
	public void collectBuilders() {
		builder(SourceTraits.ELEMENT_CAPACITY).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.element_capacity", List.of(SourceTrait.Type.CAPACITY), 500000, 1000000, 25000, 10000));
		builder(SourceTraits.RECOVER_RATE).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.recover_rate", List.of(SourceTrait.Type.RECOVER_RATE), 50, 200, 7.5f, 10000));
		builder(SourceTraits.DIURNAL_NOCTURNAL).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.nocturnal.5", 1, 2, Map.of(SourceTrait.Type.RECOVER_RATE, 1/2F, SourceTrait.Type.BREEDING_COST, 1.5F), -5, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.nocturnal.4", 3, Map.of(SourceTrait.Type.RECOVER_RATE, 1/1.75F, SourceTrait.Type.BREEDING_COST, 1.4F), -4, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.nocturnal.3", 5, Map.of(SourceTrait.Type.RECOVER_RATE, 1/1.5F, SourceTrait.Type.BREEDING_COST, 1.3F), -3, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.nocturnal.2", 10, -1, Map.of(SourceTrait.Type.RECOVER_RATE, 1/1.25F, SourceTrait.Type.BREEDING_COST, 1.2F), -3, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.nocturnal.1", 20, -2, Map.of(SourceTrait.Type.RECOVER_RATE, 1/1.1F, SourceTrait.Type.BREEDING_COST, 1.1F), -1)
				.step("source_trait.elementalcraft.diurnal.1", 20, -2, Map.of(SourceTrait.Type.RECOVER_RATE, 1.1F, SourceTrait.Type.BREEDING_COST, 1.1F), 1)
				.step("source_trait.elementalcraft.diurnal.2", 10, -1, Map.of(SourceTrait.Type.RECOVER_RATE, 1.25F, SourceTrait.Type.BREEDING_COST, 1.2F), 2, new RangeFromSpawnPredicate(500))
				.step("source_trait.elementalcraft.diurnal.3", 5, Map.of(SourceTrait.Type.RECOVER_RATE, 1.5F, SourceTrait.Type.BREEDING_COST, 1.3F), 3, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.diurnal.4", 3, Map.of(SourceTrait.Type.RECOVER_RATE, 1.75F, SourceTrait.Type.BREEDING_COST, 1.4F), 4, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.diurnal.5", 1, 2, Map.of(SourceTrait.Type.RECOVER_RATE, 2F, SourceTrait.Type.BREEDING_COST, 1.5F), 5, IBlockPosPredicate.none())
				.chance(0.5F, 0.1f)
				.predicate(new RangeFromSpawnPredicate(100))
				.build());
		builder(SourceTraits.GENEROSITY).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.selfish.5", 1, 2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.5F, SourceTrait.Type.BREEDING_COST, 1.5F), -5, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.selfish.4", 3, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.3F, SourceTrait.Type.BREEDING_COST, 1.4F), -4, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.selfish.3", 5, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.2F, SourceTrait.Type.BREEDING_COST, 1.3F), -3, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.selfish.2", 10, -1, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), -2, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.selfish.1", 20, -2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), -1)
				.step("source_trait.elementalcraft.generous.1", 20, -2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), 1)
				.step("source_trait.elementalcraft.generous.2", 10, -1, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), 2, new RangeFromSpawnPredicate(1000))
				.step("source_trait.elementalcraft.generous.3", 5, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.2F, SourceTrait.Type.BREEDING_COST, 1.3F), 3, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.generous.4", 3, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.3F, SourceTrait.Type.BREEDING_COST, 1.4F), 4, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.generous.5", 1, 2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.5F, SourceTrait.Type.BREEDING_COST, 1.5F), 5, IBlockPosPredicate.none())
				.chance(0.5F, 0.1f)
				.predicate(new RangeFromSpawnPredicate(500))
				.build());
		builder(SourceTraits.THRIFTINESS).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.wasteful.5", 1, 2, Map.of(SourceTrait.Type.PRESERVATION, 1/1.5F, SourceTrait.Type.BREEDING_COST, 1.5F), -5, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.wasteful.4", 3, Map.of(SourceTrait.Type.PRESERVATION, 1/1.3F, SourceTrait.Type.BREEDING_COST, 1.4F), -4, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.wasteful.3", 5, Map.of(SourceTrait.Type.PRESERVATION, 1/1.2F, SourceTrait.Type.BREEDING_COST, 1.3F), -3, new RangeFromSpawnPredicate(5000))
				.step("source_trait.elementalcraft.wasteful.2", 10, -1, Map.of(SourceTrait.Type.PRESERVATION, 1/1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), -2, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.wasteful.1", 20, -2, Map.of(SourceTrait.Type.PRESERVATION, 1/1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), -1)
				.step("source_trait.elementalcraft.thrifty.1", 20, -2, Map.of(SourceTrait.Type.PRESERVATION, 1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), 1)
				.step("source_trait.elementalcraft.thrifty.2", 10, -1, Map.of(SourceTrait.Type.PRESERVATION, 1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), 2, new RangeFromSpawnPredicate(2500))
				.step("source_trait.elementalcraft.thrifty.3", 5, Map.of(SourceTrait.Type.PRESERVATION, 1.2F, SourceTrait.Type.BREEDING_COST, 1.3F), 3, new RangeFromSpawnPredicate(5000))
				.step("source_trait.elementalcraft.thrifty.4", 3, Map.of(SourceTrait.Type.PRESERVATION, 1.3F, SourceTrait.Type.BREEDING_COST, 1.4F), 4, IBlockPosPredicate.none())
				.step("source_trait.elementalcraft.thrifty.5", 1, 2, Map.of(SourceTrait.Type.PRESERVATION, 1.5F, SourceTrait.Type.BREEDING_COST, 1.5F), 5, IBlockPosPredicate.none())
				.chance(0.2F, 0.05f)
				.predicate(new RangeFromSpawnPredicate(1000))
				.build());
		builder(SourceTraits.FERTILITY).value(StepsSourceTraitValueProvider.builder()
				.step("source_trait.elementalcraft.barren.5", 1, 2, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.1F), -5)
				.step("source_trait.elementalcraft.barren.4", 3, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.2F), -4)
				.step("source_trait.elementalcraft.barren.3", 5, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.35F), -3)
				.step("source_trait.elementalcraft.barren.2", 10, -1, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.5F), -2)
				.step("source_trait.elementalcraft.barren.1", 20, -2, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.75F), -1)
				.step("source_trait.elementalcraft.fertile.1", 20, -2, Map.of(SourceTrait.Type.BREEDING_COST, 0.75F), 1)
				.step("source_trait.elementalcraft.fertile.2", 10, -1, Map.of(SourceTrait.Type.BREEDING_COST, 0.5F), 2)
				.step("source_trait.elementalcraft.fertile.3", 5, Map.of(SourceTrait.Type.BREEDING_COST, 0.35F), 3)
				.step("source_trait.elementalcraft.fertile.4", 3, Map.of(SourceTrait.Type.BREEDING_COST, 0.2F), 4)
				.step("source_trait.elementalcraft.fertile.5", 1, 2, Map.of(SourceTrait.Type.BREEDING_COST, 0.1F), 5)
				.chance(0.25F)
				.predicate(IBlockPosPredicate.none())
				.build());
		builder(SourceTraits.ARTIFICIAL).value(new FixedSourceTraitValueProvider("source_trait.elementalcraft.artificial", Map.of(
				SourceTrait.Type.CAPACITY, 0.25F,
				SourceTrait.Type.RECOVER_RATE, 0F,
				SourceTrait.Type.BREEDING_COST, 0.5F)));
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
