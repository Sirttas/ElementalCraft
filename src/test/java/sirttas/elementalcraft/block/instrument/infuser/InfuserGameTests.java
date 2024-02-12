package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class InfuserGameTests {

    private static final String TEMPLATE = "elementalcraft:infusergametests.infuser";

    @GameTestGenerator
    public static Collection<TestFunction> should_craftCrystal() {
        return List.of(
                InstrumentGameTestHelper.createTestFunction("should_craftFireCrystal", TEMPLATE, h -> should_craftCrystal(h, ElementType.FIRE)),
                InstrumentGameTestHelper.createTestFunction("should_craftWaterCrystal", TEMPLATE, h -> should_craftCrystal(h, ElementType.WATER)),
                InstrumentGameTestHelper.createTestFunction("should_craftEarthCrystal", TEMPLATE, h -> should_craftCrystal(h, ElementType.EARTH)),
                InstrumentGameTestHelper.createTestFunction("should_craftAirCrystal", TEMPLATE, h -> should_craftCrystal(h, ElementType.AIR))
        );
    }

    public static void should_craftCrystal(GameTestHelper helper, ElementType elementType) {
        InstrumentGameTestHelper.<InfuserBlockEntity>runInstrument(helper, new ItemStack(ECItems.INERT_CRYSTAL.get()), elementType, infuser -> {
            assertThat(infuser.getItem())
                    .is(ElementalItemHelper.getCrystalForType(elementType))
                    .hasCount(1);
        });
    }

    @GameTestGenerator
    public static Collection<TestFunction> should_infuseTool() {
        var index = new AtomicInteger(0);

        return List.of(
                InstrumentGameTestHelper.createTestFunction("should_infuseTool#" + index.getAndIncrement(), TEMPLATE, h -> should_infuseTool(h, new ItemStack(Items.DIAMOND_SWORD), ElementType.FIRE, s -> assertThat(s.getEnchantmentLevel(Enchantments.FIRE_ASPECT)).isEqualTo(1))),
                InstrumentGameTestHelper.createTestFunction("should_infuseTool#" + index.getAndIncrement(), TEMPLATE, h -> should_infuseTool(h, new ItemStack(Items.DIAMOND_SWORD), ElementType.WATER, s -> assertThat(s.getEnchantmentLevel(Enchantments.MOB_LOOTING)).isEqualTo(1))),
                InstrumentGameTestHelper.createTestFunction("should_infuseTool#" + index.getAndIncrement(), TEMPLATE, h -> should_infuseTool(h, new ItemStack(Items.DIAMOND_SWORD), ElementType.EARTH, s -> assertThat(s.getEnchantmentLevel(Enchantments.SHARPNESS)).isEqualTo(1))),

                InstrumentGameTestHelper.createTestFunction("should_infuseTool#" + index.getAndIncrement(), TEMPLATE, h -> should_infuseTool(h, new ItemStack(Items.DIAMOND_PICKAXE), ElementType.WATER, s -> assertThat(s.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE)).isEqualTo(1))),
                InstrumentGameTestHelper.createTestFunction("should_infuseTool#" + index.getAndIncrement(), TEMPLATE, h -> should_infuseTool(h, new ItemStack(Items.DIAMOND_PICKAXE), ElementType.EARTH, s -> assertThat(s.getEnchantmentLevel(Enchantments.UNBREAKING)).isEqualTo(1))),
                InstrumentGameTestHelper.createTestFunction("should_infuseTool#" + index.getAndIncrement(), TEMPLATE, h -> should_infuseTool(h, new ItemStack(Items.DIAMOND_PICKAXE), ElementType.AIR, s -> assertThat(s.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY)).isEqualTo(1)))
        );
    }

    public static void should_infuseTool(GameTestHelper helper, ItemStack tool, ElementType elementType, Consumer<ItemStack> assertion) {
        InstrumentGameTestHelper.<InfuserBlockEntity>runInstrument(helper, tool.copy(), elementType, infuser -> {
            assertThat(infuser.getItem())
                    .is(tool.getItem())
                    .hasCount(1)
                    .satisfies(s -> assertThat(ToolInfusionHelper.getInfusion(s)).satisfies(i -> assertThat(i.getElementType()).isEqualTo(elementType)))
                    .satisfies(assertion);
        });
    }
}
