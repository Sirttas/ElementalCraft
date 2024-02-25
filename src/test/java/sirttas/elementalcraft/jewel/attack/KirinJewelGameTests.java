package sirttas.elementalcraft.jewel.attack;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

import static org.assertj.core.api.Assertions.within;
import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
@PrefixGameTestTemplate(false)
public class KirinJewelGameTests {

    @GameTest(template = ECGameTestHelper.EMPTY_TEMPLATE)
    public static void should_smiteHostile(GameTestHelper helper) {
        var target = helper.spawn(EntityType.ENDERMAN, new BlockPos(0, 1, 0));
        var player = mockPlayer(helper);

        helper.startSequence().thenExecuteAfter(2, () -> {
            player.attack(target);
        }).thenExecuteAfter(1, ECGameTestHelper.fixAssertions(() -> {
            assertThat(target.isAlive()).isTrue();
            assertThat(target.getHealth()).isCloseTo(33, within(1.9F));
        })).thenExecute(() -> {
            target.discard();
            player.discard();
        }).thenSucceed();
    }

    private static Player mockPlayer(GameTestHelper helper) {
        var player = helper.makeMockPlayer();
        var holder = new ItemStack(ECItems.PURE_HOLDER.get());

        holder.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM).fill();

        var sword = new ItemStack(Items.STONE_SWORD);

        JewelHelper.setJewel(sword, Jewels.KIRIN.get());

        player.moveTo(helper.absoluteVec(Vec3.ZERO));
        player.setItemInHand(InteractionHand.MAIN_HAND, sword);
        player.setItemInHand(InteractionHand.OFF_HAND, holder);
        helper.getLevel().addFreshEntity(player);
        return player;
    }
}
