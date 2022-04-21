package sirttas.elementalcraft.jewel.handler;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nonnull;
import java.util.List;

public interface IJewelHandler {

    Capability<IJewelHandler> JEWEL_HANDLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    @Nonnull
    List<Jewel> getActiveJewels();

}
