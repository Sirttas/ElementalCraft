package sirttas.elementalcraft.block.synthesizer.vibration;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
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



    @TestHolder(description = "Checks that vibration air synthesizer catch a vibration and changes state.")
    @GameTest
    public static void should_catchVibrationAndChangeState(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(1, 2, 2)
                .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(0, 1, 0, ECBlocks.VIBRATION_SYNTHESIZER.get().defaultBlockState()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var storage = helper.requireElementContainer(new BlockPos(0, 1, 0));

            helper.startSequence()
                    .thenExecute(() -> {
                        helper.getLevel().gameEvent(GameEvent.STEP, helper.absoluteVec(Vec3.ZERO), GameEvent.Context.of(null, null));
                    })
                    .thenExecuteAfter(1, () -> {
                        helper.assertBlockState(new BlockPos(0, 2, 0), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.ACTIVE, () -> "Vibration synthesizer should be active after receiving vibration");
                    })
                    .thenExecuteAfter(30, () -> {
                        helper.assertBlockState(new BlockPos(0, 2, 0), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.COOLDOWN, () -> "Vibration synthesizer should be in cooldown after 30 ticks");
                    })
                    .thenExecuteAfter(70, ECGameTestUtils.fixAssertions(() -> {
                        assertThat(storage.getElementType())
                                .isEqualTo(ElementType.AIR);
                        assertThat(storage.getElementAmount())
                                .isEqualTo(100);
                    }))
                    .thenExecute(() -> helper.getEntities(EntityType.CHICKEN).forEach(Entity::discard))
                    .thenSucceed();
        });
    }

}
