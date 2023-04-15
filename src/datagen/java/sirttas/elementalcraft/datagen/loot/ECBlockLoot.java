package sirttas.elementalcraft.datagen.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.enchantment.Enchantments;
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
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.extractor.ExtractorBlock;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.entry.LootRunes;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Collectors;

public class ECBlockLoot extends BlockLoot {

	@Override
	protected void addTables() {
		add(ECBlocks.CRYSTAL_ORE.get(), b -> createOreDrop(b, ECItems.INERT_CRYSTAL.get()));
		add(ECBlocks.DEEPSLATE_CRYSTAL_ORE.get(), b -> createOreDrop(b, ECItems.INERT_CRYSTAL.get()));
		add(ECBlocks.EVAPORATOR.get(), ECBlockLoot::createIER);
		add(ECBlocks.CONTAINER.get(), b -> createCopyNbt(b, ECNames.ELEMENT_STORAGE, ECNames.SMALL));
		add(ECBlocks.SMALL_CONTAINER.get(), b -> createCopyNbt(b, ECNames.ELEMENT_STORAGE, ECNames.SMALL));
		add(ECBlocks.CREATIVE_CONTAINER.get(), ECBlockLoot::createCopyElementStorage);
		add(ECBlocks.DIFFUSER.get(), ECBlockLoot::createRunnable);
		add(ECBlocks.SORTER.get(), ECBlockLoot::createRunnable);
		add(ECBlocks.PURE_INFUSER.get(), ECBlockLoot::createRunnable);
		add(ECBlocks.SOURCE_BREEDER.get(), ECBlockLoot::createRunnable);
		add(ECBlocks.SOURCE_BREEDER_PEDESTAL.get(), ECBlockLoot::createIER);
		add(ECBlocks.SOLAR_SYNTHESIZER.get(), ECBlockLoot::createIER);
		add(ECBlocks.MANA_SYNTHESIZER.get(), b -> createCopyNbt(b, "mana").withPool(createDropRunesPool()));
		add(ECBlocks.BREEDING_SHRINE.get(), ECBlockLoot::createBreedingShrine);
		add(ECBlocks.BURNT_GLASS.get(), BlockLoot::createSilkTouchOnlyTable);
		add(ECBlocks.BURNT_GLASS_PANE.get(), BlockLoot::createSilkTouchOnlyTable);
		add(ECBlocks.SPRINGALINE_GLASS.get(), BlockLoot::createSilkTouchOnlyTable);
		add(ECBlocks.SPRINGALINE_GLASS_PANE.get(), BlockLoot::createSilkTouchOnlyTable);
		add(ECBlocks.SPRINGALINE_CLUSTER.get(), ECBlockLoot::createSpringaline);
		add(ECBlocks.SMALL_SPRINGALINE_BUD.get(), noDrop());
		add(ECBlocks.MEDIUM_SPRINGALINE_BUD.get(), noDrop());
		add(ECBlocks.LARGE_SPRINGALINE_BUD.get(), noDrop());

		for (Block block : ForgeRegistries.BLOCKS) {
			var key = block.getLootTable();

			if (!ElementalCraftApi.MODID.equals(ForgeRegistries.BLOCKS.getKey(block).getNamespace()) || map.containsKey(key) || BuiltInLootTables.EMPTY.equals(key)) {
				continue;
			}
			if (block instanceof SlabBlock) {
				add(block, BlockLoot::createSlabItemTable);
			} else if (block instanceof AbstractShrineBlock) {
				add(block, ECBlockLoot::createCopyElementStorage);
			} else if (block instanceof PedestalBlock) {
				add(block, ECBlockLoot::createIER);
			} else if (block instanceof IInstrumentBlock) {
				add(block, ECBlockLoot::createRunnable);
			} else if (block instanceof ExtractorBlock) {
				add(block, ECBlockLoot::createRunnable);
			} else if (block instanceof ElementPipeBlock) {
				add(block, ECBlockLoot::createPipe);
			} else if (block instanceof ReservoirBlock) {
				add(block, ECBlockLoot::createReservoir);
			} else if (block.defaultBlockState().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
				add(block, b -> createSinglePropConditionTable(b, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
			} else {
				dropSelf(block);
			}
		}
	}

	private static Builder createPipe(Block block) {
		return createSingleItemTable(block).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ECItems.COVER_FRAME.get()))
				.when(AlternativeLootItemCondition.alternative(
						createHasStateCondition(block, ElementPipeBlock.COVER, CoverType.FRAME),
						createHasStateCondition(block, ElementPipeBlock.COVER, CoverType.COVERED))));
	}

	private static Builder createSpringaline(Block ore) {
		return BlockLoot.createSilkTouchDispatchTable(ore, LootItem.lootTableItem(ECItems.SPRINGALINE_SHARD.get())
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))
				.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
				.apply(ApplyExplosionDecay.explosionDecay()));
	}

	private static Builder createCopyNbt(Block block, String... tags) {
		return createCopyNbt(LootItem.lootTableItem(block), tags);
	}

	private static Builder createCopyElementStorage(Block item) {
		return createCopyNbt(item, ECNames.ELEMENT_STORAGE);
	}

	private static Builder createRunnable(Block item) {
		return createSingleItemTable(item).withPool(createDropRunesPool());
	}

	private static Builder createIER(Block item) {
		return createCopyElementStorage(item).withPool(createDropRunesPool());
	}

	private static Builder createReservoir(Block block) {
		return LootTable.lootTable().withPool(createCopyNbtPool(LootItem.lootTableItem(block), ECNames.ELEMENT_STORAGE)
				.when(createHasStateCondition(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)));
	}

	private static Builder createBreedingShrine(Block block) {
		return LootTable.lootTable().withPool(createCopyNbtPool(LootItem.lootTableItem(block), ECNames.ELEMENT_STORAGE)
				.when(createHasStateCondition(block, BreedingShrineBlock.PART, BreedingShrineBlock.Part.CORE)));
	}

	private static Builder createCopyNbt(LootPoolEntryContainer.Builder<?> entry, String... tags) {
		return LootTable.lootTable().withPool(createCopyNbtPool(entry, tags));
	}

	private static LootPool.Builder createDropRunesPool() {
		return LootPool.lootPool().add(LootRunes.builder()).when(ExplosionCondition.survivesExplosion());
	}

	private static LootPool.Builder createCopyNbtPool(LootPoolEntryContainer.Builder<?> entry, String... tags) {
		CopyNbtFunction.Builder func = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);

		for (String tag : tags) {
			func = func.copy(tag, ECNames.BLOCK_ENTITY_TAG + '.' + tag);
		}
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion()).apply(func);
	}

	@Nonnull
	@Override
	protected Iterable<Block> getKnownBlocks() {
		return ForgeRegistries.BLOCKS.getEntries().stream()
				.filter(e -> ElementalCraftApi.MODID.equals(e.getKey().location().getNamespace()))
				.map(Map.Entry::getValue)
				.collect(Collectors.toSet());
	}

	@Nonnull
	private static <T extends Comparable<T> & StringRepresentable> LootItemBlockStatePropertyCondition.Builder createHasStateCondition(Block block, Property<T> property, T value) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value));
	}
}
