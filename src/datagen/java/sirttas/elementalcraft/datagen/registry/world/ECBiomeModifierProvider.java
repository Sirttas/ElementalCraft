package sirttas.elementalcraft.datagen.registry.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.datagen.registry.AbstractECRegistryBootstrap;
import sirttas.elementalcraft.tag.ECTags;
import sirttas.elementalcraft.world.feature.SourceFeature;

public class ECBiomeModifierProvider extends AbstractECRegistryBootstrap<BiomeModifier> {

    public ECBiomeModifierProvider() {
        super(ForgeRegistries.Keys.BIOME_MODIFIERS);
    }

    @Override
    protected void gather() {
        add("inert_crystal_ore", new ForgeBiomeModifiers.AddFeaturesBiomeModifier(createHolderSet(ECTags.Biomes.HAS_INERT_CRYSTAL), createPlacedFeatureHolderSet("inert_crystal_ore_middle"), GenerationStep.Decoration.UNDERGROUND_ORES));

        addSources(SourceFeature.NAME, ECTags.Biomes.HAS_SOURCE_ALL);
        addSources(SourceFeature.NAME_ICY, ECTags.Biomes.HAS_SOURCE_ICY);
        addSources(SourceFeature.NAME_JUNGLE, ECTags.Biomes.HAS_SOURCE_JUNGLE);
        addSources(SourceFeature.NAME_MUSHROOM, ECTags.Biomes.HAS_SOURCE_MUSHROOM);
        addSources(SourceFeature.NAME_WET, ECTags.Biomes.HAS_SOURCE_WET);
        addSources(SourceFeature.NAME_DRY, ECTags.Biomes.HAS_SOURCE_DRY);
        addSources(SourceFeature.NAME_FOREST, ECTags.Biomes.HAS_SOURCE_FOREST);
        addSources(SourceFeature.NAME_HILL, ECTags.Biomes.HAS_SOURCE_HILL);
        addSources(SourceFeature.NAME_MOUNTAIN, ECTags.Biomes.HAS_SOURCE_MOUNTAIN);
        addSources(SourceFeature.NAME_PLAIN, ECTags.Biomes.HAS_SOURCE_PLAIN);
        addSources(SourceFeature.NAME_END, ECTags.Biomes.HAS_SOURCE_END);
        addSources(SourceFeature.NAME_NETHER_ALL, ECTags.Biomes.HAS_SOURCE_NETHER_ALL, GenerationStep.Decoration.UNDERGROUND_DECORATION);
        addSources(SourceFeature.NAME_NETHER, ECTags.Biomes.HAS_SOURCE_NETHER, GenerationStep.Decoration.UNDERGROUND_DECORATION);
        addSources(SourceFeature.NAME_NETHER_FOREST, ECTags.Biomes.HAS_SOURCE_NETHER_FOREST, GenerationStep.Decoration.UNDERGROUND_DECORATION);
        addSources(SourceFeature.NAME_OCEAN, ECTags.Biomes.HAS_SOURCE_OCEAN);
        addSources(SourceFeature.NAME_LUSH_CAVE, ECTags.Biomes.HAS_SOURCE_LUSH_CAVE, GenerationStep.Decoration.UNDERGROUND_DECORATION);
        addSources(SourceFeature.NAME_DRIPSTONE_CAVE, ECTags.Biomes.HAS_SOURCE_DRIPSTONE_CAVE, GenerationStep.Decoration.UNDERGROUND_DECORATION);
        addSources(SourceFeature.NAME_DEEP_DARK, ECTags.Biomes.HAS_SOURCE_DEEP_DARK, GenerationStep.Decoration.UNDERGROUND_DECORATION);
        addSources(SourceFeature.NAME_UNDERGROUND, ECTags.Biomes.HAS_SOURCE_UNDERGROUND, GenerationStep.Decoration.UNDERGROUND_DECORATION);
        addSources(SourceFeature.NAME_SKY, ECTags.Biomes.HAS_SOURCE_SKY);
    }

    private Holder.Reference<BiomeModifier> addSources(String feature, TagKey<Biome> biomes) {
        return addSources(feature, biomes, GenerationStep.Decoration.TOP_LAYER_MODIFICATION);
    }

    private Holder.Reference<BiomeModifier> addSources(String feature, TagKey<Biome> biomes, GenerationStep.Decoration step) {
        return add(feature, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(createHolderSet(biomes), createPlacedFeatureHolderSet(feature), step));
    }

    private HolderSet<PlacedFeature> createPlacedFeatureHolderSet(String feature) {
        return createHolderSet(Registries.PLACED_FEATURE, feature);
    }
}
