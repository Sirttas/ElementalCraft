package sirttas.elementalcraft.recipe.instrument.io.grinding;

import java.util.Random;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.instrument.mill.AirMillBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

public interface IGrindingRecipe extends IIOInstrumentRecipe<AirMillBlockEntity> {

	public static final String NAME = "grinding";
	public static final IRecipeType<IGrindingRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<IGrindingRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	
	@Override
	default ElementType getElementType() {
		return ElementType.AIR;
	}
	
	@Override
	default IRecipeType<?> getType() {
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
