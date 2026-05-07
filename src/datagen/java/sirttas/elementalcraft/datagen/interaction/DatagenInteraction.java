package sirttas.elementalcraft.datagen.interaction;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DatagenInteraction {

    static DatagenInteraction get() {
        return DatagenInteractionWrapper.INSTANCE;
    }

    default List<BudTypeDataDefinition> getBudTypeDataDefinitions() {
        return List.of();
    }

    default List<DataProvider> getProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return List.of();
    }
}
