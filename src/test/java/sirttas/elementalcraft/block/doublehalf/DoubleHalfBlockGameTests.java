package sirttas.elementalcraft.block.doublehalf;

import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DoubleHalfBlockGameTests {

    public static List<Test> should_breakBothParts() {
        var index = new AtomicInteger(0);

        return DoubleHalfBlockTestCaseHolder.HOLDERS.stream()
                .map(t -> t.createTest(
                        "should_breakBothParts#" + index.getAndIncrement(),
                        "Check if both parts of the block are broken",
                        DoubleHalfBlockGameTests::should_breakBothParts))
                .toList();

    }

    public static void should_breakBothParts(ECGameTestHelper helper, DoubleHalfBlockTestCaseHolder holder) {
        var block = holder.block().get();

        helper.assertBlockPresent(block, holder.pos1());
        helper.assertBlockPresent(block, holder.pos2());

        helper.startSequence()
                .thenExecute(() -> helper.getLevel().destroyBlock(helper.absolutePos(holder.pos1()), true))
                .thenExecuteAfter(5, () -> {
                    helper.assertBlockNotPresent(block, holder.pos1());
                    helper.assertBlockNotPresent(block, holder.pos2());
                    helper.assertItemEntityCountIs(block.asItem(), holder.pos1(), 2, 1);
                })
                .thenExecute(() -> helper.discardItems(holder.pos1(), 2))
                .thenSucceed();
    }

}
