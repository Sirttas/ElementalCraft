package sirttas.elementalcraft.data.tag;

import java.util.Comparator;
import java.util.function.Predicate;

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
import sirttas.elementalcraft.tag.ECTags;

public class ECBlockTagsProvider extends BlockTagsProvider {

	public ECBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraft.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags() {
		Predicate<Block> filter = b -> ElementalCraft.MODID.equals(b.getRegistryName().getNamespace());

		getOrCreateBuilder(BlockTags.SLABS).add(registry.stream().filter(filter).filter(b -> b instanceof SlabBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		getOrCreateBuilder(BlockTags.STAIRS).add(registry.stream().filter(filter).filter(b -> b instanceof StairsBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		getOrCreateBuilder(BlockTags.WALLS).add(registry.stream().filter(filter).filter(b -> b instanceof WallBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		getOrCreateBuilder(BlockTags.FENCES).add(registry.stream().filter(filter).filter(b -> b instanceof FenceBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		getOrCreateBuilder(Tags.Blocks.GLASS_PANES).add(registry.stream().filter(filter).filter(b -> b instanceof PaneBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));

		getOrCreateBuilder(Tags.Blocks.ORES).add(ECBlocks.crystalOre);
		getOrCreateBuilder(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Blocks.BASALT, Blocks.POLISHED_BASALT);
		getOrCreateBuilder(ECTags.Blocks.PUREROCKS).add(ECBlocks.pureRock, ECBlocks.pureRockSlab, ECBlocks.pureRockStairs, ECBlocks.pureRockWall);
		getOrCreateBuilder(ECTags.Blocks.SMALL_TANK_COMPATIBLES).add(ECBlocks.extractor).add(ECBlocks.infuser);
		getOrCreateBuilder(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);
	}
}
