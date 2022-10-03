package sirttas.elementalcraft.datagen.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.tag.ECTags;

public class ECBiomeTagsProvider extends BiomeTagsProvider {

	public ECBiomeTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraftApi.MODID, existingFileHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags() {
		tag(ECTags.Biomes.HAS_SOURCE_ALTAR).addTags(BiomeTags.IS_HILL, BiomeTags.IS_FOREST, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_BADLANDS, BiomeTags.IS_TAIGA, BiomeTags.IS_JUNGLE)
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA, Biomes.ICE_SPIKES, Biomes.SNOWY_PLAINS, Biomes.DESERT, Biomes.MUSHROOM_FIELDS, Biomes.GROVE);

		tag(ECTags.Biomes.HAS_INERT_CRYSTAL).addTags(BiomeTags.IS_OVERWORLD);
		tag(ECTags.Biomes.HAS_SOURCE_ICY).addTags(Tags.Biomes.IS_COLD_OVERWORLD);
		tag(ECTags.Biomes.HAS_SOURCE_JUNGLE).addTags(BiomeTags.IS_JUNGLE);
		tag(ECTags.Biomes.HAS_SOURCE_MUSHROOM).addTags(Tags.Biomes.IS_MUSHROOM);
		tag(ECTags.Biomes.HAS_SOURCE_WET).addTags(Tags.Biomes.IS_WET_OVERWORLD);
		tag(ECTags.Biomes.HAS_SOURCE_DRY).addTags(Tags.Biomes.IS_DRY_OVERWORLD);
		tag(ECTags.Biomes.HAS_SOURCE_FOREST).addTags(BiomeTags.IS_FOREST);
		tag(ECTags.Biomes.HAS_SOURCE_MOUNTAIN).addTags(BiomeTags.IS_MOUNTAIN);
		tag(ECTags.Biomes.HAS_SOURCE_HILL).addTags(BiomeTags.IS_HILL);
		tag(ECTags.Biomes.HAS_SOURCE_PLAIN).addTags(Tags.Biomes.IS_PLAINS);
		tag(ECTags.Biomes.HAS_SOURCE_NETHER_ALL).addTags(BiomeTags.IS_NETHER);
		tag(ECTags.Biomes.HAS_SOURCE_NETHER).add(Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY, Biomes.BASALT_DELTAS);
		tag(ECTags.Biomes.HAS_SOURCE_NETHER_FOREST).add(Biomes.CRIMSON_FOREST, Biomes.WARPED_FOREST);
		tag(ECTags.Biomes.HAS_SOURCE_END).addTags(BiomeTags.IS_END);
		tag(ECTags.Biomes.HAS_SOURCE_OCEAN).addTags(BiomeTags.IS_OCEAN);
		tag(ECTags.Biomes.HAS_SOURCE_LUSH_CAVE).addTags(Tags.Biomes.IS_LUSH);
		tag(ECTags.Biomes.HAS_SOURCE_DRIPSTONE_CAVE).add(Biomes.DRIPSTONE_CAVES);
		tag(ECTags.Biomes.HAS_SOURCE_DEEP_DARK).add(Biomes.DEEP_DARK);
		tag(ECTags.Biomes.HAS_SOURCE_UNDERGROUND).addTags(Tags.Biomes.IS_UNDERGROUND);
		tag(ECTags.Biomes.HAS_SOURCE_SKY).addTags(BiomeTags.IS_OVERWORLD, BiomeTags.IS_END);

		tag(ECTags.Biomes.HAS_SOURCE_ALL).addTags(BiomeTags.IS_OVERWORLD, BiomeTags.IS_END);
	}

}
