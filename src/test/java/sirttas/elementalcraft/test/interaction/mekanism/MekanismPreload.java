package sirttas.elementalcraft.test.interaction.mekanism;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.interaction.ECInteractions;

public class MekanismPreload implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!ECInteractions.isMekanismActive()) {
            ElementalCraftApi.LOGGER.debug("Mekanism is not active, we don't need to preload frequencies");
            return;
        }
        try {
            Class.forName("mekanism.common.lib.frequency.FrequencyManager")
                    .getMethod("load")
                    .invoke(null);
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.warn("Mekanism frequencies threw an exception but it should be thinking it is loaded now", e);
        }
    }
}
