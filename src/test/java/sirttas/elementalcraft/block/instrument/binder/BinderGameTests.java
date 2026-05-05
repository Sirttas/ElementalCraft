package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.rune.Runes;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateHelper.elementStorage;
import static sirttas.elementalcraft.template.StructureTemplateHelper.itemList;
import static sirttas.elementalcraft.template.StructureTemplateHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateHelper.withValue;

@ForEachTest(groups = BinderGameTests.GROUP)
public class BinderGameTests {
    public static final String GROUP = "level.blocks.instruments.binder";

    @TestHolder(description = "Checks that the binder can craft a swift alloy.")
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

    @TestHolder(description = "Checks that the binder keeps the bucket after crafting a fire pylon.")
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

    @TestHolder(description = "Checks that the binder can automatically craft multiple swift alloys with a sorter/retriever setup.")
    @GameTest
    public static void should_autoCraftSwiftAlloys(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(2, 3, 3)
                .fill(0, 0, 0, 1, 0, 2, ECBlocks.WHITE_ROCK_BRICKS.get().defaultBlockState())
                .placeFloorLever(0, 2, 0, true)
                .set(0, 1, 0, Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true))
                .set(1, 1, 0, Blocks.CHEST.defaultBlockState(),
                        withValue(itemList(
                                new ItemStack(Items.GOLD_INGOT, 64),
                                new ItemStack(ECItems.DRENCHED_IRON_INGOT.get(), 64),
                                new ItemStack(Items.COPPER_INGOT, 64),
                                new ItemStack(Items.REDSTONE, 64),
                                new ItemStack(ECItems.AIR_CRYSTAL.get(), 64))))
                .set(1, 1, 1, ECBlocks.CREATIVE_CONTAINER.get().defaultBlockState(),
                        withValue(elementStorage(ElementType.AIR, 10000)))
                .set(1, 1, 2, Blocks.CHEST.defaultBlockState())
                .set(1, 2, 0, ECBlocks.ORDERED_SORTER.get().defaultBlockState()
                                .setValue(ISorterBlock.SOURCE, Direction.DOWN)
                                .setValue(ISorterBlock.TARGET, Direction.SOUTH),
                        withValue(
                                itemList(
                                        new ItemStack(Items.GOLD_INGOT),
                                        new ItemStack(ECItems.DRENCHED_IRON_INGOT.get()),
                                        new ItemStack(Items.COPPER_INGOT),
                                        new ItemStack(Items.REDSTONE),
                                        new ItemStack(ECItems.AIR_CRYSTAL.get())),
                                runeHandler(Runes.CREATIVE)))
                .set(1, 2, 1, ECBlocks.BINDER.get().defaultBlockState(),
                        withValue(runeHandler(Runes.CREATIVE)))
                .set(1, 2, 2, ECBlocks.RETRIEVER.get().defaultBlockState()
                        .setValue(ISorterBlock.SOURCE, Direction.NORTH)
                        .setValue(ISorterBlock.TARGET, Direction.DOWN)));

        test.onGameTest(ECGameTestHelper.class, helper -> helper.startSequence()
                .thenExecute(() -> helper.pullLever(0, 2, 0))
                .thenExecuteAfter(2, () -> helper.assertContainerContains(new BlockPos(1, 1, 2), ECItems.SWIFT_ALLOY_INGOT.get()))
                .thenSucceed());
    }
}
