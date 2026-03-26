package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public abstract class SingleItemBlockEntityRenderer<T extends BlockEntity, S extends SingleItemBlockEntityRenderState> extends RuneBlockEntityRenderer<@NotNull T, @NotNull S> {

    protected final ItemModelResolver itemModelResolver;
	private final Vec3 position;
	private final float size;

	protected SingleItemBlockEntityRenderer(BlockEntityRendererProvider.Context context, Vec3 position) {
		this(context, position, 1);
	}

    protected SingleItemBlockEntityRenderer(BlockEntityRendererProvider.Context context, Vec3 position, float size) {
        this.itemModelResolver = context.itemModelResolver();
		this.position = position;
		this.size = size;
	}

    public static <T extends BlockEntity> SingleItemBlockEntityRenderer<T, SingleItemBlockEntityRenderState> create(BlockEntityRendererProvider.Context context, Vec3 position) {
        return create(context, position, 1);
    }

    public static <T extends BlockEntity> SingleItemBlockEntityRenderer<T, SingleItemBlockEntityRenderState> create(BlockEntityRendererProvider.Context context, Vec3 position, float size) {
        return new DefaultSingleItemBlockEntityRenderer<>(context, position, size);
    }

    @Override
    public void extractRenderState(T blockEntity, S renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.partialTick = partialTick;

        var inv = blockEntity.getLevel().getCapability(Capabilities.Item.BLOCK, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);

        itemModelResolver.updateForTopItem(renderState.item, inv == null ? ItemStack.EMPTY : inv.getResource(0).toStack(inv.getAmountAsInt(0)), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(S renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        if (!renderState.item.isEmpty()) {
            poseStack.translate(position.x, position.y, position.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(ECRendererHelper.getClientTicks(renderState.partialTick)));
            poseStack.scale(size, size, size);
            renderState.item.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
    }

    private static class DefaultSingleItemBlockEntityRenderer<T extends BlockEntity> extends SingleItemBlockEntityRenderer<T, SingleItemBlockEntityRenderState> {
        public DefaultSingleItemBlockEntityRenderer(BlockEntityRendererProvider.Context context, Vec3 position, float size) {
            super(context, position, size);
        }

        @Override
        public @NotNull SingleItemBlockEntityRenderState createRenderState() {
            return new SingleItemBlockEntityRenderState();
        }
    }
}
