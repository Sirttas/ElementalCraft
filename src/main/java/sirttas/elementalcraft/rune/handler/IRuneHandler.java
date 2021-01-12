package sirttas.elementalcraft.rune.handler;

import java.util.List;

import sirttas.elementalcraft.rune.Rune;
import sirttas.elementalcraft.rune.Rune.BonusType;

public interface IRuneHandler {

	void addRune(Rune rune);

	void removeRune(Rune rune);

	int getRuneCount();

	int getRuneCount(Rune rune);

	int getMaxRunes();

	List<Rune> getRunes();

	float getBonus(Rune.BonusType type);

	default float getTransferSpeed(float baseTransferSpeed) {
		return baseTransferSpeed * (getBonus(BonusType.SPEED) + 1);
	}

	default float getElementPreservation() {
		return getBonus(BonusType.ELEMENT_PRESERVATION) + 1;
	}

}