package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.testframework.Test;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.rune.Runes;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.withValue;

@ForEachTest(groups = PureInfuserGameTests.GROUP)
public class PureInfuserGameTests {

    public static final String GROUP = "level.blocks.pureInfuser";

    public static final String TEMPLATE_NAME = "elementalcraft:pure_infuser";

    @RegisterStructureTemplate(TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = StructureTemplateBuilder.lazy(7, 1, 7, builder -> builder
            .set(3, 0, 3, ECBlocks.PURE_INFUSER.get().defaultBlockState(), withValue(runeHandler(Runes.CREATIVE)))
            .set(0, 0, 3, ECBlocks.FIRE_PEDESTAL.get().defaultBlockState())
            .set(6, 0, 3, ECBlocks.AIR_PEDESTAL.get().defaultBlockState())
            .set(3, 0, 0, ECBlocks.WATER_PEDESTAL.get().defaultBlockState())
            .set(3, 0, 6, ECBlocks.EARTH_PEDESTAL.get().defaultBlockState()));

    @TestHolder(description = "Checks that the pure infuser can craft a pure crystal.")
    @GameTest(template = TEMPLATE_NAME)
    public static void should_craftPureCrystal(ECGameTestHelper helper) {
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

        var pureInfuserItemHandler = ECContainerHelper.getItemResourceHandler(pureInfuser, null);

        var firePedestalItemHandler = ECContainerHelper.getItemResourceHandler(firePedestal, null);
        var waterPedestalItemHandler = ECContainerHelper.getItemResourceHandler(waterPedestal, null);
        var earthPedestalItemHandler = ECContainerHelper.getItemResourceHandler(earthPedestal, null);
        var airPedestalItemHandler = ECContainerHelper.getItemResourceHandler(airPedestal, null);

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

        helper.startSequence().thenExecute(transaction -> {
            pureInfuserItemHandler.insert(0, ItemResource.of(Items.DIAMOND), 1, transaction);
            firePedestalItemHandler.insert(0, ItemResource.of(ECItems.FIRE_CRYSTAL), 1, transaction);
            waterPedestalItemHandler.insert(0, ItemResource.of(ECItems.WATER_CRYSTAL), 1, transaction);
            earthPedestalItemHandler.insert(0, ItemResource.of(ECItems.EARTH_CRYSTAL), 1, transaction);
            airPedestalItemHandler.insert(0, ItemResource.of(ECItems.AIR_CRYSTAL), 1, transaction);
        }).thenExecute(() -> {
            firePedestalElementStorage.fill();
            waterPedestalElementStorage.fill();
            earthPedestalElementStorage.fill();
            airPedestalElementStorage.fill();

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();
        }).thenExecuteAfter(2, () -> {
            assertThat(pureInfuserItemHandler).contains(0, ItemResource.of(ECItems.PURE_CRYSTAL));
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
                        TEMPLATE_NAME,
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(0, 0, 3))),
                ECGameTestUtils.createTest(
                        GROUP,
                        "shouldNot_craftWhenAPedestalIsBroken_" + i++,
                        "Check that a pure infuser can't craft a pure crystal when a pedestal is broken.",
                        TEMPLATE_NAME,
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(3, 0, 0))),
                ECGameTestUtils.createTest(
                        GROUP,
                        "shouldNot_craftWhenAPedestalIsBroken_" + i++,
                        "Check that a pure infuser can't craft a pure crystal when a pedestal is broken.",
                        TEMPLATE_NAME,
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(6, 0, 3))),
                ECGameTestUtils.createTest(
                        GROUP,
                        "shouldNot_craftWhenAPedestalIsBroken_" + i++,
                        "Check that a pure infuser can't craft a pure crystal when a pedestal is broken.",
                        TEMPLATE_NAME,
                        h -> shouldNot_craftWhenAPedestalIsBroken(h, new BlockPos(3, 0, 6)))
        );
    }

    public static void shouldNot_craftWhenAPedestalIsBroken(ECGameTestHelper helper, BlockPos pos) {
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

        var pureInfuserItemHandler = ECContainerHelper.getItemResourceHandler(pureInfuser, null);

        var firePedestalItemHandler = ECContainerHelper.getItemResourceHandler(firePedestal, null);
        var waterPedestalItemHandler = ECContainerHelper.getItemResourceHandler(waterPedestal, null);
        var earthPedestalItemHandler = ECContainerHelper.getItemResourceHandler(earthPedestal, null);
        var airPedestalItemHandler = ECContainerHelper.getItemResourceHandler(airPedestal, null);

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

        helper.startSequence().thenExecute(transaction -> {
            pureInfuserItemHandler.insert(0, ItemResource.of(Items.DIAMOND), 1, transaction);
            firePedestalItemHandler.insert(0, ItemResource.of(ECItems.FIRE_CRYSTAL), 1, transaction);
            waterPedestalItemHandler.insert(0, ItemResource.of(ECItems.WATER_CRYSTAL), 1, transaction);
            earthPedestalItemHandler.insert(0, ItemResource.of(ECItems.EARTH_CRYSTAL), 1, transaction);
            airPedestalItemHandler.insert(0, ItemResource.of(ECItems.AIR_CRYSTAL), 1, transaction);
        }).thenExecute(() -> {
            firePedestalElementStorage.fill();
            waterPedestalElementStorage.fill();
            earthPedestalElementStorage.fill();
            airPedestalElementStorage.fill();

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();

            helper.setBlock(pos, Blocks.AIR);

            assertThat(pureInfuser.isRecipeAvailable()).isFalse();
        }).thenExecuteAfter(2, () -> {
            assertThat(pureInfuserItemHandler).contains(0, ItemResource.of(Items.DIAMOND));
        }).thenSucceed();
    }

    @TestHolder(description = "Checks that the pure infuser can craft a pure holder and keep the elements that were present in the original holders.")
    @GameTest(template = TEMPLATE_NAME)
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

        var pureInfuserItemHandler = ECContainerHelper.getItemResourceHandler(pureInfuser, null);

        var firePedestalItemHandler = ECContainerHelper.getItemResourceHandler(firePedestal, null);
        var waterPedestalItemHandler = ECContainerHelper.getItemResourceHandler(waterPedestal, null);
        var earthPedestalItemHandler = ECContainerHelper.getItemResourceHandler(earthPedestal, null);
        var airPedestalItemHandler = ECContainerHelper.getItemResourceHandler(airPedestal, null);

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

        helper.startSequence().thenExecute(transaction -> {
            var fireHolder = new ItemStack(ECItems.FIRE_HOLDER);

            fireHolder.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM).fill();

            pureInfuserItemHandler.insert(0, ItemResource.of(ECItems.PURE_HOLDER_CORE), 1, transaction);
            firePedestalItemHandler.insert(0, ItemResource.of(fireHolder), 1, transaction);
            waterPedestalItemHandler.insert(0, ItemResource.of(ECItems.WATER_HOLDER), 1, transaction);
            earthPedestalItemHandler.insert(0, ItemResource.of(ECItems.EARTH_HOLDER), 1, transaction);
            airPedestalItemHandler.insert(0, ItemResource.of(ECItems.AIR_HOLDER), 1, transaction);
        }).thenExecute(() -> {
            firePedestalElementStorage.fill();
            waterPedestalElementStorage.fill();
            earthPedestalElementStorage.fill();
            airPedestalElementStorage.fill();

            assertThat(pureInfuser.isRecipeAvailable()).isTrue();
        }).thenExecuteAfter(2, () -> {
            assertThat(pureInfuserItemHandler).satisfies(0, rs -> assertThat(rs)
                    .isItem()
                    .is(ECItems.PURE_HOLDER)
                    .hasCapabilitySatisfying(ElementalCraftCapabilities.ElementStorages.ITEM, es -> {
                        assertThat(es).isNotNull();
                        assertThat(es.getElementAmount(ElementType.FIRE)).isEqualTo(10000);
                        assertThat(es.getElementAmount(ElementType.WATER)).isEqualTo(0);
                        assertThat(es.getElementAmount(ElementType.EARTH)).isEqualTo(0);
                        assertThat(es.getElementAmount(ElementType.AIR)).isEqualTo(0);
                }));
            assertThat(firePedestalItemHandler).isEmpty();
            assertThat(waterPedestalItemHandler).isEmpty();
            assertThat(earthPedestalItemHandler).isEmpty();
            assertThat(airPedestalItemHandler).isEmpty();
        }).thenSucceed();
    }
}
