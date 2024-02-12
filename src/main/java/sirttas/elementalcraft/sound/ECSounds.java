package sirttas.elementalcraft.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;


public class ECSounds {

    private static final DeferredRegister<SoundEvent> DEFERRED_REGISTER = DeferredRegister.create(Registries.SOUND_EVENT, ElementalCraftApi.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ELEMENT_CRACKLING = registerSound("block.container.element_crackling");

    private ECSounds() {}

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return DEFERRED_REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(ElementalCraftApi.createRL(name)));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }

}
