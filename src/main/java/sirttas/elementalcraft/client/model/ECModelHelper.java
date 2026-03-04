package sirttas.elementalcraft.client.model;

import net.minecraft.client.resources.model.ModelIdentifier;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECModelHelper {

    private ECModelHelper() {}

    public static ModelIdentifier standalone(String path) {
        return ModelIdentifier.standalone(ElementalCraftApi.createRL(path));
    }
}
