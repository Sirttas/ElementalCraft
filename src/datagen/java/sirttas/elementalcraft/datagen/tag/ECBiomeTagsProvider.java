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
		tag(ECTags.Biomes.HAS_SOURCE_ICY).addTags(Tags.Biomes.IS_COLD);
		tag(ECTags.Biomes.HAS_SOURCE_JUNGLE).addTags(BiomeTags.IS_JUNGLE);
		tag(ECTags.Biomes.HAS_SOURCE_MUSHROOM).addTags(Tags.Biomes.IS_MUSHROOM);
		tag(ECTags.Biomes.HAS_SOURCE_WET).addTags(Tags.Biomes.IS_WET);
		tag(ECTags.Biomes.HAS_SOURCE_DRY).addTags(Tags.Biomes.IS_DRY);
		tag(ECTags.Biomes.HAS_SOURCE_FOREST).addTags(BiomeTags.IS_FOREST);
		tag(ECTags.Biomes.HAS_SOURCE_MOUNTAIN).addTags(BiomeTags.IS_MOUNTAIN);
		tag(ECTags.Biomes.HAS_SOURCE_HILL).addTags(BiomeTags.IS_HILL);
		tag(ECTags.Biomes.HAS_SOURCE_PLAIN).addTags(Tags.Biomes.IS_PLAINS);
		tag(ECTags.Biomes.HAS_SOURCE_NETHER).addTags(BiomeTags.IS_NETHER);
		tag(ECTags.Biomes.HAS_SOURCE_END).addTags(BiomeTags.IS_END);
		tag(ECTags.Biomes.HAS_SOURCE_OCEAN).addTags(BiomeTags.IS_OCEAN);
		tag(ECTags.Biomes.HAS_SOURCE_ALL).addTags(ECTags.Biomes.HAS_SOURCE_ICY, ECTags.Biomes.HAS_SOURCE_JUNGLE, ECTags.Biomes.HAS_SOURCE_MUSHROOM, ECTags.Biomes.HAS_SOURCE_WET, ECTags.Biomes.HAS_SOURCE_DRY, ECTags.Biomes.HAS_SOURCE_FOREST, ECTags.Biomes.HAS_SOURCE_MOUNTAIN, ECTags.Biomes.HAS_SOURCE_HILL, ECTags.Biomes.HAS_SOURCE_PLAIN, ECTags.Biomes.HAS_SOURCE_NETHER, ECTags.Biomes.HAS_SOURCE_END, ECTags.Biomes.HAS_SOURCE_OCEAN);
	}

}
