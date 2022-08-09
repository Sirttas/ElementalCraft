package sirttas.elementalcraft.recipe.instrument.io.grinding;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IGrindingRecipe extends IIOInstrumentRecipe<AirMillGrindstoneBlockEntity> {

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
	default RandomSource getRand(AirMillGrindstoneBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
}
