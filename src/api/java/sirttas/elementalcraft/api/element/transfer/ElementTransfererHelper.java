package sirttas.elementalcraft.api.element.transfer;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;

import javax.annotation.Nonnull;

public class ElementTransfererHelper {

    private ElementTransfererHelper() {}

    @Nonnull
    public static LazyOptional<IElementTransferer> get(ICapabilityProvider provider, Direction side) {
        return ElementalCraftCapabilities.ELEMENT_TRANSFERER != null ? provider.getCapability(ElementalCraftCapabilities.ELEMENT_TRANSFERER, side) : LazyOptional.empty();
    }
}
