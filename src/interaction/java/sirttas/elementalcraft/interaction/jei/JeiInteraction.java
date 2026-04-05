package sirttas.elementalcraft.interaction.jei;

import mezz.jei.library.color.ColorGetter;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import sirttas.elementalcraft.ElementalCraftInteraction;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.Comparator;
import java.util.List;

public class JeiInteraction implements ElementalCraftInteraction {

    private static final Comparator<Integer> COLOR_BRIGHTNESS_COMPARATOR = Comparator.comparingInt(JeiInteraction::getBrightness);

    private boolean noJeiLogged = false;

    @Override
    public boolean isActive() {
        return ModList.get().isLoaded("jei");
    }

    @Override
    public int[] lookupColors(ItemStack stack) {
        try {
            List<Integer> colors = new ColorGetter().getColors(stack, 3);

            if (!colors.isEmpty()) {
                var array = colors.stream()
                        .map(color -> color == null ? -1 : color)
                        .sorted(COLOR_BRIGHTNESS_COMPARATOR.reversed())
                        .mapToInt(Integer::intValue)
                        .toArray();

                if (array.length == 1) {
                    return new int[] { array[0], array[0], array[0] };
                } else if (array.length == 2) {
                    return new int[] { array[0], array[0], array[1] };
                } else {
                    return new int[] { array[0], array[1], array[2] };
                }
            }
        } catch (NoClassDefFoundError e) {
            if (!noJeiLogged) {
                ElementalCraftApi.LOGGER.warn("JEI not present, can't lookup item colors", e);
                noJeiLogged = true;
            }
        }
        return null;
    }

    public static int getBrightness(int color) {
        return ((color & 0xFF) + ((color >> 8) & 0xFF) + ((color >> 16) & 0xFF)) / 3;
    }
}
