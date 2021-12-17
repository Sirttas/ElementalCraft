package sirttas.elementalcraft.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;

public class RegistryHelper {

	private RegistryHelper() {
	}

	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name) {
		reg.register(thing.setRegistryName(name));
	}

	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
		register(reg, thing, ElementalCraft.createRL(name));
	}

	public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, RegistryObject<?> object) {
		register(reg, thing, object.getId());
	}

	public static <T extends IForgeRegistryEntry<T>, U extends T> RegistryObject<U> object(final String name, IForgeRegistry<T> registry) {
		return RegistryObject.of(ElementalCraft.createRL(name), registry);
	}
}
