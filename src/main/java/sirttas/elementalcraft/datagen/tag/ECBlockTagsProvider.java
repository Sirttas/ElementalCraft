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
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.BlockElementPipe;
import sirttas.elementalcraft.block.pureinfuser.pedestal.BlockPedestal;
import sirttas.elementalcraft.block.shrine.AbstractBlockShrine;
import sirttas.elementalcraft.tag.ECTags;

public class ECBlockTagsProvider extends BlockTagsProvider {

	public ECBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraft.MODID, existingFileHelper);
	}

	private Block[] getBlocksForClass(Class<?> clazz) {
		return registry.stream().filter(b -> ElementalCraft.MODID.equals(b.getRegistryName().getNamespace()) && clazz.isInstance(b)).sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void registerTags() {
		getOrCreateBuilder(BlockTags.SLABS).add(getBlocksForClass(SlabBlock.class));
		getOrCreateBuilder(BlockTags.STAIRS).add(getBlocksForClass(StairsBlock.class));
		getOrCreateBuilder(BlockTags.WALLS).add(getBlocksForClass(WallBlock.class));
		getOrCreateBuilder(BlockTags.FENCES).add(getBlocksForClass(FenceBlock.class));
		getOrCreateBuilder(Tags.Blocks.GLASS_PANES).add(getBlocksForClass(PaneBlock.class));
		getOrCreateBuilder(ECTags.Blocks.PIPES).add(getBlocksForClass(BlockElementPipe.class));
		getOrCreateBuilder(ECTags.Blocks.SHRINES).add(getBlocksForClass(AbstractBlockShrine.class));
		getOrCreateBuilder(ECTags.Blocks.PEDESTALS).add(getBlocksForClass(BlockPedestal.class));

		getOrCreateBuilder(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.INFUSER, ECBlocks.BINDER, ECBlocks.CRYSTALLIZER, ECBlocks.INSCRIBER, ECBlocks.FIRE_FURNACE, ECBlocks.FIRE_BLAST_FURNACE,
				ECBlocks.PURIFIER, ECBlocks.BINDER_IMPROVED);

		getOrCreateBuilder(ECTags.Blocks.RUNE_AFFECTED).addTags(ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.PEDESTALS).add(ECBlocks.EVAPORATOR, ECBlocks.EXTRACTOR, ECBlocks.EXTRACTOR_IMPROVED,
				ECBlocks.SOLAR_SYNTHESIZER, ECBlocks.PURE_INFUSER);
		getOrCreateBuilder(ECTags.Blocks.RUNE_AFFECTED_SPEED).addTags(ECTags.Blocks.RUNE_AFFECTED);
		getOrCreateBuilder(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addTags(ECTags.Blocks.RUNE_AFFECTED);
		getOrCreateBuilder(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.CRYSTALLIZER, ECBlocks.PURIFIER);

		getOrCreateBuilder(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).add(ECBlocks.GROWTH_SHRINE, ECBlocks.HARVEST_SHRINE, ECBlocks.LAVA_SHRINE, ECBlocks.ORE_SHRINE,
				ECBlocks.OVERLOAD_SHRINE, ECBlocks.SWEET_SHRINE, ECBlocks.BREEDING_SHRINE, ECBlocks.GROVE_SHRINE);
		getOrCreateBuilder(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).add(ECBlocks.GROWTH_SHRINE, ECBlocks.HARVEST_SHRINE, ECBlocks.ORE_SHRINE, ECBlocks.SWEET_SHRINE, ECBlocks.VACUUM_SHRINE,
				ECBlocks.FIRE_PYLON, ECBlocks.BREEDING_SHRINE, ECBlocks.GROVE_SHRINE);
		getOrCreateBuilder(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).add(ECBlocks.SWEET_SHRINE, ECBlocks.VACUUM_SHRINE, ECBlocks.FIRE_PYLON);

		getOrCreateBuilder(Tags.Blocks.ORES).add(ECBlocks.CRYSTAL_ORE);
		getOrCreateBuilder(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Blocks.BASALT, Blocks.POLISHED_BASALT);
		getOrCreateBuilder(ECTags.Blocks.PUREROCKS).add(ECBlocks.PURE_ROCK, ECBlocks.PURE_ROCK_SLAB, ECBlocks.PURE_ROCK_STAIRS, ECBlocks.PURE_ROCK_WALL);
		getOrCreateBuilder(ECTags.Blocks.SMALL_TANK_COMPATIBLES).add(ECBlocks.EXTRACTOR, ECBlocks.INFUSER, ECBlocks.EVAPORATOR);
		getOrCreateBuilder(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);
		
		getOrCreateBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ECBlocks.DRENCHED_IRON_BLOCK, ECBlocks.SWIFT_ALLOY_BLOCK, ECBlocks.FIREITE_BLOCK);

		getOrCreateBuilder(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON).add(ECBlocks.DRENCHED_IRON_BLOCK);
		getOrCreateBuilder(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY).add(ECBlocks.SWIFT_ALLOY_BLOCK);
		getOrCreateBuilder(ECTags.Blocks.STORAGE_BLOCKS_FIREITE).add(ECBlocks.FIREITE_BLOCK);
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTags(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Blocks.STORAGE_BLOCKS_FIREITE);
	}
}
