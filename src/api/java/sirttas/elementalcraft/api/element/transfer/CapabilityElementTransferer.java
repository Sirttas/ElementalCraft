package sirttas.elementalcraft.api.element.transfer;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class CapabilityElementTransferer {

    public static final Capability<IElementTransferer> ELEMENT_TRANSFERER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    private CapabilityElementTransferer() {}

    @Nonnull
    public static LazyOptional<IElementTransferer> get(ICapabilityProvider provider, Direction side) {
        return ELEMENT_TRANSFERER_CAPABILITY != null ? provider.getCapability(ELEMENT_TRANSFERER_CAPABILITY, side) : LazyOptional.empty();
    }
}
