package sirttas.elementalcraft.datagen.interaction;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class DatagenInteractionWrapper implements DatagenInteraction {

    static final DatagenInteraction INSTANCE = new DatagenInteractionWrapper();

    private final List<DatagenInteraction> interactions;

    private DatagenInteractionWrapper() {
        ServiceLoader<DatagenInteraction> loader = ServiceLoader.load(DatagenInteraction.class);

        interactions = loader.stream()
                .map(ServiceLoader.Provider::get)
                .toList();
        ElementalCraftApi.LOGGER.info("Elemental Craft datagen loaded {} interactions: {}", interactions::size, () -> interactions.stream()
                .map(interaction -> interaction.getClass().getName())
                .collect(Collectors.joining(", ")));
    }

    @Override
    public List<BudTypeDataDefinition> getBudTypeDataDefinitions() {
        return interactions.stream()
                .flatMap(interaction -> interaction.getBudTypeDataDefinitions().stream())
                .toList();
    }

    @Override
    public List<DataProvider> getProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return interactions.stream()
                .flatMap(interaction -> interaction.getProviders(output, lookupProvider).stream())
                .toList();
    }
}
