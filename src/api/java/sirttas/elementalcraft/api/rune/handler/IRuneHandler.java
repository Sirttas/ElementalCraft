package sirttas.elementalcraft.api.rune.handler;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;

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
	
	public static ListNBT writeNBT(IRuneHandler handler) {
		return handler.getRunes().stream()
				.map(rune -> StringNBT.valueOf(rune.getId().toString()))
				.collect(Collectors.toCollection(ListNBT::new));
	}

	public static void readNBT(IRuneHandler handler, ListNBT nbtTagList) {
		nbtTagList.forEach(nbt -> {
			String name = ((StringNBT) nbt).getAsString();
			Rune rune = ElementalCraftApi.RUNE_MANAGER.get(new ResourceLocation(name));

			if (rune != null) {
				handler.addRune(rune);
			} else {
				ElementalCraftApi.LOGGER.warn("Rune not fount with id: {}", name);
			}
		});
	}
	

}