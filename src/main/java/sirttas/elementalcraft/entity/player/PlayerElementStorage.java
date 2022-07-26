package sirttas.elementalcraft.entity.player;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.curios.CuriosInteractions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerElementStorage implements IElementStorage {

	private int tickCount = -1;
	private final Player player;
	private final List<IElementStorage> storages = new ArrayList<>();

	private PlayerElementStorage(Player player) {
		this.player = player;
	}

	@Nullable
	public static ICapabilityProvider createProvider(Player player) {
		return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY != null ? new ICapabilityProvider() {
			final PlayerElementStorage storage = new PlayerElementStorage(player);
			
			@Nonnull
			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
				return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> storage));
			}
		} : null;
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

		getStorages().forEach(storage -> remaining.set(storage.insertElement(remaining.get(), type, simulate)));
		return remaining.get();
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		AtomicInteger extracted = new AtomicInteger(0);

		getStorages().forEach(storage -> {
			int e = extracted.get();

			extracted.set(e + storage.extractElement(count - e, type, simulate));
		});
		return extracted.get();
	}

	@Override
	public boolean usableInInventory() {
		return true;
	}
	
	private List<IElementStorage> getStorages() {
		if (tickCount != player.tickCount) {
			storages.clear();
			Inventory inventory = player.getInventory();

			for (int i = 0; i < inventory.getContainerSize(); i++) {
				CapabilityElementStorage.get(inventory.getItem(i))
						.filter(IElementStorage::usableInInventory)
						.ifPresent(storages::add);
			}
			if (ECinteractions.isCuriosActive()) {
				CuriosInteractions.getHolders(player).stream()
						.<IElementStorage>mapMulti((i, downstream) -> CapabilityElementStorage.get(i).ifPresent(downstream::accept))
						.filter(IElementStorage::usableInInventory)
						.forEach(storages::add);
			}
			tickCount = player.tickCount;
		}
		return storages;
	}
	
}
