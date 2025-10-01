package sirttas.elementalcraft.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;

public class ECBlockModelProvider extends BlockModelProvider {

    public ECBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ElementalCraftApi.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        var platesTemplate = ElementalCraftApi.createRL(BuddingShrineBudType.PLATE_MODEL_FOLDER + "/budding_shrine_plate");

        withExistingParent(BuddingShrineBudType.PLATE_MODEL_FOLDER + "/amethyst", platesTemplate).texture("texture", ECBlockStateProvider.prefix("minecraft:budding_amethyst"));
        withExistingParent(BuddingShrineBudType.PLATE_MODEL_FOLDER + "/springaline", platesTemplate).texture("texture", ECBlockStateProvider.prefix("budding_springaline"));
        withExistingParent(BuddingShrineBudType.PLATE_MODEL_FOLDER + "/certus_quartz", platesTemplate).texture("texture", ECBlockStateProvider.prefix("ae2:flawless_budding_quartz"));
    }
}
