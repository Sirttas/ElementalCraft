package sirttas.elementalcraft.block.doublehalf;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@GameTestHolder(ElementalCraftApi.MODID)
public class DoubleHalfBlockGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_breakBothParts() {
        var index = new AtomicInteger(0);

        return DoubleHalfBlockTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("should_breakBothParts#" + index.getAndIncrement(), DoubleHalfBlockGameTests::should_breakBothParts))
                .toList();

    }

    public static void should_breakBothParts(GameTestHelper helper, DoubleHalfBlockTestHolder holder) {
        helper.assertBlockPresent(holder.block(), holder.pos1());
        helper.assertBlockPresent(holder.block(), holder.pos2());

        helper.startSequence()
                .thenExecute(() -> helper.getLevel().destroyBlock(helper.absolutePos(holder.pos1()), true))
                .thenExecuteAfter(5, () -> {
                    helper.assertBlockNotPresent(holder.block(), holder.pos1());
                    helper.assertBlockNotPresent(holder.block(), holder.pos2());
                    helper.assertItemEntityCountIs(holder.block().asItem(), holder.pos1(), 2, 1);
                })
                .thenExecute(() -> ECGameTestHelper.discardItems(helper, holder.pos1(), 2))
                .thenSucceed();
    }

}
