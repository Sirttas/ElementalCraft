package sirttas.elementalcraft.datagen.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.tag.ECTags;

import java.util.Comparator;
import java.util.List;

public class ECBlockTagsProvider extends BlockTagsProvider {

	private static final List<Block> LOOT_BLACKLIST = List.of(ECBlocks.SOURCE.get(), ECBlocks.BURNT_GLASS, ECBlocks.BURNT_GLASS_PANE, ECBlocks.SPRINGALINE_GLASS, ECBlocks.SPRINGALINE_GLASS_PANE);
	
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
		lootTags();
		
		tag(BlockTags.SLABS).add(getBlocksForClass(SlabBlock.class));
		tag(BlockTags.STAIRS).add(getBlocksForClass(StairBlock.class));
		tag(BlockTags.WALLS).add(getBlocksForClass(WallBlock.class));
		tag(BlockTags.FENCES).add(getBlocksForClass(FenceBlock.class));
		tag(Tags.Blocks.GLASS_PANES).add(getBlocksForClass(IronBarsBlock.class));
		tag(ECTags.Blocks.PIPES).add(getBlocksForClass(ElementPipeBlock.class));
		tag(ECTags.Blocks.SHRINES).add(getBlocksForClass(AbstractShrineBlock.class));
		tag(ECTags.Blocks.SHRINE_UPGRADES).add(getBlocksForClass(AbstractShrineUpgradeBlock.class));
		tag(ECTags.Blocks.PEDESTALS).add(getBlocksForClass(PedestalBlock.class));

		tag(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.INFUSER, ECBlocks.BINDER, ECBlocks.CRYSTALLIZER, ECBlocks.INSCRIBER, ECBlocks.FIRE_FURNACE, ECBlocks.FIRE_BLAST_FURNACE,
				ECBlocks.PURIFIER, ECBlocks.AIR_MILL, ECBlocks.BINDER_IMPROVED);

		tag(ECTags.Blocks.CONTAINER_TOOLS).addTag(ECTags.Blocks.INSTRUMENTS).add(ECBlocks.EVAPORATOR, ECBlocks.EXTRACTOR, ECBlocks.EXTRACTOR_IMPROVED, ECBlocks.SOLAR_SYNTHESIZER, ECBlocks.MANA_SYNTHESIZER, ECBlocks.DIFFUSER);

		tag(ECTags.Blocks.RUNE_AFFECTED).addTags(ECTags.Blocks.CONTAINER_TOOLS, ECTags.Blocks.PEDESTALS).add(ECBlocks.PURE_INFUSER);
		tag(ECTags.Blocks.RUNE_AFFECTED_SPEED).addTags(ECTags.Blocks.RUNE_AFFECTED);
		tag(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addTags(ECTags.Blocks.RUNE_AFFECTED);
		tag(ECTags.Blocks.RUNE_AFFECTED_LUCK).add(ECBlocks.CRYSTALLIZER, ECBlocks.PURIFIER, ECBlocks.AIR_MILL);

		tag(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).add(ECBlocks.GROWTH_SHRINE, ECBlocks.HARVEST_SHRINE, ECBlocks.LAVA_SHRINE, ECBlocks.ORE_SHRINE, ECBlocks.OVERLOAD_SHRINE,
				ECBlocks.SWEET_SHRINE, ECBlocks.BREEDING_SHRINE, ECBlocks.GROVE_SHRINE, ECBlocks.SPRING_SHRINE, ECBlocks.BUDDING_SHRINE, ECBlocks.SPAWNING_SHRINE);
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).add(ECBlocks.GROWTH_SHRINE, ECBlocks.HARVEST_SHRINE, ECBlocks.ORE_SHRINE, ECBlocks.SWEET_SHRINE, ECBlocks.VACUUM_SHRINE,
				ECBlocks.FIRE_PYLON, ECBlocks.BREEDING_SHRINE, ECBlocks.GROVE_SHRINE, ECBlocks.ENDER_LOCK_SHRINE, ECBlocks.SPAWNING_SHRINE);
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).add(ECBlocks.SWEET_SHRINE, ECBlocks.VACUUM_SHRINE, ECBlocks.FIRE_PYLON);
		tag(ECTags.Blocks.SHRINES_UPGRADABLES_PROTECTION).add(ECBlocks.FIRE_PYLON, ECBlocks.ENDER_LOCK_SHRINE);

		tag(ECTags.Blocks.ORES_INERT_CRYSTAL).add(ECBlocks.CRYSTAL_ORE.get(), ECBlocks.DEEPSLATE_CRYSTAL_ORE.get());

		tag(Tags.Blocks.ORES).addTag(ECTags.Blocks.ORES_INERT_CRYSTAL);
		tag(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES).add(Blocks.BASALT, Blocks.POLISHED_BASALT, Blocks.SMOOTH_BASALT);
		tag(ECTags.Blocks.GROWTHSHRINE_BLACKLIST).add(Blocks.GRASS, Blocks.GRASS_BLOCK, Blocks.TALL_GRASS);
		tag(ECTags.Blocks.PUREROCKS).add(ECBlocks.PURE_ROCK.get(), ECBlocks.PURE_ROCK_SLAB, ECBlocks.PURE_ROCK_STAIRS, ECBlocks.PURE_ROCK_WALL);
		tag(ECTags.Blocks.SMALL_CONTAINER_COMPATIBLES).add(ECBlocks.EXTRACTOR, ECBlocks.INFUSER, ECBlocks.EVAPORATOR);
		tag(BlockTags.WITHER_IMMUNE).addTag(ECTags.Blocks.PUREROCKS);

		tag(BlockTags.BEACON_BASE_BLOCKS).add(ECBlocks.DRENCHED_IRON_BLOCK, ECBlocks.SWIFT_ALLOY_BLOCK, ECBlocks.FIREITE_BLOCK.get());

		tag(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON).add(ECBlocks.DRENCHED_IRON_BLOCK);
		tag(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY).add(ECBlocks.SWIFT_ALLOY_BLOCK);
		tag(ECTags.Blocks.STORAGE_BLOCKS_FIREITE).add(ECBlocks.FIREITE_BLOCK.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Blocks.STORAGE_BLOCKS_FIREITE);
		
		tag(ECTags.Blocks.BAG_OF_YURTING_BLACKLIST).add(ECBlocks.SOURCE.get());
	}
	
	private void lootTags() {
		var mineableWithPickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
		
		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraftApi.MODID.equals(block.getRegistryName().getNamespace())) {
				if (!LOOT_BLACKLIST.contains(block)) {
					mineableWithPickaxe.add(block);
				}
			}
		}
		tag(BlockTags.NEEDS_DIAMOND_TOOL).addTag(ECTags.Blocks.PUREROCKS);
		tag(BlockTags.NEEDS_IRON_TOOL).addTag(ECTags.Blocks.ORES_INERT_CRYSTAL);
	}
}
