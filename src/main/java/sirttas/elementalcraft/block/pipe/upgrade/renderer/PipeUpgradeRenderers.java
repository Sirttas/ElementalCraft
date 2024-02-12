package sirttas.elementalcraft.block.pipe.upgrade.renderer;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.beam.ElementBeamPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.pump.ElementPumpPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.pipe.upgrade.valve.ElementValvePipeUpgradeRenderer;

import java.util.HashMap;
import java.util.Map;

public class PipeUpgradeRenderers {

    private static final Map<ResourceLocation, IPipeUpgradeRenderer<?>> RENDERERS = new HashMap<>();

    static {
        register(PipeUpgradeTypes.ELEMENT_VALVE, new ElementValvePipeUpgradeRenderer());
        register(PipeUpgradeTypes.ELEMENT_BEAM, new ElementBeamPipeUpgradeRenderer());
        register(PipeUpgradeTypes.ELEMENT_PUMP, new ElementPumpPipeUpgradeRenderer());
    }

    private PipeUpgradeRenderers() {}

    @SuppressWarnings("unchecked")
    public static <T extends PipeUpgrade> IPipeUpgradeRenderer<T> get(T upgrade) {
        return (IPipeUpgradeRenderer<T>) get(upgrade.getType());
    }

    @SuppressWarnings("unchecked")
    public static <T extends PipeUpgrade> IPipeUpgradeRenderer<T> get(PipeUpgradeType<T> type) {
        return (IPipeUpgradeRenderer<T>) RENDERERS.get(type.getKey());
    }

    public static <T extends PipeUpgrade> void register(DeferredHolder<PipeUpgradeType<?>, ? extends PipeUpgradeType<T>> type, IPipeUpgradeRenderer<T> renderer) {
        RENDERERS.put(type.getId(), renderer);
    }
}
