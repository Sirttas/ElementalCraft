package sirttas.elementalcraft.block.shrine.overload;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.container.ContainerGameTestHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class OverloadShrineGameTests {

    private static final String TEMPLATE = "elementalcraft:overloadshrinegametests.should_speedupfurnace";

    @TestHolder
    @GameTest(template = TEMPLATE, timeoutTicks = 200)
    public static void should_speedupFurnace(GameTestHelper helper) {
        var furnace = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(0, 1, 0));
        var shrine = ShrineGameTestHelper.getShrine(helper, BlockPos.ZERO).getElementStorage();

        helper.startSequence()
                .thenExecute(() -> {
                    furnace.insertItem(0, new ItemStack(Items.RAW_IRON), false);
                    furnace.insertItem(1, new ItemStack(Items.COAL), false);
                })
                .thenExecuteFor(152, shrine::fill)
                .thenExecute(() -> assertThat(furnace)
                        .isEmpty(0)
                        .isEmpty(1)
                        .contains(2, Items.IRON_INGOT))
                .thenSucceed();
    }
}
