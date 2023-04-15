package sirttas.elementalcraft.block.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.block.pipe.upgrade.renderer.PipeUpgradeRenderers;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import java.util.Objects;
import java.util.stream.Stream;

public class ElementPipeRenderer implements BlockEntityRenderer<ElementPipeBlockEntity> {

	public static final ResourceLocation SIDE_LOCATION = ElementalCraft.createRL("block/elementpipe_side");
	public static final ResourceLocation EXTRACT_LOCATION = ElementalCraft.createRL("block/elementpipe_extract");
	
	private static final AABB BOX = new AABB(0, 0, 0, 1, 1, 1);
	
	private BakedModel sideModel;
	private BakedModel extractModel;


	@Override
	public void render(ElementPipeBlockEntity te, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
		var minecraft = Minecraft.getInstance();
		var player = minecraft.player;
		var level = Objects.requireNonNull(te.getLevel());
		BlockState coverState = te.getCoverState();
		boolean showCover = coverState != null && !coverState.isAir();

		if (sideModel == null || extractModel == null ) {
			ModelManager modelManager = minecraft.getModelManager();
			
			sideModel = modelManager.getModel(SIDE_LOCATION);
			extractModel = modelManager.getModel(EXTRACT_LOCATION);
		}
		if (showCover && ElementPipeBlock.showCover(te.getBlockState(), player)) {
			ECRendererHelper.renderBatched(coverState, poseStack, buffer, te.getLevel(), te.getBlockPos());
		} else {
			renderPipes(te, partialTicks, poseStack, buffer, light, overlay);
			if (showCover) {
				ECRendererHelper.renderBlock(te.getBlockState().setValue(ElementPipeBlock.COVER, CoverType.NONE), poseStack, buffer, light, overlay, ECRendererHelper.getModelData(level, te.getBlockPos()));
				LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.lines()), BOX, 0F, 0F, 0F, 1);
			}
		}
	}
	
	private void renderPipes(ElementPipeBlockEntity te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.5, 0.5);
		Stream.of(Direction.values()).forEach(d -> renderSide(te, partialTicks, d, matrixStack, buffer, light, overlay));
		matrixStack.popPose();
	}
	
	private void renderSide(ElementPipeBlockEntity te, float partialTicks, Direction side, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		var connection = te.getConnection(side);
		var upgrade = te.getUpgrade(side);
		var renderShape = upgrade == null ? RenderShape.INVISIBLE : upgrade.getRenderShape();

		matrixStack.pushPose();
		matrixStack.mulPose(side.getRotation());
		matrixStack.translate(-0.5, -0.5, -0.5);
		if (upgrade != null) {
			if (renderShape == RenderShape.MODEL) {
				ECRendererHelper.renderModel(upgrade.getType().getModel(), matrixStack, buffer, te, light, overlay);
			}
			if (renderShape != RenderShape.INVISIBLE) {
				var upgradeRenderer = PipeUpgradeRenderers.get(upgrade);

				if (upgradeRenderer != null) {
					upgradeRenderer.render(upgrade, te, partialTicks, matrixStack, buffer, light, overlay);
				}
			}
		}
		if (connection.isConnected()) {
			if (upgrade == null || !upgrade.replaceSection()) {
				ECRendererHelper.renderModel(sideModel, matrixStack, buffer, te, light, overlay);
			}
			if (connection == ConnectionType.EXTRACT && (upgrade == null || !upgrade.replaceExtraction())) {
				ECRendererHelper.renderModel(extractModel, matrixStack, buffer, te, light, overlay);
			}
		}
		matrixStack.popPose();
	}
}
