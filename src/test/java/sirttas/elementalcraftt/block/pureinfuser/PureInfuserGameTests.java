package sirttas.elementalcraftt.block.pureinfuser;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class PureInfuserGameTests {

    // elementalcraft:pureinfusergametests.pure_infuser
    @GameTest(template = "pure_infuser")
    public static void should_craftPureCrystal(GameTestHelper helper) {
        var pureInfuser = (PureInfuserBlockEntity) helper.getBlockEntity(new BlockPos(3, 1, 3));

        assertThat(pureInfuser).isNotNull();

        var firePedestal = pureInfuser.getPedestal(ElementType.FIRE);
        var waterPedestal = pureInfuser.getPedestal(ElementType.WATER);
        var earthPedestal = pureInfuser.getPedestal(ElementType.EARTH);
        var airPedestal = pureInfuser.getPedestal(ElementType.AIR);

        assertThat(firePedestal).isNotNull();
        assertThat(waterPedestal).isNotNull();
        assertThat(earthPedestal).isNotNull();
        assertThat(airPedestal).isNotNull();

        var pureInfuserItemHandler = ECContainerHelper.getItemHandler(pureInfuser, null);

        var firePedestalItemHandler = ECContainerHelper.getItemHandler(firePedestal, null);
        var waterPedestalItemHandler = ECContainerHelper.getItemHandler(waterPedestal, null);
        var earthPedestalItemHandler = ECContainerHelper.getItemHandler(earthPedestal, null);
        var airPedestalItemHandler = ECContainerHelper.getItemHandler(airPedestal, null);

        var firePedestalElementStorage = ElementStorageHelper.get(firePedestal).orElse(null);
        var waterPedestalElementStorage = ElementStorageHelper.get(waterPedestal).orElse(null);
        var earthPedestalElementStorage = ElementStorageHelper.get(earthPedestal).orElse(null);
        var airPedestalElementStorage = ElementStorageHelper.get(airPedestal).orElse(null);

        assertThat(pureInfuserItemHandler).isNotNull();
        assertThat(firePedestalItemHandler).isNotNull();
        assertThat(waterPedestalItemHandler).isNotNull();
        assertThat(earthPedestalItemHandler).isNotNull();
        assertThat(airPedestalItemHandler).isNotNull();
        assertThat(firePedestalElementStorage).isNotNull();
        assertThat(waterPedestalElementStorage).isNotNull();
        assertThat(earthPedestalElementStorage).isNotNull();
        assertThat(airPedestalElementStorage).isNotNull();

        helper.startSequence().thenExecute(() -> {
            pureInfuserItemHandler.insertItem(0, new ItemStack(Items.DIAMOND), false);
            firePedestalItemHandler.insertItem(0, new ItemStack(ECItems.FIRE_CRYSTAL.get()), false);
            waterPedestalItemHandler.insertItem(0, new ItemStack(ECItems.WATER_CRYSTAL.get()), false);
            earthPedestalItemHandler.insertItem(0, new ItemStack(ECItems.EARTH_CRYSTAL.get()), false);
            airPedestalItemHandler.insertItem(0, new ItemStack(ECItems.AIR_CRYSTAL.get()), false);

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();
        }).thenExecuteFor(10, () -> {
            firePedestalElementStorage.insertElement(1000000, ElementType.FIRE, false);
            waterPedestalElementStorage.insertElement(1000000, ElementType.WATER, false);
            earthPedestalElementStorage.insertElement(1000000, ElementType.EARTH, false);
            airPedestalElementStorage.insertElement(1000000, ElementType.AIR, false);
        }).thenExecuteAfter(1, () -> {
            assertThat(pureInfuserItemHandler).contains(0, ECItems.PURE_CRYSTAL);
            assertThat(firePedestalItemHandler).isEmpty();
            assertThat(waterPedestalItemHandler).isEmpty();
            assertThat(earthPedestalItemHandler).isEmpty();
            assertThat(airPedestalItemHandler).isEmpty();
        }).thenSucceed();
    }
    @GameTestGenerator
    public static Collection<TestFunction> shouldNot_craftWhenAPedestalIsBroken() {
        var index = new AtomicInteger(0);

        return List.of(
                ECGameTestHelper.createTestFunction("shouldNot_craftWhenAPedestalIsBroken#" + index.getAndIncrement(), "elementalcraft:pureinfusergametests.pure_infuser", h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(0, 1, 3))),
                ECGameTestHelper.createTestFunction("shouldNot_craftWhenAPedestalIsBroken#" + index.getAndIncrement(), "elementalcraft:pureinfusergametests.pure_infuser", h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(3, 1, 0))),
                ECGameTestHelper.createTestFunction("shouldNot_craftWhenAPedestalIsBroken#" + index.getAndIncrement(), "elementalcraft:pureinfusergametests.pure_infuser", h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(6, 1, 3))),
                ECGameTestHelper.createTestFunction("shouldNot_craftWhenAPedestalIsBroken#" + index.getAndIncrement(), "elementalcraft:pureinfusergametests.pure_infuser", h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(3, 1, 6)))
        );
    }

    public static void shouldNot_craftWhenAPedestalIsBroken(GameTestHelper helper, BlockPos pos) {
        var pureInfuser = (PureInfuserBlockEntity) helper.getBlockEntity(new BlockPos(3, 1, 3));

        assertThat(pureInfuser).isNotNull();

        var firePedestal = pureInfuser.getPedestal(ElementType.FIRE);
        var waterPedestal = pureInfuser.getPedestal(ElementType.WATER);
        var earthPedestal = pureInfuser.getPedestal(ElementType.EARTH);
        var airPedestal = pureInfuser.getPedestal(ElementType.AIR);

        assertThat(firePedestal).isNotNull();
        assertThat(waterPedestal).isNotNull();
        assertThat(earthPedestal).isNotNull();
        assertThat(airPedestal).isNotNull();

        var pureInfuserItemHandler = ECContainerHelper.getItemHandler(pureInfuser, null);

        var firePedestalItemHandler = ECContainerHelper.getItemHandler(firePedestal, null);
        var waterPedestalItemHandler = ECContainerHelper.getItemHandler(waterPedestal, null);
        var earthPedestalItemHandler = ECContainerHelper.getItemHandler(earthPedestal, null);
        var airPedestalItemHandler = ECContainerHelper.getItemHandler(airPedestal, null);

        var firePedestalElementStorage = ElementStorageHelper.get(firePedestal).orElse(null);
        var waterPedestalElementStorage = ElementStorageHelper.get(waterPedestal).orElse(null);
        var earthPedestalElementStorage = ElementStorageHelper.get(earthPedestal).orElse(null);
        var airPedestalElementStorage = ElementStorageHelper.get(airPedestal).orElse(null);

        assertThat(pureInfuserItemHandler).isNotNull();
        assertThat(firePedestalItemHandler).isNotNull();
        assertThat(waterPedestalItemHandler).isNotNull();
        assertThat(earthPedestalItemHandler).isNotNull();
        assertThat(airPedestalItemHandler).isNotNull();
        assertThat(firePedestalElementStorage).isNotNull();
        assertThat(waterPedestalElementStorage).isNotNull();
        assertThat(earthPedestalElementStorage).isNotNull();
        assertThat(airPedestalElementStorage).isNotNull();

        helper.startSequence().thenExecute(() -> {
            pureInfuserItemHandler.insertItem(0, new ItemStack(Items.DIAMOND), false);
            firePedestalItemHandler.insertItem(0, new ItemStack(ECItems.FIRE_CRYSTAL.get()), false);
            waterPedestalItemHandler.insertItem(0, new ItemStack(ECItems.WATER_CRYSTAL.get()), false);
            earthPedestalItemHandler.insertItem(0, new ItemStack(ECItems.EARTH_CRYSTAL.get()), false);
            airPedestalItemHandler.insertItem(0, new ItemStack(ECItems.AIR_CRYSTAL.get()), false);

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();
        }).thenExecuteFor(2, () -> {
            firePedestalElementStorage.insertElement(1000000, ElementType.FIRE, false);
            waterPedestalElementStorage.insertElement(1000000, ElementType.WATER, false);
            earthPedestalElementStorage.insertElement(1000000, ElementType.EARTH, false);
            airPedestalElementStorage.insertElement(1000000, ElementType.AIR, false);
        }).thenExecuteAfter(1, () -> {
            helper.setBlock(0, 1, 3, Blocks.AIR);

            assertThat(pureInfuser.isRecipeAvailable()).isFalse();
        }).thenSucceed();
    }
}
