package sirttas.elementalcraft.block.tile.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraftforge.client.model.data.EmptyModelData;
import sirttas.elementalcraft.event.TickHandler;
import sirttas.elementalcraft.rune.handler.IRuneHandler;

public abstract class AbstractRendererEC<T extends TileEntity> extends TileEntityRenderer<T> {

	protected AbstractRendererEC(TileEntityRendererDispatcher rendererDispatcherIn) {
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
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, matrixStack, buffer);
	}

	public void renderBlock(BlockState state, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, light, overlay, EmptyModelData.INSTANCE);
	}

	public void renderBlock(BlockState state, MatrixStack matrixStack, IVertexBuilder builder, World world) {
		Minecraft.getInstance().getBlockRendererDispatcher().renderModel(state, BlockPos.ZERO, world, matrixStack, builder, false, world.rand, EmptyModelData.INSTANCE);
	}

	public void renderRunes(MatrixStack matrixStack, IRenderTypeBuffer buffer, IRuneHandler handler, float tick, int light, int overlay) {
		int ruenCount = handler.getRuneCount();

		matrixStack.push();
		matrixStack.translate(0.5F, 0.75F, 0.5F);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(tick / 2));
		handler.getRunes().forEach(rune -> {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(90F / ruenCount));
			matrixStack.push();
			matrixStack.translate(0.75F, 0F, 0F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
			matrixStack.scale(1F / 64F, 1F / 64F, 1F / 64F);
			renderIcon(matrixStack, buffer, rune.getSprite(), 16, -16, light, overlay);
			matrixStack.pop();
		});
		matrixStack.pop();
	}

	public void renderIcon(MatrixStack matrixStack, IRenderTypeBuffer buffer, RenderMaterial renderMaterial, int width, int height, int light, int overlay) {
		renderIcon(matrixStack, buffer, 0, 0, renderMaterial, width, height, 1F, 1F, 1F, light, overlay);
	}
	
	public void renderIcon(MatrixStack matrixStack, IRenderTypeBuffer buffer, float x, float y, RenderMaterial renderMaterial, int width, int height, float r, float g, float b, int light, int overlay) {
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		Matrix3f normal = matrixStack.getLast().getNormal();
		IVertexBuilder builder = renderMaterial.getBuffer(buffer, RenderType::getEntityTranslucent);
		TextureAtlasSprite sprite = renderMaterial.getSprite();

		builder.pos(matrix, x, y, 0).color(r, g, b, 1F).tex(sprite.getMinU(), sprite.getMinV()).overlay(overlay).lightmap(light).normal(normal, 0, 1, 0).endVertex();
		builder.pos(matrix, x + width, y, 0).color(r, g, b, 1F).tex(sprite.getMaxU(), sprite.getMinV()).overlay(overlay).lightmap(light).normal(normal, 0, 1, 0).endVertex();
		builder.pos(matrix, x + width, y + height, 0).color(r, g, b, 1F).tex(sprite.getMaxU(), sprite.getMaxV()).overlay(overlay).lightmap(light).normal(normal, 0, 1, 0).endVertex();
		builder.pos(matrix, x, y + height, 0).color(r, g, b, 1F).tex(sprite.getMinU(), sprite.getMaxV()).overlay(overlay).lightmap(light).normal(normal, 0, 1, 0).endVertex();
	}



	public float getAngle(float partialTicks) {
		return TickHandler.getTicksInGame() + partialTicks % 360;
	}

}
