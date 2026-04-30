package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.render.model.PipeUpgradeModel;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModel;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModelResolver;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;
import sirttas.elementalcraft.rune.RuneModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ECModelProvider extends ModelProvider {

    private final PackOutput.PathProvider runePathProvider;
    private final PackOutput.PathProvider pipeUpgradePathProvider;
    private final PackOutput.PathProvider buddingShrinePlateMoelPathProvider;

    private final List<ECModelGenerator.Factory> generatorFactories;

    private RuneModelCollector runeModelCollector;
    private PipeUpgradeModelCollector pipeUpgradeModelCollector;
    private BuddingShrinePlateModelCollector buddingShrinePlateModelCollector;

    public ECModelProvider(PackOutput output, List<ECModelGenerator.Factory> generatorFactories) {
        super(output, ElementalCraftApi.MODID);
        runePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, ElementalCraftApi.RUNE_MANAGER.getFolder());
        pipeUpgradePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, PipeUpgrade.FOLDER);
        buddingShrinePlateMoelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, BuddingShrinePlateModelResolver.PLATE_MODEL_FOLDER);
        this.generatorFactories = List.copyOf(generatorFactories);
    }

    public CompletableFuture<?> run(@NonNull CachedOutput output) {
        var runeModelCollector = new RuneModelCollector();
        this.runeModelCollector = runeModelCollector;
        var pipeUpgradeModelCollector = new PipeUpgradeModelCollector();
        this.pipeUpgradeModelCollector = pipeUpgradeModelCollector;
        var buddingShrinePlateModelCollector = new BuddingShrinePlateModelCollector();
        this.buddingShrinePlateModelCollector = buddingShrinePlateModelCollector;

        CompletableFuture<?> future;
        try {
            future = super.run(output);
        } finally {
            this.runeModelCollector = null;
            this.pipeUpgradeModelCollector = null;
            this.buddingShrinePlateModelCollector = null;
        }
        return CompletableFuture.allOf(future,
                runeModelCollector.save(output, runePathProvider),
                pipeUpgradeModelCollector.save(output, pipeUpgradePathProvider),
                buddingShrinePlateModelCollector.save(output, buddingShrinePlateMoelPathProvider));
    }


    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        generatorFactories.forEach(factory -> factory.create(
                blockModels.blockStateOutput,
                itemModels.itemModelOutput,
                runeModelCollector,
                pipeUpgradeModelCollector,
                buddingShrinePlateModelCollector,
                blockModels.modelOutput).run());
    }

    private static class RuneModelCollector implements BiConsumer<ResourceKey<Rune>, RuneModel.Unbaked> {
        private final Map<ResourceKey<Rune>, RuneModel.Unbaked> models = new HashMap<>();

        public void accept(ResourceKey<Rune> key, RuneModel.Unbaked model) {
            var prev = this.models.put(key, model);

            if (prev != null) {
                throw new IllegalStateException("Duplicate model definition for " + key);
            }
        }

        public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
            return DataProvider.saveAll(cache, RuneModel.Unbaked.CODEC, pathProvider::json, this.models);
        }
    }

    private static class PipeUpgradeModelCollector implements BiConsumer<PipeUpgradeType<?>, PipeUpgradeModel.Unbaked> {
        private final Map<Identifier, PipeUpgradeModel.Unbaked> models = new HashMap<>();

        public void accept(PipeUpgradeType<?> type, PipeUpgradeModel.Unbaked model) {
            var prev = this.models.put(type.getKey(), model);

            if (prev != null) {
                throw new IllegalStateException("Duplicate model definition for " + type.getKey());
            }
        }

        public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
            return DataProvider.saveAll(cache, PipeUpgradeModel.Unbaked.CODEC, pathProvider::json, this.models);
        }
    }

    private static class BuddingShrinePlateModelCollector implements BiConsumer<BudTypeDataDefinition, BuddingShrinePlateModel.Unbaked> {
        private final Map<Identifier, BuddingShrinePlateModel.Unbaked> models = new HashMap<>();

        public void accept(BudTypeDataDefinition def, BuddingShrinePlateModel.Unbaked model) {
            var id = def.getKey().identifier();
            var prev = this.models.put(id, model);

            if (prev != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        }

        public CompletableFuture<?> save(CachedOutput cache, PackOutput.PathProvider pathProvider) {
            return DataProvider.saveAll(cache, BuddingShrinePlateModel.Unbaked.CODEC, pathProvider::json, this.models);
        }
    }
}
