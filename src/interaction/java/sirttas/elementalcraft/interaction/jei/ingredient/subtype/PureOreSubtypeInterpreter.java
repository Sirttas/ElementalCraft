package sirttas.elementalcraft.interaction.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.pureore.PureOre;

public class PureOreSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return PureOre.getId(ingredient);
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        var id = PureOre.getId(ingredient);

        return id == null ? "" : id.toString();
    }
}
