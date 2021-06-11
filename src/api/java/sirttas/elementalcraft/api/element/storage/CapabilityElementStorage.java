package sirttas.elementalcraft.api.element.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;

public class CapabilityElementStorage {

	@CapabilityInject(IElementStorage.class) public static final Capability<IElementStorage> ELEMENT_STORAGE_CAPABILITY = null;

	private CapabilityElementStorage() {}
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IElementStorage.class, new Capability.IStorage<IElementStorage>() {
			@Override
			@Deprecated
			public INBT writeNBT(Capability<IElementStorage> capability, IElementStorage instance, Direction side) {
				throw new UnsupportedOperationException("Elemental Craft doesn't support capability read/write anymore (https://github.com/MinecraftForge/MinecraftForge/issues/7622)");
			}

			@Override
			@Deprecated
			public void readNBT(Capability<IElementStorage> capability, IElementStorage instance, Direction side, INBT base) {
				throw new UnsupportedOperationException("Elemental Craft doesn't support capability read/write anymore (https://github.com/MinecraftForge/MinecraftForge/issues/7622)");
			}
		}, () -> new SingleElementStorage(100000));
	}

	@Nonnull
	public static LazyOptional<IElementStorage> get(ICapabilityProvider provider) {
		return get(provider, null);
	}
	
	@Nonnull
	public static LazyOptional<IElementStorage> get(ICapabilityProvider provider, Direction side) {
		return ELEMENT_STORAGE_CAPABILITY != null ? provider.getCapability(ELEMENT_STORAGE_CAPABILITY, side) : LazyOptional.empty();
	}
	
	@Nullable
	public static <T extends INBT, S extends IElementStorage & INBTSerializable<T>> ICapabilityProvider createProvider(S storage) {
		return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY != null ? new CapabilityProvider<>(storage) : null;
	}
	
	private static class CapabilityProvider<T extends INBT, S extends IElementStorage & INBTSerializable<T>> implements ICapabilitySerializable<T> {

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
