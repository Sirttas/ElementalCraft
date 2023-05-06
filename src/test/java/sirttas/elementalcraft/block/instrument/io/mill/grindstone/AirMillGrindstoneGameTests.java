package sirttas.elementalcraft.block.instrument.io.mill.grindstone;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.assertion.Assertions;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;

@GameTestHolder(ElementalCraftApi.MODID)
public class AirMillGrindstoneGameTests {

    // elementalcraft:airmillgrindstonegametests.air_mill_grindstone
    @GameTest(template = "air_mill_grindstone")
    public static void should_grind(GameTestHelper helper) {
        InstrumentGameTestHelper.<AirMillGrindstoneBlockEntity>runInstrument(helper, new ItemStack(Items.GRAVEL), ElementType.AIR, airMillGrindstone -> {
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(0)).isEmpty();
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(1))
                    .is(Items.SAND)
                    .hasCount(1);
        });

    }

    // elementalcraft:airmillgrindstonegametests.air_mill_grindstone
    @GameTest(template = "air_mill_grindstone")
    public static void shouldNot_grind_with_wrongElement(GameTestHelper helper) {
        InstrumentGameTestHelper.<AirMillGrindstoneBlockEntity>runInstrument(helper, new ItemStack(Items.GRAVEL), ElementType.WATER, false, airMillGrindstone -> {
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(0))
                    .is(Items.GRAVEL)
                    .hasCount(1);
            Assertions.assertThat(airMillGrindstone.getInventory().getItem(1)).isEmpty();
        });
    }
}
