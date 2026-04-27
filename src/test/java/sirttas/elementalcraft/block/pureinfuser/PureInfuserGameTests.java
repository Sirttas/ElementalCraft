package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.ECItems;

import java.util.Collection;
import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = PureInfuserGameTests.GROUP)
public class PureInfuserGameTests {

    public static final String GROUP = "level.blocks.pureInfuser";

    // elementalcraft:pureinfusergametests.pure_infuser
    @TestHolder(description = "Checks if the pure infuser can craft a pure crystal.")
    @GameTest(template = "elementalcraft:pureinfusergametests.pure_infuser")
    public static void should_craftPureCrystal(GameTestHelper helper) {
        var pureInfuser = helper.getBlockEntity(new BlockPos(3, 1, 3), PureInfuserBlockEntity.class);

        assertThat(pureInfuser).isNotNull();

        pureInfuser.refreshPedestals();

        var firePedestal = pureInfuser.getPedestal(ElementType.FIRE);
        var waterPedestal = pureInfuser.getPedestal(ElementType.WATER);
        var earthPedestal = pureInfuser.getPedestal(ElementType.EARTH);
        var airPedestal = pureInfuser.getPedestal(ElementType.AIR);

        assertThat(firePedestal).isNotNull();
        assertThat(waterPedestal).isNotNull();
        assertThat(earthPedestal).isNotNull();
        assertThat(airPedestal).isNotNull();

        var pureInfuserItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(pureInfuser, null));

        var firePedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(firePedestal, null));
        var waterPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(waterPedestal, null));
        var earthPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(earthPedestal, null));
        var airPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(airPedestal, null));

        var firePedestalElementStorage = ElementStorageGameTestHelper.get(firePedestal);
        var waterPedestalElementStorage = ElementStorageGameTestHelper.get(waterPedestal);
        var earthPedestalElementStorage = ElementStorageGameTestHelper.get(earthPedestal);
        var airPedestalElementStorage = ElementStorageGameTestHelper.get(airPedestal);

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
            firePedestalItemHandler.insertItem(0, new ItemStack(ECItems.FIRE_CRYSTAL), false);
            waterPedestalItemHandler.insertItem(0, new ItemStack(ECItems.WATER_CRYSTAL), false);
            earthPedestalItemHandler.insertItem(0, new ItemStack(ECItems.EARTH_CRYSTAL), false);
            airPedestalItemHandler.insertItem(0, new ItemStack(ECItems.AIR_CRYSTAL), false);

            firePedestalElementStorage.fill();
            waterPedestalElementStorage.fill();
            earthPedestalElementStorage.fill();
            airPedestalElementStorage.fill();

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();
        }).thenExecuteAfter(2, () -> {
            assertThat(pureInfuserItemHandler).contains(0, ECItems.PURE_CRYSTAL);
            assertThat(firePedestalItemHandler).isEmpty();
            assertThat(waterPedestalItemHandler).isEmpty();
            assertThat(earthPedestalItemHandler).isEmpty();
            assertThat(airPedestalItemHandler).isEmpty();
        }).thenSucceed();
    }

    public static Collection<Test> shouldNot_craftWhenAPedestalIsBroken() {
        var i = 0;

        return List.of(
                ECGameTestUtils.createTest(
                        GROUP,
                        "shouldNot_craftWhenAPedestalIsBroken_" + i++,
                        "Check that a pure infuser can't craft a pure crystal when a pedestal is broken.",
                        "elementalcraft:pureinfusergametests.pure_infuser",
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(0, 0, 3))),
                ECGameTestUtils.createTest(
                        GROUP,
                        "shouldNot_craftWhenAPedestalIsBroken_" + i++,
                        "Check that a pure infuser can't craft a pure crystal when a pedestal is broken.",
                        "elementalcraft:pureinfusergametests.pure_infuser",
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(3, 0, 0))),
                ECGameTestUtils.createTest(
                        GROUP,
                        "shouldNot_craftWhenAPedestalIsBroken_" + i++,
                        "Check that a pure infuser can't craft a pure crystal when a pedestal is broken.",
                        "elementalcraft:pureinfusergametests.pure_infuser",
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(6, 0, 3))),
                ECGameTestUtils.createTest(
                        GROUP,
                        "shouldNot_craftWhenAPedestalIsBroken_" + i++,
                        "Check that a pure infuser can't craft a pure crystal when a pedestal is broken.",
                        "elementalcraft:pureinfusergametests.pure_infuser",
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(3, 0, 6)))
        );
    }

    public static void shouldNot_craftWhenAPedestalIsBroken(GameTestHelper helper, BlockPos pos) {
        var pureInfuser = helper.getBlockEntity(new BlockPos(3, 0, 3), PureInfuserBlockEntity.class);

        assertThat(pureInfuser).isNotNull();

        pureInfuser.refreshPedestals();

        var firePedestal = pureInfuser.getPedestal(ElementType.FIRE);
        var waterPedestal = pureInfuser.getPedestal(ElementType.WATER);
        var earthPedestal = pureInfuser.getPedestal(ElementType.EARTH);
        var airPedestal = pureInfuser.getPedestal(ElementType.AIR);

        assertThat(firePedestal).isNotNull();
        assertThat(waterPedestal).isNotNull();
        assertThat(earthPedestal).isNotNull();
        assertThat(airPedestal).isNotNull();

        var pureInfuserItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(pureInfuser, null));

        var firePedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(firePedestal, null));
        var waterPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(waterPedestal, null));
        var earthPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(earthPedestal, null));
        var airPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(airPedestal, null));

        var firePedestalElementStorage = ElementStorageGameTestHelper.get(firePedestal);
        var waterPedestalElementStorage = ElementStorageGameTestHelper.get(waterPedestal);
        var earthPedestalElementStorage = ElementStorageGameTestHelper.get(earthPedestal);
        var airPedestalElementStorage = ElementStorageGameTestHelper.get(airPedestal);

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
            firePedestalItemHandler.insertItem(0, new ItemStack(ECItems.FIRE_CRYSTAL), false);
            waterPedestalItemHandler.insertItem(0, new ItemStack(ECItems.WATER_CRYSTAL), false);
            earthPedestalItemHandler.insertItem(0, new ItemStack(ECItems.EARTH_CRYSTAL), false);
            airPedestalItemHandler.insertItem(0, new ItemStack(ECItems.AIR_CRYSTAL), false);

            firePedestalElementStorage.fill();
            waterPedestalElementStorage.fill();
            earthPedestalElementStorage.fill();
            airPedestalElementStorage.fill();

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();

            helper.setBlock(pos, Blocks.AIR);

            assertThat(pureInfuser.isRecipeAvailable()).isFalse();
        }).thenExecuteAfter(2, () -> {
            assertThat(pureInfuserItemHandler).contains(0, Items.DIAMOND);
        }).thenSucceed();
    }

    // elementalcraft:pureinfusergametests.pure_infuser
    @TestHolder(description = "Checks if the pure infuser can craft a pure holder and keep the emements that were present in the original holders.")
    @GameTest(template = "elementalcraft:pureinfusergametests.pure_infuser")
    public static void should_craftPureHolderWithElement(ECGameTestHelper helper) {
        var pureInfuser = helper.getBlockEntity(new BlockPos(3, 0, 3), PureInfuserBlockEntity.class);

        assertThat(pureInfuser).isNotNull();

        pureInfuser.refreshPedestals();

        var firePedestal = pureInfuser.getPedestal(ElementType.FIRE);
        var waterPedestal = pureInfuser.getPedestal(ElementType.WATER);
        var earthPedestal = pureInfuser.getPedestal(ElementType.EARTH);
        var airPedestal = pureInfuser.getPedestal(ElementType.AIR);

        assertThat(firePedestal).isNotNull();
        assertThat(waterPedestal).isNotNull();
        assertThat(earthPedestal).isNotNull();
        assertThat(airPedestal).isNotNull();

        var pureInfuserItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(pureInfuser, null));

        var firePedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(firePedestal, null));
        var waterPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(waterPedestal, null));
        var earthPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(earthPedestal, null));
        var airPedestalItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(airPedestal, null));

        var firePedestalElementStorage = ElementStorageGameTestHelper.get(firePedestal);
        var waterPedestalElementStorage = ElementStorageGameTestHelper.get(waterPedestal);
        var earthPedestalElementStorage = ElementStorageGameTestHelper.get(earthPedestal);
        var airPedestalElementStorage = ElementStorageGameTestHelper.get(airPedestal);

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
            var fireHolder = new ItemStack(ECItems.FIRE_HOLDER);

            fireHolder.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM).fill();

            pureInfuserItemHandler.insertItem(0, new ItemStack(ECItems.PURE_HOLDER_CORE), false);
            firePedestalItemHandler.insertItem(0, fireHolder, false);
            waterPedestalItemHandler.insertItem(0, new ItemStack(ECItems.WATER_HOLDER), false);
            earthPedestalItemHandler.insertItem(0, new ItemStack(ECItems.EARTH_HOLDER), false);
            airPedestalItemHandler.insertItem(0, new ItemStack(ECItems.AIR_HOLDER), false);

            firePedestalElementStorage.fill();
            waterPedestalElementStorage.fill();
            earthPedestalElementStorage.fill();
            airPedestalElementStorage.fill();

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();
        }).thenExecuteAfter(2, () -> {
            assertThat(pureInfuserItemHandler).satisfies(0, s -> {
                assertThat(s)
                        .is(ECItems.PURE_HOLDER);
                assertThat(s.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM)).satisfies(es -> {
                    assertThat(es).isNotNull();
                    assertThat(es.getElementAmount(ElementType.FIRE)).isEqualTo(10000);
                    assertThat(es.getElementAmount(ElementType.WATER)).isEqualTo(0);
                    assertThat(es.getElementAmount(ElementType.EARTH)).isEqualTo(0);
                    assertThat(es.getElementAmount(ElementType.AIR)).isEqualTo(0);
                });
            });
            assertThat(firePedestalItemHandler).isEmpty();
            assertThat(waterPedestalItemHandler).isEmpty();
            assertThat(earthPedestalItemHandler).isEmpty();
            assertThat(airPedestalItemHandler).isEmpty();
        }).thenSucceed();
    }
}
