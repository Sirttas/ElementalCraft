package sirttas.elementalcraft.block.shrine.spawning;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

@GameTestHolder(ElementalCraftApi.MODID)
public class SpawningShrineGameTests {

    // elementalcraft:spawningshrinegametests.should_spawnmobs
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME, required = false)
    public static void should_spawnMobs(GameTestHelper helper) {
        helper.startSequence()
                .thenExecute(() -> ShrineGameTestHelper.forcePeriods(helper, new BlockPos(5, 2, 5), 20))
                .thenExecuteAfter(1, () -> helper.assertEntityPresent(EntityType.ZOMBIE))
                .thenSucceed();
    }
}
