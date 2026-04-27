package sirttas.elementalcraft.block.shrine.vacuum;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.container.ECContainerHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class VacuumShrineGameTests {

    private static final String TEMPLATE = "elementalcraft:vacuumshrinegametests.should_pullandpickup";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_pullAndPickup(GameTestHelper helper) {
        var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(2, 1, 2));
        var storage = shrine.getElementStorage();

        helper.startSequence().thenExecute(() -> helper.spawnItem(Items.COBBLESTONE, 0, 2, 0))
                .thenExecuteFor(10, storage::fill)
                .thenExecute(() -> {
                    var pos = helper.absolutePos(new BlockPos(2, 0, 2));
                    var itemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandlerAt(helper.getLevel(), pos));

                    helper.assertItemEntityCountIs(Items.COBBLESTONE, new BlockPos(0, 2, 0), 3, 0);
                    assertThat(itemHandler).isNotEmpty()
                            .contains(0, Items.COBBLESTONE);
                })
                .thenSucceed();
    }
}
