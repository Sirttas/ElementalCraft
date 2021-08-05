package sirttas.elementalcraft.api.rune.handler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableList;

import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

public class RuneHandler implements IRuneHandler {

	private final int max;
	private final List<Rune> runes;
	private final Map<BonusType, Float> bonuses;
	
	public RuneHandler(int max) {
		this.max = max;
		runes = new ArrayList<>(max);
		bonuses = new EnumMap<>(BonusType.class);
	}

	@Override
	public void addRune(Rune rune) {
		if (runes.size() < max) {
			runes.add(rune);
			rune.getBonuses().forEach((bonus, value) -> bonuses.put(bonus, getBonus(bonus) + value));
		}
	}

	@Override
	public void removeRune(Rune rune) {
		if (runes.contains(rune)) {
			runes.remove(rune);
			rune.getBonuses().forEach((bonus, value) -> bonuses.put(bonus, getBonus(bonus) - value));
		}
	}

	@Override
	public int getMaxRunes() {
		return max;
	}

	@Override
	public List<Rune> getRunes() {
		return runes.stream().filter(Objects::nonNull).collect(ImmutableList.toImmutableList());
	}

	@Override
	public float getBonus(BonusType type) {
		return bonuses.getOrDefault(type, 0F);
	}
}
