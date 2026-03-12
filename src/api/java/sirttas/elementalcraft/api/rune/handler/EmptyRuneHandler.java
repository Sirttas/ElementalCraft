package sirttas.elementalcraft.api.rune.handler;

import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

import java.util.Collections;
import java.util.List;

public class EmptyRuneHandler implements IRuneHandler {

	public static final EmptyRuneHandler INSTANCE = new EmptyRuneHandler();

	private EmptyRuneHandler() {
	}

	@Override
	public void addRune(Holder<@NotNull Rune> rune) {
		// nothing to do
	}

	@Override
	public void removeRune(Holder<@NotNull Rune> rune) {
		// nothing to do
	}

	@Override
	public int getRuneCount() {
		return 0;
	}

	@Override
	public int getRuneCount(Holder<@NotNull Rune> rune) {
		return 0;
	}

	@Override
	public int getMaxRunes() {
		return 0;
	}

	@Override
	public List<Holder<@NotNull Rune>> getRunes() {
		return Collections.emptyList();
	}

	@Override
	public float getBonus(BonusType type) {
		return 0;
	}

}
