package sirttas.elementalcraft.api.rune.handler;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

import java.util.List;
import java.util.stream.Collectors;

@AutoRegisterCapability
public interface IRuneHandler {

	void addRune(Rune rune);

	void removeRune(Rune rune);

	int getMaxRunes();

	default void clear() {
		getRunes().forEach(this::removeRune);
	}

	List<Rune> getRunes();
	
	default int getRuneCount() {
		return getRunes().size();
	}

	default int getRuneCount(Rune rune) {
		var runes = getRunes();
		
		return runes == null ? 0 : (int) runes.stream().filter(rune::equals).count();
	}

	default int getRuneCount(ResourceKey<Rune> rune) {
		var runes = getRunes();

		return runes == null ? 0 : (int) runes.stream().filter(r -> r.is(rune)).count();
	}

	float getBonus(Rune.BonusType type);

	default float getTransferSpeed(float baseTransferSpeed) {
		return baseTransferSpeed * (getBonus(BonusType.SPEED) + 1);
	}

	default float getElementPreservation() {
		return getBonus(BonusType.ELEMENT_PRESERVATION) + 1;
	}
	
	default int handleElementTransfer(IElementStorage from, IElementStorage to, ElementType type, float amount) {
		return from.transferTo(to, type, getTransferSpeed(amount), getElementPreservation());
	}
	
	default int handleElementTransfer(ISingleElementStorage from, IElementStorage to, float amount) {
		return handleElementTransfer(from, to, from.getElementType(), amount);
	}
	
	static ListTag writeNBT(IRuneHandler handler) {
		return handler.getRunes().stream()
				.map(rune -> StringTag.valueOf(rune.getId().toString()))
				.collect(Collectors.toCollection(ListTag::new));
	}

	static void readNBT(IRuneHandler handler, ListTag nbtTagList) {
		handler.clear();
		nbtTagList.forEach(nbt -> {
			String name = nbt.getAsString();
			Rune rune = ElementalCraftApi.RUNE_MANAGER.get(new ResourceLocation(name));

			if (rune != null) {
				handler.addRune(rune);
			} else {
				ElementalCraftApi.LOGGER.warn("Rune not fount with id: {}", name);
			}
		});
	}
}
