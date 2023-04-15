package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.DataGenerator;
import sirttas.dpanvil.api.data.remap.AbstractRemapKeysProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.source.trait.SourceTraits;

public class ECRemapKeysProvider extends AbstractRemapKeysProvider {

    public ECRemapKeysProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void collectBuilders() {
        remap(ElementalCraftApi.RUNE_MANAGER_KEY).add(ElementalCraft.createRL("cognac"), ElementalCraft.createRL("soaryn"));
        remap(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY).add(ElementalCraft.createRL("fleeting"), SourceTraits.ARTIFICIAL.location());
    }

    @Override
    public String getName() {
        return "ElementalCraft remap keys";
    }
}
