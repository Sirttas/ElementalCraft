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
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.rune.RuneModelResolver;

public abstract class IOInstrumentRenderer<T extends BlockEntity & IContainerBlockEntity, S extends IOInstrumentRenderState> implements BlockEntityRenderer<@NotNull T, @NotNull S> {

    private final ItemModelResolver itemModelResolver;
    private final RuneModelResolver runeModelResolver;

    protected IOInstrumentRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.runeModelResolver = ECModelResolver.get(RuneModelResolver.IDENTIFIER);
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTicks, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTicks, cameraPosition, breakProgress);
        renderState.partialTicks = partialTicks;
        renderState.facing = blockEntity.getBlockState().getOptionalValue(BlockStateProperties.HORIZONTAL_FACING).orElse(Direction.NORTH);
        renderState.runes.update(blockEntity, runeModelResolver, partialTicks);

        Container inv = blockEntity.getInventory();
        itemModelResolver.updateForTopItem(renderState.material, inv.getItem(0), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.result, inv.getItem(1), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
    }
}
