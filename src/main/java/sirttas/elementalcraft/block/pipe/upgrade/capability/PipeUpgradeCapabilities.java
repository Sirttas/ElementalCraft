package sirttas.elementalcraft.block.pipe.upgrade.capability;

import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;

import java.util.ArrayList;
import java.util.Objects;

public class PipeUpgradeCapabilities {

    private PipeUpgradeCapabilities() {}

    public static final PipeUpgradeCapability<IRuneHandler, Void>  RUNE_HANDLER = PipeUpgradeCapability.createVoid(ElementalCraftApi.createRL("rune_handler"), IRuneHandler.class);

    @SuppressWarnings("unchecked")
    public static <T, C, P extends PipeUpgrade> void register(PipeUpgradeCapability<T, C> capability, PipeUpgradeType<P> pipeUpgradeType, ICapabilityProvider<? super P, C, T> provider) {
        Objects.requireNonNull(provider);
        capability.providers.computeIfAbsent(pipeUpgradeType, k -> new ArrayList<>()).add((ICapabilityProvider<PipeUpgrade, C, T>) provider);
    }
}
