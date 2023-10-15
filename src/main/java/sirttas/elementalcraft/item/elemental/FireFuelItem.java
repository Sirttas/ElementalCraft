package sirttas.elementalcraft.item.elemental;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
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
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        var result = stack.copy();

        result.hurt(1, RandomSource.create(), null);
        return result;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 200;
    }
}
