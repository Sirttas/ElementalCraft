package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class SourceBreederPedestalGameTests {

    private static final String TEMPLATE = "elementalcraft:sourcebreederpedestalgametests.should_changeelementtype";

    @TestHolder
    @GameTest(template = TEMPLATE)
    public static void should_changeElementType(ECGameTestHelper helper) {
        var pedestal = helper.getBlockEntity(BlockPos.ZERO, SourceBreederPedestalBlockEntity.class);
        var itemHandler = ECContainerHelper.getItemResourceHandler(pedestal, null);
        var elementStorage = (SourceBreederPedestalElementStorage) ElementStorageGameTestHelper.get(pedestal);

        helper.startSequence().thenExecute(transaction -> {
            itemHandler.insert(0, ItemResource.of(ReceptacleGameTestHelper.createSimpleReceptacle(ElementType.FIRE)), 1, transaction);
        }).thenExecuteAfter(1, () -> {
            elementStorage.insertElement(1000, ElementType.FIRE, false);

            assertThat(elementStorage.getElementType()).isEqualTo(ElementType.FIRE);
            assertThat(elementStorage.getElementAmount()).isEqualTo(1000);
        }).thenExecuteAfter(1, transaction -> {
            itemHandler.extract(0, itemHandler.getResource(0), 1, transaction);
            itemHandler.insert(0, ItemResource.of(ReceptacleGameTestHelper.createSimpleReceptacle(ElementType.WATER)), 1, transaction);
        }).thenExecuteAfter(2, () -> {
            assertThat(elementStorage.getElementType()).isEqualTo(ElementType.WATER);
            assertThat(elementStorage.getElementAmount()).isZero();
        }).thenSucceed();
    }
}
