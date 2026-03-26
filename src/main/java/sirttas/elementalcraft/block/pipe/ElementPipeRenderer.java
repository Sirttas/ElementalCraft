package sirttas.elementalcraft.block.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.cover.CoverType;
import sirttas.elementalcraft.block.pipe.upgrade.renderer.PipeUpgradeRenderers;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import java.util.Objects;

public class ElementPipeRenderer implements BlockEntityRenderer<ElementPipeBlockEntity> {

	public static final ModelIdentifier SIDE_LOCATION = ECModelHelper.createStandaloneKey("block/elementpipe_side");
	public static final ModelIdentifier EXTRACT_LOCATION = ECModelHelper.createStandaloneKey("block/elementpipe_extract");
	
	private static final AABB BOX = new AABB(0, 0, 0, 1, 1, 1);
	
	private BakedModel sideModel;
	private BakedModel extractModel;


	@Override
	public void render(ElementPipeBlockEntity pipe, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
		var minecraft = Minecraft.getInstance();
		var player = minecraft.player;
		var level = Objects.requireNonNull(pipe.getLevel());
        var hasCover = pipe.isCovered();

		if (sideModel == null || extractModel == null) {
			ModelManager modelManager = minecraft.getModelManager();
			
			sideModel = modelManager.getModel(SIDE_LOCATION);
			extractModel = modelManager.getModel(EXTRACT_LOCATION);
		}
		if (hasCover && pipe.showCover(player)) {
			ECRendererHelper.renderBatched( pipe.getCoverState(), poseStack, buffer, pipe.getLevel(), pipe.getBlockPos());
		} else {
			renderPipes(pipe, partialTicks, poseStack, buffer, light, overlay);
			if (hasCover) {
				ECRendererHelper.renderBlock(pipe.getBlockState().setValue(CoverType.PROPERTY, CoverType.NONE), poseStack, buffer, light, overlay, ECRendererHelper.getModelData(level, pipe.getBlockPos()));
				LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.lines()), BOX, 0F, 0F, 0F, 1);
			}
		}
	}
	
	private void renderPipes(ElementPipeBlockEntity pipe, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.5, 0.5);
        for (Direction direction : Direction.values()) {
            renderSide(pipe, partialTicks, direction, matrixStack, buffer, light, overlay);
        }
		matrixStack.popPose();
	}
	
	private void renderSide(ElementPipeBlockEntity pipe, float partialTicks, Direction side, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
		var connection = pipe.getConnection(side);
		var upgrade = pipe.getUpgrade(side);
		var renderShape = upgrade == null ? RenderShape.INVISIBLE : upgrade.getRenderShape();

		poseStack.pushPose();
		poseStack.mulPose(side.getRotation());
		poseStack.translate(-0.5, -0.5, -0.5);
		if (upgrade != null) {
			if (renderShape == RenderShape.MODEL) {
				ECRendererHelper.renderModel(upgrade.getType().getModel(), poseStack, buffer, pipe, light, overlay);
			}
			if (renderShape != RenderShape.INVISIBLE) {
				var upgradeRenderer = PipeUpgradeRenderers.get(upgrade);

				if (upgradeRenderer != null) {
					upgradeRenderer.render(upgrade, pipe, partialTicks, poseStack, buffer, light, overlay);
				}
			}
		}
		if (connection.isConnected()) {
			if (upgrade == null || !upgrade.replaceSection()) {
				ECRendererHelper.renderModel(sideModel, poseStack, buffer, pipe, light, overlay);
			}
			if (connection == ConnectionType.EXTRACT && (upgrade == null || !upgrade.replaceExtraction())) {
				ECRendererHelper.renderModel(extractModel, poseStack, buffer, pipe, light, overlay);
			}
		}
		poseStack.popPose();
	}
}
