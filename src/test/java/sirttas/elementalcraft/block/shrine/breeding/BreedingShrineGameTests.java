package sirttas.elementalcraft.block.shrine.breeding;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;


@ForEachTest(groups = ShrineGameTestHelper.GROUP)
public class BreedingShrineGameTests {

    private static final String TEMPLATE = "elementalcraft:breedingshrinegametests.should_breedcows";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_breedCows(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            ShrineGameTestHelper.forcePeriod(helper, new BlockPos(0, 2, 3));
        }).thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
            var entities = helper.getEntities(EntityType.COW, new BlockPos(3, 2, 3), 3);

            assertThat(entities).hasSize(2).allSatisfy(c -> {
                assertThat(c.isAlive())
                        .withFailMessage("Cow is dead")
                        .isTrue();
                assertThat(c.isInLove())
                        .withFailMessage("Cow is not in love")
                        .isTrue();
            });
        })).thenSucceed();
    }

}
