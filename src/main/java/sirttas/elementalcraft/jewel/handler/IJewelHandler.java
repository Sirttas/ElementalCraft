package sirttas.elementalcraft.jewel.handler;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nonnull;
import java.util.List;

@AutoRegisterCapability
public interface IJewelHandler {

    Capability<IJewelHandler> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    @Nonnull
    List<Jewel> getActiveJewels();

}
