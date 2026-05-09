package sirttas.elementalcraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;

public record SimpleStandaloneModelSupplier(Identifier identifier, StandaloneModelKey<@NotNull BlockStateModelPart> key) {

    public SimpleStandaloneModelSupplier(String identifier) {
        this(ElementalCraftApi.identifier(identifier));
    }

    public SimpleStandaloneModelSupplier(Identifier identifier) {
        this(identifier, new StandaloneModelKey<>(identifier::toString));
    }

    public static SimpleStandaloneModelSupplier pipeUpgrade(String identifier) {
        return new SimpleStandaloneModelSupplier(PipeUpgrade.FOLDER + "/" + identifier);
    }

    public static SimpleStandaloneModelSupplier block(String identifier) {
        return new SimpleStandaloneModelSupplier("block/" + identifier);
    }

    public BlockStateModelPart loadModel() {
        var model = Minecraft.getInstance().getModelManager().getStandaloneModel(key);

        if (model == null) {
            throw new IllegalStateException("Model not found: " + key);
        }
        return model;
    }
}
