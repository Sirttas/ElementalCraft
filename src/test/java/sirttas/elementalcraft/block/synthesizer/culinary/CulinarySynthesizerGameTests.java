package sirttas.elementalcraft.block.synthesizer.culinary;

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

@ForEachTest(groups = CulinarySynthesizerGameTests.GROUP)
public class CulinarySynthesizerGameTests {

    public static final String GROUP = "synthesizer.culinary";

    @TestHolder(description = "Checks that the culinary synthesizer generates water from cooked beef.")
    @GameTest
    public static void should_generateWaterFromBeef(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(1, 2, 1)
                .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(0, 1, 0, ECBlocks.CULINARY_SYNTHESIZER.get().defaultBlockState()));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var ticks = new AtomicInteger(0);
            var inv = helper.getBlockEntity(new BlockPos(0, 1, 0), CulinarySynthesizerBlockEntity.class).getInventory();
            var storage = helper.requireElementContainer(BlockPos.ZERO);

            helper.startSequence().thenExecute(() -> {
                inv.setItem(0, new ItemStack(Items.COOKED_BEEF));
            }).thenIdle(1).thenExecuteFor(20, () -> {
                var t = ticks.incrementAndGet();

                assertThat(inv.getItem(0)).isEmpty();
                assertThat(storage.getElementType())
                        .isEqualTo(ElementType.WATER);
                assertThat(storage.getElementAmount())
                        .isEqualTo(t * 25);
            }).thenSucceed();
        });
    }

}
