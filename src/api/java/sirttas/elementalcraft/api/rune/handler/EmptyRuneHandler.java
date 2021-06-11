package sirttas.elementalcraft.api.rune.handler;

import java.util.Collections;
import java.util.List;

import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

public class EmptyRuneHandler implements IRuneHandler {

	public static final EmptyRuneHandler INSTANCE = new EmptyRuneHandler();

	private EmptyRuneHandler() {
	}

	@Override
	public void addRune(Rune rune) {
		// nothing to do
	}

	@Override
	public void removeRune(Rune rune) {
		// nothing to do
	}

	@Override
	public int getRuneCount() {
		return 0;
	}

	@Override
	public int getRuneCount(Rune rune) {
		return 0;
	}

	@Override
	public int getMaxRunes() {
		return 0;
	}

	@Override
	public List<Rune> getRunes() {
		return Collections.emptyList();
	}

	@Override
	public float getBonus(BonusType type) {
		return 0;
	}

}
