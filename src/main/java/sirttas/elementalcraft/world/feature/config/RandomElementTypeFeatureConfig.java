package sirttas.elementalcraft.world.feature.config;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

public class RandomElementTypeFeatureConfig implements IElementTypeFeatureConfig {

	public static final RandomElementTypeFeatureConfig ALL = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.FIRE, 1).put(ElementType.WATER, 1).put(ElementType.EARTH, 1).put(ElementType.AIR, 1).build());
	public static final RandomElementTypeFeatureConfig ICY = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder().put(ElementType.WATER, 2).put(ElementType.AIR, 1).build());
	public static final RandomElementTypeFeatureConfig JUNGLE = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.FIRE, 1).put(ElementType.WATER, 1).build());
	public static final RandomElementTypeFeatureConfig NETHER = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.FIRE, 5).put(ElementType.EARTH, 1).build());
	public static final RandomElementTypeFeatureConfig WET = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.WATER, 5).put(ElementType.EARTH, 1).build());
	public static final RandomElementTypeFeatureConfig DRY = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.FIRE, 5).put(ElementType.EARTH, 2).put(ElementType.AIR, 1).build());
	public static final RandomElementTypeFeatureConfig END = new RandomElementTypeFeatureConfig(ImmutableMap.<ElementType, Integer>builder().put(ElementType.AIR, 5).put(ElementType.FIRE, 1).build());
	public static final RandomElementTypeFeatureConfig FOREST = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.EARTH, 2).put(ElementType.WATER, 1).build());
	public static final RandomElementTypeFeatureConfig HILL = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.EARTH, 4).put(ElementType.AIR, 1).build());
	public static final RandomElementTypeFeatureConfig PLAIN = new RandomElementTypeFeatureConfig(
			ImmutableMap.<ElementType, Integer>builder().put(ElementType.EARTH, 2).put(ElementType.WATER, 1).put(ElementType.AIR, 1).put(ElementType.FIRE, 1).build());

	public static final Codec<RandomElementTypeFeatureConfig> CODEC = Codec.unboundedMap(ElementType.CODEC, Codec.INT).fieldOf(ECNames.ELEMENT_TYPE)
			.xmap(RandomElementTypeFeatureConfig::new, c -> c.elementTypes).codec();

	private final Map<ElementType, Integer> elementTypes;

	public RandomElementTypeFeatureConfig(Map<ElementType, Integer> elementTypes) {
		if (elementTypes.keySet().contains(ElementType.NONE)) {
			throw new IllegalArgumentException("elementTypes must not contain NONE!");
		}
		this.elementTypes = new EnumMap<>(elementTypes);
	}

	@Override
	public ElementType getElementType(Random rand) {
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
