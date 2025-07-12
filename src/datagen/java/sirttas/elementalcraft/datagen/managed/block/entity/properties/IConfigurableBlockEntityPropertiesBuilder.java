package sirttas.elementalcraft.datagen.managed.block.entity.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;

public interface IConfigurableBlockEntityPropertiesBuilder {

    Codec<IConfigurableBlockEntityPropertiesBuilder> CODEC = ConfigurableBlockEntityPropertiesType.REGISTRY.byNameCodec().dispatch(IConfigurableBlockEntityPropertiesBuilder::getType, IConfigurableBlockEntityPropertiesBuilder::getCodec);

    ConfigurableBlockEntityPropertiesType<?> getType();

    static MapCodec<? extends IConfigurableBlockEntityPropertiesBuilder> getCodec(ConfigurableBlockEntityPropertiesType<?> type) {
        if (type == ConfigurableBlockEntityPropertiesType.SHRINE.get()) {
            return ShrinePropertiesBuilder.CODEC;
        } else if (type == ConfigurableBlockEntityPropertiesType.CRAFTING.get()) {
            return CraftingBlockEntityPropertiesBuilder.CODEC;
        } else if (type == ConfigurableBlockEntityPropertiesType.CONTAINER.get()) {
            return ElementContainerPropertiesBuilder.CODEC;
        } else if (type == ConfigurableBlockEntityPropertiesType.SYNTHESIZER.get()) {
            return SynthesizerPropertiesBuilder.CODEC;
        }
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }
}
