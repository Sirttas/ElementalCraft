package sirttas.elementalcraft.block.doublehalf;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.airmill.AirMillTestCaseHolder;
import sirttas.elementalcraft.block.container.reservoir.ReservoirGameTests;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.block.source.breeder.SourceBreederGameTests;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record DoubleHalfBlockTestCaseHolder(
        String template,
        Supplier<Block> block,
        BlockPos pos1,
        BlockPos pos2
) {

    public static final String GROUP = "level.blocks.doubleHalf";

    public static final List<DoubleHalfBlockTestCaseHolder> HOLDERS = flatten(Stream.of(
            flatten(AirMillTestCaseHolder.HOLDERS.stream().map(h -> of(h.template(), h.block(), new BlockPos(0, 1, 0)))),
            of(InstrumentTestTemplates.ENCHANTMENT_LIQUEFIER_TEMPLATE_NAME, ECBlocks.ENCHANTMENT_LIQUEFIER, new BlockPos(0, 1, 0)),
            of("breedingshrinegametests.should_breedcows", ECBlocks.BREEDING_SHRINE, new BlockPos(0, 1, 3), new BlockPos(1, 1, 3)),
            of("enderlockshrinegametests.should_preventendermanfromteleporting", ECBlocks.ENDER_LOCK_SHRINE),
            of("overclockedaccelerationshrineupgradegametests.should_allowselementtransfer", ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE, new BlockPos(1, 1, 1)),
            of(SourceBreederGameTests.TEMPLATE_NAME, ECBlocks.SOURCE_BREEDER, new BlockPos(0, 0, 2)),
            of(ReservoirGameTests.FIRE_RESERVOIR_TEMPLATE_NAME, ECBlocks.FIRE_RESERVOIR),
            of(ReservoirGameTests.WATER_RESERVOIR_TEMPLATE_NAME, ECBlocks.WATER_RESERVOIR),
            of(ReservoirGameTests.EARTH_RESERVOIR_TEMPLATE_NAME, ECBlocks.EARTH_RESERVOIR),
            of(ReservoirGameTests.AIR_RESERVOIR_TEMPLATE_NAME, ECBlocks.AIR_RESERVOIR)
    ));

    @SuppressWarnings("unchecked")
    public static List<DoubleHalfBlockTestCaseHolder> of(String template, Supplier<? extends Block> block, BlockPos pos1, BlockPos pos2) {
        return List.of(
                new DoubleHalfBlockTestCaseHolder(template, (Supplier<Block>) block, pos1, pos2),
                new DoubleHalfBlockTestCaseHolder(template, (Supplier<Block>) block, pos2, pos1)
        );
    }

    public static List<DoubleHalfBlockTestCaseHolder> of(String template, Supplier<? extends Block> block, BlockPos pos1) {
        return of(template, block, pos1, pos1.above());
    }

    public static List<DoubleHalfBlockTestCaseHolder> of(String template, Supplier<? extends Block> block) {
        return of(template, block, BlockPos.ZERO, new BlockPos(0, 1, 0));
    }

    private static List<DoubleHalfBlockTestCaseHolder> flatten(Stream<List<DoubleHalfBlockTestCaseHolder>> stream) {
        return stream.<DoubleHalfBlockTestCaseHolder>mapMulti(List::forEach).toList();
    }

    public Test createTest(String name, String description, BiConsumer<ECGameTestHelper, DoubleHalfBlockTestCaseHolder> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, template, h -> function.accept(h, this));
    }
}
