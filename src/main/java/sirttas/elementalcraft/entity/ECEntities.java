package sirttas.elementalcraft.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.projectile.FeatherSpike;

public class ECEntities {

    private static final DeferredRegister<@NotNull EntityType<?>> DEFERRED_REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<@NotNull EntityType<?>, @NotNull EntityType<@NotNull FeatherSpike>> FEATHER_SPIKE = register(EntityType.Builder.<FeatherSpike>of(FeatherSpike::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20), FeatherSpike.NAME);

    private ECEntities() {}

    private static <T extends Entity> DeferredHolder<@NotNull EntityType<?>, @NotNull EntityType<@NotNull T>> register(EntityType.Builder<@NotNull T> builder, String name) {
        return DEFERRED_REGISTRY.register(name, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, ElementalCraftApi.identifier(name))));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }

}
