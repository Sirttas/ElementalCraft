package sirttas.elementalcraft.block.retriever;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
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

    // elementalcraft:retrievergametests.should_extractfrombinder
    @GameTest
    public static void should_extractFromBinder(GameTestHelper helper) {
        var binder = InstrumentGameTestHelper.<BinderBlockEntity>getInstrument(helper, new BlockPos(0, 3, 0));
        var container = InstrumentGameTestHelper.getContainer(helper, new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
            var inv = binder.getInventory();

            inv.setItem(0, new ItemStack(Items.GOLD_INGOT));
            inv.setItem(1, new ItemStack(ECItems.DRENCHED_IRON_INGOT.get()));
            inv.setItem(2, new ItemStack(Items.COPPER_INGOT));
            inv.setItem(3, new ItemStack(Items.REDSTONE));
            inv.setItem(4, new ItemStack(ECItems.AIR_CRYSTAL.get()));
            container.fill(ElementType.AIR);
        }).thenExecuteAfter(2, () -> {
            helper.pullLever(1, 3, 1);
        }).thenExecuteAfter(1, () -> {
            helper.assertContainerContains(new BlockPos(0, 2, 1), ECItems.SWIFT_ALLOY_INGOT.get());
        }).thenSucceed();
    }

    // elementalcraft:retrievergametests.should_extractfrombinder
    @GameTest(template = "should_extractfrombinder")
    public static void should_extractOutputAndRemainingFromBinder(GameTestHelper helper) {
        var binder = InstrumentGameTestHelper.<BinderBlockEntity>getInstrument(helper, new BlockPos(0, 3, 0));
        var container = InstrumentGameTestHelper.getContainer(helper, new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
            var inv = binder.getInventory();

            inv.setItem(0, new ItemStack(ECItems.SHRINE_BASE.get()));
            inv.setItem(1, new ItemStack(ECItems.FIRE_CRYSTAL.get()));
            inv.setItem(2, new ItemStack(Items.LAVA_BUCKET));
            inv.setItem(3, new ItemStack(Items.GOLD_INGOT));
            container.fill(ElementType.FIRE);
        }).thenExecuteAfter(2, () -> {
            helper.pullLever(1, 3, 1);
        }).thenExecuteAfter(1, () -> {
            helper.assertContainerContains(new BlockPos(0, 2, 1), ECBlocks.FIRE_PYLON.get().asItem());
            helper.assertContainerContains(new BlockPos(0, 2, 1), Items.BUCKET);
        }).thenSucceed();
    }

}
