package sirttas.elementalcraft.block.instrument.crystallizer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.renderer.ECRendererHelper;

@OnlyIn(Dist.CLIENT)
public class CrystallizerRenderer implements BlockEntityRenderer<@NotNull CrystallizerBlockEntity, @NotNull CrystallizerRenderState> {

    private final ItemModelResolver itemModelResolver;

    public CrystallizerRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CrystallizerRenderState createRenderState() {
        return new CrystallizerRenderState();
    }

    @Override
    public void extractRenderState(CrystallizerBlockEntity blockEntity, CrystallizerRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.partialTick = partialTick;
        renderState.runes.update(blockEntity, partialTick);

        Container inv = blockEntity.getInventory();
        itemModelResolver.updateForTopItem(renderState.gem, inv.getItem(0), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.crystal, inv.getItem(1), ItemDisplayContext.GROUND, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(CrystallizerRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        float tick = ECRendererHelper.getClientTicks(renderState.partialTick);

        poseStack.translate(0F, 0.25F, 0F);
        renderState.runes.submit(renderState, poseStack, nodeCollector);
        poseStack.translate(0.5F, 0.15F, 0.5F);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        submitGem(renderState, poseStack, nodeCollector, tick);
        submitCrystal(renderState, poseStack, nodeCollector, tick);
    }

	private void submitGem(CrystallizerRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, float tick) {
		if (!renderState.gem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0F, -0.15F, 0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(tick));
            renderState.gem.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
		}
	}

	private void submitCrystal(CrystallizerRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, float tick) {
		if (!renderState.crystal.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0F, 0.9F, 0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-tick));
            renderState.crystal.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
		}
	}
}
