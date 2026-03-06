package sirttas.elementalcraft.api.range;

import com.mojang.serialization.Codec;
import net.minecraft.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

import java.util.List;

public record GrowthRatio(
        double west,
        double down,
        double north,
        double east,
        double up,
        double south
) {

    public static final GrowthRatio DEFAULT = new GrowthRatio(1, 1, 1, 1, 1, 1);
    public static final GrowthRatio NONE = new GrowthRatio(0, 0, 0, 0, 0, 0);
    public static final GrowthRatio HORIZONTAL = new GrowthRatio(1, 0, 1, 1, 0, 1);
    public static final GrowthRatio VERTICAL = new GrowthRatio(0, 1, 0, 0, 1, 0);
    public static final GrowthRatio ALIGNED_DOWN = new GrowthRatio(1, 2, 1, 1, 0, 1);
    public static final GrowthRatio ALIGNED_UP = new GrowthRatio(1, 0, 1, 1, 2, 1);
    public static final GrowthRatio ALIGNED_NORTH = new GrowthRatio(1, 1, 2, 1, 1, 0);
    public static final GrowthRatio ALIGNED_SOUTH = new GrowthRatio(1, 1, 0, 1, 1, 2);
    public static final GrowthRatio ALIGNED_WEST = new GrowthRatio(2, 1, 1, 0, 1, 1);
    public static final GrowthRatio ALIGNED_EAST = new GrowthRatio(0, 1, 1, 2, 1, 1);


    public static final Codec<GrowthRatio> CODEC = Codec.DOUBLE.listOf().comapFlatMap(
            list -> Util.fixedSize(list, 6).map(doubles -> new GrowthRatio(
                    doubles.getFirst(),
                    doubles.get(1),
                    doubles.get(2),
                    doubles.get(3),
                    doubles.get(4),
                    doubles.get(5))),
            growthRatio -> List.of(
                    growthRatio.west,
                    growthRatio.down,
                    growthRatio.north,
                    growthRatio.east,
                    growthRatio.up,
                    growthRatio.south));

    public static GrowthRatio towards(Direction direction) {
        return switch (direction) {
            case NORTH -> ALIGNED_NORTH;
            case SOUTH -> ALIGNED_SOUTH;
            case WEST -> ALIGNED_WEST;
            case EAST -> ALIGNED_EAST;
            case UP -> ALIGNED_UP;
            case DOWN -> ALIGNED_DOWN;
        };
    }

    public AABB apply(AABB box, double multiplier) {
        var m = multiplier - 1;

        return new AABB(
                (box.minX - 1) * (1 + west * m) + 1,
                (box.minY - 1) * (1 + down * m) + 1,
                (box.minZ - 1) * (1 + north * m) + 1,
                box.maxX * (1 + east * m),
                box.maxY * (1 + up * m),
                box.maxZ * (1 + south * m)
        );
    }
}
