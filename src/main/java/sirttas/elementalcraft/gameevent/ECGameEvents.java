package sirttas.elementalcraft.gameevent;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECGameEvents {

    private static final DeferredRegister<GameEvent> DEFERRED_REGISTRY = DeferredRegister.create(Registries.GAME_EVENT, ElementalCraftApi.MODID);

    public static final DeferredHolder<GameEvent, GameEvent> AIR_SYNTHESIS = register("air_synthesis");

    private ECGameEvents() {}

    private static DeferredHolder<GameEvent, GameEvent> register(String name) {
        return DEFERRED_REGISTRY.register(name, () -> new GameEvent(16));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }
}
