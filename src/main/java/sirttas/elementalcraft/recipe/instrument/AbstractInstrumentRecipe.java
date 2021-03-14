package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;

public abstract class AbstractInstrumentRecipe<T extends IInstrument> implements IInstrumentRecipe<T> {

	protected ElementType elementType;
	protected ResourceLocation id;

	protected AbstractInstrumentRecipe(ResourceLocation id, ElementType type) {
		this.elementType = type;
		this.id = id;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
}