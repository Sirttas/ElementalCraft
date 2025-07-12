package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.recipe.input.RuneBonusesRecipeInput;

import java.util.Map;

public record SimpleIOInstrumentRecipeInput(
        ItemStack input,
        ItemStack output,
        int itemLimit,
        RandomSource randomSource,
        ElementType elementType,
        int elementAmount,
        Map<Rune.BonusType, Float> bonuses
) implements IOInstrumentRecipeInput, RuneBonusesRecipeInput {

    public SimpleIOInstrumentRecipeInput(ItemStack input, ItemStack output, int itemLimit, RandomSource randomSource, ElementType elementType, int elementAmount, Map<Rune.BonusType, Float> bonuses) {
        this.input = input;
        this.output = output;
        this.itemLimit = itemLimit;
        this.randomSource = randomSource;
        this.elementType = elementType;
        this.elementAmount = elementAmount;
        this.bonuses = Map.copyOf(bonuses);
    }

    public SimpleIOInstrumentRecipeInput(ItemStack input, ItemStack output, RandomSource randomSource, ElementType elementType, int elementAmount, Map<Rune.BonusType, Float> bonuses) {
        this(input, output, 64, randomSource, elementType, elementAmount, bonuses);
    }

    @Override
    public int getElementAmount(@NotNull ElementType elementType) {
        return elementType == this.elementType ? elementAmount : 0;
    }

    @Override
    public RandomSource getRandomSource() {
        return randomSource;
    }

    @Override
    @NotNull
    public ItemStack getItem(int slot) {
        return switch (slot) {
            case 0 -> input;
            case 1 -> output;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }

    @NotNull
    @Override
    public ElementType getElementType() {
        return elementType;
    }

    @Override
    public float getRuneBonus(Rune.BonusType type) {
        return bonuses.getOrDefault(type, 0F);
    }

    @Override
    public int getItemLimit(int slot) {
        return itemLimit;
    }
}
