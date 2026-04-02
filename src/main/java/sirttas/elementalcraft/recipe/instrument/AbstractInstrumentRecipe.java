package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;

public abstract class AbstractInstrumentRecipe<I extends RecipeInput> implements SingleElementInstrumentRecipe<I> {

    protected final Recipe.CommonInfo commonInfo;
    protected final ElementType elementType;
    protected final int elementAmount;

	protected AbstractInstrumentRecipe(CommonInfo commonInfo, ElementType type, int elementAmount) {
        this.commonInfo = commonInfo;
        this.elementType = type;
        this.elementAmount = elementAmount;
    }

    @Override
    public boolean showNotification() {
        return commonInfo.showNotification();
    }

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}

    @Override
    public int getElementAmount() {
        return elementAmount;
    }
}
