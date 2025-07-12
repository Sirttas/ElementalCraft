package sirttas.elementalcraft.jewel.effect;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.JewelTestHelper;
import sirttas.elementalcraft.jewel.Jewels;

import static org.assertj.core.api.Assertions.within;
import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = DemigodJewelGameTests.GROUP)
public class DemigodJewelGameTests {

    public static final String GROUP = "jewels.effect.demigod";

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Checks if a player is protected from death by the jewel of the demigod.")
    public static void should_protectPlayerFromDeath(ECGameTestHelper helper) {
        var player = helper.mockPlayerWithJewel(Jewels.DEMIGOD);

        player.getInventory().add(1, new ItemStack(Items.TOTEM_OF_UNDYING));
        helper.startSequence().thenExecuteAfter(2, () -> {
            player.hurt(player.damageSources().magic(), 1000);
        }).thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
            assertThat(player.isAlive())
                    .describedAs("Player should be alive")
                    .isTrue();
            assertThat(player.getHealth()).isCloseTo(1, within(1F));
            helper.assertMobEffectPresent(player, MobEffects.REGENERATION, "Regeneration");
            helper.assertMobEffectPresent(player, MobEffects.ABSORPTION, "Absorption");
            helper.assertMobEffectPresent(player, MobEffects.FIRE_RESISTANCE, "Fire resistance");
            JewelTestHelper.assertElementUsed(player, ElementType.AIR);
            assertThat(player.getInventory().contains(s -> s.is(Items.TOTEM_OF_UNDYING)))
                    .describedAs("Totem of undying should be consumed")
                    .isFalse();
        })).thenExecute(player::discard)
                .thenSucceed();
    }

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Checks if a player isn't protected if they doesn't have a totem of undying.")
    public static void shouldNot_protectPlayerFromDeath_when_theyDoesntHaveATotemOfUndying(ECGameTestHelper helper) {
        var player = helper.mockPlayerWithJewel(Jewels.DEMIGOD);

        helper.startSequence().thenExecuteAfter(2, () -> {
                    player.hurt(player.damageSources().magic(), 1000);
                }).thenExecuteAfter(1, ECGameTestUtils.fixAssertions(() -> {
                    assertThat(player.isAlive())
                            .describedAs("Player should be dead")
                            .isFalse();
                })).thenExecute(player::discard)
                .thenSucceed();
    }
}
