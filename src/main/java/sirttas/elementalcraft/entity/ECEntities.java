package sirttas.elementalcraft.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.projectile.FeatherSpike;
import sirttas.elementalcraft.entity.projectile.ThrownElementCrystal;

public class ECEntities {

    private static final DeferredRegister<EntityType<?>> DEFERRED_REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<FeatherSpike>> FEATHER_SPIKE = register(EntityType.Builder.<FeatherSpike>of(FeatherSpike::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), FeatherSpike.NAME);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownElementCrystal>> THROWN_ELEMENT_CRYSTAL = register(EntityType.Builder.<ThrownElementCrystal>of(ThrownElementCrystal::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), ThrownElementCrystal.NAME);

    private ECEntities() {}

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(EntityType.Builder<T> builder, String name) {
        return DEFERRED_REGISTRY.register(name, () -> builder.build(ElementalCraftApi.createRL(name).toString()));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }

}
