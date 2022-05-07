package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public interface IPurifierRecipe extends IIOInstrumentRecipe<PurifierBlockEntity> {

	@Override
	default int getElementAmount() {
		return ECConfig.COMMON.purifierBaseCost.get();
	}

	double getLuckRatio();

	@Override
	default boolean matches(ItemStack stack) {
		return getIngredients().stream().allMatch(i -> i.test(stack)) && !this.getResultItem().isEmpty() && IIOInstrumentRecipe.super.matches(stack);
	}

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return null;
	}

	@Override
	default ElementType getElementType() {
		return ElementType.EARTH;
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	default int getLuck(PurifierBlockEntity instrument) {
		return (int) Math.round(instrument.getRuneHandler().getBonus(BonusType.LUCK) * getLuckRatio());
	}

	@Override
	default Random getRand(PurifierBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
	
	@Nonnull
	@Override
	default RecipeSerializer<?> getSerializer() {
		return null;
	}
}
