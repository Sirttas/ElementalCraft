package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import sirttas.dpanvil.api.data.remap.AbstractRemapKeysProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.source.trait.SourceTraits;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ECRemapKeysProvider extends AbstractRemapKeysProvider {

    public ECRemapKeysProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        remap(ElementalCraftApi.RUNE_MANAGER_KEY).add(ElementalCraft.createRL("cognac"), ElementalCraft.createRL("soaryn"));
        remap(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY).add(ElementalCraft.createRL("fleeting"), SourceTraits.ARTIFICIAL);
    }

    @Nonnull
    @Override
    public String getName() {
        return "ElementalCraft Remap Keys";
    }

}
