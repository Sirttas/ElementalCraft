package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

import javax.annotation.Nonnull;

public abstract class AbstractBindingRecipe extends AbstractInstrumentRecipe<MultipleItemsSingleElementRecipeInput> {

	public static final String NAME = "binding";
	
	protected AbstractBindingRecipe(ElementType type) {
		super(type);
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.BINDING.get();
	}
}
