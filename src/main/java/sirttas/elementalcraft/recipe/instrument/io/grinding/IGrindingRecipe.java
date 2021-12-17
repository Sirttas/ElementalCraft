package sirttas.elementalcraft.recipe.instrument.io.grinding;

import java.util.Random;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;

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
	
	@Override
	default int getLuck(AirMillBlockEntity instrument) {
		return (int) Math.round(instrument.getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.airMillLuckRatio.get());
	}
}
