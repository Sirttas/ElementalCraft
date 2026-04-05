package sirttas.elementalcraft.block.pipe.upgrade.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.rune.RuneModelResolver;

@FunctionalInterface
public interface PipeUpgradeRendererProvider<T extends PipeUpgrade, S extends PipeUpgradeRenderState> {

    PipeUpgradeRenderer<T, S> create(Context context);

    record Context(BlockEntityRendererProvider.Context context, RuneModelResolver runeModelResolver) {

        public Context(BlockEntityRendererProvider.Context context) {
            this(context, ECModelResolver.get(RuneModelResolver.IDENTIFIER));
        }
    }
}
