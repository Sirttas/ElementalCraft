package sirttas.elementalcraft.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

public class ElementalCraftRegistries {

    public static final Registry<SourceTraitValueProviderType<?>> SOURCE_TRAIT_VALUE_PROVIDER_TYPE = new RegistryBuilder<>(Keys.SOURCE_TRAIT_VALUE_PROVIDER_TYPE).create();
    public static final Registry<ToolInfusionEffectType<?>> TOOL_INFUSION_EFFECT_TYPE = new RegistryBuilder<>(Keys.TOOL_INFUSION_EFFECT_TYPE).create();

    private ElementalCraftRegistries() {}

    public static final class Keys {

        public static final ResourceKey<Registry<SourceTraitValueProviderType<?>>> SOURCE_TRAIT_VALUE_PROVIDER_TYPE = ResourceKey.createRegistryKey(new ResourceLocation(ElementalCraftApi.MODID, ECNames.SOURCE_TRAIT_VALUE_PROVIDER_TYPE));
        public static final ResourceKey<Registry<ToolInfusionEffectType<?>>> TOOL_INFUSION_EFFECT_TYPE = ResourceKey.createRegistryKey(new ResourceLocation(ElementalCraftApi.MODID, ECNames.TOOL_INFUSION_TYPE));
        private Keys() {}
    }
}
