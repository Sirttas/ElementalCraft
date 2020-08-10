package sirttas.elementalcraft.data;

import java.util.Comparator;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.tag.ECTags;

public class ECBlockTagsProvider extends BlockTagsProvider {

	public ECBlockTagsProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerTags() {
		Predicate<Block> filter = b -> ElementalCraft.MODID.equals(b.getRegistryName().getNamespace());

		getBuilder(BlockTags.SLABS).add(registry.stream().filter(filter).filter(b -> b instanceof SlabBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		getBuilder(BlockTags.STAIRS).add(registry.stream().filter(filter).filter(b -> b instanceof StairsBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		getBuilder(BlockTags.WALLS).add(registry.stream().filter(filter).filter(b -> b instanceof WallBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		getBuilder(BlockTags.FENCES).add(registry.stream().filter(filter).filter(b -> b instanceof FenceBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));

		getBuilder(Tags.Blocks.ORES).add(ECBlocks.crystalOre);
		getBuilder(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Tags.Blocks.STONE, Tags.Blocks.COBBLESTONE);
		getBuilder(ECTags.Blocks.PUREROCKS).add(ECBlocks.pureRock, ECBlocks.pureRockSlab, ECBlocks.pureRockStairs, ECBlocks.pureRockWall);
		getBuilder(ECTags.Blocks.SMALL_TANK_COMPATIBLES).add(ECBlocks.extractor, ECBlocks.infuser);
		getBuilder(BlockTags.WITHER_IMMUNE).add(ECTags.Blocks.PUREROCKS);
	}
}
