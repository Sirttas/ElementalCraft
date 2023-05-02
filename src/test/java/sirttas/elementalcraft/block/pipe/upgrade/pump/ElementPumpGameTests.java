package sirttas.elementalcraft.block.pipe.upgrade.pump;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
@GameTestHolder(ElementalCraftApi.MODID)
public class ElementPumpGameTests {

    // elementalcraft:elementpumpgametests.should_transfer500elements
    @GameTest
    public static void should_transfer500Elements(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.pullLever(1, 2, 1);
        }).thenExecuteAfter(1, () -> {
            var sourceStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0))).getElementStorage();
            var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 2))).getElementStorage();

            assertThat(targetStorage.getElementAmount()).isEqualTo(500);
            assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - 500);
        }).thenSucceed();
    }

    // elementalcraft:elementpumpgametests.should_transfer1250elements
    @GameTest
    public static void should_transfer1250Elements(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.pullLever(1, 2, 1);
        }).thenExecuteAfter(1, () -> {
            var sourceStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0))).getElementStorage();
            var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 2))).getElementStorage();

            assertThat(targetStorage.getElementAmount()).isEqualTo(1250);
            assertThat(sourceStorage.getElementAmount()).isLessThan(100000 - 1250);
        }).thenSucceed();
    }
}
