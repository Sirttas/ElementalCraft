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
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;
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

    public static ECItem getCrystalForType(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.AIR_CRYSTAL.get();
            case EARTH -> ECItems.EARTH_CRYSTAL.get();
            case FIRE -> ECItems.FIRE_CRYSTAL.get();
            case WATER -> ECItems.WATER_CRYSTAL.get();
            default -> ECItems.INERT_CRYSTAL.get();
        };
    }

    public static ECItem getShardForType(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.AIR_SHARD.get();
            case EARTH -> ECItems.EARTH_SHARD.get();
            case FIRE -> ECItems.FIRE_SHARD.get();
            case WATER -> ECItems.WATER_SHARD.get();
            default -> throw new IllegalArgumentException("Element Type must not be NONE");
        };
    }

    public static ECItem getPowerfulShardForType(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.POWERFUL_AIR_SHARD.get();
            case EARTH -> ECItems.POWERFUL_EARTH_SHARD.get();
            case FIRE -> ECItems.POWERFUL_FIRE_SHARD.get();
            case WATER -> ECItems.POWERFUL_WATER_SHARD.get();
            default -> throw new IllegalArgumentException("Element Type must not be NONE");
        };
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
