package sirttas.elementalcraft.datagen.tag;

import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.tag.ECTags;

public abstract class ECBlockItemTagsProvider extends BlockItemTagsProvider {

    @Override
    protected void run() {
        tag(ECTags.Blocks.STRIPPED_DARK_OAK, ECTags.Items.STRIPPED_DARK_OAK)
                .add(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD);
        tag(ECTags.Blocks.STRIPPED_BIRCH, ECTags.Items.STRIPPED_BIRCH)
                .add(Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_BIRCH_WOOD);
        tag(ECTags.Blocks.STRIPPED_ACACIA, ECTags.Items.STRIPPED_ACACIA)
                .add(Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_ACACIA_WOOD);
        tag(ECTags.Blocks.STRIPPED_JUNGLE, ECTags.Items.STRIPPED_JUNGLE)
                .add(Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD);
        tag(ECTags.Blocks.STRIPPED_SPRUCE, ECTags.Items.STRIPPED_SPRUCE)
                .add(Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_WOOD);
        tag(ECTags.Blocks.STRIPPED_MANGROVE, ECTags.Items.STRIPPED_MANGROVE)
                .add(Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD);
        tag(ECTags.Blocks.STRIPPED_CRIMSON, ECTags.Items.STRIPPED_CRIMSON)
                .add(Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_STEM);
        tag(ECTags.Blocks.STRIPPED_WARPED, ECTags.Items.STRIPPED_WARPED)
                .add(Blocks.STRIPPED_WARPED_HYPHAE, Blocks.STRIPPED_WARPED_STEM);
        tag(ECTags.Blocks.STRIPPED_CHERRY, ECTags.Items.STRIPPED_CHERRY)
                .add(Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_CHERRY_WOOD);
        tag(ECTags.Blocks.STRIPPED_BAMBOO, ECTags.Items.STRIPPED_BAMBOO)
                .add(Blocks.STRIPPED_BAMBOO_BLOCK);

        // dynamic content — populated via getBlocksForClass in ECBlockTagsProvider, copied in ECItemTagsProvider
        tag(BlockTags.SLABS, ItemTags.SLABS);
        tag(BlockTags.STAIRS, ItemTags.STAIRS);
        tag(BlockTags.WALLS, ItemTags.WALLS);
        tag(BlockTags.FENCES, ItemTags.FENCES);
        tag(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
        tag(ECTags.Blocks.PIPES, ECTags.Items.PIPES);
        tag(ECTags.Blocks.SHRINES, ECTags.Items.SHRINES);
        tag(ECTags.Blocks.SHRINE_UPGRADES, ECTags.Items.SHRINE_UPGRADES);

        tag(ECTags.Blocks.ORES_INERT_CRYSTAL, ECTags.Items.ORES_INERT_CRYSTAL)
                .add(ECBlocks.CRYSTAL_ORE.get(), ECBlocks.DEEPSLATE_CRYSTAL_ORE.get());
        tag(Tags.Blocks.ORES, Tags.Items.ORES)
                .addTag(ECTags.Blocks.ORES_INERT_CRYSTAL);
        tag(Tags.Blocks.BUDDING_BLOCKS, Tags.Items.BUDDING_BLOCKS)
                .add(Blocks.BUDDING_AMETHYST);
        tag(Tags.Blocks.BUDS, Tags.Items.BUDS)
                .add(ECBlocks.SMALL_SPRINGALINE_BUD.get(), ECBlocks.MEDIUM_SPRINGALINE_BUD.get(), ECBlocks.LARGE_SPRINGALINE_BUD.get());
        tag(Tags.Blocks.CLUSTERS, Tags.Items.CLUSTERS)
                .add(ECBlocks.SPRINGALINE_CLUSTER.get());

        tag(ECTags.Blocks.PUREROCKS, ECTags.Items.PUREROCKS)
                .add(ECBlocks.PURE_ROCK.get(), ECBlocks.PURE_ROCK_SLAB.get(), ECBlocks.PURE_ROCK_STAIRS.get(), ECBlocks.PURE_ROCK_WALL.get());
        tag(ECTags.Blocks.INSTRUMENTS, ECTags.Items.INSTRUMENTS).add(ECBlocks.INFUSER.get(), ECBlocks.BINDER.get(),
                ECBlocks.CRYSTALLIZER.get(), ECBlocks.INSCRIBER.get(), ECBlocks.FIRE_FURNACE.get(),
                ECBlocks.FIRE_BLAST_FURNACE.get(), ECBlocks.PURIFIER.get(), ECBlocks.WATER_MILL_GRINDSTONE.get(),
                ECBlocks.AIR_MILL_GRINDSTONE.get(), ECBlocks.WATER_MILL_WOOD_SAW.get(),
                ECBlocks.AIR_MILL_WOOD_SAW.get(), ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECBlocks.BINDER_IMPROVED.get());

        tag(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON)
                .add(ECBlocks.DRENCHED_IRON_BLOCK.get());
        tag(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY)
                .add(ECBlocks.SWIFT_ALLOY_BLOCK.get());
        tag(ECTags.Blocks.STORAGE_BLOCKS_FIREITE, ECTags.Items.STORAGE_BLOCKS_FIREITE)
                .add(ECBlocks.FIREITE_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS)
                .addTags(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Blocks.STORAGE_BLOCKS_FIREITE);

        tag(ECTags.Blocks.SOURCES, ECTags.Items.FULL_RECEPTACLES)
                .add(ECBlocks.FIRE_SOURCE.get(), ECBlocks.WATER_SOURCE.get(), ECBlocks.EARTH_SOURCE.get(), ECBlocks.AIR_SOURCE.get());
    }
}