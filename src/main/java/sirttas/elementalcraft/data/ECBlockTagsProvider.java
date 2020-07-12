package sirttas.elementalcraft.data;

import java.util.Comparator;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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

		func_240522_a_/* getBuilder */(BlockTags.SLABS).func_240534_a_/* add */(registry.stream().filter(filter).filter(b -> b instanceof SlabBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		func_240522_a_/* getBuilder */(BlockTags.STAIRS).func_240534_a_/* add */(registry.stream().filter(filter).filter(b -> b instanceof StairsBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		func_240522_a_/* getBuilder */(BlockTags.WALLS).func_240534_a_/* add */(registry.stream().filter(filter).filter(b -> b instanceof WallBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));
		func_240522_a_/* getBuilder */(BlockTags.FENCES).func_240534_a_/* add */(registry.stream().filter(filter).filter(b -> b instanceof FenceBlock).sorted(Comparator.comparing(Block::getRegistryName)).toArray(Block[]::new));

		func_240522_a_/* getBuilder */(Tags.Blocks.ORES).func_240534_a_/* add */(ECBlocks.crystalOre);
		func_240522_a_/* getBuilder */(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).func_240534_a_/* add */(Blocks.field_235337_cO_/* BASALT */, Blocks.field_235338_cP_/* POLISHED_BASALT */);
		func_240522_a_/* getBuilder */(ECTags.Blocks.PUREROCKS).func_240534_a_/* add */(ECBlocks.pureRock, ECBlocks.pureRockSlab, ECBlocks.pureRockStairs, ECBlocks.pureRockWall);
		func_240522_a_/* getBuilder */(BlockTags.WITHER_IMMUNE).func_240531_a_/* add */(ECTags.Blocks.PUREROCKS);
	}
}
