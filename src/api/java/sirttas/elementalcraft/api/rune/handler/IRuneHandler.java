package sirttas.elementalcraft.api.rune.handler;

import net.minecraft.core.Holder;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

import java.util.List;
import java.util.stream.Collectors;

public interface IRuneHandler {

	void addRune(Holder<Rune> rune);

	void removeRune(Holder<Rune> rune);

	int getMaxRunes();

	default void clear() {
		getRunes().forEach(this::removeRune);
	}

	List<Holder<Rune>> getRunes();
	
	default int getRuneCount() {
		return getRunes().size();
	}

	default boolean isEmpty() {
		return getRunes().isEmpty();
	}

	default int getRuneCount(Holder<Rune> rune) {
		var runes = getRunes();
		
		return runes == null ? 0 : (int) runes.stream()
				.filter(r -> r.is(rune))
				.count();
	}

	default int getRuneCount(ResourceKey<Rune> rune) {
		var runes = getRunes();

		return runes == null ? 0 : (int) runes.stream()
				.filter(r -> r.is(rune))
				.count();
	}

	float getBonus(Rune.BonusType type);

	default float getTransferSpeed(float baseTransferSpeed) {
		return baseTransferSpeed * (getBonus(BonusType.SPEED) + 1);
	}

	default float getElementPreservation() {
		return getBonus(BonusType.ELEMENT_PRESERVATION) + 1;
	}

	default AABB getRange(Range range) {
		return range.scaleBox(getBonus(BonusType.RANGE) + 1);
	}

	default int handleElementTransfer(IElementStorage from, IElementStorage to, ElementType type, float amount) {
		return from.transferTo(to, type, getTransferSpeed(amount), getElementPreservation());
	}
	
	default int handleElementTransfer(ISingleElementStorage from, IElementStorage to, float amount) {
		return handleElementTransfer(from, to, from.getElementType(), amount);
	}
	
	static ListTag writeNBT(IRuneHandler handler) {
		return handler.getRunes().stream()
				.map(rune -> StringTag.valueOf(rune.getKey().identifier().toString()))
				.collect(Collectors.toCollection(ListTag::new));
	}

	static void readNBT(IRuneHandler handler, ListTag nbtTagList) {
		handler.clear();
		nbtTagList.forEach(nbt -> {
			String name = nbt.getAsString();

			handler.addRune(ElementalCraftApi.RUNE_MANAGER.getOrCreateHolder(Identifier.parse(name)));
        });
	}
}
