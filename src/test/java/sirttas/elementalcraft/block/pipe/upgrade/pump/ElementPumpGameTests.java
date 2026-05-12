package sirttas.elementalcraft.block.pipe.upgrade.pump;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.template.ECStructureTemplateBuilder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.elementStorage;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.withValue;

@ForEachTest(groups = ElementPipeGameTests.GROUP)
public class ElementPumpGameTests {

    public static final String ELEMENT_PUMP_TEMPLATE_NAME = "elementalcraft:element_pump";
    public static final String ELEMENT_PUMP_WITH_RUNES_TEMPLATE_NAME = "elementalcraft:element_pump_with_runes";

    @RegisterStructureTemplate(ELEMENT_PUMP_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> ELEMENT_PUMP_TEMPLATE = createTemplate();

    @RegisterStructureTemplate(ELEMENT_PUMP_WITH_RUNES_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> ELEMENT_PUMP_WITH_RUNES_TEMPLATE = createTemplate(Runes.ZOD, Runes.ZOD, Runes.ZOD);

    @SafeVarargs
    private static Supplier<StructureTemplate> createTemplate(ResourceKey<Rune>... runes) {
        return ECStructureTemplateBuilder.lazy(3, 3, 2, builder -> builder
                .placeFloorLever(1, 1, 1, false)
                .fill(0, 0, 0, 2, 0, 1, ECBlocks.WHITE_ROCK_BRICKS.get())
                .set(0, 1, 0, ECBlocks.CONTAINER.get().defaultBlockState(), withValue(elementStorage(ElementType.WATER, 100000)))
                .set(2, 1, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .pipeline(1, 1, 0, pipeline -> pipeline
                        .extract(Direction.WEST).upgrade(Direction.WEST, PipeUpgradeTypes.ELEMENT_PUMP, runeHandler(runes))
                        .insert(Direction.EAST).upgrade(Direction.EAST, PipeUpgradeTypes.ELEMENT_VALVE)));
    }

    @TestHolder(description = "Checks that a pipe with a pump transfer 2500 element without runes.")
    @GameTest(template = ELEMENT_PUMP_TEMPLATE_NAME)
    public static void should_transfer2500Elements(ECGameTestHelper helper) {
        var sourceStorage = helper.getBlockEntity(new BlockPos(0, 1, 0), ElementContainerBlockEntity.class).getElementStorage();
        var targetStorage = helper.getBlockEntity(new BlockPos(2, 1, 0), ElementContainerBlockEntity.class).getElementStorage();
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(1, 1, 1))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(2500 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (2500 * i));
                })
                .thenSucceed();
    }

    @TestHolder(description = "Checks that a pipe with a pump transfer 6250 element with runes.")
    @GameTest(template = ELEMENT_PUMP_WITH_RUNES_TEMPLATE_NAME)
    public static void should_transfer6250Elements(ECGameTestHelper helper) {
        var sourceStorage = helper.getBlockEntity(new BlockPos(0, 1, 0), ElementContainerBlockEntity.class).getElementStorage();
        var targetStorage = helper.getBlockEntity(new BlockPos(2, 1, 0), ElementContainerBlockEntity.class).getElementStorage();
        var ticks = new AtomicInteger(0);

        helper.startSequence()
                .thenExecute(() -> helper.pullLever(1, 1, 1))
                .thenIdle(1)
                .thenExecuteFor(10, () -> {
                    var i = ticks.incrementAndGet();

                    assertThat(targetStorage.getElementAmount()).isEqualTo(6250 * i);
                    assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - (6250 * i));
                })
                .thenSucceed();
    }
}
