package sirttas.elementalcraft.data.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.jewel.handler.JewelHandler;
import sirttas.elementalcraft.spell.tick.SpellTickManager;

import java.util.function.Supplier;

public class ECDataAttachments {

    private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ElementalCraftApi.MODID);

    public static final Supplier<AttachmentType<SpellTickManager>> SPELL_TICK_MANAGER = DEFERRED_REGISTER.register("spell_tick_manager", () -> AttachmentType.serializable(SpellTickManager::new).build());
    public static final Supplier<AttachmentType<JewelHandler>> JEWEL_HANDLER = DEFERRED_REGISTER.register("jewel_handler", () -> AttachmentType.builder(h -> {
        if (h instanceof Entity entity) {
            return new JewelHandler(entity);
        }
        throw new IllegalArgumentException("JewelHandler can only be attached to an entity.");
    }).build());

    public static final Supplier<AttachmentType<Boolean>> HAS_SEEN_SOURCE = DEFERRED_REGISTER.register("has_seen_source", () -> AttachmentType.builder(() -> false)
            .serialize(new IAttachmentSerializer<ByteTag, Boolean>() {
                @Override
                public @NotNull ByteTag write(@NotNull Boolean attachment, @NotNull HolderLookup.Provider provider) {
                    return Boolean.TRUE.equals(attachment) ? ByteTag.ONE : ByteTag.ZERO;
                }

                @Override
                public @NotNull Boolean read(@NotNull IAttachmentHolder holder, @NotNull ByteTag tag, @NotNull HolderLookup.Provider provider) {
                    return tag.getAsByte() > 0;
                }
            }).build());
    
    private ECDataAttachments() {}

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
