package sirttas.elementalcraft.item.elemental;

import sirttas.elementalcraft.api.element.ElementType;

public class ItemShard extends ItemElemental {

	private static final String NAME = "shard";
	private static final String POWERFUL = "powerful_";

	public static final String NAME_FIRE = "fire_" + NAME;
	public static final String NAME_WATER = "water_" + NAME;
	public static final String NAME_EARTH = "earth_" + NAME;
	public static final String NAME_AIR = "air_" + NAME;

	public static final String NAME_FIRE_POWERFUL = POWERFUL + NAME_FIRE;
	public static final String NAME_WATER_POWERFUL = POWERFUL + NAME_WATER;
	public static final String NAME_EARTH_POWERFUL = POWERFUL + NAME_EARTH;
	public static final String NAME_AIR_POWERFUL = POWERFUL + NAME_AIR;

	private final int elementAmount;

	public ItemShard(ElementType elementType) {
		this(elementType, 1);
	}

	public ItemShard(ElementType elementType, int elementAmount) {
		super(elementType);
		this.elementAmount = elementAmount;
	}

	public int getElementAmount() {
		return elementAmount;
	}
}
