package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class SourceBreederPedestalGameTests {

    private static final String TEMPLATE = "elementalcraft:sourcebreederpedestalgametests.should_changeelementtype";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_changeElementType(GameTestHelper helper) {
        var pedestal = helper.getBlockEntity(BlockPos.ZERO, SourceBreederPedestalBlockEntity.class);
        var itemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(pedestal, null));
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
