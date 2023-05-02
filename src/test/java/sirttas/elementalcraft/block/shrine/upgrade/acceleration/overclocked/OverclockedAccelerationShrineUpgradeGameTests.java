package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class OverclockedAccelerationShrineUpgradeGameTests {

    // elementalcraft:overclockedaccelerationshrineupgradegametests.should_allowselementtransfer
    @GameTest
    public static void should_allowsElementTransfer(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.pullLever(0, 2, 2);
        }).thenExecuteAfter(1, () -> {
            var storage = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(1, 2, 0))).resolve().orElseThrow();

            assertThat(storage.getElementAmount(ElementType.WATER)).isEqualTo(100);
        }).thenSucceed();
    }
}
