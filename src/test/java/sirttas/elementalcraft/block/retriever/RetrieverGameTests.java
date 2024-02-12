package sirttas.elementalcraft.block.retriever;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.item.ECItems;

@GameTestHolder(ElementalCraftApi.MODID)
public class RetrieverGameTests {

    // elementalcraft:retrievergametests.should_extractfrominfuser
    @GameTest
    public static void should_extractFromInfuser(GameTestHelper helper) {
        var infuser = InstrumentGameTestHelper.<InfuserBlockEntity>getInstrument(helper, new BlockPos(0, 3, 0));
        var container = InstrumentGameTestHelper.getContainer(helper, new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
                    infuser.getInventory().setItem(0, new ItemStack(Items.IRON_INGOT));
                    container.fill(ElementType.WATER);
                }).thenExecuteAfter(2, () -> {
                    helper.pullLever(1, 3, 1);
                }).thenExecuteAfter(1, () -> {
                    helper.assertContainerContains(new BlockPos(0, 2, 1), ECItems.DRENCHED_IRON_INGOT.get());
                }).thenSucceed();
    }

    // elementalcraft:retrievergametests.should_extractfrominfuser
    @GameTest(template = "should_extractfrominfuser")
    public static void should_extractFromInfuser_with_activeRetriever(GameTestHelper helper) {
        var infuser = InstrumentGameTestHelper.<InfuserBlockEntity>getInstrument(helper, new BlockPos(0, 3, 0));
        var container = InstrumentGameTestHelper.getContainer(helper, new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
            infuser.getInventory().setItem(0, new ItemStack(Items.IRON_INGOT));
            container.fill(ElementType.WATER);
            helper.pullLever(1, 3, 1);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(0, 2, 1), ECItems.DRENCHED_IRON_INGOT.get());
        }).thenSucceed();
    }
}
