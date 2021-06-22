package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.event.TickHandler;

public abstract class AbstractECRenderer<T extends TileEntity> extends TileEntityRenderer<T> {

	protected AbstractECRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	public Quaternion getRotation(Direction direction) {
		switch (direction) {
		case SOUTH:
			return Vector3f.YP.rotationDegrees(180.0F);
		case WEST:
			return Vector3f.YP.rotationDegrees(90.0F);
		case EAST:
			return Vector3f.YP.rotationDegrees(-90.0F);
		case NORTH:
		default:
			return Quaternion.ONE.copy();
		}
	}

	public void renderItem(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, matrixStack, buffer);
	}

	public void renderBlock(BlockState state, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, IModelData data) {
		Minecraft.getInstance().getBlockRenderer().renderBlock(state, matrixStack, buffer, light, overlay, data);
	}

	public void renderBlock(BlockState state, MatrixStack matrixStack, IVertexBuilder builder, World world, BlockPos pos) {
		matrixStack.pushPose();
		Minecraft.getInstance().getBlockRenderer().renderModel(state, pos, world, matrixStack, builder, false, world.random, ModelDataManager.getModelData(world, pos));
		matrixStack.popPose();
	}
	
	public void renderRunes(MatrixStack matrixStack, IRenderTypeBuffer buffer, IRuneHandler handler, float tick, int light, int overlay) {
		int ruenCount = handler.getRuneCount();

		matrixStack.pushPose();
		matrixStack.translate(0.5F, 0.75F, 0.5F);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick / 2));
		handler.getRunes().forEach(rune -> {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F / ruenCount));
			matrixStack.pushPose();
			matrixStack.translate(0.75F, 0F, 0F);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
			matrixStack.scale(1F / 64F, 1F / 64F, 1F / 64F);
			renderIcon(matrixStack, buffer, rune.getSprite(), 16, -16, light, overlay);
			matrixStack.popPose();
		});
		matrixStack.popPose();
	}

	public void renderIcon(MatrixStack matrixStack, IRenderTypeBuffer buffer, RenderMaterial renderMaterial, int width, int height, int light, int overlay) {
		renderIcon(matrixStack, buffer, 0, 0, renderMaterial, width, height, 1F, 1F, 1F, light, overlay);
	}
	
	public void renderIcon(MatrixStack matrixStack, IRenderTypeBuffer buffer, float x, float y, RenderMaterial renderMaterial, int width, int height, float r, float g, float b, int light, int overlay) {
		Matrix4f matrix = matrixStack.last().pose();
		Matrix3f normal = matrixStack.last().normal();
		IVertexBuilder builder = renderMaterial.buffer(buffer, RenderType::entityTranslucent);
		TextureAtlasSprite sprite = renderMaterial.sprite();

		builder.vertex(matrix, x, y, 0).color(r, g, b, 1F).uv(sprite.getU0(), sprite.getV0()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, x + width, y, 0).color(r, g, b, 1F).uv(sprite.getU1(), sprite.getV0()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, x + width, y + height, 0).color(r, g, b, 1F).uv(sprite.getU1(), sprite.getV1()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, x, y + height, 0).color(r, g, b, 1F).uv(sprite.getU0(), sprite.getV1()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
	}

	public void renderModel(IBakedModel model, MatrixStack matrixStack, IRenderTypeBuffer buffer, BlockState state, int light, int overlay) {
		renderModel(model, matrixStack, buffer, state, light, overlay, EmptyModelData.INSTANCE);
	}
	
	public void renderModel(IBakedModel model, MatrixStack matrixStack, IRenderTypeBuffer buffer, T te, int light, int overlay) {
		renderModel(model, matrixStack, buffer, te.getBlockState(), light, overlay, getModelData(model, te));
	}

	public void renderModel(IBakedModel model, MatrixStack matrixStack, IRenderTypeBuffer buffer, BlockState state, int light, int overlay, IModelData data) {
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(state != null ? RenderTypeLookup.getRenderType(state, false)  : Atlases.cutoutBlockSheet()), state, model, 1, 1, 1, light,
				overlay, data);
	}
	
	public float getAngle(float partialTicks) {
		return TickHandler.getTicksInGame() + partialTicks % 360;
	}
	
	public IModelData getModelData(IBakedModel model, T te) {
		World world = te.getLevel();
		BlockPos pos = te.getBlockPos();
		
        return model.getModelData(world, pos, te.getBlockState(), ModelDataManager.getModelData(world, pos));
	}

}
