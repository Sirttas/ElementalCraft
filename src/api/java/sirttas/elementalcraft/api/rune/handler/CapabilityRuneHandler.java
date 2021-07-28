package sirttas.elementalcraft.api.rune.handler;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityRuneHandler {
	
	@CapabilityInject(IRuneHandler.class) public static final Capability<IRuneHandler> RUNE_HANDLE_CAPABILITY = null;

	private CapabilityRuneHandler() {}

	@Nonnull
	public static LazyOptional<IRuneHandler> get(ICapabilityProvider provider) {
		return get(provider, null);
	}
	
	@Nonnull
	public static LazyOptional<IRuneHandler> get(ICapabilityProvider provider, Direction side) {
		return RUNE_HANDLE_CAPABILITY != null ? provider.getCapability(RUNE_HANDLE_CAPABILITY, side) : LazyOptional.empty();
	}
}
