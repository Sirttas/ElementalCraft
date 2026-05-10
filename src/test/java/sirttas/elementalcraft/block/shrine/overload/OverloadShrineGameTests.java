package sirttas.elementalcraft.block.shrine.overload;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class OverloadShrineGameTests {

    private static final String TEMPLATE = "elementalcraft:overloadshrinegametests.should_speedupfurnace";

    @TestHolder
    @GameTest(template = TEMPLATE, timeoutTicks = 200)
    public static void should_speedupFurnace(ECGameTestHelper helper) {
        var furnace = helper.getCapability(Capabilities.Item.BLOCK, new BlockPos(0, 1, 0), null);
        var shrine = ShrineGameTestHelper.getShrine(helper, BlockPos.ZERO).getElementStorage();

        helper.startSequence()
                .thenExecute(transaction -> {
                    furnace.insert(0, ItemResource.of(Items.RAW_IRON), 1, transaction);
                    furnace.insert(1, ItemResource.of(Items.COAL), 1, transaction);
                })
                .thenExecuteFor(152, () -> shrine.fill())
                .thenExecute(() -> assertThat(furnace)
                        .isEmpty(0)
                        .isEmpty(1)
                        .contains(2, ItemResource.of(Items.IRON_INGOT)))
                .thenSucceed();
    }
}
