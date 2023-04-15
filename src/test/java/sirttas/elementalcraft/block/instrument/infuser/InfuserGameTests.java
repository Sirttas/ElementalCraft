package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
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
                ECGameTestHelper.createTestFunction("should_craftFireCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.FIRE)),
                ECGameTestHelper.createTestFunction("should_craftWaterCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.WATER)),
                ECGameTestHelper.createTestFunction("should_craftEarthCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.EARTH)),
                ECGameTestHelper.createTestFunction("should_craftAirCrystal", "elementalcraft:infusergametests.infuser", h -> should_craftCrystal(h, ElementType.AIR))
        );
    }

    public static void should_craftCrystal(GameTestHelper helper, ElementType elementType) {
        var infuser = (InfuserBlockEntity) helper.getBlockEntity(new BlockPos(0, 2, 0));
        var container = ElementStorageHelper.get(helper.getBlockEntity(new BlockPos(0, 1, 0))).orElse(null);

        assertThat(infuser).isNotNull();
        assertThat(container).isNotNull();

        helper.startSequence().thenExecute(() -> {
            infuser.getInventory().setItem(0, new ItemStack(ECItems.INERT_CRYSTAL.get()));
            container.insertElement(1000000, elementType, false);

            assertThat(infuser.isRecipeAvailable()).isTrue();
        }).thenExecuteAfter(2, () -> {
            assertThat(infuser.getItem())
                    .is(ElementalItemHelper.getCrystalForType(elementType))
                    .hasCount(1);
        }).thenSucceed();
    }
}
