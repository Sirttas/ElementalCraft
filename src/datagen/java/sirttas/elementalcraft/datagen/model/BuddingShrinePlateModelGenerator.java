package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModel;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;
import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinitions;

import java.util.function.BiConsumer;

public class BuddingShrinePlateModelGenerator implements ECModelGenerator {

    public static final ECModelGenerator.Factory FACTORY = (_, _, _, buddingShrinePlateModelOutput, modelOutput) -> new BuddingShrinePlateModelGenerator(buddingShrinePlateModelOutput, modelOutput);

    public final BiConsumer<BudTypeDataDefinition, BuddingShrinePlateModel.Unbaked> buddingShrinePlateModelOutput;
    public final BiConsumer<Identifier, ModelInstance> modelOutput;

    private BuddingShrinePlateModelGenerator(BiConsumer<BudTypeDataDefinition, BuddingShrinePlateModel.Unbaked> buddingShrinePlateModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        this.buddingShrinePlateModelOutput = buddingShrinePlateModelOutput;
        this.modelOutput = modelOutput;
    }

    @Override
    public void run() {
        BudTypeDataDefinitions.getBudTypeDataDefinitions().forEach(this::createPlate);
    }

    public void createPlate(BudTypeDataDefinition definition) {
        var model = new Variant(ECModelTemplates.BUDDING_SHRINE_PLATE.create(definition.getKey().identifier(), new TextureMapping().put(TextureSlot.TEXTURE, new Material(definition.texture)), modelOutput));
        buddingShrinePlateModelOutput.accept(definition, new BuddingShrinePlateModel.Unbaked(model));
    }
}
