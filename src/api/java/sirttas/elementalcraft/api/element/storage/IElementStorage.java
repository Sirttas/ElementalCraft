package sirttas.elementalcraft.api.element.storage;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorageWrapper;

import javax.annotation.Nullable;

public interface IElementStorage {

	static IElementStorage empty() {
		return EmptyElementStorage.INSTANCE;
	}

	int getElementAmount(ElementType type);

	int getElementCapacity(ElementType type);

	int insertElement(int count, ElementType type, boolean simulate);

	int extractElement(int count, ElementType type, boolean simulate);

	default int transferTo(IElementStorage other, ElementType type, int count) {
		return transferTo(other, type, count, 1);
	}

	default int transferTo(IElementStorage other, ElementType type, float count, float multiplier) {
		if (count <= 0) {
			return 0;
		}

		int amount = Math.round(extractElement(Math.max(1, Math.round(count / multiplier)), type, true) * multiplier);
		
		amount = amount - other.insertElement(amount, type, true);
		extractElement(Math.round(amount / multiplier), type, false);
		other.insertElement(amount, type, false);
		return amount;
	}

	default boolean canPipeInsert(ElementType type, @Nullable Direction side) {
		return type != ElementType.NONE;
	}

	default boolean canPipeExtract(ElementType type, @Nullable Direction side) {
		return type != ElementType.NONE;
	}

	default boolean doesRenderGauge(Player player) {
		return false;
	}

	default boolean usableInInventory() {
		return false;
	}
	
	default boolean isEmpty() {
		return ElementType.ALL_VALID.stream().mapToInt(this::getElementAmount).allMatch(i -> i <= 0);
	}

	default void fill() {
		ElementType.ALL_VALID.forEach(this::fill);
	}

	default void fill(ElementType type) {
		if (type == ElementType.NONE) {
			return;
		}
		insertElement(getElementCapacity(type), type, false);
	}

	default ISingleElementStorage forElement(ElementType type) {
		if (type == ElementType.NONE) {
			return EmptyElementStorage.getSingle(type);
		}
		return new SingleElementStorageWrapper(type, this);
	}
}
