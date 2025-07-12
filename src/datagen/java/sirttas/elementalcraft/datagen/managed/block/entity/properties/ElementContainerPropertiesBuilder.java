package sirttas.elementalcraft.datagen.managed.block.entity.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.container.ElementContainerProperties;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;

public record ElementContainerPropertiesBuilder(
        HolderSet<Block> compatibleTools,
        int capacity
) implements IConfigurableBlockEntityPropertiesBuilder {

    public static final MapCodec<ElementContainerPropertiesBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("compatible_tools").forGetter(ElementContainerPropertiesBuilder::compatibleTools),
            Codec.INT.fieldOf(ECNames.ELEMENT_CAPACITY).forGetter(ElementContainerPropertiesBuilder::capacity)
    ).apply(builder, ElementContainerPropertiesBuilder::new));

    @Override
    public ConfigurableBlockEntityPropertiesType<ElementContainerProperties> getType() {
        return ConfigurableBlockEntityPropertiesType.CONTAINER.get();
    }
}
