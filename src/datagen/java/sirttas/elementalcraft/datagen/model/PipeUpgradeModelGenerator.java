package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.render.model.PipeUpgradeModel;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;

import java.util.function.BiConsumer;

public class PipeUpgradeModelGenerator implements ECModelGenerator {

    public static final Factory FACTORY = (_, itemModelOutput, _, pipeUpgradeModelOutput, _, modelOutput) -> new PipeUpgradeModelGenerator(itemModelOutput, pipeUpgradeModelOutput, modelOutput);

    public final ItemModelOutput itemModelOutput;
    public final BiConsumer<PipeUpgradeType<?>, PipeUpgradeModel.Unbaked> pipeUpgradeModelOutput;
    public final BiConsumer<Identifier, ModelInstance> modelOutput;

    private PipeUpgradeModelGenerator( ItemModelOutput itemModelOutput, BiConsumer<PipeUpgradeType<?>, PipeUpgradeModel.Unbaked> pipeUpgradeModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        this.itemModelOutput = itemModelOutput;
        this.pipeUpgradeModelOutput = pipeUpgradeModelOutput;
        this.modelOutput = modelOutput;
    }

    @Override
    public void run() {
        createNonTemplateModel(PipeUpgradeTypes.ELEMENT_PUMP.get());
        createNonTemplateModelWithItem(PipeUpgradeTypes.PIPE_PRIORITY_RINGS.get());
        createNonTemplateModel(PipeUpgradeTypes.ELEMENT_VALVE.get());
        createNonTemplateModel(PipeUpgradeTypes.ELEMENT_BEAM.get());
    }

    public void createNonTemplateModelWithItem(PipeUpgradeType<?> type) {
        var id = decoratePipeUpgradeModelLocation(type.getKey());

        itemModelOutput.accept(type.asItem(), ItemModelUtils.plainModel(id));
        pipeUpgradeModelOutput.accept(type, new PipeUpgradeModel.Unbaked(new Variant(id)));
    }

    public void createNonTemplateModel(PipeUpgradeType<?> type) {
        var id = decoratePipeUpgradeModelLocation(type.getKey());

        itemModelOutput.accept(type.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(type.asItem())));
        pipeUpgradeModelOutput.accept(type, new PipeUpgradeModel.Unbaked(new Variant(id)));
    }

    public static @NonNull Identifier decoratePipeUpgradeModelLocation(Identifier type) {
        return type.withPrefix(PipeUpgrade.FOLDER + "/");
    }
}
