package sirttas.elementalcraft.item.elemental;

import net.minecraft.world.item.Item;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;

public class ElementalItemHelper { // TODO move out because its unused in production

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

    public static Item getCrudeGemForElement(ElementType type) {
        return switch (type) {
            case AIR -> ECItems.CRUDE_AIR_GEM.get();
            case EARTH -> ECItems.CRUDE_EARTH_GEM.get();
            case FIRE -> ECItems.CRUDE_FIRE_GEM.get();
            case WATER -> ECItems.CRUDE_WATER_GEM.get();
            default -> throw new IllegalArgumentException(ERROR_MESSAGE);
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
}
