package sirttas.elementalcraft.block.airmill;

import net.minecraft.world.level.block.Block;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerGameTests;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record AirMillTestCaseHolder(
        String template,
        Supplier<Block> block
) {

    public static final String GROUP = "level.blocks.airMills";

    public static final List<AirMillTestCaseHolder> HOLDERS = List.of(
            of(AirMillSynthesizerGameTests.TEMPLATE_NAME, ECBlocks.AIR_MILL_SYNTHESIZER),
            of("millgametests.air_mill_grindstone", ECBlocks.AIR_MILL_GRINDSTONE),
            of("millgametests.air_mill_wood_saw", ECBlocks.AIR_MILL_WOOD_SAW)
    );

    @SuppressWarnings("unchecked")
    private static AirMillTestCaseHolder of(String template, Supplier<? extends Block> block) {
        return new AirMillTestCaseHolder(template, (Supplier<Block>) block);
    }

    public Test createTest(String name, String description, BiConsumer<ECGameTestHelper, AirMillTestCaseHolder> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, template, h -> function.accept(h, this));
    }
}
