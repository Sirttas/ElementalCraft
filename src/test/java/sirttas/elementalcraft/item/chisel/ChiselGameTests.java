package sirttas.elementalcraft.item.chisel;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.rune.RuneGameTestHelper;
import sirttas.elementalcraft.rune.RuneTestHolder;
import sirttas.elementalcraft.rune.Runes;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ChiselGameTests {

    // elementalcraft:chiselgametests.inscriber
    @GameTest(template = "inscriber", batch = InstrumentGameTestHelper.BATCH_NAME)
    public static void should_craftRune(GameTestHelper helper) {
        var pos = helper.absolutePos(new BlockPos(0, 2, 0));
        var itemHandler = ECContainerHelper.getItemHandlerAt(helper.getLevel(), pos);
        var player = mockChiselPlayer(helper, new BlockPos(0, 2, 0));

        itemHandler.insertItem(0, new ItemStack(ECItems.MAJOR_RUNE_SLATE.get()), false);
        itemHandler.insertItem(1, new ItemStack(Items.COAL_BLOCK), false);
        itemHandler.insertItem(2, new ItemStack(Items.COAL_BLOCK), false);
        itemHandler.insertItem(3, new ItemStack(ECItems.PRISTINE_FIRE_GEM.get()), false);

        for (int i = 0; i < 9; i++) {
            helper.useBlock(new BlockPos(0, 2, 0), player);
        }

        var stack = itemHandler.getStackInSlot(0);

        assertThat(stack).is(ECItems.RUNE);
        RuneGameTestHelper.assertRuneIs(stack, Runes.TANO);
        helper.succeed();
    }

    @GameTestGenerator
    public static Collection<TestFunction> should_removeRunes() {
        var index = new AtomicInteger(0);

        return RuneTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("should_removeRunes#" + index.getAndIncrement(), ChiselGameTests::should_removeRunes))
                .toList();
    }

    private static void should_removeRunes(GameTestHelper helper, RuneTestHolder holder) {
        var pos = holder.pos();
        var side = holder.side();
        var runes = holder.runes();
        var runeHandler = helper.getLevel().getCapability(ElementalCraftCapabilities.RuneHandler.BLOCK, helper.absolutePos(pos), side);

        assertThat(runeHandler).isNotNull();
        var player = mockChiselPlayer(helper, pos);

        player.setShiftKeyDown(true);
        ECGameTestHelper.useItemOn(helper, player, pos, side);

        assertThat(runeHandler.getRunes()).isEmpty();
        player.discard();

        var items = helper.getEntities(EntityType.ITEM, pos, 1);

        assertThat(items).hasSize(runes.size())
                .allSatisfy(item -> assertThat(item.getItem())
                        .is(ECItems.RUNE)
                        .satisfies(stack -> RuneGameTestHelper.assertRuneIs(stack, runes.get(items.indexOf(item)))));
        items.forEach(Entity::discard);
        helper.succeed();
    }

    @GameTestGenerator
    public static Collection<TestFunction> shouldNot_removeRunes() {
        var index = new AtomicInteger(0);

        return RuneTestHolder.HOLDERS.stream()
                .map(t -> t.createTestFunction("shouldNot_removeRunes#" + index.getAndIncrement(), ChiselGameTests::shouldNot_removeRunes))
                .toList();
    }

    private static void shouldNot_removeRunes(GameTestHelper helper, RuneTestHolder holder) {
        var pos = holder.pos();
        var side = holder.side();
        var runeHandler = helper.getLevel().getCapability(ElementalCraftCapabilities.RuneHandler.BLOCK, helper.absolutePos(pos), side);

        assertThat(runeHandler).isNotNull();

        var player = mockChiselPlayer(helper, pos);

        ECGameTestHelper.useItemOn(helper, player, pos, side);

        assertThat(runeHandler.getRunes()).hasSize(holder.runes().size())
                .allSatisfy(rune -> RuneGameTestHelper.assertRuneIs(rune, holder.runes().get(runeHandler.getRunes().indexOf(rune))));
        helper.succeed();
    }

    @Nonnull
    private static Player mockChiselPlayer(GameTestHelper helper, BlockPos pos) {
        var player = helper.makeMockPlayer();

        player.moveTo(helper.absoluteVec(Vec3.atCenterOf(pos)));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.CHISEL.get()));
        return player;
    }
}
