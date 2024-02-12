package sirttas.elementalcraft.jewel.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.jewel.Jewels;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ClientJewelHandler implements IJewelHandler {

    private List<Jewel> activeJewels = Collections.emptyList();

    @Nonnull
    @Override
    public List<Jewel> getActiveJewels() {
        return activeJewels;
    }

    @SubscribeEvent
    public static void addJewelTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        var item = stack.getItem();
        if (item != ECItems.JEWEL.get()) {
            List<Component> tooltip = event.getToolTip();
            var jewel = JewelHelper.getJewel(stack);

            if (jewel != Jewels.NONE.get()) {
                var index = IntStream.range(0, tooltip.size())
                        .filter(i -> BuiltInRegistries.ITEM.getKey(item).toString().equals(tooltip.get(i).getString()))
                        .findFirst()
                        .orElse(tooltip.size());

                var component = jewel.getDisplayName();

                if (component instanceof MutableComponent mutableComponent) {
                    component = mutableComponent.withStyle(ChatFormatting.YELLOW);
                }
                tooltip.add(index, component);
            }
        }
    }

    public void setActiveJewels(List<Jewel> jewels) {
        this.activeJewels = List.copyOf(jewels);
    }
}
