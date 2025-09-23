package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class OverclockedAccelerationShrineUpgradeGameTests {

    public static final String TEMPLATE = "elementalcraft:overclockedaccelerationshrineupgradegametests.should_allowselementtransfer";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_allowsElementTransfer(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);

        helper.startSequence().thenExecute(() -> {
            helper.pullLever(0, 2, 2);
        }).thenIdle(1).thenExecuteFor(10, ECGameTestUtils.fixAssertions(() -> {
            var storage = ElementStorageGameTestHelper.get(helper.getBlockEntity(new BlockPos(1, 2, 0)));

            assertThat(storage.getElementAmount(ElementType.WATER)).isEqualTo(500 * ticks.incrementAndGet());
        })).thenSucceed();
    }
}
