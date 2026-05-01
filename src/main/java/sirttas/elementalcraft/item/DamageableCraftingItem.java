package sirttas.elementalcraft.item;

import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public interface DamageableCraftingItem extends IItemExtension {

    @Override
    default @Nullable ItemStackTemplate getCraftingRemainder(@NotNull ItemInstance itemInstance) {
        if (!ECItemStackHelper.canBeDamaged(itemInstance)) {
            return null;
        }
        var stack = switch (itemInstance) {
            case ItemStack itemStack -> itemStack.copy();
            case ItemStackTemplate itemStackTemplate -> itemStackTemplate.create();
            default -> ItemStack.EMPTY.copy();
        };
        stack.setDamageValue(stack.getDamageValue() + 1);

        if (stack.isEmpty() || stack.getDamageValue() > stack.getMaxDamage()) {
            return null;
        }
        return ItemStackTemplate.fromNonEmptyStack(stack);
    }
}
