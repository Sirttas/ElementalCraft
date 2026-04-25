package sirttas.elementalcraft.block.airmill;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.air.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air.AirMillWoodSawBlockEntity;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerGameTests;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record AirMillTestCaseHolder<T extends BlockEntity & AirMill>(
        String template,
        Supplier<Block> block,
        Class<T> blockEntityType
) {

    public static final String GROUP = "level.blocks.airMills";

    public static final List<AirMillTestCaseHolder<?>> HOLDERS = List.of(
            of(AirMillSynthesizerGameTests.TEMPLATE_NAME, ECBlocks.AIR_MILL_SYNTHESIZER, AirMillSynthesizerBlockEntity.class),
            of(InstrumentTestTemplates.AIR_MILL_GRINDSTONE_TEMPLATE_NAME, ECBlocks.AIR_MILL_GRINDSTONE, AirMillGrindstoneBlockEntity.class),
            of(InstrumentTestTemplates.AIR_MILL_WOOD_SAW_TEMPLATE_NAME, ECBlocks.AIR_MILL_WOOD_SAW, AirMillWoodSawBlockEntity.class)
    );

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity & AirMill> AirMillTestCaseHolder<T> of(String template, Supplier<? extends Block> block, Class<T> blockEntityType) {
        return new AirMillTestCaseHolder<>(template, (Supplier<Block>) block, blockEntityType);
    }

    public Test createTest(String name, String description, BiConsumer<ECGameTestHelper, AirMillTestCaseHolder<T>> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, template, h -> function.accept(h, this));
    }
}
