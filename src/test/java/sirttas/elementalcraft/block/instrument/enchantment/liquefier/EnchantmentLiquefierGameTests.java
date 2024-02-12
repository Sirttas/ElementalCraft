package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.InstrumentGameTestHelper;

import java.util.Map;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class EnchantmentLiquefierGameTests {

    // elementalcraft:enchantmentliquefiergametests.should_transferenchantment
    @GameTest(batch = InstrumentGameTestHelper.BATCH_NAME)
    public static void should_transferEnchantment(GameTestHelper helper) {
        EnchantmentLiquefierBlockEntity enchantmentLiquefier = InstrumentGameTestHelper.getInstrument(helper, new BlockPos(0, 2, 0));

        helper.startSequence().thenExecute(() -> {
            enchantmentLiquefier.getInventory().setItem(0, Util.make(() -> {
                var book = new ItemStack(Items.ENCHANTED_BOOK);

                EnchantmentHelper.setEnchantments(Map.of(Enchantments.SHARPNESS, 4), book);
                return book;
            }));
            enchantmentLiquefier.getInventory().setItem(1, new ItemStack(Items.NETHERITE_SWORD));
        }).thenExecuteAfter(2, ECGameTestHelper.fixAssertions(() -> {
            ItemStack input = enchantmentLiquefier.getInventory().getItem(0);
            ItemStack output = enchantmentLiquefier.getInventory().getItem(1);

            assertThat(output)
                    .is(Items.NETHERITE_SWORD)
                    .satisfies(o -> assertThat(EnchantmentHelper.getEnchantments(o)).hasEntrySatisfying(Enchantments.SHARPNESS, level -> assertThat(level).isBetween(3, 4)));
            assertThat(input)
                    .is(Items.BOOK)
                    .satisfies(i -> assertThat(EnchantmentHelper.getEnchantments(i)).isEmpty());
        })).thenSucceed();

    }
}
