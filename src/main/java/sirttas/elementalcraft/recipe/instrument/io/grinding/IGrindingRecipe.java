package sirttas.elementalcraft.recipe.instrument.io.grinding;

import java.util.Random;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.mill.TileAirMill;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;
import sirttas.elementalcraft.rune.Rune.BonusType;

public interface IGrindingRecipe extends IIOInstrumentRecipe<TileAirMill> {

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
	default Random getRand(TileAirMill instrument) {
		return instrument.getWorld().getRandom();
	}
	
	@Override
	default int getLuck(TileAirMill instrument) {
		return (int) Math.round(instrument.getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.airMillLuckRatio.get());
	}
}
