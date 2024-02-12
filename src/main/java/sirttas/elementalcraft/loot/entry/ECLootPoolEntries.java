package sirttas.elementalcraft.loot.entry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootPoolEntries {

    private static final DeferredRegister<LootPoolEntryType> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.key(), ElementalCraftApi.MODID);

    public static final DeferredHolder<LootPoolEntryType, LootPoolEntryType> RUNES = register("runes", LootRunes.CODEC);

    private ECLootPoolEntries() {}

    private static DeferredHolder<LootPoolEntryType, LootPoolEntryType> register(String name, Codec<? extends LootPoolEntryContainer> codec) {
        return DEFERRED_REGISTER.register(name, () -> new LootPoolEntryType(codec));
    }
    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
