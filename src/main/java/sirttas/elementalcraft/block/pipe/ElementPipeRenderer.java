package sirttas.elementalcraft.block.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.ModelDataManager;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.config.ECConfig;

import java.util.Objects;
import java.util.stream.Stream;

public class ElementPipeRenderer implements IECRenderer<ElementPipeBlockEntity> {

	public static final ResourceLocation SIDE_LOCATION = ElementalCraft.createRL("block/elementpipe_side");
	public static final ResourceLocation EXTRACT_LOCATION = ElementalCraft.createRL("block/elementpipe_extract");
	public static final ResourceLocation PRIORITY_LOCATION = ElementalCraft.createRL("block/elementpipe_priority");
	
	private static final AABB BOX = new AABB(0, 0, 0, 1, 1, 1);
	
	private BakedModel sideModel;
	private BakedModel extractModel;
	private BakedModel priorityModel;

	@Override
	public void render(ElementPipeBlockEntity te, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
		var minecraft = Minecraft.getInstance();
		var player = minecraft.player;
		var level = Objects.requireNonNull(te.getLevel());
		BlockState coverState = te.getCoverState();

		if (sideModel == null || extractModel == null || priorityModel == null) {
			ModelManager modelManager = minecraft.getModelManager();
			
			sideModel = modelManager.getModel(SIDE_LOCATION);
			extractModel = modelManager.getModel(EXTRACT_LOCATION);
			priorityModel = modelManager.getModel(PRIORITY_LOCATION);
		}
		if (coverState != null && ElementPipeBlock.showCover(te.getBlockState(), player)) {
			renderBlock(coverState, matrixStack, buffer, combinedLightIn, combinedOverlayIn, ModelDataManager.getModelData(level, te.getBlockPos()));
		} else {
			renderPipes(te, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
			if (coverState != null) {
				renderBlock(te.getBlockState().setValue(ElementPipeBlock.COVER, CoverType.NONE), matrixStack, buffer, combinedLightIn, combinedOverlayIn,
						ModelDataManager.getModelData(level, te.getBlockPos()));
				LevelRenderer.renderLineBox(matrixStack, buffer.getBuffer(RenderType.lines()), BOX, 0F, 0F, 0F, 1);
			}
		}
		if (Boolean.TRUE.equals(ECConfig.CLIENT.pipeDebugPath.get()) && player != null && player.isCreative()) {
			te.getPathMap().values().forEach(path -> path.renderDebugPath(matrixStack, buffer));
		}
	}
	
	private void renderPipes(ElementPipeBlockEntity te, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.5, 0.5);
		Stream.of(Direction.values()).forEach(d -> renderSide(te, d, matrixStack, buffer, light, overlay));
		matrixStack.popPose();
	}
	
	private void renderSide(ElementPipeBlockEntity te, Direction side, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		IElementTransferer.ConnectionType connection = te.getConnection(side);
		
		if (connection.isConnected()) {
			matrixStack.pushPose();
			matrixStack.mulPose(side.getRotation());
			matrixStack.translate(-0.5, -0.5, -0.5);
			this.renderModel(sideModel, matrixStack, buffer, te, light, overlay);
			if (connection == IElementTransferer.ConnectionType.EXTRACT) {
				this.renderModel(extractModel, matrixStack, buffer, te, light, overlay);
			}
			if (te.isPriority(side)) {
				this.renderModel(priorityModel, matrixStack, buffer, te, light, overlay);
			}
			matrixStack.popPose();
		}
	}
}
