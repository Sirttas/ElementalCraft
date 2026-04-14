package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModelResolver;

public class ECModelProvider extends ModelProvider {

    private final PackOutput.PathProvider runePathProvider;
    private final PackOutput.PathProvider pipeUpgradePathProvider;
    private final PackOutput.PathProvider buddingShrinePlateMoelPathProvider;

    public ECModelProvider(PackOutput output) {
        super(output, ElementalCraftApi.MODID);
        runePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, ElementalCraftApi.RUNE_MANAGER.getFolder());
        pipeUpgradePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, PipeUpgrade.FOLDER);
        buddingShrinePlateMoelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, BuddingShrinePlateModelResolver.PLATE_MODEL_FOLDER);
    }

    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {

    }
}
