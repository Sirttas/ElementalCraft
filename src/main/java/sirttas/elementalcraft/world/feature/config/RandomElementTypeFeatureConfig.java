package sirttas.elementalcraft.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

public class RandomElementTypeFeatureConfig implements IElementTypeFeatureConfig {

	public static final MapCodec<RandomElementTypeFeatureConfig> CODEC = Codec.unboundedMap(ElementType.CODEC, Codec.INT)
			.fieldOf(ECNames.ELEMENT_TYPE)
			.xmap(RandomElementTypeFeatureConfig::new, c -> c.elementTypes);

	private final Map<ElementType, Integer> elementTypes;

	public RandomElementTypeFeatureConfig(Map<ElementType, Integer> elementTypes) {
		if (elementTypes.containsKey(ElementType.NONE)) {
			throw new IllegalArgumentException("elementTypes must not contain NONE!");
		}
		this.elementTypes = new EnumMap<>(elementTypes);
	}

	@Override
	public ElementType getElementType(RandomSource rand) {
		int roll = rand.nextInt(elementTypes.values().stream().mapToInt(i -> i).sum());
		
		for (Entry<ElementType, Integer> entry : elementTypes.entrySet()) {
			int weight = entry.getValue();
			
			if (weight > roll) {
				return entry.getKey();
			}
			roll -= weight;
		}
		return ElementType.NONE;
	}

	@Override
	public String getName() {
		return "random";
	}
}
