package sirttas.elementalcraft.block.pipe;

import java.util.stream.Stream;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.block.pipe.IElementPipe.ConnectionType;

public class ElementPipeRenderer extends AbstractECRenderer<ElementPipeBlockEntity> {

	public static final ResourceLocation SIDE_LOCATION = ElementalCraft.createRL("block/elementpipe_side");
	public static final ResourceLocation EXTRACT_LOCATION = ElementalCraft.createRL("block/elementpipe_extract");
	public static final ResourceLocation PRIORITY_LOCATION = ElementalCraft.createRL("block/elementpipe_priority");
	
	private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	
	private IBakedModel sideModel;
	private IBakedModel extractModel;
	private IBakedModel prioritytModel;
	
	public ElementPipeRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@SuppressWarnings("resource")
	@Override
	public void render(ElementPipeBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		BlockState coverState = te.getCoverState();

		if (sideModel == null || extractModel == null || prioritytModel == null) {
			ModelManager modelManager = Minecraft.getInstance().getModelManager();
			
			sideModel = modelManager.getModel(SIDE_LOCATION);
			extractModel = modelManager.getModel(EXTRACT_LOCATION);
			prioritytModel = modelManager.getModel(PRIORITY_LOCATION);
		}
		if (coverState != null && ElementPipeBlock.showCover(te.getBlockState(), Minecraft.getInstance().player)) {
			renderBlock(coverState, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
		} else {
			renderPipes(te, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
			if (coverState != null) {
				renderBlock(te.getBlockState().setValue(ElementPipeBlock.COVER, CoverType.NONE), matrixStack, buffer, combinedLightIn, combinedOverlayIn);
				WorldRenderer.renderLineBox(matrixStack, buffer.getBuffer(RenderType.lines()), BOX, 0F, 0F, 0F, 1);
			}
		}
	}
	
	private void renderPipes(ElementPipeBlockEntity te, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.5, 0.5);
		Stream.of(Direction.values()).forEach(d -> renderSide(te, d, matrixStack, buffer, light, overlay));
		matrixStack.popPose();
	}
	
	private void renderSide(ElementPipeBlockEntity te, Direction side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		ConnectionType connection = te.getConection(side);
		
		if (connection.isConnected()) {
			matrixStack.pushPose();
			matrixStack.mulPose(side.getRotation());
			matrixStack.translate(-0.5, -0.5, -0.5);
			this.renderModel(matrixStack, buffer, te.getBlockState(), sideModel, light, overlay);
			if (connection == ConnectionType.EXTRACT) {
				this.renderModel(matrixStack, buffer, te.getBlockState(), extractModel, light, overlay);
			}
			if (te.isPriority(side)) {
				this.renderModel(matrixStack, buffer, te.getBlockState(), prioritytModel, light, overlay);
			}
			matrixStack.popPose();
		}
	}
}
