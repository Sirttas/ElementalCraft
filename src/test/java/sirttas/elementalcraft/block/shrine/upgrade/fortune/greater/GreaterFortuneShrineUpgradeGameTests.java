package sirttas.elementalcraft.block.shrine.upgrade.fortune.greater;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineGameUpgradeTests;

import static org.assertj.core.api.Assertions.assertThat;

@ForEachTest(groups = ShrineGameUpgradeTests.GROUP)
public class GreaterFortuneShrineUpgradeGameTests {

    public static final String TEMPLATE = "elementalcraft:greaterfortuneshrineupgradegametests.should_increaseoreloot";

    @TestHolder
    @GameTest(template = TEMPLATE, required = false)
    public static void should_increaseOreLoot(ECGameTestHelper helper) {
        ShrineGameTestHelper.forcePeriods(helper, new BlockPos(12, 1, 12), 4);
        helper.succeedIf(() -> {
            helper.assertBlockState(new BlockPos(12, 0, 11), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            helper.assertBlockState(new BlockPos(12, 0, 13), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            helper.assertBlockState(new BlockPos(11, 0, 12), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            helper.assertBlockState(new BlockPos(13, 0, 12), b -> b.is(Blocks.STONE), _ -> Component.literal("Block has not been mined"));
            var count = helper.getEntities(EntityType.ITEM, new BlockPos(12, 0, 12), 2).stream()
                    .map(ItemEntity::getItem)
                    .filter(i -> i.is(Items.RAW_IRON))
                    .mapToInt(ItemStack::getCount)
                    .sum();

            assertThat(count).isGreaterThan(4);
        });
    }

}
