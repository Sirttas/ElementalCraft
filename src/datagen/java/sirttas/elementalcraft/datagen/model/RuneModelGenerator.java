package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.rune.RuneModel;
import sirttas.elementalcraft.rune.Runes;

import java.util.function.BiConsumer;

public class RuneModelGenerator implements ECModelGenerator {

    public static final Factory FACTORY = (_, _, runeModelOutput, _, _, modelOutput) -> new RuneModelGenerator(runeModelOutput, modelOutput);

    public final BiConsumer<ResourceKey<Rune>, RuneModel.Unbaked> runeModelOutput;
    public final BiConsumer<Identifier, ModelInstance> modelOutput;

    private RuneModelGenerator(BiConsumer<ResourceKey<Rune>, RuneModel.Unbaked> runeModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        this.runeModelOutput = runeModelOutput;
        this.modelOutput = modelOutput;
    }

    @Override
    public void run() {
        // speed
        createRuneMoel(Runes.WII, RuneModel.Slate.MINOR);
        createRuneMoel(Runes.FUS, RuneModel.Slate.STANDARD);
        createRuneMoel(Runes.ZOD, RuneModel.Slate.MAJOR);
        // preservation
        createRuneMoel(Runes.MANX, RuneModel.Slate.MINOR);
        createRuneMoel(Runes.JITA, RuneModel.Slate.STANDARD);
        createRuneMoel(Runes.TANO, RuneModel.Slate.MAJOR);
        // range
        createRuneMoel(Runes.KIRBY, RuneModel.Slate.MINOR);
        createRuneMoel(Runes.WHALE, RuneModel.Slate.STANDARD);
        createRuneMoel(Runes.TYRIA, RuneModel.Slate.MAJOR);
        // optimization
        createRuneMoel(Runes.SOARYN, RuneModel.Slate.MINOR);
        createRuneMoel(Runes.KAWORU, RuneModel.Slate.STANDARD);
        createRuneMoel(Runes.MEWTWO, RuneModel.Slate.MAJOR);
        // luck
        createRuneMoel(Runes.CLAPTRAP, RuneModel.Slate.MINOR);
        createRuneMoel(Runes.BOMBADIL, RuneModel.Slate.STANDARD);
        createRuneMoel(Runes.TZEENTCH, RuneModel.Slate.MAJOR);
        // creative
        createRuneMoel(Runes.CREATIVE, RuneModel.Slate.MAJOR);
    }

    public void createRuneMoel(ResourceKey<Rune> rune, RuneModel.Slate slate) {
        runeModelOutput.accept(rune, new RuneModel.Unbaked(slate, new Material(rune.identifier().withPrefix(ElementalCraftApi.RUNE_MANAGER.getFolder() + '/'))));
    }
}
