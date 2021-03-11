package sirttas.elementalcraft.world.feature.config;

import java.util.Random;

import com.mojang.serialization.Codec;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

public class ElementTypeFeatureConfig implements IElementTypeFeatureConfig {

	public static final ElementTypeFeatureConfig FIRE = new ElementTypeFeatureConfig(ElementType.FIRE);
	public static final ElementTypeFeatureConfig WATER = new ElementTypeFeatureConfig(ElementType.WATER);
	public static final ElementTypeFeatureConfig EARTH = new ElementTypeFeatureConfig(ElementType.EARTH);
	public static final ElementTypeFeatureConfig AIR = new ElementTypeFeatureConfig(ElementType.AIR);

	public static final Codec<ElementTypeFeatureConfig> CODEC = ElementType.CODEC.fieldOf(ECNames.ELEMENT_TYPE).xmap(ElementTypeFeatureConfig::new, c -> c.elementType).codec();

	private final ElementType elementType;

	private ElementTypeFeatureConfig(ElementType elementType) {
		this.elementType = elementType;
	}

	@Override
	public ElementType getElementType(Random rand) {
		return elementType;
	}

	@Override
	public String getName() {
		return "simple";
	}
}
