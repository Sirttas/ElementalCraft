package sirttas.elementalcraft.block.instrument.io.mill.woodsaw;

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
public class WaterMillWoodSawGameTests {

    // elementalcraft:watermillwoodsawgametests.water_mill_wood_saw
    @GameTest(template = "water_mill_wood_saw")
    public static void should_cut(GameTestHelper helper) {
        InstrumentGameTestHelper.<WaterMillWoodSawBlockEntity>runInstrument(helper, new ItemStack(Items.OAK_LOG), ElementType.WATER, waterMillWoodSaw -> {
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(0)).isEmpty();
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(1))
                    .is(Items.STRIPPED_OAK_LOG)
                    .hasCount(1);
        });
    }

    // elementalcraft:watermillwoodsawgametests.water_mill_wood_saw
    @GameTest(template = "water_mill_wood_saw")
    public static void shouldNot_cut_with_wrongElement(GameTestHelper helper) {
        InstrumentGameTestHelper.<WaterMillWoodSawBlockEntity>runInstrument(helper, new ItemStack(Items.OAK_LOG), ElementType.AIR, false, waterMillWoodSaw -> {
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(0))
                    .is(Items.OAK_LOG)
                    .hasCount(1);
            Assertions.assertThat(waterMillWoodSaw.getInventory().getItem(1)).isEmpty();
        });
    }
}
