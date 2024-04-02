package sirttas.elementalcraft.item.elemental;

import net.minecraft.world.item.Item;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

public class ElementalItemHelper {

    public static final String ERROR_MESSAGE = "Element Type must not be NONE";

    private ElementalItemHelper() {}

    public static Item getCrystalForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.AIR_CRYSTAL.get();
            case EARTH -> ECItems.EARTH_CRYSTAL.get();
            case FIRE -> ECItems.FIRE_CRYSTAL.get();
            case WATER -> ECItems.WATER_CRYSTAL.get();
            default -> ECItems.INERT_CRYSTAL.get();
        };
    }

    public static Item getShardForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.AIR_SHARD.get();
            case EARTH -> ECItems.EARTH_SHARD.get();
            case FIRE -> ECItems.FIRE_SHARD.get();
            case WATER -> ECItems.WATER_SHARD.get();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static Item getPowerfulShardForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.POWERFUL_AIR_SHARD.get();
            case EARTH -> ECItems.POWERFUL_EARTH_SHARD.get();
            case FIRE -> ECItems.POWERFUL_FIRE_SHARD.get();
            case WATER -> ECItems.POWERFUL_WATER_SHARD.get();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static Item getCrudeGemForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.CRUDE_AIR_GEM.get();
            case EARTH -> ECItems.CRUDE_EARTH_GEM.get();
            case FIRE -> ECItems.CRUDE_FIRE_GEM.get();
            case WATER -> ECItems.CRUDE_WATER_GEM.get();
            default -> ECItems.INERT_CRYSTAL.get();
        };
    }

    public static Item getFineGemForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.FINE_AIR_GEM.get();
            case EARTH -> ECItems.FINE_EARTH_GEM.get();
            case FIRE -> ECItems.FINE_FIRE_GEM.get();
            case WATER -> ECItems.FINE_WATER_GEM.get();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }

    public static Item getPristineGemForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.PRISTINE_AIR_GEM.get();
            case EARTH -> ECItems.PRISTINE_EARTH_GEM.get();
            case FIRE -> ECItems.PRISTINE_FIRE_GEM.get();
            case WATER -> ECItems.PRISTINE_WATER_GEM.get();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }
    public static Item getDisplacementPlateForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            case EARTH -> ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            case FIRE -> ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            case WATER -> ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE.get().asItem();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
        };
    }
}
