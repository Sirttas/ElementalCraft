package sirttas.elementalcraft.block.instrument.io.mill.grindstone;

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
public class AirMillGrindstoneGameTests {

    // elementalcraft:airmillgrindstonegametests.air_mill_grindstone
    @GameTest(template = "air_mill_grindstone")
    public static void should_grind(GameTestHelper helper) {
        var airMillGrindstone = (AirMillGrindstoneBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0));
        var container = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(0, 1, 0))).orElse(null);

        assertThat(airMillGrindstone).isNotNull();
        assertThat(container).isNotNull();

        helper.startSequence().thenExecute(() -> {
            airMillGrindstone.getInventory().setItem(0, new ItemStack(Items.GRAVEL));
            container.insertElement(1000000, ElementType.AIR, false);

            assertThat(airMillGrindstone.isRecipeAvailable()).isTrue();
        }).thenExecuteAfter(2, () -> {
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(0)).isEmpty();
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(1))
                    .is(Items.SAND)
                    .hasCount(1);
        }).thenSucceed();
    }

    // elementalcraft:airmillgrindstonegametests.air_mill_grindstone
    @GameTest(template = "air_mill_grindstone")
    public static void shouldNot_grind_with_wrongElement(GameTestHelper helper) {
        var airMillGrindstone = (AirMillGrindstoneBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0));
        var container = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(0, 1, 0))).orElse(null);

        assertThat(airMillGrindstone).isNotNull();
        assertThat(container).isNotNull();

        helper.startSequence().thenExecute(() -> {
            airMillGrindstone.getInventory().setItem(0, new ItemStack(Items.GRAVEL));
            container.insertElement(1000000, ElementType.WATER, false);

            assertThat(airMillGrindstone.isRecipeAvailable()).isFalse();
        }).thenExecuteAfter(2, () -> {
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(0))
                    .is(Items.GRAVEL)
                    .hasCount(1);
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(1)).isEmpty();
        }).thenSucceed();
    }
}
