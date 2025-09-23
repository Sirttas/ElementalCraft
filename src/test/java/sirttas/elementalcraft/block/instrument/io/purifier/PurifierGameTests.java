package sirttas.elementalcraft.block.instrument.io.purifier;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.pureore.PureOre;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = PurifierGameTests.GROUP)
public class PurifierGameTests {

    public static final String GROUP = "level.blocks.instruments.ore_purifier";

    @TestHolder
    @GameTest(template = InstrumentTestTemplates.ORE_PURIFIER_TEMPLATE_NAME)
    public static void should_purifyIronOre(ECGameTestHelper helper) {
        helper.<PurifierBlockEntity>runInstrument(new ItemStack(Items.IRON_ORE), ElementType.EARTH, purifier -> {
            assertThat(purifier.getInventory().getItem(0)).isEmpty();
            assertThat(purifier.getInventory().getItem(1))
                    .is(ECItems.PURE_ORE)
                    .hasCount(2)
                    .satisfies(s -> assertThat(PureOre.getId(s)).isEqualTo(ResourceLocation.fromNamespaceAndPath(ECNames.COMMON_TAGS_NAMESPACE, "iron")));
        });
    }

    @TestHolder
    @GameTest(template = InstrumentTestTemplates.ORE_PURIFIER_TEMPLATE_NAME)
    public static void shouldNot_purify_with_wrongElement(ECGameTestHelper helper) {
        helper.<PurifierBlockEntity>runInstrument(new ItemStack(Items.IRON_ORE), ElementType.WATER, false, purifier -> {
            assertThat(purifier.getInventory().getItem(0))
                    .is(Items.IRON_ORE)
                    .hasCount(1);
            assertThat(purifier.getInventory().getItem(1)).isEmpty();
        });
    }
}
