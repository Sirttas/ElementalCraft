package sirttas.elementalcraft.recipe.display;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

public class ECSlotDisplays {

    private static final DeferredRegister<SlotDisplay.Type<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.SLOT_DISPLAY, ElementalCraftApi.MODID);

    public static final DeferredHolder<SlotDisplay.Type<?>, SlotDisplay.Type<ToolInfusionSlotDisplay>> TOOL_INFUSION = register(ToolInfusionRecipe.NAME, ToolInfusionSlotDisplay.TYPE);

    private ECSlotDisplays() {}

    private static <T extends SlotDisplay> DeferredHolder<SlotDisplay.Type<?>, SlotDisplay.Type<T>> register(String name, SlotDisplay.Type<T> type) {
        return DEFERRED_REGISTER.register(name, () -> type);
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
