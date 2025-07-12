package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.Map;

public class FixedNamePureOreLoader extends AbstractPureOreLoader {

    public static final MapCodec<FixedNamePureOreLoader> CODEC = RecordCodecBuilder.mapCodec(builder -> AbstractPureOreLoader.codec(builder).and(
            ResourceLocation.CODEC.fieldOf("fixed_name").forGetter(l -> l.fixedName)
    ).apply(builder, FixedNamePureOreLoader::new));

    private final ResourceLocation fixedName;

    public FixedNamePureOreLoader(HolderSet<Item> source, int elementConsumption, int inputSize, int outputSize, double luckRatio, int order, ResourceLocation fixedName) {
        super(source, elementConsumption, inputSize, outputSize, luckRatio, order);
        this.fixedName = fixedName;
    }

    @Override
    public PureOreLoaderType<FixedNamePureOreLoader> type() {
        return PureOreLoaderTypes.FIXED_NAME.get();
    }

    @Override
    protected PureOreTagGroup load(Map<ResourceLocation, LoadedPureOre> pureOres, Holder<Item> ore) {
        return new PureOreTagGroup(fixedName, Collections.emptyList());
    }
}
