package sirttas.elementalcraft.recipe.instrument.io.grinding;

import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import javax.annotation.Nonnull;
import java.util.List;

public interface IGrindingRecipe extends IOInstrumentRecipe<SimpleIOInstrumentRecipeInput> {

	String NAME = "grinding";
	

	@Override
	default List<ElementType> getValidElementTypes() {
		return List.of(ElementType.WATER, ElementType.AIR);
	}
	
	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return ECRecipeTypes.GRINDING.get();
	}
}
