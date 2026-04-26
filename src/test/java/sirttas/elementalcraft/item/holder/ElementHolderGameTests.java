package sirttas.elementalcraft.item.holder;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.block.container.ContainerGameTests;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.SourceGameTestTemplates;
import sirttas.elementalcraft.item.ECItems;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;


public class ElementHolderGameTests {

    public static List<Test> collectTests() {
        var index = new AtomicInteger(0);

        return ElementHolderTestCaseHolder.HOLDERS.stream()
                .<Test>mapMulti((t, downstream) -> {
                    var i = index.getAndIncrement();

                    downstream.accept(t.createTest(
                            "ElementHolderGameTests.should_fillHolder_" + i,
                            "Check if an holder can be filled from a container.",
                            ContainerGameTests.EMPTY_CONTAINER_TEMPLATE_NAME,
                            ElementHolderGameTests::should_fillHolder));
                    downstream.accept(t.createTest(
                            "ElementHolderGameTests.should_emptyHolder_" + i,
                            "Check if an holder can be emptied into a container.",
                            ContainerGameTests.EMPTY_CONTAINER_TEMPLATE_NAME,
                            ElementHolderGameTests::should_emptyHolder));
                    downstream.accept(t.createTest(
                            "ElementHolderGameTests.should_exhaustSource_" + i,
                            "Check if an holder can be filled from a source and exhaust it.",
                            SourceGameTestTemplates.getSourceTemplate(t.type()),
                            ElementHolderGameTests::should_exhaustSource));
                    downstream.accept(t.createTest(
                            "ElementHolderGameTests.should_dropStabilizer_when_sourceGetExhausted_" + i,
                            "Check if an holder can be filled from a source, exhaust it and drop a stabilizer when it is stabilized.",
                            SourceGameTestTemplates.getSourceWithStabilizerTemplate(t.type()),
                            ElementHolderGameTests::should_dropStabilizer_when_sourceGetExhausted));
                })
                .toList();
    }

    private static void should_fillHolder(ECGameTestHelper helper, ElementHolderTestCaseHolder holder) {
        var elementType = holder.type();
        var player = holder.mockPlayer(helper);
        var storage = helper.requireElementContainer(BlockPos.ZERO);
        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

        assertThat(storage).isNotNull();
        assertThat(playerStorage).isNotNull();
        helper.startSequence()
                .thenExecute(() -> storage.fill(elementType))
                .thenExecuteAfter(1, () -> {
                    player.setShiftKeyDown(true);
                    helper.useItemOn(player, BlockPos.ZERO);
                })
                .thenExecuteAfter(10, () -> assertThat(playerStorage.getElementAmount(elementType)).isEqualTo(holder.getTransferAmount() * 11))
                .thenExecute(player::discard)
                .thenSucceed();
    }

    private static void should_emptyHolder(ECGameTestHelper helper, ElementHolderTestCaseHolder holder) {
        var elementType = holder.type();
        var player = holder.mockPlayer(helper);
        var storage = helper.requireElementContainer(BlockPos.ZERO);
        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

        assertThat(storage).isNotNull();
        assertThat(playerStorage).isNotNull();
        helper.startSequence()
                .thenExecute(() -> {
                    playerStorage.fill(elementType);
                    storage.insertElement(100, elementType, false);
                })
                .thenExecuteAfter(1, () -> helper.useBlock(BlockPos.ZERO, player))
                .thenExecuteAfter(10, () -> assertThat(storage.getElementAmount(elementType)).isEqualTo(100 + (holder.getTransferAmount() * 11)))
                .thenExecute(player::discard)
                .thenSucceed();
    }

    private static void should_exhaustSource(ECGameTestHelper helper, ElementHolderTestCaseHolder holder) {
        var player = holder.mockPlayer(helper);
        var sourceStorage = (SourceElementStorage) helper.getBlockEntity(BlockPos.ZERO, SourceBlockEntity.class).getElementStorage();
        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

        assertThat(sourceStorage).isNotNull();
        assertThat(playerStorage).isNotNull();
        helper.startSequence()
                .thenExecute(() -> sourceStorage.setElementAmount(10))
                .thenExecuteAfter(1, () -> helper.useBlock(BlockPos.ZERO, player))
                .thenExecuteAfter(10, () -> helper.assertBlockNotPresent(SourceBlock.findSourceBlock(holder.type()), BlockPos.ZERO))
                .thenExecute(player::discard)
                .thenSucceed();
    }

    private static void should_dropStabilizer_when_sourceGetExhausted(ECGameTestHelper helper, ElementHolderTestCaseHolder holder) {
        var player = holder.mockPlayer(helper);
        var sourceStorage = (SourceElementStorage) helper.getBlockEntity(BlockPos.ZERO, SourceBlockEntity.class).getElementStorage();
        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

        assertThat(sourceStorage).isNotNull();
        assertThat(playerStorage).isNotNull();
        helper.startSequence()
                .thenExecute(() -> sourceStorage.setElementAmount(10))
                .thenExecuteAfter(1, () -> helper.useBlock(BlockPos.ZERO, player))
                .thenExecuteAfter(10, () -> {
                    helper.assertBlockNotPresent(SourceBlock.findSourceBlock(holder.type()), BlockPos.ZERO);
                    assertThat(IItemHandler.of(player.getCapability(Capabilities.Item.ENTITY))).contains(ECItems.SOURCE_STABILIZER);
                    assertThat(playerStorage.getElementAmount(holder.type())).isGreaterThanOrEqualTo(10);
                })
                .thenExecute(player::discard)
                .thenSucceed();
    }
}
