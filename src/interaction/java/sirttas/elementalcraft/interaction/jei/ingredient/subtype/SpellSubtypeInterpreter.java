package sirttas.elementalcraft.interaction.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.spell.SpellHelper;

public class SpellSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return SpellHelper.getSpell(ingredient);
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        var key = SpellHelper.getSpell(ingredient).getKey();

        return key == null ? "" : key.identifier().toString();
    }
}
