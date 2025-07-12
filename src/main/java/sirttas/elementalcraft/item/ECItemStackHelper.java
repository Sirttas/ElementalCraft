package sirttas.elementalcraft.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ECItemStackHelper {

    private ECItemStackHelper() {}

    public static boolean canBeDamaged(@NotNull ItemStack stack) {
        return !stack.isEmpty() && stack.getMaxDamage() - stack.getDamageValue() > 1;
    }

    public static @NotNull ItemStack damageItem(@NotNull ItemStack stack) {
        return damageItem(stack, 1);
    }

    public static @NotNull ItemStack damageItem(@NotNull ItemStack stack, int damage) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        var result = stack.copy();
        var damageValue = result.getDamageValue() + damage; // TODO: enchantment ?

        result.setDamageValue(damageValue);
        if (damageValue >= result.getMaxDamage()) {
            result.shrink(1);
        }
        if (result.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return result;
    }
}
