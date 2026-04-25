package sirttas.elementalcraft.datagen.tag;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.common.Tags;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;
import sirttas.elementalcraft.tag.ECTags;

import java.util.Map;

public abstract class ECBlockItemTagsProvider extends BlockItemTagsProvider {

    public static Block[] getBlocksForClass(Class<?> clazz) {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> ElementalCraft.owns(e) && clazz.isInstance(e.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toArray(Block[]::new);
    }

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

        tag(BlockTags.SLABS, ItemTags.SLABS)
                .add(getBlocksForClass(SlabBlock.class));
        tag(BlockTags.STAIRS, ItemTags.STAIRS)
                .add(getBlocksForClass(StairBlock.class));
        tag(BlockTags.WALLS, ItemTags.WALLS)
                .add(getBlocksForClass(WallBlock.class));
        tag(BlockTags.FENCES, ItemTags.FENCES)
                .add(getBlocksForClass(FenceBlock.class));
        tag(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES)
                .add(getBlocksForClass(IronBarsBlock.class));
        tag(ECTags.Blocks.PIPES, ECTags.Items.PIPES)
                .add(getBlocksForClass(ElementPipeBlock.class));
        tag(ECTags.Blocks.SHRINES, ECTags.Items.SHRINES)
                .add(getBlocksForClass(AbstractShrineBlock.class));
        tag(ECTags.Blocks.SHRINE_UPGRADES, ECTags.Items.SHRINE_UPGRADES)
                .add(getBlocksForClass(ShrineUpgradeBlock.class));

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
        tag(ECTags.Blocks.INSTRUMENTS, ECTags.Items.INSTRUMENTS)
                .add(ECBlocks.INFUSER.get(), ECBlocks.BINDER.get(), ECBlocks.CRYSTALLIZER.get(), ECBlocks.INSCRIBER.get(),
                        ECBlocks.FIRE_FURNACE.get(), ECBlocks.FIRE_BLAST_FURNACE.get(), ECBlocks.PURIFIER.get(),
                        ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_GRINDSTONE.get(),
                        ECBlocks.WATER_MILL_WOOD_SAW.get(), ECBlocks.AIR_MILL_WOOD_SAW.get(),
                        ECBlocks.ENCHANTMENT_LIQUEFIER.get(), ECBlocks.BINDER_IMPROVED.get());

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