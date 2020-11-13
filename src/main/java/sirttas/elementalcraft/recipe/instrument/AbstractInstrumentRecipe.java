package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;

public abstract class AbstractInstrumentRecipe<T extends IInstrument> implements IInstrumentRecipe<T> {

	protected ElementType elementType;
	protected ResourceLocation id;

	public AbstractInstrumentRecipe(ResourceLocation id, ElementType type) {
		this.elementType = type;
		this.id = id;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public void process(T instrument) {
		instrument.getInventory().setInventorySlotContents(0, this.getCraftingResult(instrument));
	}

}