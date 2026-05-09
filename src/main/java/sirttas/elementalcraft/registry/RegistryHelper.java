package sirttas.elementalcraft.registry;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class RegistryHelper {

	private RegistryHelper() {
	}

	public static <T> void register(RegisterEvent.RegisterHelper<T> reg, Identifier name, T thing) {
		reg.register(name, thing);
	}

	public static <T> void register(RegisterEvent.RegisterHelper<T> reg, T thing, String name) {
		register(reg, ElementalCraftApi.identifier(name), thing);
	}

	public static <T> void register(RegisterEvent.RegisterHelper<T> reg, T thing, DeferredHolder<?, ?> object) {
		register(reg, object.getId(), thing);
	}
}
