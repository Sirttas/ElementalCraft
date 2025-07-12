package sirttas.elementalcraft.block.entity.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerProperties;
import sirttas.elementalcraft.block.entity.crafting.CraftingBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.ShrineProperties;
import sirttas.elementalcraft.block.synthesizer.SynthesizerProperties;

public record ConfigurableBlockEntityPropertiesType<T extends IConfigurableBlockEntityProperties>(MapCodec<T> codec) {

    public static final ResourceKey<Registry<ConfigurableBlockEntityPropertiesType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ElementalCraftApi.createRL("configurable_block_entity_properties_type"));
    private static final DeferredRegister<ConfigurableBlockEntityPropertiesType<?>> DEFERRED_REGISTRY = DeferredRegister.create(REGISTRY_KEY, ElementalCraftApi.MODID);

    public static final Registry<ConfigurableBlockEntityPropertiesType<?>> REGISTRY = DEFERRED_REGISTRY.makeRegistry(b -> b.sync(true));

    public static final DeferredHolder<ConfigurableBlockEntityPropertiesType<?>, ConfigurableBlockEntityPropertiesType<ShrineProperties>> SHRINE = register("shrine", ShrineProperties.CODEC);
    public static final DeferredHolder<ConfigurableBlockEntityPropertiesType<?>, ConfigurableBlockEntityPropertiesType<CraftingBlockEntityProperties>> CRAFTING = register("crafting", CraftingBlockEntityProperties.CODEC);
    public static final DeferredHolder<ConfigurableBlockEntityPropertiesType<?>, ConfigurableBlockEntityPropertiesType<ElementContainerProperties>> CONTAINER = register("container", ElementContainerProperties.CODEC);
    public static final DeferredHolder<ConfigurableBlockEntityPropertiesType<?>, ConfigurableBlockEntityPropertiesType<SynthesizerProperties>> SYNTHESIZER = register("synthesizer", SynthesizerProperties.CODEC);

    private static <T extends IConfigurableBlockEntityProperties> DeferredHolder<ConfigurableBlockEntityPropertiesType<?>, ConfigurableBlockEntityPropertiesType<T>> register(String name, MapCodec<T> codec) {
        return DEFERRED_REGISTRY.register(name, () -> new ConfigurableBlockEntityPropertiesType<>(codec));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }
}
