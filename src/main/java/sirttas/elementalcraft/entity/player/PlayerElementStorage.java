package sirttas.elementalcraft.entity.player;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.curios.CuriosInteractions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerElementStorage implements IElementStorage {

	private int tickCount = -1;
	private final Player player;
	private final List<IElementStorage> storages = new ArrayList<>();

	public PlayerElementStorage(Player player) {
		this.player = player;
	}
	
	@Override
	public int getElementAmount(ElementType type) {
		return getStorages().stream().mapToInt(storage -> storage.getElementAmount(type)).sum();
	}

	@Override
	public int getElementCapacity(ElementType type) {
		return getStorages().stream().mapToInt(storage -> storage.getElementCapacity(type)).sum();
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		AtomicInteger remaining = new AtomicInteger(count);

		getStorages().forEach(storage -> {
			if (storage.getElementCapacity(type) > 0) {
				remaining.set(storage.insertElement(remaining.get(), type, simulate));
			}
		});
		return remaining.get();
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		AtomicInteger extracted = new AtomicInteger(0);

		getStorages().forEach(storage -> {
			if (storage.getElementCapacity(type) > 0) {
				int e = extracted.get();

				extracted.set(e + storage.extractElement(count - e, type, simulate));
			}
		});
		return extracted.get();
	}

	@Override
	public boolean usableInInventory() {
		return true;
	}

	@Override
	public void fill() {
		getStorages().forEach(IElementStorage::fill);
	}

	@Override
	public void fill(ElementType type) {
		getStorages().forEach(storage -> storage.fill(type));
	}

	private List<IElementStorage> getStorages() {
		if (tickCount != player.tickCount) {
			storages.clear();
			Inventory inventory = player.getInventory();

			for (int i = 0; i < inventory.getContainerSize(); i++) {
				var storage = inventory.getItem(i).getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);

				if (storage != null && storage.usableInInventory()) {
					storages.add(storage);
				}
			}
			if (ECinteractions.isCuriosActive()) {
				CuriosInteractions.getHolders(player).forEach(i -> {
					var storage = i.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);

					if (storage != null && storage.usableInInventory()) {
						storages.add(storage);
					}
				});
			}
			tickCount = player.tickCount;
		}
		return storages;
	}
	
}
