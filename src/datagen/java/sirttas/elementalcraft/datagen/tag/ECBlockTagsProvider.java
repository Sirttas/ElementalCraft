package sirttas.elementalcraft.datagen.tag;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.tag.ECTags;

import java.util.Comparator;
import java.util.List;

public class ECBlockTagsProvider extends BlockTagsProvider {

	private static final List<Block> LOOT_BLACKLIST = List.of(ECBlocks.SOURCE.get(), ECBlocks.BURNT_GLASS.get(), ECBlocks.BURNT_GLASS_PANE.get(), ECBlocks.SPRINGALINE_GLASS.get(), ECBlocks.SPRINGALINE_GLASS_PANE.get());
	
	public ECBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraftApi.MODID, existingFileHelper);
	}

	private Block[] getBlocksForClass(Class<?> clazz) {
		return registry.stream().filter(b -> ElementalCraftApi.MODID.equals(ForgeRegistries.BLOCKS.getKey(b).getNamespace()) && clazz.isInstance(b)).sorted(Comparator.comparing(ForgeRegistries.BLOCKS::getKey)).toArray(Block[]::new);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags() {
		tag(BlockTags.SLABS).add(getBlocksForClass(SlabBlock.class));
		tag(BlockTags.STAIRS).add(getBlocksForClass(StairBlock.class));
		tag(BlockTags.WALLS).add(getBlocksForClass(WallBlock.class));
		tag(BlockTags.FENCES).add(getBlocksForClass(FenceBlock.class));
		tag(Tags.Blocks.GLASS_PANES).add(getBlocksForClass(IronBarsBlock.class));
		tag(ECTags.Blocks.PIPES).add(getBlocksForClass(ElementPipeBlock.class));
		tag(ECTags.Blocks.SHRINES).add(getBlocksForClass(AbstractShrineBlock.class));
		tag(ECTags.Blocks.SHRINE_UPGRADES).add(getBlocksForClass(AbstractShrineUpgradeBlock.class));
		tag(ECTags.Blocks.PEDESTALS).add(getBlocksForClass(PedestalBlock.class));

		tag(ECTags.Blocks.STRIPPED_OAK).add(Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_OAK_WOOD);
		tag(ECTags.Blocks.STRIPPED_DARK_OAK).add(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD);
		tag(ECTags.Blocks.STRIPPED_BIRCH).add(Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_BIRCH_WOOD);
		tag(ECTags.Blocks.STRIPPED_ACACIA).add(Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_ACACIA_WOOD);
		tag(ECTags.Blocks.STRIPPED_JUNGLE).add(Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD);
		tag(ECTags.Blocks.STRIPPED_SPRUCE).add(Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_WOOD);
		tag(ECTags.Blocks.STRIPPED_MANGROVE).add(Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD);
		tag(ECTags.Blocks.STRIPPED_CRIMSON).add(Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_STEM);
		tag(ECTags.Blocks.STRIPPED_WARPED).add(Blocks.STRIPPED_WARPED_HYPHAE, Blocks.STRIPPED_WARPED_STEM);

		tag(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.INFUSER.get(), ECBlocks.BINDER.get(), ECBlocks.CRYSTALLIZER.get(), ECBlocks.INSCRIBER.get(), ECBlocks.FIRE_FURNACE.get(), ECBlocks.FIRE_BLAST_FURNACE.get(), ECBlocks.PURIFIER.get(), ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.BINDER_IMPROVED.get());

		tag(ECTags.Blocks.CONTAINER_TOOLS).addTag(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.EVAPORATOR.get(), ECBlocks.EXTRACTOR.get(), ECBlocks.EXTRACTOR_IMPROVED.get(), ECBlocks.SOLAR_SYNTHESIZER.get(), ECBlocks.MANA_SYNTHESIZER.get(), ECBlocks.DIFFUSER.get());

		runeBase(ECTags.Blocks.RUNE_AFFECTED_SPEED).add(ECBlocks.DIFFUSER.get(), ECBlocks.SORTER.get());
		runeBase(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION);
		runeBase(ECTags.Blocks.RUNE_AFFECTED_OPTIMIZATION);
		tag(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.CRYSTALLIZER.get(), ECBlocks.PURIFIER.get(), ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.SOURCE_BREEDER.get());

		tag(ECTags.Blocks.SHRINES_HARVEST_HARVESTABLE_TALL_PLANTS).add(Blocks.SUGAR_CANE, Blocks.BAMBOO, Blocks.KELP, Blocks.KELP_PLANT, Blocks.CACTUS).addOptional(IEBlocks.Misc.HEMP_PLANT.getId());
		tag(ECTags.Blocks.SHRINES_LAVA_LIQUIFIABLES).add(Blocks.BASALT, Blocks.POLISHED_BASALT, Blocks.SMOOTH_BASALT);
		tag(ECTags.Blocks.SHRINES_GROWTH_BLACKLIST).add(Blocks.GRASS, Blocks.GRASS_BLOCK, Blocks.TALL_GRASS);
		tag(ECTags.Blocks.SHRINES_ORE_HARVESTABLE_CRYSTALS).addTag(ECTags.Blocks.CLUSTERS);

		tag(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).add(ECBlocks.GROWTH_SHRINE.get(), ECBlocks.HARVEST_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get(), ECBlocks.LAVA_SHRINE.get(), ECBlocks.ORE_SHRINE.get(), ECBlocks.OVERLOAD_SHRINE.get(), ECBlocks.SWEET_SHRINE.get(), ECBlocks.BREEDING_SHRINE.get(), ECBlocks.GROVE_SHRINE.get(), ECBlocks.SPRING_SHRINE.get(), ECBlocks.BUDDING_SHRINE.get(), ECBlocks.SPAWNING_SHRINE.get(),  ECBlocks.FIRE_PYLON.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).add(ECBlocks.GROWTH_SHRINE.get(), ECBlocks.HARVEST_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get(), ECBlocks.ORE_SHRINE.get(), ECBlocks.SWEET_SHRINE.get(), ECBlocks.VACUUM_SHRINE.get(), ECBlocks.FIRE_PYLON.get(), ECBlocks.BREEDING_SHRINE.get(), ECBlocks.GROVE_SHRINE.get(), ECBlocks.ENDER_LOCK_SHRINE.get(), ECBlocks.SPAWNING_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).add(ECBlocks.SWEET_SHRINE.get(), ECBlocks.VACUUM_SHRINE.get(), ECBlocks.FIRE_PYLON.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_PROTECTION).add(ECBlocks.FIRE_PYLON.get(), ECBlocks.ENDER_LOCK_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_PLANTING).add(ECBlocks.HARVEST_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_FORTUNE).add(ECBlocks.ORE_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_SILK_TOUCH).add(ECBlocks.ORE_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_CRYSTAL_HARVEST).add(ECBlocks.ORE_SHRINE.get(), ECBlocks.BUDDING_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_SILK_TOUCH_ATTACHED).add(ECBlocks.ORE_SHRINE.get(), ECBlocks.BUDDING_SHRINE.get());

		tag(ECTags.Blocks.TREE_PARTS).addTags(BlockTags.LOGS, BlockTags.LEAVES).add(Blocks.VINE);

		tag(ECTags.Blocks.ORES_INERT_CRYSTAL).add(ECBlocks.CRYSTAL_ORE.get(), ECBlocks.DEEPSLATE_CRYSTAL_ORE.get());

		tag(Tags.Blocks.ORES).addTag(ECTags.Blocks.ORES_INERT_CRYSTAL);
		tag(ECTags.Blocks.BUDDING).add(Blocks.BUDDING_AMETHYST);
		tag(ECTags.Blocks.BUDS).add(Blocks.SMALL_AMETHYST_BUD, Blocks.MEDIUM_AMETHYST_BUD, Blocks.LARGE_AMETHYST_BUD, ECBlocks.SMALL_SPRINGALINE_BUD.get(), ECBlocks.MEDIUM_SPRINGALINE_BUD.get(), ECBlocks.LARGE_SPRINGALINE_BUD.get());
		tag(ECTags.Blocks.CLUSTERS).add(Blocks.AMETHYST_CLUSTER, ECBlocks.SPRINGALINE_CLUSTER.get());

		tag(ECTags.Blocks.PUREROCKS).add(ECBlocks.PURE_ROCK.get(), ECBlocks.PURE_ROCK_SLAB.get(), ECBlocks.PURE_ROCK_STAIRS.get(), ECBlocks.PURE_ROCK_WALL.get());
		tag(ECTags.Blocks.SMALL_CONTAINER_COMPATIBLES).add(ECBlocks.EXTRACTOR.get(), ECBlocks.INFUSER.get(), ECBlocks.EVAPORATOR.get(), ECBlocks.FIRE_FURNACE.get());
		tag(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);

		tag(BlockTags.BEACON_BASE_BLOCKS).add(ECBlocks.DRENCHED_IRON_BLOCK.get(), ECBlocks.SWIFT_ALLOY_BLOCK.get(), ECBlocks.FIREITE_BLOCK.get());

		tag(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON).add(ECBlocks.DRENCHED_IRON_BLOCK.get());
		tag(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY).add(ECBlocks.SWIFT_ALLOY_BLOCK.get());
		tag(ECTags.Blocks.STORAGE_BLOCKS_FIREITE).add(ECBlocks.FIREITE_BLOCK.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Blocks.STORAGE_BLOCKS_FIREITE);
		
		tag(ECTags.Blocks.BAG_OF_YURTING_BLACKLIST).add(ECBlocks.SOURCE.get());
		tag(MekanismTags.Blocks.CARDBOARD_BLACKLIST).add(ECBlocks.SOURCE.get());

		lootTags();
	}

	@SuppressWarnings("unchecked")
	private TagAppender<Block> runeBase(TagKey<Block> tag) {
		return tag(tag).addTags(ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.PEDESTALS).add(ECBlocks.EVAPORATOR.get(), ECBlocks.EXTRACTOR.get(), ECBlocks.EXTRACTOR_IMPROVED.get(), ECBlocks.SOLAR_SYNTHESIZER.get(), ECBlocks.MANA_SYNTHESIZER.get(), ECBlocks.PURE_INFUSER.get(), ECBlocks.SOURCE_BREEDER.get(), ECBlocks.SOURCE_BREEDER_PEDESTAL.get());
	}

	private void lootTags() {
		var mineableWithPickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
		
		for (var entry : ForgeRegistries.BLOCKS.getEntries()) {
			var block = entry.getValue();

			if (ElementalCraft.owns(entry)) {
				if (!LOOT_BLACKLIST.contains(block)) {
					mineableWithPickaxe.add(block);
				}
			}
		}
		tag(BlockTags.NEEDS_DIAMOND_TOOL).addTag(ECTags.Blocks.PUREROCKS);
		tag(BlockTags.NEEDS_IRON_TOOL).addTag(ECTags.Blocks.ORES_INERT_CRYSTAL);
	}
}
