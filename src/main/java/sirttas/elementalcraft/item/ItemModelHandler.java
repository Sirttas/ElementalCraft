package sirttas.elementalcraft.item;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.pureore.display.PureOreTint;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ItemModelHandler {

    private ItemModelHandler() {}

    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ElementalCraftApi.createRL("pure_ore"), PureOreTint.MAP_CODEC);
    }


    /* TODO
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register();

        event.register((s, l) -> l == 0 ? -1 : ARGB.opaque(SpellHelper.getSpell(s).value().getColor()), ECItems.SCROLL.get());
        event.register((s, l) -> l == 0 ? -1 : ARGB.opaque(((ElementHolderItem) s.getItem()).getElementType().getColor()), ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get(), ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get()); // TODO create icon for each holder
    }
    */
}
