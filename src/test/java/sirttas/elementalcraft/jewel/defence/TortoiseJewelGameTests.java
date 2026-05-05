package sirttas.elementalcraft.jewel.defence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.EmptyTemplate;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.jewel.attack.KirinJewelGameTests;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = KirinJewelGameTests.GROUP)
public class TortoiseJewelGameTests {

    public static final String GROUP = "jewels.defence.tortoise";

    @GameTest
    @EmptyTemplate(value = "3x10x3", floor = true)
    @TestHolder(description = "Checks that the tortoise jewel protects the player from a falling anvil.")
    public static void should_protectFromAnvil(ECGameTestHelper helper) {
        var level = helper.getLevel();
        var player = helper.mockPlayerWithJewel(Jewels.TORTOISE);
        var anvil = FallingBlockEntity.fall(level, helper.absolutePos(new BlockPos(1, 10, 1)), Blocks.ANVIL.defaultBlockState());

        anvil.setHurtsEntities(2.0F, 40);
        level.addFreshEntity(anvil);
        helper.startSequence().thenExecuteAfter(100, () -> {
            helper.assertEntityAlive(player);
            helper.assertJewelActive(player, Jewels.TORTOISE);
            assertThat(player.getHealth()).isEqualTo(20);
            assertThat(anvil.isAlive())
                    .describedAs("Anvil should have dropped")
                    .isFalse();
            helper.assertElementUsed(player, ElementType.EARTH);
            helper.assertBlockNotPresent(Blocks.ANVIL, 1, 2, 1);
        }).thenExecute(() -> {
            anvil.discard();
            player.discard();
        }).thenSucceed();
    }
}
