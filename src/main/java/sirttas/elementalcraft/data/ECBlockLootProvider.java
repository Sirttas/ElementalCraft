package sirttas.elementalcraft.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pureinfuser.BlockPedestal;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.ECNBTTags;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECBlockLootProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;
	private final Map<Block, Function<IItemProvider, Builder>> functionTable = new HashMap<>();

	public ECBlockLootProvider(DataGenerator generator) {
		this.generator = generator;

		for (Block block : ForgeRegistries.BLOCKS) {
			if (!ElementalCraft.MODID.equals(block.getRegistryName().getNamespace())) {
				continue;
			}
			if (block instanceof SlabBlock) {
				functionTable.put(block, ECBlockLootProvider::genSlab);
			} else if (isTileInstanceOf(block, TileShrine.class)) {
				functionTable.put(block, i -> genCopyNbt(i, ECNBTTags.ELEMENT_TYPE, ECNBTTags.ELEMENT_AMOUNT));
			} else if (block instanceof BlockPedestal) {
				functionTable.put(block, i -> genCopyNbt(i, ECNBTTags.ELEMENT_AMOUNT));
			}
		}

		functionTable.put(ECBlocks.crystalOre, i -> genRegular(ECItems.inertCrystal));
		functionTable.put(ECBlocks.tank, i -> genCopyNbt(i, ECNBTTags.ELEMENT_TYPE, ECNBTTags.ELEMENT_AMOUNT, ECNBTTags.ELEMENT_MAX));
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
		return block.hasTileEntity(block.getDefaultState()) && clazz.isAssignableFrom(block.createTileEntity(block.getDefaultState(), null).getClass());
	}

	private void save(DirectoryCache cache, Block block) throws IOException {
		Function<IItemProvider, Builder> func = functionTable.get(block);
		Builder builder = func != null ? func.apply(block) : genRegular(block);
		
		IDataProvider.save(GSON, cache, LootTableManager.toJson(builder.setParameterSet(LootParameterSets.BLOCK).build()),
				getPath(generator.getOutputFolder(), block.getRegistryName()));
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	private static Builder genRegular(IItemProvider item) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(item);
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry).acceptCondition(SurvivesExplosion.builder());

		return LootTable.builder().addLootPool(pool);
	}

	private static Builder genCopyNbt(IItemProvider item, String... tags) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(item);
		CopyNbt.Builder func = CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY);

		for (String tag : tags) {
			func = func.replaceOperation(tag, ECNBTTags.EC_NBT_TE + tag);
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
