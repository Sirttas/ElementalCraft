package sirttas.elementalcraft.item.chisel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.block.pipe.ElementPipeGameTests;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.rune.RuneTestCaseHolder;
import sirttas.elementalcraft.rune.Runes;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = ChiselGameTests.GROUP_NAME)
public class ChiselGameTests {

    public static final String GROUP_NAME = "stacks.chisel";

    @TestHolder(description = "Checks if the inscriber can craft a rune.")
    @GameTest(template = InstrumentTestTemplates.INSCRIBER_TEMPLATE_NAME)
    public static void should_craftRune(ECGameTestHelper helper) {
        var pos = helper.absolutePos(new BlockPos(0, 1, 0));
        var itemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandlerAt(helper.getLevel(), pos));
        var container = ElementPipeGameTests.getElementStorage(helper, 0, 0, 0);
        var player = helper.mockChiselPlayer(new Vec3(0, 1, 0));

        itemHandler.insertItem(0, new ItemStack(ECItems.MINOR_RUNE_SLATE), false);
        itemHandler.insertItem(1, new ItemStack(Items.COAL), false);
        itemHandler.insertItem(2, new ItemStack(Items.COAL), false);
        itemHandler.insertItem(3, new ItemStack(ECItems.CRUDE_FIRE_GEM), false);
        container.fill(ElementType.FIRE);

        for (int i = 0; i < 3; i++) {
            helper.useBlock(new BlockPos(0, 1, 0), player);
        }

        var stack = itemHandler.getStackInSlot(0);

        assertThat(stack).is(ECItems.RUNE);
        helper.assertRuneIs(stack, Runes.MANX);
        helper.succeed();
    }

    public static List<Test> collectTests() {
        var index = new AtomicInteger(0);

        return RuneTestCaseHolder.HOLDERS.stream()
                .<Test>mapMulti((t, downstream) -> {
                    var i = index.getAndIncrement();

                    downstream.accept(t.createTest(
                            "should_removeRunes_" + i,
                            "Check that a player can remove a rune while sneaking",
                            ChiselGameTests::should_removeRunes));
                    downstream.accept(t.createTest(
                            "shouldNot_removeRunes_" + i,
                            "Check that a player cannot remove a rune if not sneaking",
                            ChiselGameTests::shouldNot_removeRunes));
                })
                .toList();
    }

    private static void should_removeRunes(ECGameTestHelper helper, RuneTestCaseHolder holder) {
        var pos = holder.pos();
        var side = holder.side();
        var runes = holder.runes();
        var runeHandler = helper.getLevel().getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, helper.absolutePos(pos), side);

        assertThat(runeHandler)
                .as("RuneHandler should not be null")
                .isNotNull();

        var player = helper.mockChiselPlayer(pos);

        helper.startSequence()
                .thenExecute(() -> {
                    player.setShiftKeyDown(true);
                    helper.useItemOn(player, pos, side);
                }).thenExecuteAfter(1, () -> {
                    var items = helper.getEntities(EntityType.ITEM, pos, 1);

                    assertThat(runeHandler.getRunes()).isEmpty();
                    assertThat(items).hasSize(runes.size())
                            .allSatisfy(item -> assertThat(item.getItem())
                                    .is(ECItems.RUNE)
                                    .satisfies(stack -> helper.assertRuneIs(stack, runes.get(items.indexOf(item)))));
                }).thenExecute(() -> {
                    player.discard();
                    helper.discardItems(pos, 1);
                }).thenSucceed();
    }

    private static void shouldNot_removeRunes(ECGameTestHelper helper, RuneTestCaseHolder holder) {
        var pos = holder.pos();
        var side = holder.side();
        var runeHandler = helper.getLevel().getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, helper.absolutePos(pos), side);

        assertThat(runeHandler)
                .as("RuneHandler should not be null")
                .isNotNull();

        var player = helper.mockChiselPlayer(pos);

        helper.startSequence()
                .thenExecute(() -> helper.useItemOn(player, pos, side))
                .thenExecute(() -> assertThat(runeHandler.getRunes()).hasSize(holder.runes().size())
                        .allSatisfy(rune -> helper.assertRuneIs(rune, holder.runes().get(runeHandler.getRunes().indexOf(rune)))))
                .thenExecute(player::discard)
                .thenSucceed();
    }
}
