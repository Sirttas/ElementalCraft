package sirttas.elementalcraft.datagen.loot;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import sirttas.elementalcraft.loot.ECLootContextParamSets;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ECLootTableProvider extends LootTableProvider {

    public ECLootTableProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Nonnull
    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of(
                Pair.of(ECBlockLoot::new, LootContextParamSets.BLOCK),
                Pair.of(ECChestLoot::new, LootContextParamSets.CHEST),
                Pair.of(ECEntityLoot::new, LootContextParamSets.ENTITY),
                Pair.of(PipeUpgradeLoot::new, ECLootContextParamSets.PIPE_UPGRADE)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationContext) {
        map.forEach((id, table) -> LootTables.validate(validationContext, id, table));
    }

    @Nonnull
    @Override
    public String getName() {
        return "ElementalCraft loot tables";
    }
}
