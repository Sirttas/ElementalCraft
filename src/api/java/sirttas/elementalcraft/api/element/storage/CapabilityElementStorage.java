package sirttas.elementalcraft.api.element.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityElementStorage {

	public static final Capability<IElementStorage> ELEMENT_STORAGE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

	private CapabilityElementStorage() {}

	@Nonnull
	public static LazyOptional<IElementStorage> get(ICapabilityProvider provider) {
		return get(provider, null);
	}
	
	@Nonnull
	public static LazyOptional<IElementStorage> get(ICapabilityProvider provider, Direction side) {
		return ELEMENT_STORAGE_CAPABILITY != null ? provider.getCapability(ELEMENT_STORAGE_CAPABILITY, side) : LazyOptional.empty();
	}
	
	@Nullable
	public static <T extends Tag, S extends IElementStorage & INBTSerializable<T>> ICapabilityProvider createProvider(S storage) {
		return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY != null ? new CapabilityProvider<>(storage) : null;
	}
	
	private static class CapabilityProvider<T extends Tag, S extends IElementStorage & INBTSerializable<T>> implements ICapabilitySerializable<T> {

		private final S storage;
		
		public CapabilityProvider(S storage) {
			this.storage = storage;
		}

		@Override
		public <U> LazyOptional<U> getCapability(Capability<U> cap, Direction side) {
			return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> storage));
		}

		@Override
		public T serializeNBT() {
			return storage.serializeNBT();
		}

		@Override
		public void deserializeNBT(T nbt) {
			storage.deserializeNBT(nbt);
		}
	}
}
