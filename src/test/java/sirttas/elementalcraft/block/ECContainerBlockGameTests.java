package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class ECContainerBlockGameTests {

    public static Collection<Test> collectTests() {
        var index = new AtomicInteger(0);

        return ECContainerBlockTestCaseHolder.HOLDERS.stream()
                .<Test>mapMulti((holder, consumer) -> {
                    var i = index.getAndIncrement();

                    consumer.accept(holder.createTest(
                            "should_moveItemsToSlot_" + i,
                            "Check that items are moved from player inventory to slot on right click.",
                            ECContainerBlockGameTests::should_moveItemsToSlot));
                    consumer.accept(holder.createTest(
                            "should_moveItemsFromSlot_" + i,
                            "Check that items are moved from slot to player inventory on right click.",
                            ECContainerBlockGameTests::should_moveItemsFromSlot));
                })
                .toList();
    }

    private static void should_moveItemsToSlot(ECGameTestHelper helper, ECContainerBlockTestCaseHolder holder) {
        var item = holder.item().get();
        var pos = holder.pos();
        var slot = holder.slot();
        var player = helper.mockPlayerWithItem(Vec3.ZERO, new ItemStack(item, 64));
        var container = helper.getCapability(Capabilities.Item.BLOCK, pos, Direction.UP);

        assertThat(container).isNotNull();

        var size = Math.min(container.getCapacityAsInt(slot, ItemResource.of(item)), 64);

        helper.startSequence()
                .thenExecuteAfter(1, () -> helper.useItemOn(player, pos))
                .thenExecuteAfter(1, () -> {
                    assertThat(container.getResource(slot).toStack(container.getAmountAsInt(slot)))
                            .as("Items should have been moved to container")
                            .is(item)
                            .hasCount(size);
                    if (size == 64) {
                        assertThat(player.getMainHandItem())
                                .as("Items should have been moved from player")
                                .isEmpty();
                    } else {
                        assertThat(player.getMainHandItem())
                                .as("Items should have been moved from player")
                                .is(item)
                                .hasCount(64 - size);
                    }
                })
                .thenExecute(player::discard)
                .thenSucceed();
    }

    private static void should_moveItemsFromSlot(ECGameTestHelper helper, ECContainerBlockTestCaseHolder holder) {
        var item = holder.item().get();
        var pos = holder.pos();
        var slot = holder.slot();
        var player = helper.mockPlayerWithItem(Vec3.ZERO, ItemStack.EMPTY);
        var container = helper.getCapability(Capabilities.Item.BLOCK, pos, Direction.UP);

        assertThat(container).isNotNull();

        var size = Math.min(container.getCapacityAsInt(slot, ItemResource.of(item)), 64);

        try (Transaction rootTransaction = Transaction.openRoot()) {
            container.insert(slot, ItemResource.of(item), size, rootTransaction);
            rootTransaction.commit();
        }
        helper.startSequence()
                .thenExecuteAfter(1, () -> helper.useItemOn(player, pos))
                .thenExecuteAfter(1, () -> {
                    assertThat(container.getResource(slot).toStack())
                            .as("Items should have been moved from container")
                            .isEmpty();
                    assertThat(helper.getEntities(EntityType.ITEM, BlockPos.ZERO, 1)).hasSize(1)
                            .allSatisfy(i -> assertThat(i.getItem())
                                    .as("Items should have been moved to player")
                                    .is(item)
                                    .hasCount(size));
                })
                .thenExecute(player::discard)
                .thenSucceed();
    }
}
