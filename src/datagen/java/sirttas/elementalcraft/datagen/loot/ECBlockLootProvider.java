package sirttas.elementalcraft.datagen.loot;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECBlockLootProvider extends AbstractECLootProvider {
	private final Map<Block, Function<ItemLike, Builder>> functionTable = new HashMap<>();

	public ECBlockLootProvider(DataGenerator generator) {
		super(generator);

		for (Block block : ForgeRegistries.BLOCKS) {
			if (!ElementalCraftApi.MODID.equals(block.getRegistryName().getNamespace())) {
				continue;
			}
			if (block instanceof SlabBlock) {
				functionTable.put(block, ECBlockLootProvider::genSlab);
			} else if (block instanceof AbstractShrineBlock) {
				functionTable.put(block, ECBlockLootProvider::genCopyElementStorage);
			} else if (block instanceof PedestalBlock) {
				functionTable.put(block, ECBlockLootProvider::genCopyElementStorage);
			} else if (block instanceof ElementPipeBlock) {
				functionTable.put(block, ECBlockLootProvider::genPipe);
			} else if (block instanceof ReservoirBlock) {
				functionTable.put(block, ECBlockLootProvider::genCopyElementStorage);
			}	
		}

		functionTable.put(ECBlocks.CRYSTAL_ORE, i -> genOre(i, ECItems.INERT_CRYSTAL));
		functionTable.put(ECBlocks.EVAPORATOR, ECBlockLootProvider::genCopyElementStorage);
		functionTable.put(ECBlocks.CONTAINER, i -> genCopyNbt(i, ECNames.ELEMENT_STORAGE, ECNames.SMALL));
		functionTable.put(ECBlocks.SMALL_CONTAINER.get(), i -> genCopyNbt(i, ECNames.ELEMENT_STORAGE, ECNames.SMALL));
		functionTable.put(ECBlocks.CREATIVE_CONTAINER, ECBlockLootProvider::genCopyElementStorage);
		functionTable.put(ECBlocks.BURNT_GLASS, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.BURNT_GLASS_PANE, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.SPRINGALINE_CLUSTER, i -> genOre(i, ECItems.SPRINGALINE_SHARD, 4));
		functionTable.put(ECBlocks.SMALL_SPRINGALINE_BUD, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.MEDIUM_SPRINGALINE_BUD, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.LARGE_SPRINGALINE_BUD, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.SPRINGALINE_GLASS, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.SPRINGALINE_GLASS_PANE, ECBlockLootProvider::genOnlySilkTouch);
	}

	@Override
	public void run(@Nonnull HashCache cache) throws IOException {
		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraftApi.MODID.equals(block.getRegistryName().getNamespace())) {
				save(cache, block);
			}
		}
	}

	private static Builder genRegular(ItemLike item) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(item);
		LootPool.Builder pool = LootPool.lootPool().name("main").setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion());

		return LootTable.lootTable().withPool(pool);
	}


	private static Builder genPipe(ItemLike block) {
		return genRegular(block).withPool(LootPool.lootPool().name("frame").setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(ECItems.COVER_FRAM))
				.when(AlternativeLootItemCondition.alternative(
						LootItemBlockStatePropertyCondition.hasBlockStateProperties((Block) block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ElementPipeBlock.COVER, CoverType.FRAME)), 
						LootItemBlockStatePropertyCondition.hasBlockStateProperties((Block) block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ElementPipeBlock.COVER, CoverType.FRAME)))));
	}
	
	private static Builder genOnlySilkTouch(ItemLike item) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main")
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))))
						.setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(item)));
	}

	private static Builder genOre(ItemLike ore, ItemLike item) {
		return genOre(ore, item, 1);
	}
		
	private static Builder genOre(ItemLike ore, ItemLike item, int count) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main").setRolls(ConstantValue.exactly(count))
						.add(LootItem.lootTableItem(ore)
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))))
								.otherwise(LootItem.lootTableItem(item).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).apply(ApplyExplosionDecay.explosionDecay()))));
	}

	private static Builder genCopyNbt(ItemLike item, String... tags) {
		return genCopyNbt(LootItem.lootTableItem(item), tags);
	}

	private static Builder genCopyElementStorage(ItemLike item) {
		return genCopyNbt(item, ECNames.ELEMENT_STORAGE);
	}

	private static Builder genCopyNbt(LootPoolEntryContainer.Builder<?> entry, String... tags) {
		CopyNbtFunction.Builder func = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);

		for (String tag : tags) {
			func = func.copy(tag, ECNames.BLOCK_ENTITY_TAG + '.' + tag);
		}
		LootPool.Builder pool = LootPool.lootPool().name("main").setRolls(ConstantValue.exactly(1)).add(entry).when(ExplosionCondition.survivesExplosion()).apply(func);

		return LootTable.lootTable().withPool(pool);
	}

	private static Builder genSlab(ItemLike item) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)).when(LootItemBlockStatePropertyCondition
				.hasBlockStateProperties((Block) item).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))).apply(ApplyExplosionDecay.explosionDecay());

		return LootTable.lootTable().withPool(LootPool.lootPool().name("main").setRolls(ConstantValue.exactly(1)).add(entry));
	}

	private void save(HashCache cache, Block block) throws IOException {
		Function<ItemLike, Builder> func = functionTable.get(block);
		Builder builder = func != null ? func.apply(block) : genRegular(block);

		save(cache, builder.setParamSet(LootContextParamSets.BLOCK), getPath(block.getRegistryName()));
	}

	private Path getPath(ResourceLocation id) {
		return generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft block loot tables";
	}
}
