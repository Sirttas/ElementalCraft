package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

public abstract class AbstractInstrumentRecipe<I extends RecipeInput> implements ISingleElementInstrumentRecipe<I> {

	protected final ElementType elementType;

	protected AbstractInstrumentRecipe(ElementType type) {
		this.elementType = type;
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}
}
