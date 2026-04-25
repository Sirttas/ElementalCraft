package sirttas.elementalcraft.jewel.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.Jewels;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = PhoenixJewelGameTests.GROUP)
public class PhoenixJewelGameTests {

    public static final String GROUP = "jewels.effect.phoenix";

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Checks if a player is given fire resistance and regeneration by the jewel of the phoenix while burning.")
    public static void should_haveFireResistanceAndRegenerationWhileBurning(ECGameTestHelper helper) {
        var player = helper.mockPlayerWithJewel(Jewels.PHOENIX);

        helper.startSequence().thenExecuteAfter(2, () -> {
            player.igniteForSeconds(5);
        }).thenExecuteAfter(80, () -> {
            assertThat(player.isAlive())
                    .describedAs("Player should be alive")
                    .isTrue();
            assertThat(player.getHealth()).isEqualTo(20);
            helper.assertMobEffectPresent(player, MobEffects.FIRE_RESISTANCE, Component.literal("Fire resistance"));
            helper.assertMobEffectPresent(player, MobEffects.REGENERATION, Component.literal("Regeneration"));
            helper.assertElementUsed(player, ElementType.FIRE);
        }).thenExecute(player::discard)
        .thenSucceed();
    }
}
