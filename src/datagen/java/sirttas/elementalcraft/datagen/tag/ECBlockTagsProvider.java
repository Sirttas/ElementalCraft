package sirttas.elementalcraft.datagen.tag;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import mekanism.common.tags.MekanismTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ECBlockTagsProvider extends BlockTagsProvider {

	private static final List<Block> LOOT_BLACKLIST = List.of(ECBlocks.FIRE_SOURCE.get(), ECBlocks.WATER_SOURCE.get(), ECBlocks.EARTH_SOURCE.get(), ECBlocks.AIR_SOURCE.get(),
			ECBlocks.BURNT_GLASS.get(), ECBlocks.BURNT_GLASS_PANE.get(), ECBlocks.SPRINGALINE_GLASS.get(), ECBlocks.SPRINGALINE_GLASS_PANE.get());
	
	public ECBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, ElementalCraftApi.MODID, existingFileHelper);
	}

	private Block[] getBlocksForClass(Class<?> clazz) {
		return BuiltInRegistries.BLOCK.entrySet().stream()
				.filter(e -> ElementalCraft.owns(e) && clazz.isInstance(e.getValue()))
				.sorted(Map.Entry.comparingByKey())
				.map(Map.Entry::getValue)
				.toArray(Block[]::new);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(@Nonnull HolderLookup.Provider provider) {
		tag(BlockTags.SLABS).add(getBlocksForClass(SlabBlock.class));
		tag(BlockTags.STAIRS).add(getBlocksForClass(StairBlock.class));
		tag(BlockTags.WALLS).add(getBlocksForClass(WallBlock.class));
		tag(BlockTags.FENCES).add(getBlocksForClass(FenceBlock.class));
		tag(Tags.Blocks.GLASS_PANES).add(getBlocksForClass(IronBarsBlock.class));
		tag(ECTags.Blocks.PIPES).add(getBlocksForClass(ElementPipeBlock.class));
		tag(ECTags.Blocks.SHRINES).add(getBlocksForClass(AbstractShrineBlock.class));
		tag(ECTags.Blocks.SHRINE_UPGRADES).add(getBlocksForClass(AbstractShrineUpgradeBlock.class));
		tag(ECTags.Blocks.PEDESTALS).add(getBlocksForClass(PedestalBlock.class)).add(ECBlocks.SOURCE_BREEDER_PEDESTAL.get());

		tag(ECTags.Blocks.SOURCES).add(ECBlocks.FIRE_SOURCE.get(), ECBlocks.WATER_SOURCE.get(), ECBlocks.EARTH_SOURCE.get(), ECBlocks.AIR_SOURCE.get());

		tag(ECTags.Blocks.STRIPPED_OAK).add(Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_OAK_WOOD);
		tag(ECTags.Blocks.STRIPPED_DARK_OAK).add(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD);
		tag(ECTags.Blocks.STRIPPED_BIRCH).add(Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_BIRCH_WOOD);
		tag(ECTags.Blocks.STRIPPED_ACACIA).add(Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_ACACIA_WOOD);
		tag(ECTags.Blocks.STRIPPED_JUNGLE).add(Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD);
		tag(ECTags.Blocks.STRIPPED_SPRUCE).add(Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_WOOD);
		tag(ECTags.Blocks.STRIPPED_MANGROVE).add(Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD);
		tag(ECTags.Blocks.STRIPPED_CRIMSON).add(Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_STEM);
		tag(ECTags.Blocks.STRIPPED_WARPED).add(Blocks.STRIPPED_WARPED_HYPHAE, Blocks.STRIPPED_WARPED_STEM);
		tag(ECTags.Blocks.STRIPPED_CHERRY).add(Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_CHERRY_WOOD);
		tag(ECTags.Blocks.STRIPPED_BAMBOO).add(Blocks.STRIPPED_BAMBOO_BLOCK);

		tag(ECTags.Blocks.EXTRACTORS).add(ECBlocks.RUDIMENTARY_EXTRACTOR.get(), ECBlocks.EXTRACTOR.get(), ECBlocks.IMPROVED_EXTRACTOR.get());
		tag(ECTags.Blocks.SYNTHESIZERS).add(ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.COMBUSTION_SYNTHESIZER.get(), ECBlocks.DRAINING_SYNTHESIZER.get(), ECBlocks.VIBRATION_SYNTHESIZER.get(), ECBlocks.SOLAR_SYNTHESIZER.get(), ECBlocks.CULINARY_SYNTHESIZER.get(), ECBlocks.SCULK_CRACKING_SYNTHESIZER.get(), ECBlocks.AIR_MILL_SYNTHESIZER.get());
		tag(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.INFUSER.get(), ECBlocks.BINDER.get(), ECBlocks.CRYSTALLIZER.get(), ECBlocks.INSCRIBER.get(), ECBlocks.FIRE_FURNACE.get(), ECBlocks.FIRE_BLAST_FURNACE.get(), ECBlocks.PURIFIER.get(), ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.AIR_MILL_WOOD_SAW.get(), ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECBlocks.BINDER_IMPROVED.get());

		tag(ECTags.Blocks.SMALL_CONTAINER_TOOLS).add(ECBlocks.RUDIMENTARY_EXTRACTOR.get(), ECBlocks.INFUSER.get(), ECBlocks.FIRE_FURNACE.get(), ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.COMBUSTION_SYNTHESIZER.get(), ECBlocks.DRAINING_SYNTHESIZER.get(), ECBlocks.VIBRATION_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.FIRE_CONTAINER_TOOLS).add(ECBlocks.FIRE_FURNACE.get(), ECBlocks.FIRE_BLAST_FURNACE.get(), ECBlocks.COMBUSTION_SYNTHESIZER.get(), ECBlocks.SOLAR_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.WATER_CONTAINER_TOOLS).add(ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECBlocks.DRAINING_SYNTHESIZER.get(), ECBlocks.CULINARY_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.EARTH_CONTAINER_TOOLS).add(ECBlocks.PURIFIER.get(), ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.SCULK_CRACKING_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.AIR_CONTAINER_TOOLS).add(ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_WOOD_SAW.get(), ECBlocks.VIBRATION_SYNTHESIZER.get(), ECBlocks.AIR_MILL_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.CONTAINER_TOOLS).addTags(ECTags.Blocks.EXTRACTORS, ECTags.Blocks.SYNTHESIZERS, ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.SMALL_CONTAINER_TOOLS, ECTags.Blocks.FIRE_CONTAINER_TOOLS, ECTags.Blocks.WATER_CONTAINER_TOOLS, ECTags.Blocks.EARTH_CONTAINER_TOOLS, ECTags.Blocks.AIR_CONTAINER_TOOLS);

		runeBase(ECTags.Blocks.RUNE_AFFECTED_SPEED).add(ECBlocks.DIFFUSER.get(), ECBlocks.SORTER.get());
		runeBase(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION);
		runeBase(ECTags.Blocks.RUNE_AFFECTED_OPTIMIZATION);
		tag(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.PURIFIER.get(), ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.AIR_MILL_WOOD_SAW.get(), ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECBlocks.SOURCE_BREEDER.get());
		tag(ECTags.Blocks.RUNE_AFFECTED_TZEENTCH).addTag(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get());
		tag(ECTags.Blocks.RUNE_AFFECTED_RANGE).add(ECBlocks.DIFFUSER.get(), ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.VIBRATION_SYNTHESIZER.get());

		tag(ECTags.Blocks.CULTIVABLE_TALL_PLANTS).add(Blocks.SUGAR_CANE, Blocks.BAMBOO, Blocks.KELP, Blocks.KELP_PLANT, Blocks.CACTUS).addOptional(IEBlocks.Misc.HEMP_PLANT.getId());

		tag(ECTags.Blocks.SHRINES_HARVEST_HARVESTABLE_TALL_PLANTS).addTag(ECTags.Blocks.CULTIVABLE_TALL_PLANTS);
		tag(ECTags.Blocks.SHRINES_MELTING_LIQUIFIABLES_LAVA).add(Blocks.BASALT, Blocks.POLISHED_BASALT, Blocks.SMOOTH_BASALT);
		tag(ECTags.Blocks.SHRINES_MELTING_LIQUIFIABLES_WATER).add(Blocks.SNOW_BLOCK).addTag(BlockTags.ICE);
		tag(ECTags.Blocks.SHRINES_GROWTH_BLACKLIST).add(Blocks.SHORT_GRASS, Blocks.GRASS_BLOCK, Blocks.TALL_GRASS);
		tag(ECTags.Blocks.SHRINES_GROWTH_BONELESS).addTags(BlockTags.CROPS, ECTags.Blocks.CULTIVABLE_TALL_PLANTS).add(Blocks.SWEET_BERRY_BUSH, Blocks.CAVE_VINES,Blocks.CAVE_VINES_PLANT);
		tag(ECTags.Blocks.SHRINES_ORE_HARVESTABLE_CRYSTALS).addTag(ECTags.Blocks.CLUSTERS);

		tag(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).add(ECBlocks.GROWTH_SHRINE.get(), ECBlocks.HARVEST_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get(), ECBlocks.MELTING_SHRINE.get(), ECBlocks.ORE_SHRINE.get(), ECBlocks.OVERLOAD_SHRINE.get(), ECBlocks.SWEET_SHRINE.get(), ECBlocks.BREEDING_SHRINE.get(), ECBlocks.GROVE_SHRINE.get(), ECBlocks.SPRING_SHRINE.get(), ECBlocks.BUDDING_SHRINE.get(), ECBlocks.SPAWNING_SHRINE.get(),  ECBlocks.FIRE_PYLON.get());
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
		tag(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);

		tag(BlockTags.BEACON_BASE_BLOCKS).add(ECBlocks.DRENCHED_IRON_BLOCK.get(), ECBlocks.SWIFT_ALLOY_BLOCK.get(), ECBlocks.FIREITE_BLOCK.get());

		tag(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON).add(ECBlocks.DRENCHED_IRON_BLOCK.get());
		tag(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY).add(ECBlocks.SWIFT_ALLOY_BLOCK.get());
		tag(ECTags.Blocks.STORAGE_BLOCKS_FIREITE).add(ECBlocks.FIREITE_BLOCK.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Blocks.STORAGE_BLOCKS_FIREITE);
		
		tag(ECTags.Blocks.BAG_OF_YURTING_BLACKLIST).addTag(ECTags.Blocks.SOURCES);
		tag(MekanismTags.Blocks.CARDBOARD_BLACKLIST).addTag(ECTags.Blocks.SOURCES);

		lootTags();
	}

	@SuppressWarnings("unchecked")
	private IntrinsicTagAppender<Block> runeBase(TagKey<Block> tag) {
		return tag(tag).addTags(ECTags.Blocks.EXTRACTORS, ECTags.Blocks.SYNTHESIZERS, ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.PEDESTALS)
				.add(ECBlocks.PURE_INFUSER.get(), ECBlocks.SOURCE_BREEDER.get());
	}

	private IntrinsicTagAppender<Block> containerToolsBase(TagKey<Block> tag) {
		return tag(tag).add(
				ECBlocks.INFUSER.get(),
				ECBlocks.BINDER.get(),
				ECBlocks.CRYSTALLIZER.get(),
				ECBlocks.INSCRIBER.get(),
				ECBlocks.BINDER_IMPROVED.get(),
				ECBlocks.RUDIMENTARY_EXTRACTOR.get(),
				ECBlocks.EXTRACTOR.get(),
				ECBlocks.IMPROVED_EXTRACTOR.get(),
				ECBlocks.DIFFUSER.get());
	}

	private void lootTags() {
		var mineableWithPickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);

		BuiltInRegistries.BLOCK.entrySet().stream()
				.filter(ElementalCraft::owns)
				.sorted(Map.Entry.comparingByKey())
				.map(Map.Entry::getValue)
				.filter(b -> !LOOT_BLACKLIST.contains(b))
				.forEach(mineableWithPickaxe::add);

		tag(BlockTags.NEEDS_DIAMOND_TOOL).addTag(ECTags.Blocks.PUREROCKS);
		tag(BlockTags.NEEDS_IRON_TOOL).addTag(ECTags.Blocks.ORES_INERT_CRYSTAL);
	}
}
