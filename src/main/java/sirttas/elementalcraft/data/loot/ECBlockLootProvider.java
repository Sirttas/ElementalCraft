package sirttas.elementalcraft.data.loot;

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
import net.minecraft.item.Items;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTable.Builder;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.MatchTool;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.CopyNbt;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pureinfuser.BlockPedestal;
import sirttas.elementalcraft.block.shrine.BlockPylonShrine;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.block.shrine.firepylon.BlockFirePylon;
import sirttas.elementalcraft.block.spelldesk.BlockSpellDesk;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.ECNames;

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
			if (!ElementalCraft.MODID.equals(block.getRegistryName().getNamespace())) {
				continue;
			}
			if (block instanceof SlabBlock) {
				functionTable.put(block, ECBlockLootProvider::genSlab);
			} else if (block instanceof BlockPylonShrine) {
				functionTable.put(block, ECBlockLootProvider::genPylonShrine);
			} else if (isTileInstanceOf(block, TileShrine.class)) {
				functionTable.put(block, i -> genCopyNbt(i, ECNames.ELEMENT_TYPE, ECNames.ELEMENT_AMOUNT));
			} else if (block instanceof BlockPedestal) {
				functionTable.put(block, i -> genCopyNbt(i, ECNames.ELEMENT_AMOUNT));
			} else if (block instanceof BlockSpellDesk) {
				functionTable.put(block, ECBlockLootProvider::genSpellDesk);
			}
		}

		functionTable.put(ECBlocks.crystalOre, i -> genOre(i, ECItems.inertCrystal));
		functionTable.put(ECBlocks.tank, i -> genCopyNbt(i, ECNames.ELEMENT_TYPE, ECNames.ELEMENT_AMOUNT, ECNames.ELEMENT_MAX, ECNames.SMALL));
		functionTable.put(ECBlocks.tankSmall, i -> genCopyNbt(i, ECNames.ELEMENT_TYPE, ECNames.ELEMENT_AMOUNT, ECNames.ELEMENT_MAX, ECNames.SMALL));
		functionTable.put(ECBlocks.burntGlass, ECBlockLootProvider::genOnlySilkTouch);
		functionTable.put(ECBlocks.burntGlassPane, ECBlockLootProvider::genOnlySilkTouch);
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraft.MODID.equals(block.getRegistryName().getNamespace())) {
				save(cache, block);
			}
		}
	}

	private boolean isTileInstanceOf(Block block, Class<?> clazz) {
		return block.hasTileEntity(block.getDefaultState()) && clazz.isAssignableFrom(block.getDefaultState().createTileEntity(null).getClass());
	}

	private void save(DirectoryCache cache, Block block) throws IOException {
		Function<IItemProvider, Builder> func = functionTable.get(block);
		Builder builder = func != null ? func.apply(block) : genRegular(block);
		
		save(cache, builder.setParameterSet(LootParameterSets.BLOCK), getPath(generator.getOutputFolder(), block.getRegistryName()));
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	private static Builder genRegular(IItemProvider item) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(item);
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry).acceptCondition(SurvivesExplosion.builder());

		return LootTable.builder().addLootPool(pool);
	}

	private static Builder genSpellDesk(IItemProvider block) {
		return genRegular(block).addLootPool(LootPool.builder().name("paper").rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.PAPER))
				.acceptCondition(BlockStateProperty.builder((Block) block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withBoolProp(BlockSpellDesk.HAS_PAPER, true))));
	}

	private static Builder genOnlySilkTouch(IItemProvider item) {
		return LootTable.builder()
				.addLootPool(LootPool.builder().name("main")
						.acceptCondition(MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))))
						.rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(item)));
	}

	private static Builder genOre(IItemProvider ore, IItemProvider item) {
		return LootTable.builder()
				.addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1))
						.addEntry(ItemLootEntry.builder(ore)
								.acceptCondition(MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))))
								.alternatively(ItemLootEntry.builder(item).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)).acceptFunction(ExplosionDecay.builder()))));
	}

	private static Builder genCopyNbt(IItemProvider item, String... tags) {
		return genCopyNbt(ItemLootEntry.builder(item), tags);
	}

	private static Builder genPylonShrine(IItemProvider item) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(item)
				.acceptCondition(BlockStateProperty.builder((Block) item).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(BlockFirePylon.HALF, DoubleBlockHalf.LOWER)));

		return genCopyNbt(entry, ECNames.ELEMENT_TYPE, ECNames.ELEMENT_AMOUNT);
	}

	private static Builder genCopyNbt(LootEntry.Builder<?> entry, String... tags) {
		CopyNbt.Builder func = CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY);

		for (String tag : tags) {
			func = func.replaceOperation(tag, ECNames.BLOCK_ENTITY_TAG + '.' + tag);
		}
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry).acceptCondition(SurvivesExplosion.builder()).acceptFunction(func);

		return LootTable.builder().addLootPool(pool);
	}

	private static Builder genSlab(IItemProvider item) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(item).acceptFunction(SetCount.builder(ConstantRange.of(2)).acceptCondition(BlockStateProperty
				.builder((Block) item).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(SlabBlock.TYPE, SlabType.DOUBLE)))).acceptFunction(ExplosionDecay.builder());

		return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry));
	}

	@Override
	public String getName() {
		return "ElementalCraft block loot tables";
	}
}
