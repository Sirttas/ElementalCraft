package sirttas.elementalcraft.api.range;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import sirttas.dpanvil.api.DPAnvilNames;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.dpanvil.api.data.DataManagerCodecs;
import sirttas.dpanvil.api.json.merger.JsonMerger;
import sirttas.dpanvil.api.json.merger.JsonObjectMerger;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.Optional;

public record Range(
        AABB box,
        boolean stitch,
        GrowthRatio growthRatio
) {

    public static final Range DEFAULT = new Range(new AABB(BlockPos.ZERO), false, GrowthRatio.DEFAULT);
    public static final Holder<Range> DEFAULT_HOLDER = Holder.direct(DEFAULT);
    public static final Codec<Range> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codecs.AABB.optionalFieldOf("box", new AABB(BlockPos.ZERO)).forGetter(Range::box),
            Codec.BOOL.optionalFieldOf("stitch", false).forGetter(Range::stitch),
            GrowthRatio.CODEC.optionalFieldOf("growth_ratio", GrowthRatio.DEFAULT).forGetter(Range::growthRatio)
    ).apply(builder, Range::new));
    public static final Codec<Holder<Range>> HOLDER_CODEC = DataManagerCodecs.holderCodec(ElementalCraftApi.RANGE_MANAGER_KEY, CODEC);
    public static final JsonMerger MERGER = JsonObjectMerger.builder()
            .with("box", JsonMerger.SECOND)
            .with("growth_ratio", JsonMerger.SECOND)
            .build();

    public static Range.Builder builder() {
        return new Range.Builder(null);
    }

    public static Range.Builder withParent(ResourceKey<Range> parent) {
        return withParent(parent.location());
    }

    public static Range.Builder withParent(ResourceLocation parent) {
        return new Range.Builder(parent);
    }

    public AABB scaleBox(double multiplier) {
        var box = growthRatio.apply(this.box, multiplier);

        if (stitch) {
            box = stitchAABB(box);
        }
        return box;
    }

    private static AABB stitchAABB(AABB source) {
        return new AABB(Math.floor(source.minX), Math.floor(source.minY), Math.floor(source.minZ), Math.ceil(source.maxX), Math.ceil(source.maxY), Math.ceil(source.maxZ));
    }

    public static class Builder {

        private static final String PARENT_TAG_NAME = DPAnvilNames.ResourceLocations.PARENT.toString();

        public static final Codec<Range.Builder> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                ResourceLocation.CODEC.optionalFieldOf(PARENT_TAG_NAME).forGetter(b -> Optional.ofNullable(b.parent)),
                Codecs.AABB.optionalFieldOf("box").forGetter(b -> Optional.ofNullable(b.box)),
                Codec.BOOL.optionalFieldOf("stitch", false).forGetter(b -> b.stitch),
                GrowthRatio.CODEC.optionalFieldOf("growth_ratio", GrowthRatio.DEFAULT).forGetter(b -> b.growthRatio)
        ).apply(builder, (a1, a2, a3, a4) -> {
            throw new UnsupportedOperationException("Builder deserialization is not supported.");
        }));

        private final ResourceLocation parent;
        private AABB box;
        private GrowthRatio growthRatio;
        private boolean stitch;

        private Builder(ResourceLocation parent) {
            this.parent = parent;
            this.box = null;
            this.growthRatio = GrowthRatio.DEFAULT;
            this.stitch = false;
        }

        public Range.Builder box(double range) {
            return box(range, range);
        }

        public Range.Builder boxTowards(Direction direction, double range) {
            var ratio = GrowthRatio.towards(direction);

            return box(ratio.west() * -range, ratio.down() * -range, ratio.north() * -range, ratio.east() * range + 1, ratio.up() * range + 1, ratio.south() * range + 1)
                    .growthRatio(ratio);
        }

        public Range.Builder box(double horizontal, double vertical) {
            return box(horizontal, vertical, horizontal);
        }

        public Range.Builder expendingUp(double horizontal, double vertical) {
            return box(-horizontal, 0, -horizontal, horizontal + 1, vertical, horizontal + 1);
        }

        public Range.Builder expendingDown(double horizontal, double vertical) {
            return box(-horizontal, -vertical, -horizontal, horizontal + 1, 0, horizontal + 1);
        }

        public Range.Builder box(double x, double y, double z) {
            return box(-x, -y, -z, x + 1, y + 1, z + 1);
        }

        public Range.Builder box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            return box(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
        }

        public Range.Builder box(AABB aabb) {
            this.box = aabb;
            return this;
        }

        public Range.Builder move(double x, double y, double z) {
            this.box = this.box.move(x, y, z);
            return this;
        }

        public Range.Builder stitch() {
            this.stitch = true;
            return this;
        }

        public Range.Builder growthRatio(GrowthRatio growthRatio) {
            this.growthRatio = growthRatio;
            return this;
        }

        public Range.Builder fixedHeight() {
            return growthRatio(GrowthRatio.HORIZONTAL);
        }

        public Range.Builder fixedWidth() {
            return growthRatio(GrowthRatio.VERTICAL);
        }
    }
}
