package sirttas.elementalcraft.api.rune.handler;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

import javax.annotation.Nonnull;
import java.util.List;

public interface IRuneHandler {

	void addRune(Holder<@NotNull Rune> rune);

	void removeRune(Holder<@NotNull Rune> rune);

	int getMaxRunes();

	default void clear() {
		getRunes().forEach(this::removeRune);
	}

	List<Holder<@NotNull Rune>> getRunes();
	
	default int getRuneCount() {
		return getRunes().size();
	}

	default boolean isEmpty() {
		return getRunes().isEmpty();
	}

	default int getRuneCount(Holder<@NotNull Rune> rune) {
		var runes = getRunes();
		
		return runes == null ? 0 : (int) runes.stream()
				.filter(r -> r.is(rune))
				.count();
	}

	default int getRuneCount(ResourceKey<@NotNull Rune> rune) {
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
	
	default void save(@Nonnull ValueOutput valueOutput) {
        var list = valueOutput.list(ECNames.RUNE_HANDLER, Identifier.CODEC);

		for ( var rune : this.getRunes()) {
            list.add(rune.getKey().identifier());
        }
	}

    default void load(@Nonnull ValueInput valueInput) {
        this.clear();
        valueInput.list(ECNames.RUNE_HANDLER, Identifier.CODEC).ifPresent(list -> list.forEach(id -> this.addRune(ElementalCraftApi.RUNE_MANAGER.getOrCreateHolder(id))));
	}
}
