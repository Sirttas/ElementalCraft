package sirttas.elementalcraft.api.rune.handler;

import javax.annotation.Nonnull;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityRuneHandler {
	
	@CapabilityInject(IRuneHandler.class) public static final Capability<IRuneHandler> RUNE_HANDLE_CAPABILITY = null;

	private CapabilityRuneHandler() {}
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IRuneHandler.class, new Capability.IStorage<IRuneHandler>() {
			@Override
			@Deprecated
			public INBT writeNBT(Capability<IRuneHandler> capability, IRuneHandler instance, Direction side) {
				throw new UnsupportedOperationException("Elemental Craft doesn't support capability read/write anymore (https://github.com/MinecraftForge/MinecraftForge/issues/7622)");
			}

			@Override
			@Deprecated
			public void readNBT(Capability<IRuneHandler> capability, IRuneHandler instance, Direction side, INBT base) {
				throw new UnsupportedOperationException("Elemental Craft doesn't support capability read/write anymore (https://github.com/MinecraftForge/MinecraftForge/issues/7622)");
			}
		}, () -> new RuneHandler(3));
	}

	@Nonnull
	public static LazyOptional<IRuneHandler> get(ICapabilityProvider provider) {
		return get(provider, null);
	}
	
	@Nonnull
	public static LazyOptional<IRuneHandler> get(ICapabilityProvider provider, Direction side) {
		return RUNE_HANDLE_CAPABILITY != null ? provider.getCapability(RUNE_HANDLE_CAPABILITY, side) : LazyOptional.empty();
	}
}
