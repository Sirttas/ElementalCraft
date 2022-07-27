package sirttas.elementalcraft.renderer;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class ECRendererHelper {

    private ECRendererHelper() {}

    public static Material getBlockMaterial(ResourceLocation loc)  {
        return new Material(TextureAtlas.LOCATION_BLOCKS, loc);
    }

}
