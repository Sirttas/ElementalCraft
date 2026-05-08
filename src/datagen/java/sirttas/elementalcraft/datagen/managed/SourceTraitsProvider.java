package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.RangeBasedSourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.StepsSourceTraitValueProvider;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.data.predicate.block.RangeFromSpawnPredicate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SourceTraitsProvider extends AbstractManagedDataBuilderProvider<SourceTrait, SourceTrait.Builder> {


	public SourceTraitsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries, ElementalCraftApi.SOURCE_TRAIT_MANAGER, SourceTrait.Builder.ENCODER);
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		builder(SourceTraits.ELEMENT_CAPACITY_KEY).value(new RangeBasedSourceTraitValueProvider("source_trait.elementalcraft.element_capacity", List.of(SourceTrait.Type.CAPACITY), SourceElementStorage.DEFAULT_CAPACITY, 10000000, 250000, 100000));
		builder(SourceTraits.DIURNAL_NOCTURNAL_KEY).value(steps()
				.step("nocturnal_5", 1, 2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/2F, SourceTrait.Type.BREEDING_COST, 1.5F), -5, IBlockPosPredicate.none())
				.step("nocturnal_4", 3, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.75F, SourceTrait.Type.BREEDING_COST, 1.4F), -4, IBlockPosPredicate.none())
				.step("nocturnal_3", 5, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.5F, SourceTrait.Type.BREEDING_COST, 1.3F), -3, new RangeFromSpawnPredicate(1000))
				.step("nocturnal_2", 10, -1, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.25F, SourceTrait.Type.BREEDING_COST, 1.2F), -3, new RangeFromSpawnPredicate(500))
				.step("nocturnal_1", 20, -2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.1F, SourceTrait.Type.BREEDING_COST, 1.1F), -1)
				.step("diurnal_1", 20, -2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.1F, SourceTrait.Type.BREEDING_COST, 1.1F), 1)
				.step("diurnal_2", 10, -1, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.25F, SourceTrait.Type.BREEDING_COST, 1.2F), 2, new RangeFromSpawnPredicate(500))
				.step("diurnal_3", 5, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.5F, SourceTrait.Type.BREEDING_COST, 1.3F), 3, new RangeFromSpawnPredicate(1000))
				.step("diurnal_4", 3, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.75F, SourceTrait.Type.BREEDING_COST, 1.4F), 4, IBlockPosPredicate.none())
				.step("diurnal_5", 1, 2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 2F, SourceTrait.Type.BREEDING_COST, 1.5F), 5, IBlockPosPredicate.none())
				.chance(0.5F, 0.1f, 0.25F, 0.05F)
				.predicate(new RangeFromSpawnPredicate(100))
				.build());
		builder(SourceTraits.GENEROSITY_KEY).value(steps()
				.step("selfish_5", 1, 2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/2F, SourceTrait.Type.BREEDING_COST, 1.5F), -5, IBlockPosPredicate.none())
				.step("selfish_4", 3, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.5F, SourceTrait.Type.BREEDING_COST, 1.4F), -4, IBlockPosPredicate.none())
				.step("selfish_3", 5, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.25F, SourceTrait.Type.BREEDING_COST, 1.3F), -3, new RangeFromSpawnPredicate(2500))
				.step("selfish_2", 10, -1, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), -2, new RangeFromSpawnPredicate(1000))
				.step("selfish_1", 20, -2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1/1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), -1)
				.step("generous_1", 20, -2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), 1)
				.step("generous_2", 10, -1, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), 2, new RangeFromSpawnPredicate(1000))
				.step("generous_3", 5, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.25F, SourceTrait.Type.BREEDING_COST, 1.3F), 3, new RangeFromSpawnPredicate(2500))
				.step("generous_4", 3, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 1.5F, SourceTrait.Type.BREEDING_COST, 1.4F), 4, IBlockPosPredicate.none())
				.step("generous_5", 1, 2, Map.of(SourceTrait.Type.EXTRACTION_SPEED, 2F, SourceTrait.Type.BREEDING_COST, 1.5F), 5, IBlockPosPredicate.none())
				.chance(0.5F, 0.1f, 0.25F, 0.05F)
				.predicate(new RangeFromSpawnPredicate(500))
				.build());
		builder(SourceTraits.THRIFTINESS_KEY).value(steps()
				.step("wasteful_5", 1, 2, Map.of(SourceTrait.Type.PRESERVATION, 1/2F, SourceTrait.Type.BREEDING_COST, 1.5F), -5, IBlockPosPredicate.none())
				.step("wasteful_4", 3, Map.of(SourceTrait.Type.PRESERVATION, 1/1.5F, SourceTrait.Type.BREEDING_COST, 1.4F), -4, IBlockPosPredicate.none())
				.step("wasteful_3", 5, Map.of(SourceTrait.Type.PRESERVATION, 1/1.25F, SourceTrait.Type.BREEDING_COST, 1.3F), -3, new RangeFromSpawnPredicate(5000))
				.step("wasteful_2", 10, -1, Map.of(SourceTrait.Type.PRESERVATION, 1/1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), -2, new RangeFromSpawnPredicate(2500))
				.step("wasteful_1", 20, -2, Map.of(SourceTrait.Type.PRESERVATION, 1/1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), -1)
				.step("thrifty_1", 20, -2, Map.of(SourceTrait.Type.PRESERVATION, 1.05F, SourceTrait.Type.BREEDING_COST, 1.1F), 1)
				.step("thrifty_2", 10, -1, Map.of(SourceTrait.Type.PRESERVATION, 1.1F, SourceTrait.Type.BREEDING_COST, 1.2F), 2, new RangeFromSpawnPredicate(2500))
				.step("thrifty_3", 5, Map.of(SourceTrait.Type.PRESERVATION, 1.25F, SourceTrait.Type.BREEDING_COST, 1.3F), 3, new RangeFromSpawnPredicate(5000))
				.step("thrifty_4", 3, Map.of(SourceTrait.Type.PRESERVATION, 1.5F, SourceTrait.Type.BREEDING_COST, 1.4F), 4, IBlockPosPredicate.none())
				.step("thrifty_5", 1, 2, Map.of(SourceTrait.Type.PRESERVATION, 2F, SourceTrait.Type.BREEDING_COST, 1.5F), 5, IBlockPosPredicate.none())
				.chance(0.2F, 0.05f, 0.1F, 0.02F)
				.predicate(new RangeFromSpawnPredicate(1000))
				.build());
		builder(SourceTraits.FERTILITY_KEY).value(steps()
				.step("barren_5", 1, 2, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.5F), -5)
				.step("barren_4", 3, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.6F), -4)
				.step("barren_3", 5, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.7F), -3)
				.step("barren_2", 10, -1, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.8F), -2)
				.step("barren_1", 20, -2, Map.of(SourceTrait.Type.BREEDING_COST, 1/0.9F), -1)
				.step("fertile_1", 20, -2, Map.of(SourceTrait.Type.BREEDING_COST, 0.9F), 1)
				.step("fertile_2", 10, -1, Map.of(SourceTrait.Type.BREEDING_COST, 0.8F), 2)
				.step("fertile_3", 5, Map.of(SourceTrait.Type.BREEDING_COST, 0.7F), 3)
				.step("fertile_4", 3, Map.of(SourceTrait.Type.BREEDING_COST, 0.6F), 4)
				.step("fertile_5", 1, 2, Map.of(SourceTrait.Type.BREEDING_COST, 0.5F), 5)
				.chance(0.1F, 0.05F)
				.predicate(IBlockPosPredicate.none())
				.build());
	}

	private static StepsSourceTraitValueProvider.Builder steps() {
		return StepsSourceTraitValueProvider.builder("source_trait.elementalcraft");
	}

	private SourceTrait.Builder builder(ResourceKey<SourceTrait> key) {
		var builder = SourceTrait.builder();
		
		add(key, builder);
		return builder; 
	}

	@Override
	public String getName() {
		return "ElementalCraft Source Traits";
	}
}
