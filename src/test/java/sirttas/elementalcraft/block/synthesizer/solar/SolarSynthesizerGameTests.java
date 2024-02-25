package sirttas.elementalcraft.block.synthesizer.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.item.ECItems;

import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class SolarSynthesizerGameTests {

    // elementalcraft:solarsynthesizergametests.solar_synthesizer
    @GameTest(template = "solar_synthesizer")
    public static void should_generateElementFromLens(GameTestHelper helper) {
        var ticks = new AtomicInteger(0);
        var inv = ((SolarSynthesizerBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0))).getInventory();
        var storage = InstrumentGameTestHelper.getContainer(helper, new BlockPos(0, 1, 0));

        helper.startSequence().thenExecute(() -> {
            inv.setItem(0, new ItemStack(ECItems.FIRE_LENS.get()));
        }).thenIdle(1).thenExecuteFor(20, ECGameTestHelper.fixAssertions(() -> {
            var t = ticks.incrementAndGet();

            assertThat(inv.getItem(0))
                    .is(ECItems.FIRE_LENS)
                    .hasDamage(t);
            assertThat(storage.getElementAmount(ElementType.FIRE))
                    .isEqualTo(t * 10);
        })).thenSucceed();
    }

}
