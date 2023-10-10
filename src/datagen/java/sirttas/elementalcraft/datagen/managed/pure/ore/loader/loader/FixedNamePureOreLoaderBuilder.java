package sirttas.elementalcraft.datagen.managed.pure.ore.loader.loader;

import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import sirttas.elementalcraft.pureore.loader.FixedNamePureOreLoader;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;

public class FixedNamePureOreLoaderBuilder extends AbstractPureOreLoaderBuilder {

    private final ResourceLocation fixedName;

    protected FixedNamePureOreLoaderBuilder(HolderSet<Item> source, ResourceLocation fixedName) {
        super(source);
        this.fixedName = fixedName;
    }


    @Override
    public IPureOreLoader build() {
        return new FixedNamePureOreLoader(this.source, this.elementConsumption, this.inputSize, this.outputSize, this.luckRatio, this.order, this.fixedName);
    }
}
