package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public interface ECModelGenerator {

    void run();

    @FunctionalInterface
    interface Factory {
        ECBlockStateModelGenerator create(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators);
    }
}
