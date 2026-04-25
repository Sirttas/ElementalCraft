package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModel;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ECModelGenerator {

    void run();

    @FunctionalInterface
    interface Factory {
        ECModelGenerator create(Consumer<BlockModelDefinitionGenerator> blockStateOutput,
                                ItemModelOutput itemModelOutput,
                                BiConsumer<BudTypeDataDefinition, BuddingShrinePlateModel.Unbaked> buddingShrinePlateModelOutput,
                                BiConsumer<Identifier, ModelInstance> modelOutput);
    }
}
