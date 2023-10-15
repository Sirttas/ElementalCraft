package sirttas.elementalcraft.recipe.instrument.io.grinding;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public interface IGrindingRecipe extends IIOInstrumentRecipe<AbstractMillGrindstoneBlockEntity> {

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
	
	@Override
	default RandomSource getRand(AbstractMillGrindstoneBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
}
