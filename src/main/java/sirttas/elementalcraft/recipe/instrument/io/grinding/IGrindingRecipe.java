package sirttas.elementalcraft.recipe.instrument.io.grinding;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IGrindingRecipe extends IIOInstrumentRecipe<AirMillBlockEntity> {

	String NAME = "grinding";
	
	@Override
	default ElementType getElementType() {
		return ElementType.AIR;
	}
	
	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return ECRecipeTypes.AIR_MILL_GRINDING.get();
	}
	
	@Override
	default RandomSource getRand(AirMillBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
}
