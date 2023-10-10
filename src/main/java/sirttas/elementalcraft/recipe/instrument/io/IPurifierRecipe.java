package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.recipe.instrument.ISingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IPurifierRecipe extends IIOInstrumentRecipe<PurifierBlockEntity>, ISingleElementInstrumentRecipe<PurifierBlockEntity> {

	double getLuckRatio();

	@Override
	default boolean matches(ItemStack stack, @Nonnull Level level) {
		return getIngredients().stream().allMatch(i -> i.test(stack)) && !this.getResultItem(level.registryAccess()).isEmpty() && IIOInstrumentRecipe.super.matches(stack, level);
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
	default RandomSource getRand(PurifierBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
	
	@Nonnull
	@Override
	default RecipeSerializer<?> getSerializer() {
		return null;
	}
}
