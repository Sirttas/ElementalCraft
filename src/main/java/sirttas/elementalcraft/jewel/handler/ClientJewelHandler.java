package sirttas.elementalcraft.jewel.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ClientJewelHandler implements IJewelHandler {

    private List<Jewel> activeJewels = Collections.emptyList();

    private ClientJewelHandler() {}

    @Nullable
    public static ICapabilityProvider createProvider() {
        if (CAPABILITY != null) {
            var handler = new ClientJewelHandler();

            return new ICapabilityProvider() {
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
                    return CAPABILITY.orEmpty(cap, LazyOptional.of(() -> handler));
                }
            };
        }
        return null;
    }

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

            if (jewel != null) {
                var index = IntStream.range(0, tooltip.size())
                        .filter(i -> ForgeRegistries.ITEMS.getKey(item).toString().equals(tooltip.get(i).getString()))
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
