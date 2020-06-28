package sirttas.elementalcraft.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class EarthBiome extends Biome {

	public static final String NAME = "earth";

	protected EarthBiome() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.BADLANDS, new SurfaceBuilderConfig(SurfaceBuilder.STONE, SurfaceBuilder.STONE, SurfaceBuilder.STONE))
				.precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(2.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011)
				.parent((String) null));
		this.addCarver(GenerationStage.Carving.AIR, createCarver(WorldCarver.HELL_CAVE, new ProbabilityConfig(0.2F)));
	}
}