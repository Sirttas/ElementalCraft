package sirttas.elementalcraft.block.instrument.io.firefurnace;

import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class FireFurnaceGameTests {

    public static final String GROUP = "level.blocks.instruments.firefurnace";

    public static Collection<Test> should_smelt() {
        var index = new AtomicInteger(0);

        return FireFurnaceTestHolder.HOLDERS.stream()
                .map(t -> t.createTest(
                        GROUP,
                        "should_smelt#" + index.getAndIncrement(),
                        "Check if the fire furnace can smelt the input item.",
                        "firefurnacegametests.fire_furnace",
                        FireFurnaceGameTests::should_smelt))
                .toList();
    }

    private static void should_smelt(ECGameTestHelper helper, FireFurnaceTestHolder holder) {
        helper.<AbstractFireFurnaceBlockEntity<?>>runInstrument(holder.input().get(), ElementType.FIRE, mill -> {
            assertThat(mill.getInventory().getItem(0)).isEmpty();
            assertThat(mill.getInventory().getItem(1))
                    .is(holder.output())
                    .hasCount(1);
        });
    }
}
