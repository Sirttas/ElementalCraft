package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class SourceBreederPedestalGameTests {

    // elementalcraft:sourcebreederpedestalgametests.should_changeelementtype
    @GameTest
    public static void should_changeElementType(GameTestHelper helper) {
        var pedestal = (SourceBreederPedestalBlockEntity) helper.getBlockEntity(new BlockPos(0, 1, 0));
        var itemHandler = ECContainerHelper.getItemHandler(pedestal, null);
        var elementStorage = (SourceBreederPedestalElementStorage) ElementStorageGameTestHelper.get(pedestal);

        helper.startSequence().thenExecute(() -> {
            itemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(ElementType.FIRE), false);
        }).thenExecuteAfter(1, () -> {
            elementStorage.insertElement(1000, ElementType.FIRE, false);

            assertThat(elementStorage.getElementType()).isEqualTo(ElementType.FIRE);
            assertThat(elementStorage.getElementAmount()).isEqualTo(1000);
        }).thenExecuteAfter(1, () -> {
            itemHandler.extractItem(0, 1, false);
            itemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(ElementType.WATER), false);
        }).thenExecuteAfter(2, () -> {
            assertThat(elementStorage.getElementType()).isEqualTo(ElementType.WATER);
            assertThat(elementStorage.getElementAmount()).isZero();
        }).thenSucceed();
    }
}
