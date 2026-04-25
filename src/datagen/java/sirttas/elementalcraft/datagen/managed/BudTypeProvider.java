package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinitions;

import java.util.concurrent.CompletableFuture;

public class BudTypeProvider extends AbstractManagedDataBuilderProvider<BuddingShrineBudType, BudTypeDataDefinition> {

    public BudTypeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, ElementalCraftApi.BUD_TYPE_MANAGER, BudTypeDataDefinition.CODEC);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        BudTypeDataDefinitions.getBudTypeDataDefinitions().forEach(def -> add(def.getKey(), def));
    }

    @Override
    public @NotNull String getName() {
        return "ElementalCraft Budding Shrine Bud Types";
    }
}
