package sirttas.elementalcraft.jewel.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ClientJewelHandler implements IJewelHandler {

    private List<Jewel> activeJewels = Collections.emptyList();

    @Nonnull
    @Override
    public List<Jewel> getActiveJewels() {
        return activeJewels;
    }

    @SubscribeEvent
    public static void addJewelTooltip(ItemTooltipEvent event) {
        var stack = event.getItemStack();

        if (stack.isEmpty() || stack.is(ECTags.Items.JEWELS)) {
            return;
        }

        var tooltip = event.getToolTip();
        var jewel = JewelHelper.getJewel(stack);

        if (jewel != null) {
            var index = IntStream.range(0, tooltip.size())
                    .filter(i -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(tooltip.get(i).getString()))
                    .findFirst()
                    .orElse(tooltip.size());

            var component = jewel.getDisplayName();

            if (component instanceof MutableComponent mutableComponent) {
                component = mutableComponent.withStyle(ChatFormatting.YELLOW);
            }
            tooltip.add(index, component);
        }
    }

    public void setActiveJewels(List<Jewel> jewels) {
        this.activeJewels = List.copyOf(jewels);
    }
}
