package sirttas.elementalcraft.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.projectile.FeatherSpike;
import sirttas.elementalcraft.entity.spectral.SpectralTool;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ECEntities {

    private static final DeferredRegister<EntityType<?>> DEFERRED_REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FeatherSpike>> FEATHER_SPIKE = register(EntityType.Builder.<FeatherSpike>of(FeatherSpike::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20), FeatherSpike.NAME);
    public static final DeferredHolder<EntityType<?>, EntityType<SpectralTool>> SPECTRAL_TOOL = register(EntityType.Builder.<SpectralTool>of(SpectralTool::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(10), SpectralTool.NAME);

    private ECEntities() {}

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(SPECTRAL_TOOL.get(), SpectralTool.createAttributes().build());
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(EntityType.Builder<T> builder, String name) {
        return DEFERRED_REGISTRY.register(name, () -> builder.build(ElementalCraftApi.createRL(name).toString()));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }

}
