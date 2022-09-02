package sirttas.elementalcraft.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;

public class ElementalCraftCapabilities {

    public static final Capability<IElementStorage> ELEMENT_STORAGE = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IElementTransferer> ELEMENT_TRANSFERER = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<ISourceTraitHolder> SOURCE_TRAIT_HOLDER = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IRuneHandler> RUNE_HANDLE = CapabilityManager.get(new CapabilityToken<>(){});

    private ElementalCraftCapabilities() {}

}
