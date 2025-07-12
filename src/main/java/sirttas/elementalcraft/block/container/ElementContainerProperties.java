package sirttas.elementalcraft.block.container;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;

public record ElementContainerProperties(
        HolderSet<Block> compatibleTools,
        int capacity
) implements IConfigurableBlockEntityProperties {

    public static final ElementContainerProperties DEFAULT = new ElementContainerProperties(HolderSet.empty(), 0);
    public static final MapCodec<ElementContainerProperties> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("compatible_tools").forGetter(ElementContainerProperties::compatibleTools),
            Codec.INT.fieldOf(ECNames.ELEMENT_CAPACITY).forGetter(ElementContainerProperties::capacity)
    ).apply(builder, ElementContainerProperties::new));

    @Override
    public ConfigurableBlockEntityPropertiesType<ElementContainerProperties> getType() {
        return ConfigurableBlockEntityPropertiesType.CONTAINER.get();
    }
}
