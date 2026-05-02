package sirttas.elementalcraft.block.shrine.spawning;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class SpawningShrineGameTests {

    private static final String TEMPLATE = "elementalcraft:spawningshrinegametests.should_spawnmobs";

    @TestHolder
    @GameTest(template = TEMPLATE, required = false)
    public static void should_spawnMobs(GameTestHelper helper) {
        helper.startSequence()
                .thenExecuteAfter(1, () -> ShrineGameTestHelper.forcePeriods(helper, new BlockPos(5, 1, 5), 20))
                .thenExecuteAfter(1, () -> helper.assertEntityPresent(EntityType.ZOMBIE))
                .thenSucceed();
    }
}
