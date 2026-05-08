package sirttas.elementalcraft.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ECBlockTagsProvider extends BlockTagsProvider {

	private static final List<Block> LOOT_BLACKLIST = List.of(ECBlocks.FIRE_SOURCE.get(), ECBlocks.WATER_SOURCE.get(), ECBlocks.EARTH_SOURCE.get(), ECBlocks.AIR_SOURCE.get(),
			ECBlocks.BURNT_GLASS.get(), ECBlocks.BURNT_GLASS_PANE.get(), ECBlocks.SPRINGALINE_GLASS.get(), ECBlocks.SPRINGALINE_GLASS_PANE.get());

	public ECBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, ElementalCraftApi.MODID);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		(new ECBlockItemTagsProvider() {
			@Override
			protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
				return ECBlockTagsProvider.this.tag(blockTag);
			}
		}).run();

		tag(ECTags.Blocks.PEDESTALS).add(ECBlockItemTagsProvider.getBlocksForClass(PedestalBlock.class)).add(ECBlocks.SOURCE_BREEDER_PEDESTAL.get());

		tag(ECTags.Blocks.EXTRACTORS).add(ECBlocks.RUDIMENTARY_EXTRACTOR.get(), ECBlocks.EXTRACTOR.get(), ECBlocks.IMPROVED_EXTRACTOR.get());
		tag(ECTags.Blocks.SYNTHESIZERS).add(ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.COMBUSTION_SYNTHESIZER.get(), ECBlocks.DRAINING_SYNTHESIZER.get(), ECBlocks.VIBRATION_SYNTHESIZER.get(), ECBlocks.SOLAR_SYNTHESIZER.get(), ECBlocks.CULINARY_SYNTHESIZER.get(), ECBlocks.SCULK_CRACKING_SYNTHESIZER.get(), ECBlocks.AIR_MILL_SYNTHESIZER.get());

		tag(ECTags.Blocks.SMALL_CONTAINER_TOOLS).add(ECBlocks.RUDIMENTARY_EXTRACTOR.get(), ECBlocks.INFUSER.get(), ECBlocks.FIRE_FURNACE.get(), ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.COMBUSTION_SYNTHESIZER.get(), ECBlocks.DRAINING_SYNTHESIZER.get(), ECBlocks.VIBRATION_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.FIRE_CONTAINER_TOOLS).add(ECBlocks.FIRE_FURNACE.get(), ECBlocks.FIRE_BLAST_FURNACE.get(), ECBlocks.COMBUSTION_SYNTHESIZER.get(), ECBlocks.SOLAR_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.WATER_CONTAINER_TOOLS).add(ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECBlocks.DRAINING_SYNTHESIZER.get(), ECBlocks.CULINARY_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.EARTH_CONTAINER_TOOLS).add(ECBlocks.PURIFIER.get(), ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.SCULK_CRACKING_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.AIR_CONTAINER_TOOLS).add(ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_WOOD_SAW.get(), ECBlocks.VIBRATION_SYNTHESIZER.get(), ECBlocks.AIR_MILL_SYNTHESIZER.get());
		containerToolsBase(ECTags.Blocks.CONTAINER_TOOLS).addTags(ECTags.Blocks.EXTRACTORS, ECTags.Blocks.SYNTHESIZERS, ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.SMALL_CONTAINER_TOOLS, ECTags.Blocks.FIRE_CONTAINER_TOOLS, ECTags.Blocks.WATER_CONTAINER_TOOLS, ECTags.Blocks.EARTH_CONTAINER_TOOLS, ECTags.Blocks.AIR_CONTAINER_TOOLS);

		runeBase(ECTags.Blocks.RUNE_AFFECTED_SPEED).add(ECBlocks.DIFFUSER.get(), ECBlocks.ORDERED_SORTER.get());
		runeBase(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION);
		runeBase(ECTags.Blocks.RUNE_AFFECTED_OPTIMIZATION);
		tag(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.PURIFIER.get(), ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.AIR_MILL_WOOD_SAW.get(), ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECBlocks.SOURCE_BREEDER.get());
		tag(ECTags.Blocks.RUNE_AFFECTED_TZEENTCH).addTag(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get());
		tag(ECTags.Blocks.RUNE_AFFECTED_RANGE).add(ECBlocks.DIFFUSER.get(), ECBlocks.CRACKING_SYNTHESIZER.get(), ECBlocks.VIBRATION_SYNTHESIZER.get());

		tag(ECTags.Blocks.USES_SINGLE_SET_FROM_ORDERED_SORTER).addTags(ECTags.Blocks.INSTRUMENTS);
		tag(ECTags.Blocks.CULTIVABLE_TALL_PLANTS).add(Blocks.SUGAR_CANE, Blocks.BAMBOO, Blocks.KELP, Blocks.KELP_PLANT, Blocks.CACTUS);
		getOrCreateRawBuilder(ECTags.Blocks.CULTIVABLE_TALL_PLANTS).addOptionalElement(Identifier.fromNamespaceAndPath("immersiveengineering", "hemp"));

		tag(ECTags.Blocks.SHRINES_HARVEST_HARVESTABLE_TALL_PLANTS).addTag(ECTags.Blocks.CULTIVABLE_TALL_PLANTS);
		tag(ECTags.Blocks.SHRINES_MELTING_LIQUIFIABLES_LAVA).add(Blocks.BASALT, Blocks.POLISHED_BASALT, Blocks.SMOOTH_BASALT);
		tag(ECTags.Blocks.SHRINES_MELTING_LIQUIFIABLES_WATER).add(Blocks.SNOW_BLOCK).addTag(BlockTags.ICE);
		tag(ECTags.Blocks.SHRINES_GROWTH_BLACKLIST).add(Blocks.SHORT_GRASS, Blocks.GRASS_BLOCK, Blocks.TALL_GRASS);
		tag(ECTags.Blocks.SHRINES_GROWTH_BONELESS).addTags(BlockTags.CROPS, ECTags.Blocks.CULTIVABLE_TALL_PLANTS).add(Blocks.SWEET_BERRY_BUSH, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT);
		tag(ECTags.Blocks.SHRINES_ORE_HARVESTABLE_CRYSTALS).addTag(Tags.Blocks.CLUSTERS);

		tag(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).add(ECBlocks.GROWTH_SHRINE.get(), ECBlocks.HARVEST_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get(), ECBlocks.MELTING_SHRINE.get(), ECBlocks.ORE_SHRINE.get(), ECBlocks.OVERLOAD_SHRINE.get(), ECBlocks.SWEET_SHRINE.get(), ECBlocks.BREEDING_SHRINE.get(), ECBlocks.GROVE_SHRINE.get(), ECBlocks.SPRING_SHRINE.get(), ECBlocks.BUDDING_SHRINE.get(), ECBlocks.SPAWNING_SHRINE.get(), ECBlocks.FIRE_PYLON.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).add(ECBlocks.GROWTH_SHRINE.get(), ECBlocks.HARVEST_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get(), ECBlocks.ORE_SHRINE.get(), ECBlocks.SWEET_SHRINE.get(), ECBlocks.VACUUM_SHRINE.get(), ECBlocks.FIRE_PYLON.get(), ECBlocks.BREEDING_SHRINE.get(), ECBlocks.GROVE_SHRINE.get(), ECBlocks.ENDER_LOCK_SHRINE.get(), ECBlocks.SPAWNING_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).add(ECBlocks.SWEET_SHRINE.get(), ECBlocks.VACUUM_SHRINE.get(), ECBlocks.FIRE_PYLON.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_PROTECTION).add(ECBlocks.FIRE_PYLON.get(), ECBlocks.ENDER_LOCK_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_PLANTING).add(ECBlocks.HARVEST_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_FORTUNE).add(ECBlocks.ORE_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_SILK_TOUCH).add(ECBlocks.ORE_SHRINE.get(), ECBlocks.LUMBER_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_CRYSTAL_HARVEST).add(ECBlocks.ORE_SHRINE.get(), ECBlocks.BUDDING_SHRINE.get());
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_SILK_TOUCH_ATTACHED).add(ECBlocks.ORE_SHRINE.get(), ECBlocks.BUDDING_SHRINE.get());

		tag(ECTags.Blocks.TREE_PARTS).addTags(BlockTags.LOGS, BlockTags.LEAVES).add(Blocks.VINE);

		tag(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);
		tag(BlockTags.BEACON_BASE_BLOCKS).add(ECBlocks.DRENCHED_IRON_BLOCK.get(), ECBlocks.SWIFT_ALLOY_BLOCK.get(), ECBlocks.FIREITE_BLOCK.get());

		tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED).addTag(ECTags.Blocks.SOURCES);

		lootTags();
	}

	@SuppressWarnings("unchecked")
	private TagAppender<Block, Block> runeBase(TagKey<Block> tag) {
		return tag(tag).addTags(ECTags.Blocks.EXTRACTORS, ECTags.Blocks.SYNTHESIZERS, ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.PEDESTALS)
				.add(ECBlocks.PURE_INFUSER.get(), ECBlocks.SOURCE_BREEDER.get());
	}

	private TagAppender<Block, Block> containerToolsBase(TagKey<Block> tag) {
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