package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.testframework.Test;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.enchantment.ECEnchantmentHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

public class InfuserGameTests {

    public static final String GROUP = "level.blocks.instruments.infuser";

    public static Collection<Test> collectTests() {
        return List.of(
                createTest(
                        "should_craftFireCrystal",
                        "Check if an infuser can craft a fire crystal",
                        helper -> should_craftCrystal(helper, ElementType.FIRE)),
                createTest(
                        "should_craftWaterCrystal",
                        "Check if an infuser can craft a water crystal",
                        helper -> should_craftCrystal(helper, ElementType.WATER)),
                createTest(
                        "should_craftEarthCrystal",
                        "Check if an infuser can craft an earth crystal",
                        helper -> should_craftCrystal(helper, ElementType.EARTH)),
                createTest(
                        "should_craftAirCrystal",
                        "Check if an infuser can craft an air crystal",
                        helper -> should_craftCrystal(helper, ElementType.AIR)),

                createTest(
                        "should_craftCrudeFireGem",
                        "Check if an infuser can craft a fire crude gem",
                        helper -> should_craftCrudeGem(helper, ElementType.FIRE)),
                createTest(
                        "should_craftCrudeWaterGem",
                        "Check if an infuser can craft a water crude gem",
                        helper -> should_craftCrudeGem(helper, ElementType.WATER)),
                createTest(
                        "should_craftCrudeEarthGem",
                        "Check if an infuser can craft an earth crude gem",
                        helper -> should_craftCrudeGem(helper, ElementType.EARTH)),
                createTest(
                        "should_craftCrudeAirGem",
                        "Check if an infuser can craft an air crude gem",
                        helper -> should_craftCrudeGem(helper, ElementType.AIR)),

                createTest(
                        "should_infuseDiamondSwordWithFire",
                        "Checks that a diamond sword can be infused with fire and has fire aspect",
                        helper -> should_infuseTool(helper, new ItemStack(Items.DIAMOND_SWORD), ElementType.FIRE, stack -> assertHasToolInfusionWithEnchantment(helper, stack, Enchantments.FIRE_ASPECT))),
                createTest(
                        "should_infuseDiamondSwordWithWater",
                        "Checks that a diamond sword can be infused with water and has looting",
                        helper -> should_infuseTool(helper, new ItemStack(Items.DIAMOND_SWORD), ElementType.WATER, stack -> assertHasToolInfusionWithEnchantment(helper, stack, Enchantments.LOOTING))),
                createTest(
                        "should_infuseDiamondSwordWithAir",
                        "Checks that a diamond sword can be infused with earth and has sharpness",
                        helper -> should_infuseTool(helper, new ItemStack(Items.DIAMOND_SWORD), ElementType.EARTH, stack -> assertHasToolInfusionWithEnchantment(helper, stack, Enchantments.SHARPNESS))),
                createTest(
                        "should_infuseDiamondPickaxeWithWater",
                        "Checks that a diamond pickaxe can be infused with water and has fortune",
                        helper -> should_infuseTool(helper, new ItemStack(Items.DIAMOND_PICKAXE), ElementType.WATER, stack -> assertHasToolInfusionWithEnchantment(helper, stack, Enchantments.FORTUNE))),
                createTest(
                        "should_infuseDiamondPickaxeWithEarth",
                        "Checks that a diamond pickaxe can be infused with earth and has unbreaking",
                        helper -> should_infuseTool(helper, new ItemStack(Items.DIAMOND_PICKAXE), ElementType.EARTH, stack -> assertHasToolInfusionWithEnchantment(helper, stack, Enchantments.UNBREAKING))),
                createTest(
                        "should_infuseDiamondPickaxeWithAir",
                        "Checks that a diamond pickaxe can be infused with air and has efficiency",
                        helper -> should_infuseTool(helper, new ItemStack(Items.DIAMOND_PICKAXE), ElementType.AIR, stack -> assertHasToolInfusionWithEnchantment(helper, stack, Enchantments.EFFICIENCY)))
        );
    }

    private static void should_craftCrystal(ECGameTestHelper helper, ElementType elementType) {
        helper.<InfuserBlockEntity>runInstrument(new ItemStack(ECItems.INERT_CRYSTAL.get()), elementType, infuser -> {
            assertThat(infuser.getItem())
                    .is(ElementalItemHelper.getCrystalForElement(elementType))
                    .hasCount(1);
        });
    }

    private static void should_craftCrudeGem(ECGameTestHelper helper, ElementType elementType) {
        helper.<InfuserBlockEntity>runInstrument(new ItemStack(Items.DIAMOND), elementType, infuser -> {
            assertThat(infuser.getItem())
                    .is(ElementalItemHelper.getCrudeGemForElement(elementType))
                    .hasCount(1);
        });
    }

    private static void should_infuseTool(ECGameTestHelper helper, ItemStack tool, ElementType elementType, Consumer<ItemStack> assertion) {
        helper.<InfuserBlockEntity>runInstrument(tool.copy(), elementType, infuser -> {
            assertThat(infuser.getItem())
                    .is(tool.getItem())
                    .hasCount(1)
                    .satisfies(s -> assertThat(ToolInfusionHelper.getInfusion(s)).satisfies(i -> assertThat(i.value().getElementType()).isEqualTo(elementType)))
                    .satisfies(assertion);
        });
    }

    private static void assertHasToolInfusionWithEnchantment(GameTestHelper helper, ItemStack stack, ResourceKey<@NotNull Enchantment> enchantment) {
        assertThat(stack.getEnchantmentLevel(ECEnchantmentHelper.getEnchantmentHolder(helper.getLevel().registryAccess(), enchantment))).isEqualTo(1);
    }

    private static Test createTest(String name, String description, Consumer<ECGameTestHelper> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, InstrumentTestTemplates.INFUSER_TEMPLATE_NAME, function);
    }
}
