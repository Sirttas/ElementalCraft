package sirttas.elementalcraft.advancements;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECCriteriaTriggers {

    private static final DeferredRegister<CriterionTrigger<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.TRIGGER_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, LookAtSourceTrigger> LOOK_AT_SOURCE = DEFERRED_REGISTER.register(LookAtSourceTrigger.NAME, LookAtSourceTrigger::new);

    private ECCriteriaTriggers() { }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
