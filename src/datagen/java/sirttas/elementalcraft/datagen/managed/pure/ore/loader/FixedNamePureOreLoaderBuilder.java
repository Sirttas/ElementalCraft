package sirttas.elementalcraft.datagen.managed.pure.ore.loader;

import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import sirttas.elementalcraft.pureore.loader.FixedNamePureOreLoader;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;

public class FixedNamePureOreLoaderBuilder extends AbstractPureOreLoaderBuilder {

    private final Identifier fixedName;

    protected FixedNamePureOreLoaderBuilder(HolderSet<Item> source, Identifier fixedName) {
        super(source);
        this.fixedName = fixedName;
    }


    @Override
    public IPureOreLoader build() {
        return new FixedNamePureOreLoader(this.source, this.elementConsumption, this.inputSize, this.outputSize, this.luckRatio, this.order, this.fixedName);
    }
}
