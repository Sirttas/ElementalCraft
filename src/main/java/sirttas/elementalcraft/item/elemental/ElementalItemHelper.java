package sirttas.elementalcraft.item.elemental;

import net.minecraft.world.item.Item;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

public class ElementalItemHelper {

    public static final String ERROR_MESSAGE = "Element Type must not be NONE";

    private ElementalItemHelper() {}

    public static Item getCrystalForType(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.AIR_CRYSTAL.get();
            case EARTH -> ECItems.EARTH_CRYSTAL.get();
            case FIRE -> ECItems.FIRE_CRYSTAL.get();
            case WATER -> ECItems.WATER_CRYSTAL.get();
            default -> ECItems.INERT_CRYSTAL.get();
        };
    }

    public static Item getShardForType(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.AIR_SHARD.get();
            case EARTH -> ECItems.EARTH_SHARD.get();
            case FIRE -> ECItems.FIRE_SHARD.get();
            case WATER -> ECItems.WATER_SHARD.get();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static Item getPowerfulShardForType(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.POWERFUL_AIR_SHARD.get();
            case EARTH -> ECItems.POWERFUL_EARTH_SHARD.get();
            case FIRE -> ECItems.POWERFUL_FIRE_SHARD.get();
            case WATER -> ECItems.POWERFUL_WATER_SHARD.get();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static Item getDisplacementPlate(ElementType type) {
        return switch (type) {
            case AIR -> ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            case EARTH -> ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            case FIRE -> ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            case WATER -> ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }
}
