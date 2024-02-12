package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.recipe.IRuntimeContainerBlockEntityRecipe;
import sirttas.elementalcraft.recipe.instrument.ISingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IPurifierRecipe extends IIOInstrumentRecipe<PurifierBlockEntity>, ISingleElementInstrumentRecipe<PurifierBlockEntity>, IRuntimeContainerBlockEntityRecipe<PurifierBlockEntity> {

	double getLuckRatio();

	@Override
	default boolean matches(ItemStack stack, @Nonnull Level level) {
		return getIngredients().stream().allMatch(i -> i.test(stack)) && !this.getResultItem(level.registryAccess()).isEmpty() && IIOInstrumentRecipe.super.matches(stack, level);
	}

	@Override
	default ElementType getElementType() {
		return ElementType.EARTH;
	}

    @Override
	default int getLuck(PurifierBlockEntity instrument) {
		return (int) Math.round(instrument.getRuneHandler().getBonus(BonusType.LUCK) * getLuckRatio());
	}

	@Override
	default RandomSource getRand(PurifierBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}
}
