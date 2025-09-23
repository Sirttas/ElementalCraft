package sirttas.elementalcraft.block.instrument.io.firefurnace.blast;

import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceTestHolder;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class FireBlastFurnaceGameTests {

    public static final String GROUP = "level.blocks.instruments.fireblastfurnace";

    public static Collection<Test> collectTests() {
        var index = new AtomicInteger(0);

        return FireFurnaceTestHolder.HOLDERS.stream()
                .map(t -> t.blast() ?
                        t.createTest(
                            GROUP,
                            "should_smelt#" + index.getAndIncrement(),
                            "Check that the fire blast furnace can smelt the input item.",
                            InstrumentTestTemplates.FIRE_BLAST_FURNACE_TEMPLATE_NAME,
                            FireBlastFurnaceGameTests::should_melt) :
                        t.createTest(
                            GROUP,
                            "shouldNot_melt#" + index.getAndIncrement(),
                            "Check that the fire blast furnace cannot smelt the input item.",
                            InstrumentTestTemplates.FIRE_BLAST_FURNACE_TEMPLATE_NAME,
                            FireBlastFurnaceGameTests::shouldNot_melt))
                .toList();
    }

    private static void should_melt(ECGameTestHelper helper, FireFurnaceTestHolder holder) {
        helper.<AbstractFireFurnaceBlockEntity<?>>runInstrument(holder.input().get(), ElementType.FIRE, furnace -> {
            assertThat(furnace.getInventory().getItem(0)).isEmpty();
            assertThat(furnace.getInventory().getItem(1))
                    .is(holder.output())
                    .hasCount(1);
        });
    }

    private static void shouldNot_melt(ECGameTestHelper helper, FireFurnaceTestHolder holder) {
        helper.<AbstractFireFurnaceBlockEntity<?>>runInstrument(holder.input().get(), ElementType.FIRE, false, furnace -> {
            assertThat(furnace.getInventory().getItem(0))
                    .is(holder.input().get().getItem())
                    .hasCount(1);
            assertThat(furnace.getInventory().getItem(1)).isEmpty();
        });
    }
}
