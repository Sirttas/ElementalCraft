package sirttas.elementalcraft.block.entity.properties;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;

public interface IConfigurableBlockEntityProperties {

    Codec<IConfigurableBlockEntityProperties> CODEC = ConfigurableBlockEntityPropertiesType.REGISTRY.byNameCodec().dispatch(IConfigurableBlockEntityProperties::getType, ConfigurableBlockEntityPropertiesType::codec);

    @Nonnull
    static ResourceKey<IConfigurableBlockEntityProperties> createKey(@Nonnull String name) {
        return IDataManager.createKey(ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER_KEY, ElementalCraftApi.identifier(name));
    }

    ConfigurableBlockEntityPropertiesType<?> getType();
}
