package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import net.minecraft.Util;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;

import java.util.List;
import java.util.Map;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class EnchantmentLiquefierGameTests {

    // elementalcraft:enchantmentliquefiergametests.should_transferenchantment
    @GameTest(batch = InstrumentGameTestHelper.BATCH_NAME)
    public static void should_transferEnchantment(GameTestHelper helper) {
        InstrumentGameTestHelper.<EnchantmentLiquefierBlockEntity>runInstrument(helper, List.of(
                Util.make(() -> {
                    var book = new ItemStack(Items.ENCHANTED_BOOK);

                    EnchantmentHelper.setEnchantments(Map.of(Enchantments.SHARPNESS, 4), book);
                    return book;
                }),
                new ItemStack(Items.NETHERITE_SWORD)
        ), ElementType.WATER, enchantmentLiquefier -> {
            var inv = enchantmentLiquefier.getInventory();
            var input = inv.getItem(0);
            var output = inv.getItem(1);

            assertThat(output)
                    .is(Items.NETHERITE_SWORD)
                    .satisfies(o -> assertThat(EnchantmentHelper.getEnchantments(o)).hasEntrySatisfying(Enchantments.SHARPNESS, level -> assertThat(level).isBetween(3, 4)));
            assertThat(input)
                    .is(Items.BOOK)
                    .satisfies(i -> assertThat(EnchantmentHelper.getEnchantments(i)).isEmpty());
        });
    }
}
