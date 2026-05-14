package sirttas.elementalcraft.block.pipe.upgrade.render;

import com.google.common.collect.ImmutableMap;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.beam.ElementBeamPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.pump.ElementPumpPipeUpgradeRenderer;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.pipe.upgrade.valve.ElementValvePipeUpgradeRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PipeUpgradeRenderers {

    private static final Map<PipeUpgradeType<?>, PipeUpgradeRendererProvider<?, ?>> PROVIDERS = new HashMap<>();

    static {
        register(PipeUpgradeTypes.ELEMENT_VALVE, ElementValvePipeUpgradeRenderer::new);
        register(PipeUpgradeTypes.ELEMENT_BEAM, ElementBeamPipeUpgradeRenderer::new);
        register(PipeUpgradeTypes.ELEMENT_PUMP, ElementPumpPipeUpgradeRenderer::new);
    }

    private PipeUpgradeRenderers() {}

    public static Map<PipeUpgradeType<?>, PipeUpgradeRenderer<?, ?>> createRenderers(PipeUpgradeRendererProvider.Context context) {
        ImmutableMap.Builder<PipeUpgradeType<?>, PipeUpgradeRenderer<?, ?>> result = ImmutableMap.builder();
        PROVIDERS.forEach((type, provider) -> {
            try {
                result.put(type, provider.create(context));
            } catch (Exception e) {
                throw new IllegalStateException("Failed to create model for " + type.getKey(), e);
            }
        });
        return result.build();
    }

    public static <T extends PipeUpgrade, S extends PipeUpgradeRenderState> void register(DeferredHolder<PipeUpgradeType<?>, ? extends PipeUpgradeType<T>> type, Supplier<PipeUpgradeRenderer<T, S>> supplier) {
        register(type, c -> supplier.get());
    }

    public static <T extends PipeUpgrade, S extends PipeUpgradeRenderState> void register(DeferredHolder<PipeUpgradeType<?>, ? extends PipeUpgradeType<T>> type, PipeUpgradeRendererProvider<T, S> provider) {
        PROVIDERS.put(type.get(), provider);
    }
}
