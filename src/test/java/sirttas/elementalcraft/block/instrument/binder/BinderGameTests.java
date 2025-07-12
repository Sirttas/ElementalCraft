package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = BinderGameTests.GROUP)
public class BinderGameTests {
    public static final String GROUP = "level.blocks.instruments.binder";

    // elementalcraft:bindergametests.binder
    @TestHolder(description = "Checks if the binder can craft a swift alloy.")
    @GameTest(templateNamespace = ElementalCraftApi.MODID, template = "bindergametests.binder")
    public static void should_craftSwiftAlloy(ECGameTestHelper helper) {
        helper.<BinderBlockEntity>runInstrument(List.of(
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(ECItems.DRENCHED_IRON_INGOT),
                new ItemStack(Items.COPPER_INGOT),
                new ItemStack(Items.REDSTONE),
                new ItemStack(ECItems.AIR_CRYSTAL)
        ), ElementType.AIR, binder -> {
            assertThat(binder.getInventory().getItem(0))
                    .is(ECItems.SWIFT_ALLOY_INGOT)
                    .hasCount(1);
        });
    }

    // elementalcraft:bindergametests.binder
    @TestHolder(description = "Checks if the binder keeps the bucket after crafting a fire pylon.")
    @GameTest(templateNamespace = ElementalCraftApi.MODID, template = "bindergametests.binder")
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
        });
    }

    // elementalcraft:bindergametests.should_autocraftswiftalloys
    @TestHolder(description = "Checks if the binder can automaticaly craft multiple swift alloys with a sorter/retriever setup.")
    @GameTest(templateNamespace = ElementalCraftApi.MODID, template = "bindergametests.should_autocraftswiftalloys")
    public static void should_autoCraftSwiftAlloys(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.pullLever(0, 3, 0);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(1, 2, 2), ECItems.SWIFT_ALLOY_INGOT.get());
        }).thenSucceed();
    }
}
