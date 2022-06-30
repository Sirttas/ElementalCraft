package sirttas.elementalcraft.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RegistryHelper {

	private RegistryHelper() {
	}

	public static <T> void register(IForgeRegistry<T> reg, T thing, ResourceLocation name) {
		reg.register(name, thing);
	}

	public static <T> void register(IForgeRegistry<T> reg, T thing, String name) {
		register(reg, thing, ElementalCraft.createRL(name));
	}

	public static <T> void register(IForgeRegistry<T> reg, T thing, RegistryObject<?> object) {
		register(reg, thing, object.getId());
	}

	public static <T> Supplier<RegistryBuilder<T>> builder(UnaryOperator<RegistryBuilder<T>> mapper) {
		return () -> mapper.apply(new RegistryBuilder<>());
	}
}
