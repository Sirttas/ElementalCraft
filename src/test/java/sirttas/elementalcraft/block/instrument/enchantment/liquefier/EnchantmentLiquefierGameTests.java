package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.enchantment.ECEnchantmentHelper;

import java.util.List;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;


@ForEachTest(groups = EnchantmentLiquefierGameTests.GROUP)
public class EnchantmentLiquefierGameTests {

    public static final String GROUP = "level.blocks.instruments.enchantment_liquefier";


    @TestHolder
    @GameTest(template = InstrumentTestTemplates.ENCHANTMENT_LIQUEFIER_TEMPLATE_NAME)
    public static void should_transferEnchantment(ECGameTestHelper helper) {
        var sharpness = ECEnchantmentHelper.getEnchantmentHolder(helper.getLevel().registryAccess(), Enchantments.SHARPNESS);

        helper.<EnchantmentLiquefierBlockEntity>runInstrument(List.of(
                Util.make(() -> {
                    var book = new ItemStack(Items.ENCHANTED_BOOK);
                    var mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

                    mutable.set(sharpness, 4);
                    EnchantmentHelper.setEnchantments(book, mutable.toImmutable());
                    return book;
                }),
                new ItemStack(Items.NETHERITE_SWORD)
        ), ElementType.WATER, enchantmentLiquefier -> {
            var inv = enchantmentLiquefier.getInventory();
            var input = inv.getItem(0);
            var output = inv.getItem(1);

            assertThat(output)
                    .is(Items.NETHERITE_SWORD)
                    .hasDataComponentSatisfying(DataComponents.ENCHANTMENTS, e -> assertThat(e.getLevel(sharpness)).isBetween(3, 4));
            assertThat(input)
                    .is(Items.BOOK)
                    .doesNotHaveDataComponent(DataComponents.STORED_ENCHANTMENTS)
                    .hasDataComponentSatisfying(DataComponents.ENCHANTMENTS, e -> assertThat(e.isEmpty()).isTrue());
        });
    }
}
