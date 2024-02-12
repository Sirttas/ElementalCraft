package sirttas.elementalcraft.jewel.handler;

import net.neoforged.neoforge.capabilities.EntityCapability;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nonnull;
import java.util.List;

public interface IJewelHandler {

    EntityCapability<IJewelHandler, Void> CAPABILITY = EntityCapability.createVoid(ElementalCraftApi.createRL("jewel_handler"), IJewelHandler.class);

    @Nonnull
    List<Jewel> getActiveJewels();

}
