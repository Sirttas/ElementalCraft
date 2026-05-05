package sirttas.elementalcraft.block.synthesizer.combustion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = CombustionSynthesizerGameTests.GROUP)
public class CombustionSynthesizerGameTests {

    public static final String GROUP = "synthesizer.combustion";

    @TestHolder(description = "Checks that the combustion synthesizer generates fire from coal.")
    @GameTest
    public static void should_generateFireFromCoal(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(1, 2, 1)
                .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(0, 1, 0, ECBlocks.COMBUSTION_SYNTHESIZER.get().defaultBlockState()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var ticks = new AtomicInteger(0);
            var inv = helper.getBlockEntity(new BlockPos(0, 1, 0), CombustionSynthesizerBlockEntity.class).getInventory();
            var storage = helper.requireElementContainer(BlockPos.ZERO);

            helper.startSequence().thenExecute(() -> {
                inv.setItem(0, new ItemStack(Items.COAL));
            }).thenIdle(1).thenExecuteFor(20, () -> {
                var t = ticks.incrementAndGet();

                assertThat(inv.getItem(0)).isEmpty();
                assertThat(storage.getElementType())
                        .isEqualTo(ElementType.FIRE);
                assertThat(storage.getElementAmount())
                        .isEqualTo(t * 5);
            }).thenSucceed();
        });
    }

}
