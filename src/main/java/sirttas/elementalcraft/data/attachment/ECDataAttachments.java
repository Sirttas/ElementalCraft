package sirttas.elementalcraft.data.attachment;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.spell.tick.SpellTickManager;

import java.util.function.Supplier;

public class ECDataAttachments {

    private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ElementalCraftApi.MODID);

    public static final Supplier<AttachmentType<SpellTickManager>> SPELL_TICK_MANAGER = DEFERRED_REGISTER.register("spell_tick_manager", () -> AttachmentType.serializable(SpellTickManager::new).build());
    public static final Supplier<AttachmentType<ToolInfusion>> TOOL_INFUSION = DEFERRED_REGISTER.register("tool_infusion", () -> AttachmentType.builder(() -> ToolInfusion.NONE)
            .serialize(ElementalCraftApi.TOOL_INFUSION_MANAGER)
            .build());
    public static final Supplier<AttachmentType<Jewel>> JEWEL = DEFERRED_REGISTER.register(ECNames.JEWEL, () -> AttachmentType.builder(Jewels.NONE)
            .serialize(Jewels.REGISTRY.byNameCodec())
            .build());

    private ECDataAttachments() {}

    public static void register(IEventBus modBus) {
        DEFERRED_REGISTER.register(modBus);
    }

}
