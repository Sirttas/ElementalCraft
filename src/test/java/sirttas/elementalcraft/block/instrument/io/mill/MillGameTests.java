package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class MillGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_mill() {
        var index = new AtomicInteger(0);

        return MillTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("should_mill#" + index.getAndIncrement(), MillGameTests::should_mill))
                .toList();
    }

    public static void should_mill(GameTestHelper helper, MillTestHolder holder) {
        InstrumentGameTestHelper.<AbstractMillBlockEntity<?, ?>>runInstrument(helper, new ItemStack(holder.input()), holder.type(), mill -> {
            assertThat(mill.getInventory().getItem(0)).isEmpty();
            assertThat(mill.getInventory().getItem(1))
                    .is(holder.output())
                    .hasCount(1);
        });
    }

    @GameTestGenerator
    public static Collection<TestFunction> shouldNot_mill_with_wrongElement() {
        var index = new AtomicInteger(0);

        return MillTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("shouldNot_mill_with_wrongElement#" + index.getAndIncrement(), MillGameTests::shouldNot_mill_with_wrongElement))
                .toList();
    }

    public static void shouldNot_mill_with_wrongElement(GameTestHelper helper, MillTestHolder holder) {
        var type = holder.type() == ElementType.AIR ? ElementType.WATER : ElementType.AIR;

        InstrumentGameTestHelper.<AbstractMillBlockEntity<?, ?>>runInstrument(helper, new ItemStack(holder.input()), type, false, mill -> {
            assertThat(mill.getInventory().getItem(0))
                    .is(holder.input())
                    .hasCount(1);
            assertThat(mill.getInventory().getItem(1)).isEmpty();
        });
    }
}
