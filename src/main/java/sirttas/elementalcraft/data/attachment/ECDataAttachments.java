package sirttas.elementalcraft.data.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.jewel.handler.JewelHandler;
import sirttas.elementalcraft.spell.tick.SpellTickManager;

import java.util.function.Supplier;

public class ECDataAttachments {

    private static final DeferredRegister<@NotNull AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ElementalCraftApi.MODID);

    public static final Supplier<AttachmentType<@NotNull SpellTickManager>> SPELL_TICK_MANAGER = DEFERRED_REGISTER.register("spell_tick_manager", () -> AttachmentType.serializable(SpellTickManager::new).build());
    public static final Supplier<AttachmentType<@NotNull JewelHandler>> JEWEL_HANDLER = DEFERRED_REGISTER.register("jewel_handler", () -> AttachmentType.builder(h -> {
        if (h instanceof Entity entity) {
            return new JewelHandler(entity);
        }
        throw new IllegalArgumentException("JewelHandler can only be attached to an entity.");
    }).build());

    public static final Supplier<AttachmentType<@NotNull Boolean>> HAS_SEEN_SOURCE = DEFERRED_REGISTER.register("has_seen_source", () -> AttachmentType.builder(() -> false)
            .serialize(Codec.BOOL.fieldOf("value"))
            .build());
    
    private ECDataAttachments() {}

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
