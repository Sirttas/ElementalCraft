package sirttas.elementalcraft.block.diffuser;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;
import sirttas.elementalcraft.item.holder.ElementHolderTestHolder;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class DiffuserGameTests {

    // elementalcraft:diffusergametests.diffuser
    @GameTestGenerator
    public static Collection<TestFunction> should_fillHolder() {
        var index = new AtomicInteger(0);

        return ElementHolderTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("diffusergametests.should_fillHolder#" + index.getAndIncrement(), "elementalcraft:diffusergametests.diffuser", DiffuserGameTests::should_fillHolder))
                .toList();
    }

    private static void should_fillHolder(GameTestHelper helper, ElementHolderTestHolder holder) {
        var elementType = holder.type();
        var player = holder.mockPlayer(helper);
        var storage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(0, 1, 0))).getElementStorage();
        var playerStorage = ElementStorageHelper.get(player).resolve().orElseThrow();

        helper.startSequence()
                .thenExecute(() -> storage.fill(elementType))
                .thenExecuteAfter(10, () -> assertThat(playerStorage.getElementAmount(elementType)).isEqualTo(50))
                .thenExecute(player::discard)
                .thenSucceed();
    }

}
