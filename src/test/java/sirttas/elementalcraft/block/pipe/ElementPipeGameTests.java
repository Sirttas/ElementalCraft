package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlockEntity;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ElementPipeGameTests {

    public static final String BATCH_NAME = "pipe";


    // elementalcraft:elementpipegametests.shouldnot_transferabovemax
    @GameTest(batch = BATCH_NAME)
    public static void shouldNot_transferAboveMax(GameTestHelper helper) {
        var targetStorage = ((ElementContainerBlockEntity) helper.getBlockEntity(new BlockPos(1, 2, 0))).getElementStorage();

        helper.startSequence().thenExecute(() -> helper.pullLever(0, 2, 1))
                .thenExecuteAfter(1, ECGameTestHelper.fixAssertions(() -> assertThat(targetStorage.getElementAmount()).isEqualTo(100)))
                .thenExecuteAfter(1, ECGameTestHelper.fixAssertions(() -> assertThat(targetStorage.getElementAmount()).isEqualTo(200)))
                .thenExecuteAfter(1, ECGameTestHelper.fixAssertions(() -> assertThat(targetStorage.getElementAmount()).isEqualTo(300)))
                .thenSucceed();
    }

}
