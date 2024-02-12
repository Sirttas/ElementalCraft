package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sirttas.elementalcraft.tag.ECTags;

public class EnchantmentLiquefierHelper {

    private EnchantmentLiquefierHelper() {}

    public static boolean isValidInput(ItemStack stack) {
        return stack.isEnchanted() || stack.is(Items.ENCHANTED_BOOK);
    }

    public static boolean isValidOutput(ItemStack stack) {
        return stack.isEnchantable() || stack.is(ECTags.Items.ENCHANTMENT_HOLDER);
    }
}
