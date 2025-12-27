package sirttas.elementalcraft.entity.ai;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.function.Supplier;

public class ECSensorTypes {

    private static final DeferredRegister<SensorType<?>> DEFERRED_REGISTRY = DeferredRegister.create(Registries.SENSOR_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<SensorType<?>, SensorType<WatchOwnerFightSensor>> WATCH_OWNER_FIGHT_SENSOR = register("watch_owner_fight_sensor", WatchOwnerFightSensor::new);

    private ECSensorTypes() {}

    private static <U extends Sensor<?>> DeferredHolder<SensorType<?>, SensorType<U>> register(String key, Supplier<U> sensorSupplier) {
        return DEFERRED_REGISTRY.register(key, () -> new SensorType<>(sensorSupplier));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }
}
