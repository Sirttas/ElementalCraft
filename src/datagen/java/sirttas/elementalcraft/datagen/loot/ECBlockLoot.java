package sirttas.elementalcraft.datagen.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.extractor.AbstractElementExtractorBlock;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.entry.LootRunes;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ECBlockLoot extends BlockLootSubProvider {

	protected ECBlockLoot(HolderLookup.Provider registries) {
		super(Set.of(
				ECBlocks.PURE_ROCK.get().asItem(),
				ECBlocks.PURE_ROCK_SLAB.get().asItem(),
				ECBlocks.PURE_ROCK_STAIRS.get().asItem(),
				ECBlocks.PURE_ROCK_WALL.get().asItem()
		), FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	protected void generate() {
		add(ECBlocks.CRYSTAL_ORE.get(), this::createInertCrystalOreDrops);
		add(ECBlocks.DEEPSLATE_CRYSTAL_ORE.get(), this::createInertCrystalOreDrops);

		add(ECBlocks.CONTAINER.get(), ECBlockLoot::createCopyElementStorage);
		add(ECBlocks.SMALL_CONTAINER.get(), ECBlockLoot::createCopyElementStorage);
		add(ECBlocks.CREATIVE_CONTAINER.get(), ECBlockLoot::createCopyElementStorage);

		add(ECBlocks.DIFFUSER.get(), this::createRuneable);
		add(ECBlocks.ORDERED_SORTER.get(), this::createRuneable);
		add(ECBlocks.PURE_INFUSER.get(), this::createRuneable);

		add(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get(), b -> createCopyComponents(b, ECDataComponents.TARGET_POS.get()));
		add(ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get(), this::createRuneable);

		add(ECBlocks.AIR_MILL_GRINDSTONE.get(), this::createAirMill);
		add(ECBlocks.AIR_MILL_WOOD_SAW.get(), this::createAirMill);
		add(ECBlocks.ENCHANTMENT_LIQUEFIER.get(), this::createDoubleHalfRuneable);

		add(ECBlocks.SOURCE_BREEDER.get(), this::createDoubleHalfRuneable);
		add(ECBlocks.SOURCE_BREEDER_PEDESTAL.get(), this::createRuneable);

		add(ECBlocks.CRACKING_SYNTHESIZER.get(), this::createIER);
		add(ECBlocks.COMBUSTION_SYNTHESIZER.get(), this::createIER);
		add(ECBlocks.DRAINING_SYNTHESIZER.get(), this::createIER);
		add(ECBlocks.VIBRATION_SYNTHESIZER.get(), this::createIER);
		add(ECBlocks.SOLAR_SYNTHESIZER.get(), this::createIER);
		add(ECBlocks.CULINARY_SYNTHESIZER.get(), this::createIER);
		add(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get(), this::createIER);
		add(ECBlocks.AIR_MILL_SYNTHESIZER.get(), this::createAirMill);

		add(ECBlocks.BREEDING_SHRINE.get(), ECBlockLoot::createBreedingShrine);

		add(ECBlocks.BURNT_GLASS.get(), this::createSilkTouchOnlyTable);
		add(ECBlocks.BURNT_GLASS_PANE.get(), this::createSilkTouchOnlyTable);
		add(ECBlocks.SPRINGALINE_GLASS.get(), this::createSilkTouchOnlyTable);
		add(ECBlocks.SPRINGALINE_GLASS_PANE.get(), this::createSilkTouchOnlyTable);

		add(ECBlocks.SPRINGALINE_CLUSTER.get(), this::createSpringaline);
		add(ECBlocks.SMALL_SPRINGALINE_BUD.get(), noDrop());
		add(ECBlocks.MEDIUM_SPRINGALINE_BUD.get(), noDrop());
		add(ECBlocks.LARGE_SPRINGALINE_BUD.get(), noDrop());

		add(ECBlocks.ELEMENTAL_EMBER.get(), noDrop());

		for (var entry : BuiltInRegistries.BLOCK.entrySet()) {
			var block = entry.getValue();
			var key = block.getLootTable();

			if (!ElementalCraft.owns(entry) || map.containsKey(key) || BuiltInLootTables.EMPTY.equals(key)) {
				continue;
			}
			if (block instanceof SlabBlock) {
				add(block, this::createSlabItemTable);
			} else if (block instanceof AbstractPylonShrineBlock<?>) {
				add(block, ECBlockLoot::createDoubleHalfElementStorage);
			} else if (block instanceof AbstractShrineBlock) {
				add(block, ECBlockLoot::createCopyElementStorage);
			} else if (block instanceof PedestalBlock) {
				add(block, this::createIER);
			} else if (block instanceof IInstrumentBlock) {
				add(block, this::createRuneable);
			} else if (block instanceof AbstractElementExtractorBlock) {
				add(block, this::createRuneable);
			} else if (block instanceof ElementPipeBlock) {
				add(block, this::createPipe);
			} else if (block instanceof ReservoirBlock) {
				add(block, ECBlockLoot::createDoubleHalfElementStorage);
			} else if (block instanceof SourceBlock) {
				add(block, noDrop());
			} else if (block.defaultBlockState().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
				add(block, b -> createSinglePropConditionTable(b, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
			} else {
				dropSelf(block);
			}
		}
	}

	@Nonnull
	private LootTable.Builder createInertCrystalOreDrops(Block block) {
		var registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		return createSilkTouchDispatchTable(
				block,
				this.applyExplosionDecay(
						block,
						LootItem.lootTableItem(ECItems.INERT_CRYSTAL.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
								.apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
				)
		);
	}

	@Nonnull
	private Builder createDoubleHalfRuneable(Block block) {
		return createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
				.withPool(doubleHalfDropRunes(block));
	}

	@Nonnull
	private Builder createAirMill(Block block) {
		return LootTable.lootTable()
				.withPool(createCopyComponentsPool(LootItem.lootTableItem(block), ECDataComponents.AIR_MILL_DAMAGE.get())
						.when(createHasStateCondition(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
				.withPool(doubleHalfDropRunes(block));
	}

	private Builder createPipe(Block block) {
		return createSingleItemTable(block).withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(ECItems.COVER_FRAME.get()))
				.when(AnyOfCondition.anyOf(
						createHasStateCondition(block, ElementPipeBlock.COVER, CoverType.FRAME),
						createHasStateCondition(block, ElementPipeBlock.COVER, CoverType.COVERED))));
	}

	private Builder createSpringaline(Block ore) {
		var registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		return createSilkTouchDispatchTable(ore, LootItem.lootTableItem(ECItems.SPRINGALINE_SHARD.get())
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))
				.apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
				.apply(ApplyExplosionDecay.explosionDecay()));
	}

	private static Builder createCopyComponents(Block block,  DataComponentType<?>... components) {
		return createCopyComponents(LootItem.lootTableItem(block), components);
	}

	private static Builder createCopyElementStorage(Block item) {
		return createCopyComponents(item, ECDataComponents.ELEMENT_TYPE.get(), ECDataComponents.ELEMENT_AMOUNT.get());
	}

	public Builder createRuneable(ItemLike item) {
		return createSingleItemTable(item).withPool(dropRunes());
	}

	private Builder createIER(Block item) {
		return createCopyElementStorage(item).withPool(dropRunes());
	}

	private static Builder createDoubleHalfElementStorage(Block block) {
		return LootTable.lootTable().withPool(createCopyComponentsPool(LootItem.lootTableItem(block), ECDataComponents.ELEMENT_AMOUNT.get())
				.when(createHasStateCondition(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)));
	}

	private static Builder createBreedingShrine(Block block) {
		return LootTable.lootTable().withPool(createCopyComponentsPool(LootItem.lootTableItem(block), ECDataComponents.ELEMENT_AMOUNT.get())
				.when(createHasStateCondition(block, BreedingShrineBlock.PART, BreedingShrineBlock.Part.CORE)));
	}

	private static Builder createCopyComponents(LootPoolEntryContainer.Builder<?> entry, DataComponentType<?>... components) {
		return LootTable.lootTable().withPool(createCopyComponentsPool(entry, components));
	}

	public static @NotNull LootPool.Builder dropRunes() {
		return LootPool.lootPool()
				.name("elementalcraft:runes")
				.add(LootRunes.builder())
				.when(ExplosionCondition.survivesExplosion());
	}

	private static @NotNull LootPool.Builder doubleHalfDropRunes(Block block) {
		return dropRunes()
				.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)));
	}

	private static LootPool.Builder createCopyComponentsPool(LootPoolEntryContainer.Builder<?> entry, DataComponentType<?>... components) {
		CopyComponentsFunction.Builder func = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY);

		for (var component : components) {
			func = func.include(component);
		}
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion()).apply(func);
	}

	@Nonnull
	@Override
	protected Iterable<Block> getKnownBlocks() {
		return BuiltInRegistries.BLOCK.entrySet().stream()
				.filter(ElementalCraft::owns)
				.map(Map.Entry::getValue)
				.collect(Collectors.toSet());
	}

	@Nonnull
	private static <T extends Comparable<T> & StringRepresentable> LootItemBlockStatePropertyCondition.Builder createHasStateCondition(Block block, Property<T> property, T value) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value));
	}
}
