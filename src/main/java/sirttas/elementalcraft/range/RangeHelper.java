package sirttas.elementalcraft.range;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RangeHelper {

    private RangeHelper() { }

    @Nonnull
    public static Stream<BlockPos> getBlocksInAABB(AABB box) {
        return getRange(box.minX, box.maxX)
                .mapToObj(x -> getRange(box.minZ, box.maxZ)
                        .mapToObj(z -> getRange(box.minY, box.maxY)
                                .mapToObj(y -> new BlockPos(x, y, z))))
                .mapMulti((s, downstream) -> s.forEach(s2 -> s2.forEach(downstream)));
    }

    @Nonnull
    private static IntStream getRange(double min, double max) {
        return IntStream.range((int) Math.floor(min + 0.00001), (int) Math.ceil(max - 0.00001));
    }
}
