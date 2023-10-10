package sirttas.elementalcraft.datagen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import sirttas.elementalcraft.loot.parameter.ECLootContextParamSets;

import java.util.Collections;
import java.util.List;

public class ECLootTableProvider extends LootTableProvider {

    public ECLootTableProvider(PackOutput output) {
        super(output, Collections.emptySet(), List.of(
                new SubProviderEntry(ECBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ECChestLoot::new, LootContextParamSets.CHEST),
                new SubProviderEntry(ECEntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(PipeUpgradeLoot::new, ECLootContextParamSets.PIPE_UPGRADE)
        ));
    }
}
