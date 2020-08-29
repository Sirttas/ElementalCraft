package sirttas.elementalcraft.mixin;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;

@Mixin(BiomeGenerationSettings.class)
public interface AccessorBiomeGenerationSettings {

	@Accessor("field_242484_f")
	void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> list);
}