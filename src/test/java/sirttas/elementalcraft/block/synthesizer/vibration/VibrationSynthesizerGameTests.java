package sirttas.elementalcraft.block.synthesizer.vibration;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.template.ECStructureTemplateBuilder;

import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = VibrationSynthesizerGameTests.GROUP)
public class VibrationSynthesizerGameTests {

    public static final String GROUP = "synthesizer.vibration";

    public static final String TEMPLATE_NAME = "elementalcraft:vibration_synthesizer";

    @RegisterStructureTemplate(TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = ECStructureTemplateBuilder.lazy(21, 3, 21,
            builder -> builder.fill(0, 0, 0, 20, 0, 20, ECBlocks.WHITE_ROCK_BRICKS.get())
                    .set(10, 1, 10, ECBlocks.CONTAINER.get().defaultBlockState())
                    .set(10, 2, 10, ECBlocks.VIBRATION_SYNTHESIZER.get().defaultBlockState()));

    @TestHolder(description = "Checks that the vibration air synthesizer generates air from wandering chickens.")
    @GameTest(template = TEMPLATE_NAME, timeoutTicks = 200, required = false)
    public static void should_generateAirFromSurroundingEntityMovement(ECGameTestHelper helper) {
        var storage = helper.requireElementContainer(new BlockPos(10, 1, 10));

        helper.startSequence()
                .thenExecuteAfter(1, () -> {
                    for (int i = 0; i < 5; i++) {
                        helper.spawn(EntityType.CHICKEN, new BlockPos(9, 2, 9));
                    }
                })
                .thenExecuteAfter(100, () -> {
                    assertThat(storage.getElementType())
                            .isEqualTo(ElementType.AIR);
                    assertThat(storage.getElementAmount())
                            .isGreaterThan(100);
                })
                .thenExecute(() -> helper.getEntities(EntityType.CHICKEN).forEach(Entity::discard))
                .thenSucceed();
    }

    @TestHolder(description = "Checks that vibration air synthesizer catch a vibration and changes state.")
    @GameTest(timeoutTicks =  200)
    public static void should_catchVibrationAndChangeState(DynamicTest test) {
        test.registerGameTestTemplate(() -> ECStructureTemplateBuilder.withSize(3, 2, 3)
                .fill(0, 0, 0, 2, 1, 2, Blocks.WHITE_WOOL)
                .set(1, 0, 1, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(1, 1, 1, ECBlocks.VIBRATION_SYNTHESIZER.get().defaultBlockState())
                .unpack());

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var storage = helper.requireElementContainer(new BlockPos(1, 0, 1));

            helper.startSequence()
                    .thenExecute(() -> {
                        helper.fireGameEvent(GameEvent.STEP, new Vec3(1, 0, 1));
                    })
                    .thenExecuteAfter(5, () -> {
                        helper.assertBlockState(new BlockPos(1, 1, 1), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.ACTIVE, _ -> Component.literal("Vibration synthesizer should be active after receiving vibration"));
                    })
                    .thenExecuteAfter(30, () -> {
                        helper.assertBlockState(new BlockPos(1, 1, 1), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.COOLDOWN, _ -> Component.literal("Vibration synthesizer should be in cooldown after 30 ticks"));
                    })
                    .thenExecuteAfter(70, () -> {
                        assertThat(storage.getElementType())
                                .isEqualTo(ElementType.AIR);
                        assertThat(storage.getElementAmount())
                                .isEqualTo(200);
                    })
                    .thenSucceed();
        });
    }

    @TestHolder(description = "Checks that vibration air synthesizer prevent multiple synthesizers from synthesizing air from the same vibration.")
    @GameTest(timeoutTicks =  200)
    public static void should_preventMultipleSynthesizersFromSynthesizing(DynamicTest test) {
        test.registerGameTestTemplate(() -> ECStructureTemplateBuilder.withSize(4, 2, 3)
                .fill(0, 0, 0, 3, 1, 2, Blocks.WHITE_WOOL)
                .set(1, 0, 1, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(2, 0, 1, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(1, 1, 1, ECBlocks.VIBRATION_SYNTHESIZER.get().defaultBlockState())
                .set(2, 1, 1, ECBlocks.VIBRATION_SYNTHESIZER.get().defaultBlockState())
                .unpack());

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var storage1 = helper.requireElementContainer(new BlockPos(1, 0, 1));
            var storage2 = helper.requireElementContainer(new BlockPos(2, 0, 1));

            helper.startSequence()
                    .thenExecute(() -> {
                        helper.fireGameEvent(GameEvent.STEP, new Vec3(1, 0, 1));
                    })
                    .thenExecuteAfter(5, () -> {
                        helper.assertBlockState(new BlockPos(1, 1, 1), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.ACTIVE, _ -> Component.literal("Vibration synthesizer should be active after receiving vibration"));
                        helper.assertBlockState(new BlockPos(2, 1, 1), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.ACTIVE, _ -> Component.literal("Vibration synthesizer should be active after receiving vibration"));
                    })
                    .thenExecuteAfter(30, () -> {
                        helper.assertBlockState(new BlockPos(1, 1, 1), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.COOLDOWN, _ -> Component.literal("Vibration synthesizer should be in cooldown after 30 ticks"));
                        helper.assertBlockState(new BlockPos(2, 1, 1), state -> state.getValue(VibrationSynthesizerBlock.PHASE) == SculkSensorPhase.COOLDOWN, _ -> Component.literal("Vibration synthesizer should be in cooldown after 30 ticks"));
                    })
                    .thenExecuteAfter(10, () -> {
                        helper.fireGameEvent(GameEvent.STEP, new Vec3(1, 0, 1));
                    })
                    .thenExecuteAfter(60, () -> {
                        assertThat(storage1.getElementType())
                                .isEqualTo(ElementType.AIR);
                        assertThat(storage1.getElementAmount())
                                .isEqualTo(200);
                        assertThat(storage2.getElementType())
                                .isEqualTo(ElementType.AIR);
                        assertThat(storage2.getElementAmount())
                                .isEqualTo(200);
                    })
                    .thenSucceed();
        });
    }
}
