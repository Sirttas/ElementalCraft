package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

import javax.annotation.Nonnull;

public abstract class AbstractBindingRecipe extends AbstractInstrumentRecipe<MultipleItemsSingleElementRecipeInput> {

	public static final String NAME = "binding";
	
	protected AbstractBindingRecipe(CommonInfo commonInfo, ElementType type, int elementAmount) {
		super(commonInfo, type, elementAmount);
	}

	@Nonnull
	@Override
	public RecipeType<? extends @NotNull AbstractBindingRecipe> getType() {
		return ECRecipeTypes.BINDING.get();
	}
}
