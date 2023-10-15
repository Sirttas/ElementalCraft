package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;

import java.util.Collection;
import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class InfuserGameTests {

    @GameTestGenerator
    public static Collection<TestFunction> should_craftCrystal() {
        return List.of(
                InstrumentGameTestHelper.createTestFunction("should_craftFireCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.FIRE)),
                InstrumentGameTestHelper.createTestFunction("should_craftWaterCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.WATER)),
                InstrumentGameTestHelper.createTestFunction("should_craftEarthCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.EARTH)),
                InstrumentGameTestHelper.createTestFunction("should_craftAirCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.AIR))
        );
    }

    public static void should_craftCrystal(GameTestHelper helper, ElementType elementType) {
        InstrumentGameTestHelper.<InfuserBlockEntity>runInstrument(helper, new ItemStack(ECItems.INERT_CRYSTAL.get()), elementType, infuser -> {
            assertThat(infuser.getItem())
                    .is(ElementalItemHelper.getCrystalForType(elementType))
                    .hasCount(1);
        });
    }
}
