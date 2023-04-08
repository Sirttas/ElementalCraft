package sirttas.elementalcraft.sound;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;


public class ECSounds {

    private static final DeferredRegister<SoundEvent> DEFERRED_REGISTER = DeferredRegister.create(Registry.SOUND_EVENT_REGISTRY, ElementalCraftApi.MODID);

    public static final RegistryObject<SoundEvent> ELEMENT_CRACKLING = registerSound("block.container.element_crackling");

    private ECSounds() {}

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return DEFERRED_REGISTER.register(name, () -> new SoundEvent(ElementalCraft.createRL(name)));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }

}
