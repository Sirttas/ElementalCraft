package sirttas.elementalcraft.block.shrine.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import sirttas.dpanvil.api.codec.Codecs;

public record ShrineRange(
        AABB box,
        boolean stitch,
        boolean fixedHeight
) {

    public static final AABB DEFAULT_BOX = new AABB(BlockPos.ZERO);
    public static final ShrineRange DEFAULT = new ShrineRange(DEFAULT_BOX, false, false);
    public static final Codec<ShrineRange> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codecs.AABB.fieldOf("box").forGetter(ShrineRange::box),
            Codec.BOOL.optionalFieldOf("stitch", false).forGetter(ShrineRange::stitch),
            Codec.BOOL.optionalFieldOf("fixed_height", false).forGetter(ShrineRange::fixedHeight)
    ).apply(builder, ShrineRange::new));

    public static ShrineRange.Builder box(double range) {
        return box(range, range);
    }

    public static ShrineRange.Builder box(double horizontal, double vertical) {
        return box(horizontal, vertical, horizontal);
    }

    public static ShrineRange.Builder expendingUp(double horizontal, double vertical) {
        return box(-horizontal, 0, -horizontal, horizontal + 1, vertical, horizontal + 1);
    }

    public static ShrineRange.Builder expendingDown(double horizontal, double vertical) {
        return box(-horizontal, -vertical, -horizontal, horizontal + 1, 0, horizontal + 1);
    }

    public static ShrineRange.Builder box(double x, double y, double z) {
        return box(-x, -y, -z, x + 1, y + 1, z + 1);
    }

    public static ShrineRange.Builder box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return box(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    public static ShrineRange.Builder box(AABB aabb) {
        return new ShrineRange.Builder(aabb);
    }

    public static class Builder {

        private final AABB box;
        private boolean stitch;
        private boolean fixedHeight;

        private Builder(AABB aabb) {
            this.box = aabb;
            this.stitch = false;
            this.fixedHeight = false;
        }

        public ShrineRange.Builder stitch() {
            this.stitch = true;
            return this;
        }

        public ShrineRange.Builder fixedHeight() {
            this.fixedHeight = true;
            return this;
        }

        public ShrineRange build() {
            return new ShrineRange(box, stitch, fixedHeight);
        }
    }
}
