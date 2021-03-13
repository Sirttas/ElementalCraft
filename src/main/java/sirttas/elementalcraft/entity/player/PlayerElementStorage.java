package sirttas.elementalcraft.entity.player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

public class PlayerElementStorage implements IElementStorage {

	private final PlayerEntity player;

	private PlayerElementStorage(PlayerEntity player) {
		this.player = player;
	}

	@Nullable
	public static ICapabilityProvider createProvider(PlayerEntity player) {
		return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY != null ? new ICapabilityProvider() {
			PlayerElementStorage storage = new PlayerElementStorage(player);
			
			@Override
			public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
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
		List<IElementStorage> storages = Lists.newArrayList();

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			CapabilityElementStorage.get(player.inventory.getStackInSlot(i)).filter(IElementStorage::usableInInventory).ifPresent(storages::add);
		}
		return storages;
	}
	
}
