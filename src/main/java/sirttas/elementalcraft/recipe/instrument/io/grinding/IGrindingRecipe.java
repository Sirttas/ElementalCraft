package sirttas.elementalcraft.recipe.instrument.io.grinding;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.Random;

public interface IGrindingRecipe extends IIOInstrumentRecipe<AirMillBlockEntity> {

	String NAME = "grinding";
	RecipeType<IGrindingRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new RecipeType<IGrindingRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	
	@Override
	default ElementType getElementType() {
		return ElementType.AIR;
	}
	
	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return TYPE;
	}
	
	@Override
	default Random getRand(AirMillBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
}
