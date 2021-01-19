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
import sirttas.elementalcraft.block.shrine.BlockShrine;
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
		getOrCreateBuilder(ECTags.Blocks.SHRINES).add(getBlocksForClass(BlockShrine.class));
		getOrCreateBuilder(ECTags.Blocks.PEDESTALS).add(getBlocksForClass(BlockPedestal.class));

		getOrCreateBuilder(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.infuser, ECBlocks.binder, ECBlocks.crystallizer, ECBlocks.inscriber, ECBlocks.fireFurnace, ECBlocks.fireBlastFurnace,
				ECBlocks.purifier, ECBlocks.improvedBinder);

		getOrCreateBuilder(ECTags.Blocks.RUNE_AFFECTED_SPEED).addTags(ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.PEDESTALS).add(ECBlocks.evaporator, ECBlocks.extractor, ECBlocks.improvedExtractor,
				ECBlocks.pureInfuser);
		getOrCreateBuilder(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addTags(ECTags.Blocks.INSTRUMENTS, ECTags.Blocks.PEDESTALS).add(ECBlocks.evaporator);
		getOrCreateBuilder(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.crystallizer, ECBlocks.purifier);

		getOrCreateBuilder(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).add(ECBlocks.growthShrine, ECBlocks.harvestShrine, ECBlocks.lavaShrine, ECBlocks.oreShrine,
				ECBlocks.overloadShrine, ECBlocks.sweetShrine, ECBlocks.breedingShrine);
		getOrCreateBuilder(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).add(ECBlocks.growthShrine, ECBlocks.harvestShrine, ECBlocks.oreShrine, ECBlocks.sweetShrine, ECBlocks.vacuumShrine,
				ECBlocks.firePylon, ECBlocks.breedingShrine);
		getOrCreateBuilder(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).add(ECBlocks.sweetShrine, ECBlocks.vacuumShrine, ECBlocks.firePylon);

		getOrCreateBuilder(Tags.Blocks.ORES).add(ECBlocks.crystalOre);
		getOrCreateBuilder(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Blocks.BASALT, Blocks.POLISHED_BASALT);
		getOrCreateBuilder(ECTags.Blocks.PUREROCKS).add(ECBlocks.pureRock, ECBlocks.pureRockSlab, ECBlocks.pureRockStairs, ECBlocks.pureRockWall);
		getOrCreateBuilder(ECTags.Blocks.SMALL_TANK_COMPATIBLES).add(ECBlocks.extractor, ECBlocks.infuser, ECBlocks.evaporator);
		getOrCreateBuilder(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);

		getOrCreateBuilder(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON).add(ECBlocks.drenchedIronBlock);
		getOrCreateBuilder(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY).add(ECBlocks.swiftAlloyBlock);
		getOrCreateBuilder(ECTags.Blocks.STORAGE_BLOCKS_FIREITE).add(ECBlocks.fireiteBlock);
		getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTags(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Blocks.STORAGE_BLOCKS_FIREITE);
	}
}
