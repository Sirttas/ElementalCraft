package sirttas.elementalcraft.block.sorter.ordered;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.container.ContainerGameTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.rune.Runes;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateHelper.itemList;
import static sirttas.elementalcraft.template.StructureTemplateHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateHelper.withValue;

public class OrderedSorterGameTests {

    @TestHolder(description = "Checks if the ordered sorter correctly transfers only the filtered item from the source chest to the target chest.")
    @GameTest
    public static void should_onlyTransferFilteredItem(DynamicTest test) {
        test.registerGameTestTemplate(() -> createTemplate(
                List.of(new ItemStack(ECItems.PRISTINE_FIRE_GEM.get(), 64), new ItemStack(Blocks.COAL_BLOCK, 64), new ItemStack(Blocks.DIAMOND_BLOCK, 64)),
                List.of(),
                List.of(new ItemStack(Blocks.COAL_BLOCK))));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var sourceChest = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 0));
            var targetChest = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 2));

            assertThat(sourceChest).isNotEmpty();
            assertThat(targetChest).isEmpty();

            helper.startSequence()
                    .thenExecuteAfter(1, () -> helper.pullLever(0, 2, 1))
                    .thenIdle(1)
                    .thenExecuteAfter(10, () -> assertThat(targetChest)
                            .isNotEmpty()
                            .satisfies(0, s -> assertThat(s).is(Blocks.COAL_BLOCK).hasCount(1)))
                    .thenExecuteAfter(10, () -> assertThat(targetChest)
                            .isNotEmpty()
                            .satisfies(0, s -> assertThat(s).is(Blocks.COAL_BLOCK).hasCount(2)))
                    .thenExecuteAfter(10, () -> assertThat(targetChest)
                            .isNotEmpty()
                            .satisfies(0, s -> assertThat(s).is(Blocks.COAL_BLOCK).hasCount(3)))
                    .thenSucceed();
        });
    }

    @TestHolder(description = "Checks if the ordered sorter correctly transfers items from the source chest to the target chest in the right order.")
    @GameTest
    public static void should_transferItemsInRightOrder(DynamicTest test) {
        test.registerGameTestTemplate(() -> createTemplate(
                List.of(new ItemStack(ECItems.PRISTINE_FIRE_GEM.get(), 64), new ItemStack(Blocks.COAL_BLOCK, 64), new ItemStack(Blocks.DIAMOND_BLOCK, 64)),
                List.of(),
                List.of(new ItemStack(ECItems.PRISTINE_FIRE_GEM.get()), new ItemStack(Blocks.COAL_BLOCK))));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var sourceChest = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 0));
            var targetChest = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 2));

            assertThat(sourceChest).isNotEmpty();
            assertThat(targetChest).isEmpty();

            helper.startSequence()
                    .thenExecuteAfter(1, () -> helper.pullLever(0, 2, 1))
                    .thenIdle(1)
                    .thenExecuteAfter(10, () -> assertThat(targetChest)
                            .isNotEmpty()
                            .satisfies(0, s -> assertThat(s).is(ECItems.PRISTINE_FIRE_GEM).hasCount(1)))
                    .thenExecuteAfter(10, () -> assertThat(targetChest)
                            .isNotEmpty()
                            .satisfies(0, s -> assertThat(s).is(ECItems.PRISTINE_FIRE_GEM).hasCount(1))
                            .satisfies(1, s -> assertThat(s).is(Items.COAL_BLOCK).hasCount(1)))
                    .thenExecuteAfter(10, () -> assertThat(targetChest)
                            .isNotEmpty()
                            .satisfies(0, s -> assertThat(s).is(ECItems.PRISTINE_FIRE_GEM).hasCount(2))
                            .satisfies(1, s -> assertThat(s).is(Items.COAL_BLOCK).hasCount(1)))
                    .thenSucceed();
        });
    }

    @TestHolder(description = "Checks if the ordered sorter correctly transfers all items from the source chest to the target chest in one tick when a creative rune is applied.")
    @GameTest
    public static void should_transferAllItemsInOneTickWithCreativeRune(DynamicTest test) {
        test.registerGameTestTemplate(() -> createTemplate(
                List.of(new ItemStack(ECItems.PRISTINE_FIRE_GEM.get(), 64), new ItemStack(Blocks.COAL_BLOCK, 64), new ItemStack(Blocks.DIAMOND_BLOCK, 64)),
                List.of(),
                List.of(),
                List.of(Runes.CREATIVE)));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var sourceChest = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 0));
            var targetChest = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 2));

            assertThat(sourceChest).isNotEmpty();
            assertThat(targetChest).isEmpty();

            helper.startSequence()
                    .thenExecuteAfter(1, () -> helper.pullLever(0, 2, 1))
                    .thenExecuteAfter(1, () -> {
                        assertThat(targetChest).isNotEmpty()
                                .satisfies(0, s -> assertThat(s).is(ECItems.PRISTINE_FIRE_GEM).hasCount(64))
                                .satisfies(1, s -> assertThat(s).is(Blocks.COAL_BLOCK).hasCount(64))
                                .satisfies(2, s -> assertThat(s).is(Blocks.DIAMOND_BLOCK).hasCount(64));
                        assertThat(sourceChest).isEmpty();
                    }).thenSucceed();
        });
    }

    private static StructureTemplateBuilder createTemplate(List<ItemStack> sourceStacks, List<ItemStack> targetStacks, List<ItemStack> sorterStacks) {
        return createTemplate(sourceStacks, targetStacks, sorterStacks, List.of());
    }

    @SuppressWarnings("unchecked")
    private static StructureTemplateBuilder createTemplate(List<ItemStack> sourceStacks, List<ItemStack> targetStacks, List<ItemStack> sorterStacks, List<ResourceKey<Rune>> runes) {
        return StructureTemplateBuilder.withSize(2, 2, 3)
                .fill(0, 0, 0, 1, 0, 2, ECBlocks.WHITE_ROCK_BRICKS.get())
                .set(0, 0, 1, Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, true))
                .set(0, 1, 1, Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACING, Direction.EAST).setValue(LeverBlock.POWERED, true).setValue(LeverBlock.FACE, AttachFace.FLOOR))
                .set(1, 1, 0, Blocks.CHEST.defaultBlockState(), withValue(itemList(sourceStacks)))
                .set(1, 1, 2, Blocks.CHEST.defaultBlockState(), withValue(itemList(targetStacks)))
                .set(1, 1, 1, ECBlocks.ORDERED_SORTER.get().defaultBlockState().setValue(ISorterBlock.SOURCE, Direction.NORTH).setValue(ISorterBlock.TARGET, Direction.SOUTH), withValue(
                        itemList(sorterStacks),
                        runeHandler(runes)));
        }
}
