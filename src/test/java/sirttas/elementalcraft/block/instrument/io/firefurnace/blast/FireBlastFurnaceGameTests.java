package sirttas.elementalcraft.block.instrument.io.firefurnace.blast;

import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceTestHolder;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class FireBlastFurnaceGameTests {

    public static final String GROUP = "level.blocks.instruments.fireblastfurnace";

    public static Collection<Test> should_smelt() {
        var index = new AtomicInteger(0);

        return FireFurnaceTestHolder.HOLDERS.stream()
                .filter(FireFurnaceTestHolder::blast)
                .map(t -> t.createTest(
                        GROUP,
                        "should_smelt#" + index.getAndIncrement(),
                        "Check if the fire blast furnace can smelt the input item.",
                        "fireblastfurnacegametests.fire_blast_furnace",
                        FireBlastFurnaceGameTests::should_smelt))
                .toList();
    }

    private static void should_smelt(ECGameTestHelper helper, FireFurnaceTestHolder holder) {
        helper.<AbstractFireFurnaceBlockEntity<?>>runInstrument(holder.input().get(), ElementType.FIRE, furnace -> {
            assertThat(furnace.getInventory().getItem(0)).isEmpty();
            assertThat(furnace.getInventory().getItem(1))
                    .is(holder.output())
                    .hasCount(1);
        });
    }
}
