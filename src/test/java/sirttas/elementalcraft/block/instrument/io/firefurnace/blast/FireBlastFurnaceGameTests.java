package sirttas.elementalcraft.block.instrument.io.firefurnace.blast;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceTestHolder;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class FireBlastFurnaceGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_smelt() {
        var index = new AtomicInteger(0);

        return FireFurnaceTestHolder.HOLDERS.stream()
                .filter(FireFurnaceTestHolder::blast)
                .map(t -> t.createTestFunction("should_smelt#" + index.getAndIncrement(), "fireblastfurnacegametests.fire_blast_furnace", FireBlastFurnaceGameTests::should_smelt))
                .toList();
    }

    public static void should_smelt(GameTestHelper helper, FireFurnaceTestHolder holder) {
        InstrumentGameTestHelper.<AbstractFireFurnaceBlockEntity<?>>runInstrument(helper, holder.input().get(), ElementType.FIRE, furnace -> {
            assertThat(furnace.getInventory().getItem(0)).isEmpty();
            assertThat(furnace.getInventory().getItem(1))
                    .is(holder.output())
                    .hasCount(1);
        });
    }
}
