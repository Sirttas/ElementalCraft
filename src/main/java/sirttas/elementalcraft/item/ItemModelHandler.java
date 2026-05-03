package sirttas.elementalcraft.item;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.airmill.AirMillDamageRangeSelectItemModelProperty;
import sirttas.elementalcraft.item.spell.ScrollRibbonTint;
import sirttas.elementalcraft.pureore.display.PureOreTint;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ItemModelHandler {

    private ItemModelHandler() {}

    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ElementalCraftApi.createRL("pure_ore"), PureOreTint.MAP_CODEC);
        event.register(ElementalCraftApi.createRL("scroll_ribbon"), ScrollRibbonTint.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerRangeSelectItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(ElementalCraftApi.createRL("air_mill_damage"), AirMillDamageRangeSelectItemModelProperty.MAP_CODEC);
    }
}
