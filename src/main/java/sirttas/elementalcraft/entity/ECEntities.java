package sirttas.elementalcraft.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.projectile.FeatherSpike;
import sirttas.elementalcraft.entity.projectile.ThrownElementCrystal;

public class ECEntities {

    private static final DeferredRegister<EntityType<?>> DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, ElementalCraftApi.MODID);

    public static final RegistryObject<EntityType<FeatherSpike>> FEATHER_SPIKE = register(EntityType.Builder.<FeatherSpike>of(FeatherSpike::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), FeatherSpike.NAME);
    public static final RegistryObject<EntityType<ThrownElementCrystal>> THROWN_ELEMENT_CRYSTAL = register(EntityType.Builder.<ThrownElementCrystal>of(ThrownElementCrystal::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), ThrownElementCrystal.NAME);

    private ECEntities() {}

    private static <T extends Entity> RegistryObject<EntityType<T>> register(EntityType.Builder<T> builder, String name) {
        return DEFERRED_REGISTRY.register(name, () -> builder.build(ElementalCraft.createRL(name).toString()));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }

}
