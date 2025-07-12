package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.world.item.ItemStack;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class CrystallizerGameTests {

    public static final String GROUP = "level.blocks.instruments.crystallizer";

    private static final String TEMPLATE = "elementalcraft:crystallizergametests.crystallizer"; // TODO move to template generation

    public static Collection<Test> collectTests() {
        return List.of(
                createTest(
                        "should_craftFineFireGem",
                        "Check if a crystallizer can craft a fine fire gem",
                        h -> should_craftFineGem(h, ElementType.FIRE)),
                createTest(
                        "should_craftFineWaterGem",
                        "Check if a crystallizer can craft a fine water gem",
                        h -> should_craftFineGem(h, ElementType.WATER)),
                createTest(
                        "should_craftFineEarthGem",
                        "Check if a crystallizer can craft a fine earth gem",
                        h -> should_craftFineGem(h, ElementType.EARTH)),
                createTest(
                        "should_craftFineAirGem",
                        "Check if a crystallizer can craft a fine air gem",
                        h -> should_craftFineGem(h, ElementType.AIR)),
                createTest(
                        "should_craftPristineFireGem",
                        "Check if a crystallizer can craft a pristine rire gem",
                        h -> should_craftPristineGem(h, ElementType.FIRE)),
                createTest(
                        "should_craftPristineWaterGem",
                        "Check if a crystallizer can craft a pristine water gem",
                        h -> should_craftPristineGem(h, ElementType.WATER)),
                createTest(
                        "should_craftPristineEarthGem",
                        "Check if a crystallizer can craft a pristine earth gem",
                        h -> should_craftPristineGem(h, ElementType.EARTH)),
                createTest(
                        "should_craftPristineAirGem",
                        "Check if a crystallizer can craft a pristine air gem",
                        h -> should_craftPristineGem(h, ElementType.AIR))

        );
    }

    private static void should_craftFineGem(ECGameTestHelper helper, ElementType elementType) {
        helper.<CrystallizerBlockEntity>runInstrument(List.of(
                new ItemStack(ElementalItemHelper.getCrudeGemForElement(elementType)),
                new ItemStack(ElementalItemHelper.getCrystalForElement(elementType))
        ), elementType, crystallizer -> {
            var inv = crystallizer.getInventory();

            assertThat(inv.getItem(0))
                    .is(ElementalItemHelper.getFineGemForElement(elementType))
                    .hasCount(1);
            assertThat(inv.getItem(1))
                    .isEmpty();
        });
    }

    private static void should_craftPristineGem(ECGameTestHelper helper, ElementType elementType) {
        helper.<CrystallizerBlockEntity>runInstrument(List.of(
                new ItemStack(ElementalItemHelper.getFineGemForElement(elementType)),
                new ItemStack(ECItems.PRISTINE_SHARD)
        ), elementType, crystallizer -> {
            var inv = crystallizer.getInventory();

            assertThat(inv.getItem(0))
                    .is(ElementalItemHelper.getPristineGemForElement(elementType))
                    .hasCount(1);
            assertThat(inv.getItem(1))
                    .isEmpty();
        });
    }

    private static Test createTest(String name, String description, Consumer<ECGameTestHelper> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, TEMPLATE, function);
    }
}
