package sirttas.elementalcraft.api.rune.handler;

import net.minecraft.core.Holder;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RuneHandler implements IRuneHandler {

	private final int max;
	private final List<Holder<Rune>> runes;
	private final Map<BonusType, Float> bonuses;

	private final Runnable onChange;
	
	public RuneHandler(int max) {
		this(max, null);
	}

	public RuneHandler(int max, Runnable onChange) {
		this.max = max;
		runes = new ArrayList<>(max);
		bonuses = new EnumMap<>(BonusType.class);
		this.onChange = onChange;
	}

	@Override
	public void addRune(Holder<Rune> rune) {
		if (runes.size() < max) {
			runes.add(rune);
			rune.value().getBonuses().forEach((bonus, value) -> bonuses.put(bonus, getBonus(bonus) + value));
			if (onChange != null) {
				onChange.run();
			}
		}
	}

	@Override
	public void removeRune(Holder<Rune> rune) {
		if (runes.contains(rune)) {
			runes.remove(rune);
			rune.value().getBonuses().forEach((bonus, value) -> bonuses.put(bonus, getBonus(bonus) - value));
			if (onChange != null) {
				onChange.run();
			}
		}
	}

	@Override
	public int getMaxRunes() {
		return max;
	}

	@Override
	public List<Holder<Rune>> getRunes() {
		return runes.stream().filter(Objects::nonNull).toList();
	}

	@Override
	public float getBonus(BonusType type) {
		return bonuses.getOrDefault(type, 0F);
	}

	@Override
	public void clear() {
		runes.clear();
		bonuses.clear();
	}

	public Map<BonusType, Float> getBonuses() {
		return Map.copyOf(this.bonuses);
	}
}
