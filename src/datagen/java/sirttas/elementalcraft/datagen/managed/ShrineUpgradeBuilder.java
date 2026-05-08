package sirttas.elementalcraft.datagen.managed;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Encoder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.dpanvil.api.predicate.block.world.CacheBlockPredicate;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class ShrineUpgradeBuilder {

    public static final Encoder<ShrineUpgradeBuilder> ENCODER = ShrineUpgrade.CODEC.comap(builder -> new ShrineUpgrade(builder.getPredicate(), builder.bonuses, builder.maxAmount));

    private final ResourceKey<ShrineUpgrade> key;
    private IBlockPosPredicate predicate;
    private final Map<ShrineUpgrade.BonusType, Float> bonuses;
    private int maxAmount;
    private final List<ResourceKey<ShrineUpgrade>> incompatibilities;

    ShrineUpgradeBuilder(ResourceKey<ShrineUpgrade> key) {
        this.key = key;
        this.bonuses = new EnumMap<>(ShrineUpgrade.BonusType.class);
        this.predicate = IBlockPosPredicate.any();
        this.maxAmount = 0;
        this.incompatibilities = new ArrayList<>();
    }

    public ShrineUpgradeBuilder match(Block... block) {
        return predicate(IBlockPosPredicate.match(block));
    }

    public ShrineUpgradeBuilder match(TagKey<Block> tag) {
        return predicate(IBlockPosPredicate.match(tag));
    }

    public ShrineUpgradeBuilder predicate(IBlockPosPredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public ShrineUpgradeBuilder max(int max) {
        maxAmount = max;
        return this;
    }

    @SafeVarargs
    public final ShrineUpgradeBuilder incompatibleWith(ResourceKey<ShrineUpgrade>... upgrades) {
        return incompatibleWith(Arrays.asList(upgrades));
    }

    public final ShrineUpgradeBuilder incompatibleWith(Iterable<@Nullable ResourceKey<ShrineUpgrade>> upgrades) {
        incompatibilities.addAll(StreamSupport.stream(upgrades.spliterator(), false)
                .distinct()
                .filter(k -> k != null && !this.key.equals(k) && !incompatibilities.contains(k))
                .toList());
        return this;
    }

    public ShrineUpgradeBuilder addBonus(ShrineUpgrade.BonusType type, float value) {
        this.bonuses.put(type, value);
        return this;
    }

    private IBlockPosPredicate getPredicate() {
        if (incompatibilities.isEmpty()) {
            return predicate;
        }

        var incompatiblePredicate = (incompatibilities.size() == 1
                ? new HasShrineUpgradePredicate(Iterables.getOnlyElement(incompatibilities))
                : IBlockPosPredicate.createOr(incompatibilities.stream()
                .map(HasShrineUpgradePredicate::new)
                .toArray(HasShrineUpgradePredicate[]::new))
        ).not();

        return (predicate instanceof CacheBlockPredicate cacheBlockPredicate ? cacheBlockPredicate.predicate().and(incompatiblePredicate).cache() : predicate.and(incompatiblePredicate)).simplify();
    }
}
