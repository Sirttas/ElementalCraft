package sirttas.elementalcraft.block.instrument.io;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.container.IContainerBlockEntity;

public abstract class IOInstrumentRenderer<T extends BlockEntity & IContainerBlockEntity, S extends IOInstrumentRenderState> implements BlockEntityRenderer<@NotNull T, @NotNull S> {

    private final ItemModelResolver itemModelResolver;

    protected IOInstrumentRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.partialTick = partialTick;
        renderState.facing = blockEntity.getBlockState().getOptionalValue(BlockStateProperties.HORIZONTAL_FACING).orElse(Direction.NORTH);
        renderState.runes.update(blockEntity, partialTick);

        Container inv = blockEntity.getInventory();
        itemModelResolver.updateForTopItem(renderState.material, inv.getItem(0), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.result, inv.getItem(1), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
    }
}
