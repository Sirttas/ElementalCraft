package sirttas.elementalcraft.datagen.interaction.mekanism;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.ModList;
import sirttas.elementalcraft.datagen.interaction.DatagenInteraction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MekanismDatagenInteraction implements DatagenInteraction {

    @Override
    public List<DataProvider> getProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        if (!ModList.get().isLoaded("mekanism")) {
            return List.of();
        }
        return List.of(new MekanismRecipeProvider(output, lookupProvider));
    }
}
