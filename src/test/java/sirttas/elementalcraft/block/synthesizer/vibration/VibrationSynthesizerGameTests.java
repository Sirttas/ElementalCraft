package sirttas.elementalcraft.block.synthesizer.vibration;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = VibrationSynthesizerGameTests.GROUP)
public class VibrationSynthesizerGameTests {

    public static final String GROUP = "synthesizer.vibration";

    public static final String TEMPLATE_NAME = "elementalcraft:vibration_synthesizer";

    @RegisterStructureTemplate(TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = StructureTemplateBuilder.lazy(21, 3, 21,
            builder -> builder.fill(0, 0, 0, 20, 0, 20, ECBlocks.WHITE_ROCK_BRICK.get())
                    .set(10, 1, 10, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(10, 2, 10, ECBlocks.VIBRATION_SYNTHESIZER.get().defaultBlockState()));

    @TestHolder(description = "Checks if the vibration air synthesizer generates air from wandering chickens.")
    @GameTest(template = TEMPLATE_NAME, timeoutTicks = 200, required = false)
    public static void should_generateAirFromSurroundingEntityMovement(ECGameTestHelper helper) {
        var storage = helper.requireElementContainer(new BlockPos(10, 2, 10));

        helper.startSequence()
                .thenExecuteAfter(1, () -> {
                    for (int i = 0; i < 5; i++) {
                        helper.spawn(EntityType.CHICKEN, new BlockPos(9, 3, 9));
                    }
                })
                .thenExecuteAfter(100, ECGameTestUtils.fixAssertions(() -> {
                    assertThat(storage.getElementType())
                            .isEqualTo(ElementType.AIR);
                    assertThat(storage.getElementAmount())
                            .isGreaterThan(100);
                }))
                .thenExecute(() -> helper.getEntities(EntityType.CHICKEN).forEach(Entity::discard))
                .thenSucceed();
    }

}
