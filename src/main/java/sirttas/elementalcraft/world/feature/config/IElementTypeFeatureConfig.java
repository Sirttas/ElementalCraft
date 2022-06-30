package sirttas.elementalcraft.world.feature.config;

import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import sirttas.elementalcraft.api.element.ElementType;

public interface IElementTypeFeatureConfig extends FeatureConfiguration {

	Codec<IElementTypeFeatureConfig> CODEC = Codec.STRING.dispatch(IElementTypeFeatureConfig::getName, name -> {
		if (name.equals("simple")) {
			return ElementTypeFeatureConfig.CODEC;
		} else if (name.equals("random")) {
			return RandomElementTypeFeatureConfig.CODEC;
		}
		return null;
	});

	ElementType getElementType(RandomSource rand);

	String getName();
}
