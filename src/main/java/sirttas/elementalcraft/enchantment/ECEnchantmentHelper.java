package sirttas.elementalcraft.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class ECEnchantmentHelper {

    private ECEnchantmentHelper() { }

    public static Holder<@NotNull Enchantment> getEnchantmentHolder(RegistryAccess registry, ResourceKey<@NotNull Enchantment> key) {
        return registry.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }
}
