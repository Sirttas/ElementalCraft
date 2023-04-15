package sirttas.elementalcraft.block.sorter;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.entity.BlockEntityGameTestHelper;
import sirttas.elementalcraft.container.ContainerGameTestHelper;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class OrderedSorterGameTests {

    // elementalcraft:orderedsortergametests.should_transferitems
    @GameTest
    public static void should_transferItems(GameTestHelper helper) {
        var sorter = getOrderedSorter(helper, new BlockPos(1, 2, 1));
        var sourceChest = ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 2));
        var targetChest =  ContainerGameTestHelper.getItemHandler(helper, new BlockPos(1, 2, 0));

        assertThat(sourceChest).isNotEmpty();
        assertThat(targetChest).isEmpty();
        helper.pullLever(0, 2, 1);
        sorter.transfer();
        sorter.transfer();
        sorter.transfer();
        assertThat(targetChest)
                .isNotEmpty()
                .satisfies(0, s -> assertThat(s).is(ECItems.PRISTINE_FIRE_GEM).hasCount(2))
                .satisfies(1, s -> assertThat(s).is(Items.COAL_BLOCK).hasCount(1));
        helper.succeed();
    }

    @Nonnull
    private static SorterBlockEntity getOrderedSorter(GameTestHelper helper, BlockPos pos) {
        return (SorterBlockEntity) BlockEntityGameTestHelper.getBlockEntity(helper, pos);
    }
}
