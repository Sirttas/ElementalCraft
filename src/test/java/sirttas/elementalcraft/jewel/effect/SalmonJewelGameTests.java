package sirttas.elementalcraft.jewel.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.testframework.DynamicTest;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.jewel.Jewels;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = SalmonJewelGameTests.GROUP)
public class SalmonJewelGameTests {

    public static final String GROUP = "jewels.effect.salmon";

    @GameTest
    @TestHolder(description = "Checks if a player is given water breathing by the jewel of the salmon while under water.")
    public static void should_givePlayerWaterBreathing(DynamicTest test) {
        test.registerGameTestTemplate(() -> StructureTemplateBuilder.withSize(5, 5, 5)
                .fill(0, 0, 0, 4, 4, 4, ECBlocks.WHITE_ROCK_BRICKS.get())
                .fill(1, 1, 1, 3, 4, 3, Blocks.WATER));

        test.onGameTest(ECGameTestHelper.class, helper -> {
            var player = helper.mockPlayerWithJewel(new Vec3(2, 2, 2), Jewels.SALMON);

            helper.startSequence().thenExecuteAfter(10, () -> {
                        assertThat(player.isAlive())
                                .describedAs("Player should be alive")
                                .isTrue();
                        assertThat(player.getHealth()).isEqualTo(20);
                        helper.assertMobEffectPresent(player, MobEffects.WATER_BREATHING, Component.literal("Water breathing"));
                        helper.assertElementUsed(player, ElementType.WATER);
                    }).thenExecute(player::discard)
                    .thenSucceed();
        });
    }
}
