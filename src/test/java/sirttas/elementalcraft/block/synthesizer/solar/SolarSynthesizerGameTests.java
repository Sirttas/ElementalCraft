package sirttas.elementalcraft.block.synthesizer.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.template.ECStructureTemplateBuilder;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = SolarSynthesizerGameTests.GROUP)
public class SolarSynthesizerGameTests {

    public static final String GROUP = "synthesizer.solar";

    @TestHolder(description = "Checks that the solar synthesizer generates fire from the lens.")
    @GameTest
    public static void should_generateFireFromLens(DynamicTest test) {
        test.registerGameTestTemplate(() -> ECStructureTemplateBuilder.withSize(1, 2, 1)
                .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(0, 1, 0, ECBlocks.SOLAR_SYNTHESIZER.get().defaultBlockState())
                .unpack());

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var ticks = new AtomicInteger(0);
            var inv = helper.getBlockEntity(new BlockPos(0, 1, 0), SolarSynthesizerBlockEntity.class).getInventory();
            var storage = helper.requireElementContainer(BlockPos.ZERO);

            helper.startSequence().thenExecute(() -> {
                inv.setItem(0, new ItemStack(ECItems.FIRE_LENS));
            }).thenIdle(1).thenExecuteFor(20, () -> {
                var t = ticks.incrementAndGet();

                assertThat(inv.getItem(0))
                        .is(ECItems.FIRE_LENS)
                        .hasDamageSatisfying(damage -> assertThat(damage)
                                .isBetween((int) Math.floor(t / 2F), (int) Math.ceil(t / 2F))); // 2 ticks per damage, the solar synthesizer generate by ticks of 50 but only transfer 25
                assertThat(storage.getElementType())
                        .isEqualTo(ElementType.FIRE);
                assertThat(storage.getElementAmount())
                        .isEqualTo(t * 25);
            }).thenSucceed();
        });
    }

}
