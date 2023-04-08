package sirttas.elementalcraft.item.chisel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.RuneHandlerHelper;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.rune.RuneItem;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class ChiselItemGameTests {

    private ChiselItemGameTests() {}

    // elementalcraft:chiselitemgametests.inscriber
    @GameTest(template = "inscriber")
    public static void should_craftRune(GameTestHelper helper) {
        var pos = helper.absolutePos(new BlockPos(0, 2, 0));
        var itemHandler = ECContainerHelper.getItemHandlerAt(helper.getLevel(), pos);
        var player = helper.makeMockPlayer();
        var state = helper.getBlockState(new BlockPos(0, 2, 0));

        itemHandler.insertItem(0, new ItemStack(ECItems.MAJOR_RUNE_SLATE.get()), false);
        itemHandler.insertItem(1, new ItemStack(Items.COAL_BLOCK), false);
        itemHandler.insertItem(2, new ItemStack(Items.COAL_BLOCK), false);
        itemHandler.insertItem(3, new ItemStack(ECItems.PRISTINE_FIRE_GEM.get()), false);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.CHISEL.get()));

        for (int i = 0; i < 9; i++) {
            state.use(helper.getLevel(), player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(pos), Direction.NORTH, pos, true));
        }

        var stack = itemHandler.getStackInSlot(0);

        assertThat(stack).is(ECItems.RUNE);
        assertRuneIs(RuneItem.getRune(stack), "tano");
        helper.succeed();
    }

    // elementalcraft:chiselitemgametests.inscriber
    @GameTest(template = "inscriber")
    public static void should_removeRune(GameTestHelper helper) {
        var runeHandler = RuneHandlerHelper.get(helper.getBlockEntity(new BlockPos(0, 2, 0))).resolve().orElseThrow();
        var player = helper.makeMockPlayer();

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.CHISEL.get()));
        player.setShiftKeyDown(true);

        useItemOn(helper, player, InteractionHand.MAIN_HAND, new BlockPos(0, 2, 0));

        assertThat(runeHandler.getRunes()).isEmpty();
        helper.succeed();
    }

    // elementalcraft:chiselitemgametests.inscriber
    @GameTest(template = "inscriber")
    public static void shouldNot_removeRune(GameTestHelper helper) {
        var runeHandler = RuneHandlerHelper.get(helper.getBlockEntity(new BlockPos(0, 2, 0))).resolve().orElseThrow();
        var player = helper.makeMockPlayer();

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ECItems.CHISEL.get()));

        useItemOn(helper, player, InteractionHand.MAIN_HAND, new BlockPos(0, 2, 0));

        assertThat(runeHandler.getRunes()).hasSize(1);
        assertRuneIs(runeHandler.getRunes().get(0), "mewtwo");
        helper.succeed();
    }

    private static void assertRuneIs(Rune rune, String name) {
        if (!rune.is(IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, ElementalCraft.createRL(name)))) {
            throw new GameTestAssertException("Expected rune " + name + " but got " + rune);
        }
    }

    public static void useItemOn(GameTestHelper helper, Player player, InteractionHand hand, BlockPos pos) {
        var absolutePos = helper.absolutePos(pos);
        var result = new BlockHitResult(Vec3.atCenterOf(absolutePos), Direction.NORTH, absolutePos, true);
        var stack = player.getItemInHand(hand);
        var useOnContext = new UseOnContext(player, hand, result);

        stack.useOn(useOnContext);
    }
}
