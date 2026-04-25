package sirttas.elementalcraft.jewel.attack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.Jewels;

import static org.assertj.core.api.Assertions.within;
import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = KirinJewelGameTests.GROUP)
public class KirinJewelGameTests {

    public static final String GROUP = "jewels.attack.kirin";

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Checks if the kirin jewel smites an enderman.")
    public static void should_smiteEnderman(ECGameTestHelper helper) {
        var target = helper.spawn(EntityType.ENDERMAN, new BlockPos(0, 1, 0));
        var player = helper.mockPlayerWithJewel(Jewels.KIRIN);

        helper.startSequence().thenExecuteAfter(2, () -> {
            player.attack(target);
        }).thenExecuteAfter(1, () -> {
            assertThat(target.isAlive())
                    .describedAs("Enderman should be alive")
                    .isTrue();
            assertThat(target.getHealth()).isCloseTo(33, within(1.9F));
            helper.assertElementUsed(player, ElementType.FIRE);
        }).thenExecute(() -> {
            target.discard();
            player.discard();
        }).thenSucceed();
    }

    @GameTest
    @EmptyTemplate(floor = true)
    @TestHolder(description = "Checks if the kirin jewel smites a zombie with double damage.")
    public static void should_smiteZombie(ECGameTestHelper helper) {
        var target = helper.spawn(EntityType.ZOMBIE, new BlockPos(0, 1, 0));
        var player = helper.mockPlayerWithJewel(Jewels.KIRIN);

        helper.startSequence().thenExecuteAfter(2, () -> {
            player.attack(target);
        }).thenExecuteAfter(1, () -> {
            assertThat(target.isAlive())
                    .describedAs("Zombie should be alive")
                    .isTrue();
            assertThat(target.getHealth()).isCloseTo(8, within(1.9F));
            helper.assertElementUsed(player, ElementType.FIRE);
        }).thenExecute(() -> {
            target.discard();
            player.discard();
        }).thenSucceed();
    }
}
