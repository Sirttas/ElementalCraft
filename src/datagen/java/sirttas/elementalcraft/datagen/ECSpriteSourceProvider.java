package sirttas.elementalcraft.datagen;

import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.concurrent.CompletableFuture;

public class ECSpriteSourceProvider extends SpriteSourceProvider {


    public ECSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ElementalCraftApi.MODID);
    }

    @Override
    protected void gather() {
        atlas(AtlasIds.ITEMS)
                .addSource(directory("elementalcraft/jewels"))
                .addSource(manager(ElementalCraftApi.RUNE_MANAGER));
    }

    private DirectoryLister directory(String sourcePath) {
        return new DirectoryLister(sourcePath, sourcePath + "/");
    }

    private DirectoryLister manager(IDataManager<?> manager) {
        return directory(manager.getFolder());
    }

}
