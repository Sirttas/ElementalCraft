package sirttas.elementalcraft.block.instrument.binder;

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
import sirttas.elementalcraft.item.ECItems;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class BinderGameTests {

    // elementalcraft:bindergametests.binder
    @GameTest(template = "binder", batch = InstrumentGameTestHelper.BATCH_NAME)
    public static void should_craftSwiftAlloy(GameTestHelper helper) {
        InstrumentGameTestHelper.<BinderBlockEntity>runInstrument(helper, List.of(
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(ECItems.DRENCHED_IRON_INGOT.get()),
                new ItemStack(Items.COPPER_INGOT),
                new ItemStack(Items.REDSTONE),
                new ItemStack(ECItems.AIR_CRYSTAL.get())
        ), ElementType.AIR, binder -> {
            assertThat(binder.getInventory().getItem(0))
                    .is(ECItems.SWIFT_ALLOY_INGOT.get())
                    .hasCount(1);
        });
    }

    // elementalcraft:bindergametests.binder
    @GameTest(template = "binder", batch = InstrumentGameTestHelper.BATCH_NAME)
    public static void should_keepBucketAfterCraftingFirePylon(GameTestHelper helper) {
        InstrumentGameTestHelper.<BinderBlockEntity>runInstrument(helper, List.of(
                new ItemStack(ECItems.SHRINE_BASE.get()),
                new ItemStack(ECItems.FIRE_CRYSTAL.get()),
                new ItemStack(Items.LAVA_BUCKET),
                new ItemStack(Items.GOLD_INGOT)
        ), ElementType.FIRE, binder -> {
            var inv = binder.getInventory();

            assertThat(inv.getItem(0))
                    .is(ECBlocks.FIRE_PYLON.get())
                    .hasCount(1);
            assertThat(inv.getItem(1))
                    .is(Items.BUCKET)
                    .hasCount(1);
        });
    }

    // elementalcraft:bindergametests.should_autocraftswiftalloys
    @GameTest(batch = InstrumentGameTestHelper.BATCH_NAME)
    public static void should_autoCraftSwiftAlloys(GameTestHelper helper) {
        helper.startSequence().thenExecute(() -> {
            helper.pullLever(0, 3, 0);
        }).thenExecuteAfter(2, () -> {
            helper.assertContainerContains(new BlockPos(1, 2, 2), ECItems.SWIFT_ALLOY_INGOT.get());
        }).thenSucceed();
    }
}
