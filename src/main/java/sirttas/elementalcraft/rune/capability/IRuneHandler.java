package sirttas.elementalcraft.rune.capability;

import java.util.List;

import sirttas.elementalcraft.rune.Rune;

public interface IRuneHandler {

	void addRune(Rune rune);

	void removeRune(Rune rune);

	int getRuneCount();

	int getRuneCount(Rune rune);

	int getMaxRunes();

	List<Rune> getRunes();

	float getBonus(Rune.BonusType type);
}