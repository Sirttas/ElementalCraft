package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.world.item.ItemStack;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class MillGameTests {

    public static Collection<Test> collectTests() {
        var index = new AtomicInteger(0);

        return MillTestCaseHolder.HOLDERS.stream()
                .<Test>mapMulti((holder, consumer) -> {
                    var i = index.getAndIncrement();

                    consumer.accept(holder.createTest(
                            "should_mill_" + i,
                            "Check if the mill can mill the input item.",
                            MillGameTests::should_mill));
                    consumer.accept(holder.createTest(
                            "shouldNot_mill_with_wrongElement_" + i,
                            "Check if the mill can't mill the input item with the wrong element.",
                            MillGameTests::shouldNot_mill_with_wrongElement));
                })
                .toList();
    }

    private static void should_mill(ECGameTestHelper helper, MillTestCaseHolder holder) {
        helper.<AbstractMillBlockEntity<?>>runInstrument(new ItemStack(holder.input()), holder.type(), mill -> {
            assertThat(mill.getInventory().getItem(0)).isEmpty();
            assertThat(mill.getInventory().getItem(1))
                    .is(holder.output())
                    .hasCount(1);
        });
    }

    private static void shouldNot_mill_with_wrongElement(ECGameTestHelper helper, MillTestCaseHolder holder) {
        var type = holder.type() == ElementType.AIR ? ElementType.WATER : ElementType.AIR;

        helper.<AbstractMillBlockEntity<?>>runInstrument(new ItemStack(holder.input()), type, false, mill -> {
            assertThat(mill.getInventory().getItem(0))
                    .is(holder.input())
                    .hasCount(1);
            assertThat(mill.getInventory().getItem(1)).isEmpty();
        });
    }
}
