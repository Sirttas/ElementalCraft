package sirttas.elementalcraft.interaction.jei.ingredient.subtype;

import it.unimi.dsi.fastutil.Pair;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.component.ECDataComponents;

public class ElementStorageSubtypeInterpreter implements ISubtypeInterpreter<@NotNull ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        var elementType = ingredient.getOrDefault(ECDataComponents.ELEMENT_TYPE, ElementType.getElementType(ingredient));
        var elementAmount = ingredient.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0D);

        return Pair.of(elementType, elementAmount);
    }
}
