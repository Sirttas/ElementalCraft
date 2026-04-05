package sirttas.elementalcraft.loot.entry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootPoolEntries {

    private static final DeferredRegister<@NotNull MapCodec<? extends LootPoolEntryContainer>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.key(), ElementalCraftApi.MODID);

    public static final DeferredHolder<@NotNull MapCodec<? extends LootPoolEntryContainer>, @NotNull MapCodec<LootRunes>> RUNES = register("runes", LootRunes.CODEC);

    private ECLootPoolEntries() {}

    private static <T extends LootPoolEntryContainer> DeferredHolder<@NotNull MapCodec<? extends LootPoolEntryContainer>, @NotNull MapCodec<T>> register(String name, MapCodec<T> codec) {
        return DEFERRED_REGISTER.register(name, () -> codec);
    }
    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
