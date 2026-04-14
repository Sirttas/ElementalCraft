package sirttas.elementalcraft.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import sirttas.elementalcraft.loot.parameter.ECLootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ECLootTableProvider extends LootTableProvider {

    public ECLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Collections.emptySet(), List.of(
                new SubProviderEntry(ECBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(_ -> new ECChestLoot(), LootContextParamSets.CHEST),
                new SubProviderEntry(_ -> new PipeUpgradeLoot(), ECLootContextParamSets.PIPE_UPGRADE)
        ), registries);
    }
}
