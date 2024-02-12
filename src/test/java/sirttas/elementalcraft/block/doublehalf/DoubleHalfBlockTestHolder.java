package sirttas.elementalcraft.block.doublehalf;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public record DoubleHalfBlockTestHolder(
        String template,
        Block block,
        BlockPos pos1,
        BlockPos pos2
) {

    public static final String BATCH_NAME = "doubleHalf";

    public static final List<DoubleHalfBlockTestHolder> HOLDERS = Stream.of(
            of("millgametests.air_mill_grindstone", ECBlocks.AIR_MILL_GRINDSTONE.get(), new BlockPos(0, 2, 0)),
            of("millgametests.air_mill_wood_saw", ECBlocks.AIR_MILL_WOOD_SAW.get(), new BlockPos(0, 2, 0)),
            of("enchantmentliquefiergametests.should_transferenchantment", ECBlocks.ENCHANTMENT_LIQUEFIER.get(), new BlockPos(0, 2, 0)),
            of("breedingshrinegametests.should_breedcows", ECBlocks.BREEDING_SHRINE.get(), new BlockPos(0, 2, 3), new BlockPos(1, 2, 3)),
            of("enderlockshrinegametests.should_preventendermanfromteleporting", ECBlocks.ENDER_LOCK_SHRINE.get()),
            of("overclockedaccelerationshrineupgradegametests.should_allowselementtransfer", ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get(), new BlockPos(1, 2, 1)),
            of("sourcebreedergametests.source_breeder", ECBlocks.SOURCE_BREEDER.get(), new BlockPos(0, 1, 2))
    ).<DoubleHalfBlockTestHolder>mapMulti(List::forEach).toList();

    public static List<DoubleHalfBlockTestHolder> of(String template, Block block, BlockPos pos1, BlockPos pos2) {
        return List.of(
                new DoubleHalfBlockTestHolder(template, block, pos1, pos2),
                new DoubleHalfBlockTestHolder(template, block, pos2, pos1)
        );
    }

    public static List<DoubleHalfBlockTestHolder> of(String template, Block block, BlockPos pos1) {
        return of(template, block, pos1, pos1.above());
    }

    public static List<DoubleHalfBlockTestHolder> of(String template, Block block) {
        return of(template, block, new BlockPos(0, 1, 0), new BlockPos(0, 2, 0));
    }

    public TestFunction createTestFunction(String name, BiConsumer<GameTestHelper, DoubleHalfBlockTestHolder> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, Rotation.NONE, h -> function.accept(h, this));
    }
}
