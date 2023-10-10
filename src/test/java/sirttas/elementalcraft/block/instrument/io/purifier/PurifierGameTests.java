package sirttas.elementalcraft.block.instrument.io.purifier;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.item.ECItems;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class PurifierGameTests {

    // elementalcraft:purifiergametests.ore_purifier
    @GameTest(template = "ore_purifier")
    public static void should_purify(GameTestHelper helper) {
        InstrumentGameTestHelper.<PurifierBlockEntity>runInstrument(helper, new ItemStack(Items.IRON_ORE), ElementType.EARTH, purifier -> {
            assertThat(purifier.getInventory().getItem(0)).isEmpty();
            assertThat(purifier.getInventory().getItem(1))
                    .is(ECItems.PURE_ORE)
                    .hasCount(2)
                    .satisfies(s -> assertThat(ElementalCraft.PURE_ORE_MANAGER.getPureOreId(s)).isEqualTo(new ResourceLocation("forge", "iron")));
        });
    }

    // elementalcraft:purifiergametests.ore_purifier
    @GameTest(template = "ore_purifier")
    public static void shouldNot_purify_with_wrongElement(GameTestHelper helper) {
        InstrumentGameTestHelper.<PurifierBlockEntity>runInstrument(helper, new ItemStack(Items.IRON_ORE), ElementType.WATER, false, purifier -> {
            assertThat(purifier.getInventory().getItem(0))
                    .is(Items.IRON_ORE)
                    .hasCount(1);
            assertThat(purifier.getInventory().getItem(1)).isEmpty();
        });
    }
}
