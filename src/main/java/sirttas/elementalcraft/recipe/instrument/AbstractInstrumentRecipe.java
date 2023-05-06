package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;

import javax.annotation.Nonnull;

public abstract class AbstractInstrumentRecipe<T extends IInstrument> implements ISingleElementInstrumentRecipe<T> {

	protected final ElementType elementType;
	protected final ResourceLocation id;

	protected AbstractInstrumentRecipe(ResourceLocation id, ElementType type) {
		this.elementType = type;
		this.id = id;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Nonnull
    @Override
	public ResourceLocation getId() {
		return id;
	}
}
