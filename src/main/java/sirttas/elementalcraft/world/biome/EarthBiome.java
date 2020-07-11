package sirttas.elementalcraft.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class EarthBiome extends Biome {

	public static final String NAME = "earth";

	protected EarthBiome() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.BADLANDS, new SurfaceBuilderConfig(SurfaceBuilder.STONE, SurfaceBuilder.STONE, SurfaceBuilder.STONE))
				.precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(2.0F).downfall(0.0F).func_235097_a_(new BiomeAmbience.Builder()
						.func_235246_b_(4159204).func_235248_c_(329011).func_235239_a_(12638463).func_235243_a_(MoodSoundAmbience.field_235027_b_).func_235238_a_())
				.parent((String) null));
		this.addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.CAVE, new ProbabilityConfig(0.2F)));
	}
}