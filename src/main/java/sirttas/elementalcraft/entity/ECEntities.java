package sirttas.elementalcraft.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.projectile.FeatherSpike;
import sirttas.elementalcraft.entity.projectile.ThrownElementCrystal;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECEntities {

    private ECEntities() {}

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        var r = event.getRegistry();

        register(r, EntityType.Builder.<FeatherSpike>of(FeatherSpike::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), FeatherSpike.NAME);
        register(r, EntityType.Builder.<ThrownElementCrystal>of(ThrownElementCrystal::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20), ThrownElementCrystal.NAME);

    }
    private static <T extends Entity> EntityType<T> register(IForgeRegistry<EntityType<?>> registry, EntityType.Builder<T> builder, String name) {
        var id = ElementalCraft.createRL(name);
        var type = builder.build(id.toString());

        RegistryHelper.register(registry, type, id);
        return type;
    }

}
