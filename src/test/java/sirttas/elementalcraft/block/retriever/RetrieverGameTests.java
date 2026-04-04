package sirttas.elementalcraft.block.retriever;

import net.minecraft.core.BlockPos;
import net.neoforged.testframework.gametest.GameTest;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.item.ECItems;

@ForEachTest(groups = RetrieverGameTests.GROUP)
public class RetrieverGameTests {

    public static final String GROUP = "level.blocks.retriever";

    public static final String INFUSER_TEMPLATE = "elementalcraft:retrievergametests.should_extractfrominfuser";
    public static final String BINDER_TEMPLATE = "elementalcraft:retrievergametests.should_extractfrombinder";

    @GameTest(template = INFUSER_TEMPLATE)
    @TestHolder
    public static void should_extractFromInfuser(ECGameTestHelper helper) {
        InfuserBlockEntity infuser = helper.getBlockEntity(new BlockPos(0, 3, 0));
        var container = helper.requireElementContainer(new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
                    infuser.getInventory().setItem(0, new ItemStack(Items.IRON_INGOT));
                    container.fill(ElementType.WATER);
                }).thenExecuteAfter(2, () -> {
                    helper.pullLever(1, 3, 1);
                }).thenExecuteAfter(1, () -> {
                    helper.assertContainerContains(new BlockPos(0, 2, 1), ECItems.DRENCHED_IRON_INGOT.get());
                }).thenSucceed();
    }

    @GameTest(template = INFUSER_TEMPLATE)
    @TestHolder
    public static void should_extractFromInfuser_with_activeRetriever(ECGameTestHelper helper) {
        InfuserBlockEntity infuser = helper.getBlockEntity(new BlockPos(0, 3, 0));
        var container = helper.requireElementContainer(new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
            infuser.getInventory().setItem(0, new ItemStack(Items.IRON_INGOT));
            container.fill(ElementType.WATER);
            helper.pullLever(1, 3, 1);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(0, 2, 1), ECItems.DRENCHED_IRON_INGOT.get());
        }).thenSucceed();
    }

    @GameTest(template = BINDER_TEMPLATE)
    @TestHolder
    public static void should_extractFromBinder(ECGameTestHelper helper) {
        BinderBlockEntity binder = helper.getBlockEntity(new BlockPos(0, 3, 0));
        var container = helper.requireElementContainer(new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
            var inv = binder.getInventory();

            inv.setItem(0, new ItemStack(Items.GOLD_INGOT));
            inv.setItem(1, new ItemStack(ECItems.DRENCHED_IRON_INGOT));
            inv.setItem(2, new ItemStack(Items.COPPER_INGOT));
            inv.setItem(3, new ItemStack(Items.REDSTONE));
            inv.setItem(4, new ItemStack(ECItems.AIR_CRYSTAL));
            container.fill(ElementType.AIR);
        }).thenExecuteAfter(2, () -> {
            helper.pullLever(1, 3, 1);
        }).thenExecuteAfter(1, () -> {
            helper.assertContainerContains(new BlockPos(0, 2, 1), ECItems.SWIFT_ALLOY_INGOT.get());
        }).thenSucceed();
    }

    @GameTest(template = BINDER_TEMPLATE)
    @TestHolder
    public static void should_extractOutputAndRemainingFromBinder(ECGameTestHelper helper) {
        BinderBlockEntity binder = helper.getBlockEntity(new BlockPos(0, 3, 0));
        var container = helper.requireElementContainer(new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
            var inv = binder.getInventory();

            inv.setItem(0, new ItemStack(ECItems.SHRINE_BASE));
            inv.setItem(1, new ItemStack(ECItems.FIRE_CRYSTAL));
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
