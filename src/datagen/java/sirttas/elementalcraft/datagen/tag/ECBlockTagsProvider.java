package sirttas.elementalcraft.datagen.tag;

import java.util.Comparator;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.tag.ECTags;

public class ECBlockTagsProvider extends BlockTagsProvider {

	public ECBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraftApi.MODID, existingFileHelper);
	}

	private Block[] getBlocksForClass(Class<?> clazz) {
		return registry.stream().filter(b -> ElementalCraftApi.MODID.equals(b.getRegistryName().getNamespace()) && clazz.isInstance(b)).sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags() {
		tag(BlockTags.SLABS).add(getBlocksForClass(SlabBlock.class));
		tag(BlockTags.STAIRS).add(getBlocksForClass(StairsBlock.class));
		tag(BlockTags.WALLS).add(getBlocksForClass(WallBlock.class));
		tag(BlockTags.FENCES).add(getBlocksForClass(FenceBlock.class));
		tag(Tags.Blocks.GLASS_PANES).add(getBlocksForClass(PaneBlock.class));
		tag(ECTags.Blocks.PIPES).add(getBlocksForClass(ElementPipeBlock.class));
		tag(ECTags.Blocks.SHRINES).add(getBlocksForClass(AbstractShrineBlock.class));
		tag(ECTags.Blocks.PEDESTALS).add(getBlocksForClass(PedestalBlock.class));

		tag(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.INFUSER, ECBlocks.BINDER, ECBlocks.CRYSTALLIZER, ECBlocks.INSCRIBER, ECBlocks.FIRE_FURNACE, ECBlocks.FIRE_BLAST_FURNACE,
				ECBlocks.PURIFIER, ECBlocks.AIR_MILL, ECBlocks.BINDER_IMPROVED);

		tag(ECTags.Blocks.CONTAINER_TOOLS).addTag(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.EVAPORATOR, ECBlocks.EXTRACTOR, ECBlocks.EXTRACTOR_IMPROVED, ECBlocks.SOLAR_SYNTHESIZER);

		tag(ECTags.Blocks.RUNE_AFFECTED).addTags(ECTags.Blocks.CONTAINER_TOOLS, ECTags.Blocks.PEDESTALS).add(ECBlocks.PURE_INFUSER);
		tag(ECTags.Blocks.RUNE_AFFECTED_SPEED).addTags(ECTags.Blocks.RUNE_AFFECTED);
		tag(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addTags(ECTags.Blocks.RUNE_AFFECTED);
		tag(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.CRYSTALLIZER, ECBlocks.PURIFIER, ECBlocks.AIR_MILL);

		tag(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).add(ECBlocks.GROWTH_SHRINE, ECBlocks.HARVEST_SHRINE, ECBlocks.LAVA_SHRINE, ECBlocks.ORE_SHRINE, ECBlocks.OVERLOAD_SHRINE,
				ECBlocks.SWEET_SHRINE, ECBlocks.BREEDING_SHRINE, ECBlocks.GROVE_SHRINE);
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).add(ECBlocks.GROWTH_SHRINE, ECBlocks.HARVEST_SHRINE, ECBlocks.ORE_SHRINE, ECBlocks.SWEET_SHRINE, ECBlocks.VACUUM_SHRINE,
				ECBlocks.FIRE_PYLON, ECBlocks.BREEDING_SHRINE, ECBlocks.GROVE_SHRINE);
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).add(ECBlocks.SWEET_SHRINE, ECBlocks.VACUUM_SHRINE, ECBlocks.FIRE_PYLON);

		tag(Tags.Blocks.ORES).add(ECBlocks.CRYSTAL_ORE);
		tag(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Blocks.BASALT, Blocks.POLISHED_BASALT);
		tag(ECTags.Blocks.PUREROCKS).add(ECBlocks.PURE_ROCK, ECBlocks.PURE_ROCK_SLAB, ECBlocks.PURE_ROCK_STAIRS, ECBlocks.PURE_ROCK_WALL);
		tag(ECTags.Blocks.SMALL_TANK_COMPATIBLES).add(ECBlocks.EXTRACTOR, ECBlocks.INFUSER, ECBlocks.EVAPORATOR);
		tag(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);

		tag(BlockTags.BEACON_BASE_BLOCKS).add(ECBlocks.DRENCHED_IRON_BLOCK, ECBlocks.SWIFT_ALLOY_BLOCK, ECBlocks.FIREITE_BLOCK);

		tag(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON).add(ECBlocks.DRENCHED_IRON_BLOCK);
		tag(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY).add(ECBlocks.SWIFT_ALLOY_BLOCK);
		tag(ECTags.Blocks.STORAGE_BLOCKS_FIREITE).add(ECBlocks.FIREITE_BLOCK);
		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Blocks.STORAGE_BLOCKS_FIREITE);
		
		tag(ECTags.Blocks.BAG_OF_YURTING_BLACKLIST).add(ECBlocks.SOURCE);
	}
}
