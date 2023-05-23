package sirttas.elementalcraft.block.shrine.vacuum;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.container.ECContainerHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class VacuumShrineGameTests {

    // elementalcraft:vacuumshrinegametests.should_pullandpickup
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_pullAndPickup(GameTestHelper helper) {
        var shrine = ShrineGameTestHelper.getShrine(helper, new BlockPos(2, 2, 2));
        var storage = shrine.getElementStorage();

        helper.startSequence().thenExecute(() -> helper.spawnItem(Items.COBBLESTONE, 0, 3, 0))
                .thenExecuteFor(10, storage::fill)
                .thenExecute(() -> {
                    var pos = helper.absolutePos(new BlockPos(2, 1, 2));
                    var itemHandler = ECContainerHelper.getItemHandlerAt(helper.getLevel(), pos);

                    helper.assertItemEntityCountIs(Items.COBBLESTONE, new BlockPos(0, 3, 0), 3, 0);
                    assertThat(itemHandler).isNotEmpty()
                            .contains(0, Items.COBBLESTONE);
                })
                .thenSucceed();
    }
}
