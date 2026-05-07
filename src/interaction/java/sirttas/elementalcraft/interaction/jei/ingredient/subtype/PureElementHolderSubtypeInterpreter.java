package sirttas.elementalcraft.interaction.jei.ingredient.subtype;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.element.ElementAmounts;

public class PureElementHolderSubtypeInterpreter implements ISubtypeInterpreter<@NotNull ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        var elementAmounts = ingredient.getOrDefault(ECDataComponents.ELEMENT_AMOUNTS, ElementAmounts.EMPTY);

        return elementAmounts.get(ElementType.FIRE) > 0 ? "full" : "empty";
    }
}
