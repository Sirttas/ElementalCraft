package sirttas.elementalcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECModelHelper {

    private ECModelHelper() {}

    public static StandaloneModelKey<@NotNull BlockModelPart> createStandaloneKey(String path) {
        var name = ElementalCraftApi.createRL(path).toString();

        return new StandaloneModelKey<>(() -> name);
    }

    public static SingleVariant loadStandaloneModel(StandaloneModelKey<@NotNull BlockModelPart> key) {
        var part = Minecraft.getInstance().getModelManager().getStandaloneModel(key);

        if (part == null) {
            throw new IllegalStateException("Model not found: " + key);
        }
        return new SingleVariant(part);
    }
}
