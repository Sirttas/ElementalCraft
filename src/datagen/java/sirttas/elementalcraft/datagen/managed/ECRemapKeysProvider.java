package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import sirttas.dpanvil.api.data.remap.AbstractRemapKeysProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.concurrent.CompletableFuture;

public class ECRemapKeysProvider extends AbstractRemapKeysProvider {

    public ECRemapKeysProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        remap(ElementalCraftApi.RUNE_MANAGER_KEY).add(ElementalCraftApi.createRL("cognac"), ElementalCraftApi.createRL("soaryn"));
    }

    @Override
    public String getName() {
        return "ElementalCraft Remap Keys";
    }

}
