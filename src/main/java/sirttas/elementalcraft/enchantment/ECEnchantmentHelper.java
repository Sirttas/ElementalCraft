package sirttas.elementalcraft.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ECEnchantmentHelper {

    private ECEnchantmentHelper() { }

    public static Holder<Enchantment> getEnchantmentHolder(RegistryAccess registry, ResourceKey<Enchantment> key) {
        return registry.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(key);
    }
}
