package sirttas.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.ShrineGameTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class GreaterFortuneShrineUpgradeGameTests {

    // elementalcraft:greaterfortuneshrineupgradegametests.should_increaseoreloot
    @GameTest(batch = ShrineGameTestHelper.BATCH_NAME)
    public static void should_increaseOreLoot(GameTestHelper helper) {
        ShrineGameTestHelper.forcePeriods(helper, new BlockPos(12, 2, 12), 4);
        helper.succeedIf(() -> {
            helper.assertBlockState(new BlockPos(12, 1, 11), b -> b.is(Blocks.STONE), () -> "Block has not been mined");
            helper.assertBlockState(new BlockPos(12, 1, 13), b -> b.is(Blocks.STONE), () -> "Block has not been mined");
            helper.assertBlockState(new BlockPos(11, 1, 12), b -> b.is(Blocks.STONE), () -> "Block has not been mined");
            helper.assertBlockState(new BlockPos(13, 1, 12), b -> b.is(Blocks.STONE), () -> "Block has not been mined");
            var count = helper.getEntities(EntityType.ITEM, new BlockPos(12, 1, 12), 2).stream()
                    .map(ItemEntity::getItem)
                    .filter(i -> i.is(Items.RAW_IRON))
                    .mapToInt(ItemStack::getCount)
                    .sum();

            assertThat(count).isGreaterThan(4);
        });
    }

}
