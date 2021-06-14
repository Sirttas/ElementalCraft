package sirttas.elementalcraft.datagen.loot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.conditions.Alternative;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
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

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECBlockLootProvider extends AbstractECLootProvider {
	private final Map<Block, Function<IItemProvider, Builder>> functionTable = new HashMap<>();

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
		functionTable.put(ECBlocks.TANK, i -> genCopyNbt(i, ECNames.ELEMENT_STORAGE, ECNames.SMALL));
		functionTable.put(ECBlocks.TANK_SMALL, i -> genCopyNbt(i, ECNames.ELEMENT_STORAGE, ECNames.SMALL));
		functionTable.put(ECBlocks.TANK_CREATIVE, ECBlockLootProvider::genCopyElementStorage);
		functionTable.put(ECBlocks.BURNT_GLASS, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.BURNT_GLASS_PANE, ECBlockLootProvider::genOnlySilkTouch);
	}

	@Override
	public void run(DirectoryCache cache) throws IOException {
		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraftApi.MODID.equals(block.getRegistryName().getNamespace())) {
				save(cache, block);
			}
		}
	}

	private static Builder genRegular(IItemProvider item) {
		LootEntry.Builder<?> entry = ItemLootEntry.lootTableItem(item);
		LootPool.Builder pool = LootPool.lootPool().name("main").setRolls(ConstantRange.exactly(1)).add(entry).when(SurvivesExplosion.survivesExplosion());

		return LootTable.lootTable().withPool(pool);
	}


	private static Builder genPipe(IItemProvider block) {
		return genRegular(block).withPool(LootPool.lootPool().name("frame").setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(ECItems.COVER_FRAM))
				.when(Alternative.alternative(
						BlockStateProperty.hasBlockStateProperties((Block) block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ElementPipeBlock.COVER, CoverType.FRAME)), 
						BlockStateProperty.hasBlockStateProperties((Block) block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ElementPipeBlock.COVER, CoverType.FRAME)))));
	}
	
	private static Builder genOnlySilkTouch(IItemProvider item) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main")
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))))
						.setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(item)));
	}

	private static Builder genOre(IItemProvider ore, IItemProvider item) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main").setRolls(ConstantRange.exactly(1))
						.add(ItemLootEntry.lootTableItem(ore)
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))))
								.otherwise(ItemLootEntry.lootTableItem(item).apply(ApplyBonus.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).apply(ExplosionDecay.explosionDecay()))));
	}

	private static Builder genCopyNbt(IItemProvider item, String... tags) {
		return genCopyNbt(ItemLootEntry.lootTableItem(item), tags);
	}

	private static Builder genCopyElementStorage(IItemProvider item) {
		return genCopyNbt(item, ECNames.ELEMENT_STORAGE);
	}

	private static Builder genCopyNbt(LootEntry.Builder<?> entry, String... tags) {
		CopyNbt.Builder func = CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY);

		for (String tag : tags) {
			func = func.copy(tag, ECNames.BLOCK_ENTITY_TAG + '.' + tag);
		}
		LootPool.Builder pool = LootPool.lootPool().name("main").setRolls(ConstantRange.exactly(1)).add(entry).when(SurvivesExplosion.survivesExplosion()).apply(func);

		return LootTable.lootTable().withPool(pool);
	}

	private static Builder genSlab(IItemProvider item) {
		LootEntry.Builder<?> entry = ItemLootEntry.lootTableItem(item).apply(SetCount.setCount(ConstantRange.exactly(2)).when(BlockStateProperty
				.hasBlockStateProperties((Block) item).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))).apply(ExplosionDecay.explosionDecay());

		return LootTable.lootTable().withPool(LootPool.lootPool().name("main").setRolls(ConstantRange.exactly(1)).add(entry));
	}

	private void save(DirectoryCache cache, Block block) throws IOException {
		Function<IItemProvider, Builder> func = functionTable.get(block);
		Builder builder = func != null ? func.apply(block) : genRegular(block);

		save(cache, builder.setParamSet(LootParameterSets.BLOCK), getPath(block.getRegistryName()));
	}

	private Path getPath(ResourceLocation id) {
		return generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "ElementalCraft block loot tables";
	}
}
