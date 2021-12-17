package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.event.TickHandler;

public interface IECRenderer<T extends BlockEntity> extends BlockEntityRenderer<T> {

	default Quaternion getRotation(Direction direction) {
		return switch (direction) {
			case SOUTH -> Vector3f.YP.rotationDegrees(180.0F);
			case WEST -> Vector3f.YP.rotationDegrees(90.0F);
			case EAST -> Vector3f.YP.rotationDegrees(-90.0F);
			default -> Quaternion.ONE.copy();
		};
	}

	default void renderItem(ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, light, overlay, matrixStack, buffer, 0);
	}

	default void renderBlock(BlockState state, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay, IModelData data) {
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, matrixStack, buffer, light, overlay, data);
	}

	default void renderBlock(BlockState state, PoseStack matrixStack, VertexConsumer builder, Level world, BlockPos pos) {
		matrixStack.pushPose();
		Minecraft.getInstance().getBlockRenderer().renderBatched(state, pos, world, matrixStack, builder, false, world.random, ModelDataManager.getModelData(world, pos));
		matrixStack.popPose();
	}
	
	default void renderRunes(PoseStack matrixStack, MultiBufferSource buffer, IRuneHandler handler, float tick, int light, int overlay) {
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

	default void renderIcon(PoseStack matrixStack, MultiBufferSource buffer, Material renderMaterial, int width, int height, int light, int overlay) {
		renderIcon(matrixStack, buffer, 0, 0, renderMaterial, width, height, 1F, 1F, 1F, light, overlay);
	}
	
	default void renderIcon(PoseStack matrixStack, MultiBufferSource buffer, float x, float y, Material renderMaterial, int width, int height, float r, float g, float b, int light, int overlay) {
		Matrix4f matrix = matrixStack.last().pose();
		Matrix3f normal = matrixStack.last().normal();
		VertexConsumer builder = renderMaterial.buffer(buffer, RenderType::entityTranslucent);
		TextureAtlasSprite sprite = renderMaterial.sprite();

		builder.vertex(matrix, x, y, 0).color(r, g, b, 1F).uv(sprite.getU0(), sprite.getV0()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, x + width, y, 0).color(r, g, b, 1F).uv(sprite.getU1(), sprite.getV0()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, x + width, y + height, 0).color(r, g, b, 1F).uv(sprite.getU1(), sprite.getV1()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, x, y + height, 0).color(r, g, b, 1F).uv(sprite.getU0(), sprite.getV1()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
	}

	@Deprecated
	default void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockState state, int light, int overlay) {
		renderModel(model, matrixStack, buffer, state, light, overlay, EmptyModelData.INSTANCE);
	}
	
	default void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, T te, int light, int overlay) {
		renderModel(model, matrixStack, buffer, te.getBlockState(), light, overlay, getModelData(model, te));
	}

	default void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockState state, int light, int overlay, IModelData data) {
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(state != null ? ItemBlockRenderTypes.getRenderType(state, false)  : Sheets.cutoutBlockSheet()), state, model, 1, 1, 1, light,
				overlay, data);
	}
	
	default float getAngle(float partialTicks) {
		return TickHandler.getTicksInGame() + partialTicks % 360;
	}
	
	default IModelData getModelData(BakedModel model, T te) {
		Level world = te.getLevel();
		BlockPos pos = te.getBlockPos();
		
        return model.getModelData(world, pos, te.getBlockState(), ModelDataManager.getModelData(world, pos));
	}

}
