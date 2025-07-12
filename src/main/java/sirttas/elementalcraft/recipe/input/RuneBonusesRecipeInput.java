package sirttas.elementalcraft.recipe.input;

import sirttas.elementalcraft.api.rune.Rune;

public interface RuneBonusesRecipeInput extends ECRecipeInput {

    float getRuneBonus(Rune.BonusType type);
}
