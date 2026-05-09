package sirttas.elementalcraft.range;

import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.range.Range;

public class Ranges {

    public static final ResourceKey<Range> BOX_RADIUS_10 = createKey("box_radius_10");

    public static final ResourceKey<Range> BELOW = createKey("below");
    public static final ResourceKey<Range> ABOVE = createKey("above");
    public static final ResourceKey<Range> NORTH = createKey("north");
    public static final ResourceKey<Range> SOUTH = createKey("south");
    public static final ResourceKey<Range> WEST = createKey("west");
    public static final ResourceKey<Range> EAST = createKey("east");

    public static final ResourceKey<Range> DIFFUSER = createKey("diffuser");

    private Ranges() {}

    private static ResourceKey<Range> createKey(String name) {
        return IDataManager.createKey(ElementalCraftApi.RANGE_MANAGER_KEY, ElementalCraftApi.identifier(name));
    }
}
