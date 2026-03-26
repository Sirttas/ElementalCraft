package sirttas.elementalcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

public record SimpleStandaloneModelSupplier(Identifier identifier, StandaloneModelKey<@NotNull BlockStateModel> key) {

    public SimpleStandaloneModelSupplier(String identifier) {
        this(ElementalCraftApi.createRL(identifier));
    }

    public SimpleStandaloneModelSupplier(Identifier identifier) {
        this(identifier, new StandaloneModelKey<>(identifier::toString));
    }
    
    public BlockStateModel loadModel() {
        var model = Minecraft.getInstance().getModelManager().getStandaloneModel(key);

        if (model == null) {
            throw new IllegalStateException("Model not found: " + key);
        }
        return model;
    }
}
