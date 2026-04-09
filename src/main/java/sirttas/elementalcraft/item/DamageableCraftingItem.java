package sirttas.elementalcraft.item;

import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public interface DamageableCraftingItem extends IItemExtension {

    @Override
    default @Nullable ItemStackTemplate getCraftingRemainder(@NotNull ItemInstance stack) {
        if (!ECItemStackHelper.canBeDamaged(stack)) {
            return null;
        }
        return switch (stack) {
            case ItemStack itemStack -> itemStack.isEmpty() ? null : ItemStackTemplate.fromNonEmptyStack(itemStack);
            case ItemStackTemplate itemStackTemplate -> itemStackTemplate;
            default -> null;
        };
    }
}
