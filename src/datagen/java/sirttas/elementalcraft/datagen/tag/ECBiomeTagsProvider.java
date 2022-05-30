package sirttas.elementalcraft.datagen.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
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
	}

}
