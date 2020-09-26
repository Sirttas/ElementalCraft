package sirttas.elementalcraft.data.tag;

import java.util.Comparator;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.BlockElementPipe;
import sirttas.elementalcraft.tag.ECTags;

public class ECBlockTagsProvider extends BlockTagsProvider {

	public ECBlockTagsProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	private Block[] getBlocksForClass(Class<?> clazz) {
		return registry.stream().filter(b -> ElementalCraft.MODID.equals(b.getRegistryName().getNamespace()) && clazz.isInstance(b)).sorted(Comparator.comparing(Block::getRegistryName))
				.toArray(Block[]::new);
	}

	@Override
	protected void registerTags() {
		getBuilder(BlockTags.SLABS).add(getBlocksForClass(SlabBlock.class));
		getBuilder(BlockTags.STAIRS).add(getBlocksForClass(StairsBlock.class));
		getBuilder(BlockTags.WALLS).add(getBlocksForClass(WallBlock.class));
		getBuilder(BlockTags.FENCES).add(getBlocksForClass(FenceBlock.class));
		getBuilder(Tags.Blocks.GLASS_PANES).add(getBlocksForClass(PaneBlock.class));
		getBuilder(ECTags.Blocks.PIPES).add(getBlocksForClass(BlockElementPipe.class));

		getBuilder(Tags.Blocks.ORES).add(ECBlocks.crystalOre);
		getBuilder(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Tags.Blocks.STONE, Tags.Blocks.COBBLESTONE);
		getBuilder(ECTags.Blocks.PUREROCKS).add(ECBlocks.pureRock, ECBlocks.pureRockSlab, ECBlocks.pureRockStairs, ECBlocks.pureRockWall);
		getBuilder(ECTags.Blocks.SMALL_TANK_COMPATIBLES).add(ECBlocks.extractor, ECBlocks.infuser);
		getBuilder(BlockTags.WITHER_IMMUNE).add(ECTags.Blocks.PUREROCKS);
	}
}
