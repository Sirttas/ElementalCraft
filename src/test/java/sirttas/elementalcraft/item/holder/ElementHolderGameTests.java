package sirttas.elementalcraft.item.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;


@GameTestHolder(ElementalCraftApi.MODID)
public class ElementHolderGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_fillHolder() {
        var index = new AtomicInteger(0);

        return ElementHolderTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("should_fillHolder#" + index.getAndIncrement(), ECGameTestHelper.EMPTY_CONTAINER_TEMPLATE, ElementHolderGameTests::should_fillHolder))
                .toList();
    }

    private static void should_fillHolder(GameTestHelper helper, ElementHolderTestHolder holder) {
        var pos = new BlockPos(0, 1, 0);
        var elementType = holder.type();
        var player = holder.mockPlayer(helper);
        var storage = ((ElementContainerBlockEntity) helper.getBlockEntity(pos)).getElementStorage();
        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorage.ENTITY);

        assertThat(storage).isNotNull();
        assertThat(playerStorage).isNotNull();
        helper.startSequence()
                .thenExecute(() -> storage.fill(elementType))
                .thenExecuteAfter(1, () -> {
                    player.setShiftKeyDown(true);
                    ECGameTestHelper.useItemOn(helper, player, pos);
                })
                .thenExecuteAfter(10, ECGameTestHelper.fixAssertions(() -> assertThat(playerStorage.getElementAmount(elementType)).isEqualTo(holder.getTransferAmount() * 11)))
                .thenExecute(player::discard)
                .thenSucceed();
    }

    @GameTestGenerator
    public static Collection<TestFunction> should_emptyHolder() {
        var index = new AtomicInteger(0);

        return ElementHolderTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("should_emptyHolder#" + index.getAndIncrement(), ECGameTestHelper.EMPTY_CONTAINER_TEMPLATE, ElementHolderGameTests::should_emptyHolder))
                .toList();
    }

    private static void should_emptyHolder(GameTestHelper helper, ElementHolderTestHolder holder) {
        var pos = new BlockPos(0, 1, 0);
        var elementType = holder.type();
        var player = holder.mockPlayer(helper);
        var storage = ((ElementContainerBlockEntity) helper.getBlockEntity(pos)).getElementStorage();
        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorage.ENTITY);

        assertThat(storage).isNotNull();
        assertThat(playerStorage).isNotNull();
        helper.startSequence()
                .thenExecute(() -> {
                    playerStorage.fill(elementType);
                    storage.insertElement(100, elementType, false);
                })
                .thenExecuteAfter(1, () -> helper.useBlock(pos, player))
                .thenExecuteAfter(10, ECGameTestHelper.fixAssertions(() -> assertThat(storage.getElementAmount(elementType)).isEqualTo(100 + (holder.getTransferAmount() * 11))))
                .thenExecute(player::discard)
                .thenSucceed();
    }
}
