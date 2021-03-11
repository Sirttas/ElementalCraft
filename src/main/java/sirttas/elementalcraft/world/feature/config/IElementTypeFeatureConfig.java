package sirttas.elementalcraft.world.feature.config;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.feature.IFeatureConfig;
import sirttas.elementalcraft.api.element.ElementType;

public interface IElementTypeFeatureConfig extends IFeatureConfig {

	public static final Codec<IElementTypeFeatureConfig> CODEC = Codec.STRING.dispatch(IElementTypeFeatureConfig::getName, name -> {
		if (name.equals("simple")) {
			return ElementTypeFeatureConfig.CODEC;
		} else if (name.equals("random")) {
			return RandomElementTypeFeatureConfig.CODEC;
		}
		return null;
	});

	ElementType getElementType(Random rand);

	String getName();
}
