package sirttas.elementalcraft.item.elemental;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;

public class FireFuelItem extends ElementalItem {

    public static final String NAME = "elemental_firefuel";

    public FireFuelItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .durability(500), ElementType.FIRE);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamageValue() > 1;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        var result = stack.copy();

        if (result.hurt(1, RandomSource.create(), null)) {
            return ItemStack.EMPTY;
        }
        return result;
    }

    @Override
    public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 200;
    }
}
