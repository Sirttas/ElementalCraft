package sirttas.elementalcraft.block.instrument.io.mill.saw;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.assertion.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class WaterMillWoodSawGameTests {

    // elementalcraft:watermillwoodsawgametests.water_mill_wood_saw
    @GameTest(template = "water_mill_wood_saw")
    public static void should_cut(GameTestHelper helper) {
        var waterMillWoodSaw = (WaterMillWoodSawBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0));
        var container = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(0, 1, 0))).orElse(null);

        assertThat(waterMillWoodSaw).isNotNull();
        assertThat(container).isNotNull();

        helper.startSequence().thenExecute(() -> {
            waterMillWoodSaw.getInventory().setItem(0, new ItemStack(Items.OAK_LOG));
            container.insertElement(1000000, ElementType.WATER, false);

            assertThat(waterMillWoodSaw.isRecipeAvailable()).isTrue();
        }).thenExecuteAfter(2, () -> {
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(0)).isEmpty();
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(1))
                    .is(Items.STRIPPED_OAK_LOG)
                    .hasCount(1);
        }).thenSucceed();
    }

    // elementalcraft:watermillwoodsawgametests.water_mill_wood_saw
    @GameTest(template = "water_mill_wood_saw")
    public static void shouldNot_cut_with_wrongElement(GameTestHelper helper) {
        var waterMillWoodSaw = (WaterMillWoodSawBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0));
        var container = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(0, 1, 0))).orElse(null);

        assertThat(waterMillWoodSaw).isNotNull();
        assertThat(container).isNotNull();

        helper.startSequence().thenExecute(() -> {
            waterMillWoodSaw.getInventory().setItem(0, new ItemStack(Items.OAK_LOG));
            container.insertElement(1000000, ElementType.AIR, false);

            assertThat(waterMillWoodSaw.isRecipeAvailable()).isFalse();
        }).thenExecuteAfter(2, () -> {
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(0))
                    .is(Items.OAK_LOG)
                    .hasCount(1);
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(1)).isEmpty();
        }).thenSucceed();
    }
}
