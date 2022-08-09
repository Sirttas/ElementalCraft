package sirttas.elementalcraft.datagen.world;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.datagen.AbstractECJsonCodecProvider;
import sirttas.elementalcraft.world.feature.ECFeatures;
import sirttas.elementalcraft.world.feature.SourceFeature;
import sirttas.elementalcraft.world.feature.config.ElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.RandomElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.placement.SourcePlacement;

import java.util.Arrays;
import java.util.List;

public class ECFeaturesProvider extends AbstractECJsonCodecProvider<PlacedFeature> {

    public static final RandomElementTypeFeatureConfig ALL = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.FIRE, 1)
            .put(ElementType.WATER, 1)
            .put(ElementType.EARTH, 1)
            .put(ElementType.AIR, 1)
            .build());
    public static final RandomElementTypeFeatureConfig ICY = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.WATER, 2)
            .put(ElementType.AIR, 1)
            .build());
    public static final RandomElementTypeFeatureConfig JUNGLE = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.FIRE, 1)
            .put(ElementType.WATER, 1)
            .build());
    public static final RandomElementTypeFeatureConfig NETHER = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.FIRE, 5)
            .put(ElementType.EARTH, 1)
            .build());
    public static final RandomElementTypeFeatureConfig WET = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.WATER, 5)
            .put(ElementType.EARTH, 1)
            .build());
    public static final RandomElementTypeFeatureConfig DRY = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.FIRE, 5)
            .put(ElementType.EARTH, 2)
            .put(ElementType.AIR, 1)
            .build());
    public static final RandomElementTypeFeatureConfig END = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.AIR, 5)
            .put(ElementType.FIRE, 1)
            .build());
    public static final RandomElementTypeFeatureConfig FOREST = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.EARTH, 2)
            .put(ElementType.WATER, 1)
            .build());
    public static final RandomElementTypeFeatureConfig HILL = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.EARTH, 4)
            .put(ElementType.AIR, 1)
            .build());
    public static final RandomElementTypeFeatureConfig MOUNTAIN = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.AIR, 2)
            .put(ElementType.EARTH, 1)
            .build());
    public static final RandomElementTypeFeatureConfig PLAIN = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.EARTH, 2)
            .put(ElementType.WATER, 1)
            .put(ElementType.AIR, 1)
            .put(ElementType.FIRE, 1)
            .build());
    public static final RandomElementTypeFeatureConfig LUSH_CAVE = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.WATER, 1)
            .put(ElementType.EARTH, 5)
            .build());
    public static final RandomElementTypeFeatureConfig DRIPSTONE_CAVE = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.WATER, 1)
            .put(ElementType.EARTH, 3)
            .build());
    public static final RandomElementTypeFeatureConfig DEEP_DARK = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder()
            .put(ElementType.AIR, 1)
            .put(ElementType.EARTH, 3)
            .build());

    public ECFeaturesProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, existingFileHelper, Registry.PLACED_FEATURE_REGISTRY);
    }


    @Override
    protected void gather() {

        add("inert_crystal_ore_middle", new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ECBlocks.CRYSTAL_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ECBlocks.DEEPSLATE_CRYSTAL_ORE.get().defaultBlockState())), 9))), OrePlacements.commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(96)))));

        addSourceChanced(SourceFeature.NAME, ALL, 900);
        addSourceChanced(SourceFeature.NAME_ICY, ICY);
        addSourceChanced(SourceFeature.NAME_JUNGLE, JUNGLE, 30);
        addSourceChanced(SourceFeature.NAME_MUSHROOM, ALL);
        addSourceChanced(SourceFeature.NAME_WET, WET);
        addSourceChanced(SourceFeature.NAME_DRY, DRY);
        addSourceChanced(SourceFeature.NAME_END, END, 400);
        addSourceChanced(SourceFeature.NAME_FOREST, FOREST, 30);
        addSourceChanced(SourceFeature.NAME_HILL, HILL);
        addSourceChanced(SourceFeature.NAME_MOUNTAIN, MOUNTAIN);
        addSourceChanced(SourceFeature.NAME_PLAIN, PLAIN);
        addSourceChanced(SourceFeature.NAME_NETHER, NETHER, 30);
        addSourceChanced(SourceFeature.NAME_OCEAN, ElementTypeFeatureConfig.WATER, 400);
        addSourceUnderground(SourceFeature.NAME_LUSH_CAVE, LUSH_CAVE);
        addSourceUnderground(SourceFeature.NAME_DRIPSTONE_CAVE, DRIPSTONE_CAVE);
        addSourceUnderground(SourceFeature.NAME_DEEP_DARK, DEEP_DARK);
        addSourceUnderground(SourceFeature.NAME_UNDERGROUND, ALL, 100);
    }

    private PlacedFeature addSourceUnderground(String nameLushCave, RandomElementTypeFeatureConfig lushCave) {
        return addSourceUnderground(nameLushCave, lushCave, 10);
    }

    private PlacedFeature addSourceUnderground(String nameLushCave, RandomElementTypeFeatureConfig lushCave, int chance) {
        return addSourceChanced(nameLushCave, lushCave, sourcePlacementUnderground(RarityFilter.onAverageOnceEvery(chance)));
    }

    private PlacedFeature addSourceChanced(String name, IElementTypeFeatureConfig config) {
        return addSourceChanced(name, config, 90);
    }

    private PlacedFeature addSourceChanced(String name, IElementTypeFeatureConfig config, int oneEvery) {
        return addSourceChanced(name, config, sourcePlacement(RarityFilter.onAverageOnceEvery(oneEvery)));
    }

    private PlacedFeature addSourceChanced(String name, IElementTypeFeatureConfig config, List<PlacementModifier> placement) {
        return add(name, new PlacedFeature(Holder.direct(new ConfiguredFeature<>(ECFeatures.SOURCE.get(), config)), placement));
    }

    private static List<PlacementModifier> sourcePlacement(PlacementModifier ... modifiers) {
        var list = Lists.newArrayList(SourcePlacement.INSTANCE, InSquarePlacement.spread(), BiomeFilter.biome());

        addPlacement(list, modifiers);
        return List.copyOf(list);
    }

    private static List<PlacementModifier> sourcePlacementUnderground(PlacementModifier ... modifiers) {
        var list = Lists.newArrayList(InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, BiomeFilter.biome());

        addPlacement(list, modifiers);
        return List.copyOf(list);
    }

    private static void addPlacement(List<PlacementModifier> list, PlacementModifier ... modifiers) {
        if (modifiers != null && modifiers.length > 0) {
            list.addAll(Arrays.asList(modifiers));
        }
    }

}
