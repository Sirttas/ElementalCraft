package sirttas.elementalcraft.color;

import mezz.jei.library.color.ColorGetter;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.Comparator;

@OnlyIn(Dist.CLIENT)
public class ECColorHelper {


    private static final Comparator<Integer> COLOR_BRIGHTNESS_COMPARATOR = Comparator.comparingInt(ECColorHelper::getBrightness);

    private static boolean noJeiLogged = false;

    private ECColorHelper() {}

    public static int[] lookupColors(ItemStack stack) {
        try {
            var colors = new ColorGetter().getColors(stack, 3); // FIXME extract ColorGetter from JEI

            if (colors != null && !colors.isEmpty()) {
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
        return new int[] { -1, -1, -1 };
    }

    public static int getBrightness(int color) {
        return ((color & 0xFF) + ((color >> 8) & 0xFF) + ((color >> 16) & 0xFF)) / 3;
    }

}
