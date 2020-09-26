package sirttas.elementalcraft.data.tag;

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
import sirttas.elementalcraft.tag.ECTags;

public class ECBlockTagsProvider extends BlockTagsProvider {

	public ECBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraft.MODID, existingFileHelper);
	}

	private Block[] getBlocksForClass(Class<?> clazz) {
		return registry.stream().filter(b -> ElementalCraft.MODID.equals(b.getRegistryName().getNamespace()) && clazz.isInstance(b)).sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new);
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(BlockTags.SLABS).add(getBlocksForClass(SlabBlock.class));
		getOrCreateBuilder(BlockTags.STAIRS).add(getBlocksForClass(StairsBlock.class));
		getOrCreateBuilder(BlockTags.WALLS).add(getBlocksForClass(WallBlock.class));
		getOrCreateBuilder(BlockTags.FENCES).add(getBlocksForClass(FenceBlock.class));
		getOrCreateBuilder(Tags.Blocks.GLASS_PANES).add(getBlocksForClass(PaneBlock.class));
		getOrCreateBuilder(ECTags.Blocks.PIPES).add(getBlocksForClass(BlockElementPipe.class));

		getOrCreateBuilder(Tags.Blocks.ORES).add(ECBlocks.crystalOre);
		getOrCreateBuilder(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Blocks.BASALT, Blocks.POLISHED_BASALT);
		getOrCreateBuilder(ECTags.Blocks.PUREROCKS).add(ECBlocks.pureRock, ECBlocks.pureRockSlab, ECBlocks.pureRockStairs, ECBlocks.pureRockWall);
		getOrCreateBuilder(ECTags.Blocks.SMALL_TANK_COMPATIBLES).add(ECBlocks.extractor).add(ECBlocks.infuser);
		getOrCreateBuilder(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);
	}
}
