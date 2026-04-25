package sirttas.elementalcraft.datagen.definition;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.Map;
import java.util.stream.Stream;

public class ECBlockFamilies {

    private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

    public static final BlockFamily WHITE_ROCK = familyBuilder(ECBlocks.WHITE_ROCK.get())
            .wall(ECBlocks.WHITE_ROCK_WALL.get())
            .slab(ECBlocks.WHITE_ROCK_SLAB.get())
            .stairs(ECBlocks.WHITE_ROCK_STAIRS.get())
            .bricks(ECBlocks.WHITE_ROCK_BRICKS.get())
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily WHITE_ROCK_BRICKS = familyBuilder(ECBlocks.WHITE_ROCK_BRICKS.get())
            .wall(ECBlocks.WHITE_ROCK_BRICK_WALL.get())
            .stairs(ECBlocks.WHITE_ROCK_BRICK_STAIRS.get())
            .slab(ECBlocks.WHITE_ROCK_BRICK_SLAB.get())
            .dontGenerateCraftingRecipe()
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily MOSSY_WHITE_ROCK = familyBuilder(ECBlocks.MOSSY_WHITE_ROCK.get())
            .wall(ECBlocks.MOSSY_WHITE_ROCK_WALL.get())
            .stairs(ECBlocks.MOSSY_WHITE_ROCK_STAIRS.get())
            .slab(ECBlocks.MOSSY_WHITE_ROCK_SLAB.get())
            .dontGenerateCraftingRecipe()
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily BURNT_WHITE_ROCK = familyBuilder(ECBlocks.BURNT_WHITE_ROCK.get())
            .wall(ECBlocks.BURNT_WHITE_ROCK_WALL.get())
            .stairs(ECBlocks.BURNT_WHITE_ROCK_STAIRS.get())
            .slab(ECBlocks.BURNT_WHITE_ROCK_SLAB.get())
            .dontGenerateCraftingRecipe()
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily PURE_ROCK = familyBuilder(ECBlocks.PURE_ROCK.get())
            .wall(ECBlocks.PURE_ROCK_WALL.get())
            .stairs(ECBlocks.PURE_ROCK_STAIRS.get())
            .slab(ECBlocks.PURE_ROCK_SLAB.get())
            .dontGenerateCraftingRecipe()
            .generateStonecutterRecipe()
            .getFamily();

    private ECBlockFamilies() { }

    private static BlockFamily.Builder familyBuilder(Block base) {
        BlockFamily.Builder builder = new BlockFamily.Builder(base);
        BlockFamily blockFamily = MAP.put(base, builder.getFamily());

        if (blockFamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(base));
        } else {
            return builder;
        }
    }

    public static Stream<BlockFamily> getAllFamilies() {
        return MAP.values().stream();
    }

    public static @Nullable BlockFamily getFamily(Block base) {
        return MAP.get(base);
    }
}
