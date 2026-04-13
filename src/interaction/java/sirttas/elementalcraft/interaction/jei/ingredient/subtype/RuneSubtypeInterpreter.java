package sirttas.elementalcraft.interaction.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.item.rune.RuneItem;

public class RuneSubtypeInterpreter implements ISubtypeInterpreter<@NotNull ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return RuneItem.getRune(ingredient);
    }
}
