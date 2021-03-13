package sirttas.elementalcraft.api.element.storage;

import javax.annotation.Nonnull;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
				throw new UnsupportedOperationException("ElementalCraft doesn't fupport capability read/write anymore (https://github.com/MinecraftForge/MinecraftForge/issues/7622)");
			}

			@Override
			@Deprecated
			public void readNBT(Capability<IElementStorage> capability, IElementStorage instance, Direction side, INBT base) {
				throw new UnsupportedOperationException("ElementalCraft doesn't fupport capability read/write anymore (https://github.com/MinecraftForge/MinecraftForge/issues/7622)");
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
}
