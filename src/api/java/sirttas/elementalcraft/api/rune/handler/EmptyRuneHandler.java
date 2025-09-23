package sirttas.elementalcraft.api.rune.handler;

import net.minecraft.core.Holder;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

import java.util.Collections;
import java.util.List;

public class EmptyRuneHandler implements IRuneHandler {

	public static final EmptyRuneHandler INSTANCE = new EmptyRuneHandler();

	private EmptyRuneHandler() {
	}

	@Override
	public void addRune(Holder<Rune> rune) {
		// nothing to do
	}

	@Override
	public void removeRune(Holder<Rune> rune) {
		// nothing to do
	}

	@Override
	public int getRuneCount() {
		return 0;
	}

	@Override
	public int getRuneCount(Holder<Rune> rune) {
		return 0;
	}

	@Override
	public int getMaxRunes() {
		return 0;
	}

	@Override
	public List<Holder<Rune>> getRunes() {
		return Collections.emptyList();
	}

	@Override
	public float getBonus(BonusType type) {
		return 0;
	}

}
