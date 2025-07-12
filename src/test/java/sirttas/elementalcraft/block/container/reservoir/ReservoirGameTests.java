package sirttas.elementalcraft.block.container.reservoir;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ContainerGameTests;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservoirGameTests {

    public static final String GROUP = ContainerGameTests.GROUP + ".reservoirs";

    public static final String FIRE_RESERVOIR_TEMPLATE_NAME = "elementalcraft:fire_reservoir";
    public static final String WATER_RESERVOIR_TEMPLATE_NAME = "elementalcraft:water_reservoir";
    public static final String EARTH_RESERVOIR_TEMPLATE_NAME = "elementalcraft:earth_reservoir";
    public static final String AIR_RESERVOIR_TEMPLATE_NAME = "elementalcraft:air_reservoir";

    @RegisterStructureTemplate(FIRE_RESERVOIR_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> FIRE_RESERVOIR_TEMPLATE = createReservoirTemplate(ECBlocks.FIRE_RESERVOIR);
    @RegisterStructureTemplate(WATER_RESERVOIR_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> WATER_RESERVOIR_TEMPLATE = createReservoirTemplate(ECBlocks.WATER_RESERVOIR);
    @RegisterStructureTemplate(EARTH_RESERVOIR_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EARTH_RESERVOIR_TEMPLATE = createReservoirTemplate(ECBlocks.EARTH_RESERVOIR);
    @RegisterStructureTemplate(AIR_RESERVOIR_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> AIR_RESERVOIR_TEMPLATE = createReservoirTemplate(ECBlocks.AIR_RESERVOIR);

    private static @NotNull Supplier<StructureTemplate> createReservoirTemplate(Supplier<ReservoirBlock> reservoirSupplier) {
        return StructureTemplateBuilder.lazy(1, 3, 1, builder -> { // taller so we can add instruments on top
            var defaultState = reservoirSupplier.get().defaultBlockState();

            return builder.set(0, 0, 0, defaultState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))
                    .set(0, 1, 0, defaultState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER));
        });
    }

    public static List<Test> should_insertElementFromBothParts() {
        var i = 0;

        return List.of(
                ECGameTestUtils.createTest(GROUP, "should_insertElementFromBothParts#" + i++, "Check if element can be inserted into both part of the fire reservoir", FIRE_RESERVOIR_TEMPLATE_NAME, h -> should_insertElementFromBothParts(h, ElementType.FIRE)),
                ECGameTestUtils.createTest(GROUP, "should_insertElementFromBothParts#" + i++, "Check if element can be inserted into both part of the water reservoir", WATER_RESERVOIR_TEMPLATE_NAME, h -> should_insertElementFromBothParts(h, ElementType.WATER)),
                ECGameTestUtils.createTest(GROUP, "should_insertElementFromBothParts#" + i++, "Check if element can be inserted into both part of the earth reservoir", EARTH_RESERVOIR_TEMPLATE_NAME, h -> should_insertElementFromBothParts(h, ElementType.EARTH)),
                ECGameTestUtils.createTest(GROUP, "should_insertElementFromBothParts#" + i++, "Check if element can be inserted into both part of the air reservoir", AIR_RESERVOIR_TEMPLATE_NAME, h -> should_insertElementFromBothParts(h, ElementType.AIR)));
    }

    private static void should_insertElementFromBothParts(ECGameTestHelper helper, ElementType type) {
        helper.startSequence().thenExecuteAfter(1, () -> {
            var bottomStorage = helper.getElementStorage(new BlockPos(0, 1, 0));
            var topStorage = helper.getElementStorage(new BlockPos(0, 1, 0));

            assertThat(bottomStorage.getElementAmount(type)).isZero();
            assertThat(topStorage.getElementAmount(type)).isZero();

            bottomStorage.insertElement(1, type, false);

            assertThat(bottomStorage.getElementAmount(type)).isEqualTo(1);
            assertThat(topStorage.getElementAmount(type)).isEqualTo(1);

            topStorage.insertElement(1, type, false);

            assertThat(bottomStorage.getElementAmount(type)).isEqualTo(2);
            assertThat(topStorage.getElementAmount(type)).isEqualTo(2);
        }).thenSucceed();
    }
}
