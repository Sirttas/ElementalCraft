package sirttas.elementalcraft.pureore.loader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import sirttas.elementalcraft.pureore.PureOre;

import java.util.Collections;
import java.util.Map;

public class FixedNamePureOreLoader extends AbstractPureOreLoader {

    public static final Codec<FixedNamePureOreLoader> CODEC = RecordCodecBuilder.create(builder -> AbstractPureOreLoader.codec(builder).and(
            ResourceLocation.CODEC.fieldOf("fixed_name").forGetter(l -> l.fixedName)
    ).apply(builder, FixedNamePureOreLoader::new));

    private final ResourceLocation fixedName;

    public FixedNamePureOreLoader(HolderSet<Item> source, int elementConsumption, int inputSize, int outputSize, double luckRatio, int order, ResourceLocation fixedName) {
        super(source, elementConsumption, inputSize, outputSize, luckRatio, order);
        this.fixedName = fixedName;
    }

    @Override
    public Codec<FixedNamePureOreLoader> codec() {
        return CODEC;
    }

    @Override
    protected GeneratedPureOre load(Map<ResourceLocation, PureOre> pureOres, Item ore) {
        return new GeneratedPureOre(fixedName, Collections.emptyList());
    }
}
