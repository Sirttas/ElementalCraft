package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.DataGenerator;
import sirttas.dpanvil.api.data.remap.AbstractRemapKeysProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECRemapKeysProvider extends AbstractRemapKeysProvider {

    public ECRemapKeysProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void collectBuilders() {
        remap(ElementalCraftApi.RUNE_MANAGER_KEY).add(ElementalCraft.createRL("cognac"), ElementalCraft.createRL("soaryn"));
    }

    @Override
    public String getName() {
        return "ElementalCraft remap keys";
    }
}
