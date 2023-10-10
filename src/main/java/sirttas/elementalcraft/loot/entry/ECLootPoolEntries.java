package sirttas.elementalcraft.loot.entry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootPoolEntries {

    private static final DeferredRegister<LootPoolEntryType> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.key(), ElementalCraftApi.MODID);

    public static final RegistryObject<LootPoolEntryType> RUNES = register("runes", new LootRunes.Serializer());

    private ECLootPoolEntries() {}

    private static RegistryObject<LootPoolEntryType> register(String name, Serializer<? extends LootPoolEntryContainer> serializer) {
        return DEFERRED_REGISTER.register(name, () -> new LootPoolEntryType(serializer));
    }
    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
