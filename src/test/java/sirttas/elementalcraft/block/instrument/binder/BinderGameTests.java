package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.item.ECItems;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = BinderGameTests.GROUP)
public class BinderGameTests {
    public static final String GROUP = "level.blocks.instruments.binder";

    @TestHolder(description = "Checks if the binder can craft a swift alloy.")
    @GameTest(template = InstrumentTestTemplates.BINDER_TEMPLATE_NAME)
    public static void should_craftSwiftAlloy(ECGameTestHelper helper) {
        helper.<BinderBlockEntity>runInstrument(List.of(
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(ECItems.DRENCHED_IRON_INGOT),
                new ItemStack(Items.COPPER_INGOT),
                new ItemStack(Items.REDSTONE),
                new ItemStack(ECItems.AIR_CRYSTAL)
        ), ElementType.AIR, binder -> {
            var inv = binder.getInventory();

            assertThat(inv.getItem(0))
                    .is(ECItems.SWIFT_ALLOY_INGOT)
                    .hasCount(1);
            for (int i = 1; i < inv.getContainerSize(); i++) {
                assertThat(inv.getItem(i)).isEmpty();
            }
        });
    }

    @TestHolder(description = "Checks if the binder keeps the bucket after crafting a fire pylon.")
    @GameTest(template = InstrumentTestTemplates.BINDER_TEMPLATE_NAME)
    public static void should_keepBucketAfterCraftingFirePylon(ECGameTestHelper helper) {
        helper.<BinderBlockEntity>runInstrument(List.of(
                new ItemStack(ECItems.SHRINE_BASE),
                new ItemStack(ECItems.FIRE_CRYSTAL),
                new ItemStack(Items.LAVA_BUCKET),
                new ItemStack(Items.GOLD_INGOT)
        ), ElementType.FIRE, binder -> {
            var inv = binder.getInventory();

            assertThat(inv.getItem(0))
                    .is(ECBlocks.FIRE_PYLON)
                    .hasCount(1);
            assertThat(inv.getItem(1))
                    .is(Items.BUCKET)
                    .hasCount(1);
            for (int i = 2; i < inv.getContainerSize(); i++) {
                assertThat(inv.getItem(i)).isEmpty();
            }
        });
    }

    @TestHolder(description = "Checks if the binder can automaticaly craft multiple swift alloys with a sorter/retriever setup.")
    @GameTest(template = "elementalcraft:bindergametests.should_autocraftswiftalloys")
    public static void should_autoCraftSwiftAlloys(ECGameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.pullLever(0, 2, 0);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(1, 1, 2), ECItems.SWIFT_ALLOY_INGOT.get());
        }).thenSucceed();
    }
}
