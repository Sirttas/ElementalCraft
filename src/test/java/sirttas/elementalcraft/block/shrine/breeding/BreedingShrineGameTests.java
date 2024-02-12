package sirttas.elementalcraft.block.shrine.breeding;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class BreedingShrineGameTests {

    // elementalcraft:breedingshrinegametests.should_breedcows
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_breedCows(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 2, 3));
        }).thenExecuteAfter(1, () -> {
            var entities = helper.getEntities(EntityType.COW, new BlockPos(3, 2, 3), 3);

            assertThat(entities).hasSize(2).allSatisfy(c -> {
                assertThat(c.isAlive()).withFailMessage("Cow is dead").isTrue();
                assertThat(c.isInLove()).withFailMessage("Cow is not in love").isTrue();
            });
        }).thenSucceed();
    }

}
