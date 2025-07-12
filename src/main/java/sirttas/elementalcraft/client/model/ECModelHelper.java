package sirttas.elementalcraft.client.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECModelHelper {

    private ECModelHelper() {}

    public static ModelResourceLocation standalone(String path) {
        return ModelResourceLocation.standalone(ElementalCraftApi.createRL(path));
    }
}
