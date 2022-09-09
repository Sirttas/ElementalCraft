package sirttas.elementalcraft.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.event.TickHandler;


public interface IECGenericRenderer {

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

    default void renderBlock(BlockState state, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay, ModelData data) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, matrixStack, buffer, light, overlay, data, null);
    }

    default void renderBlock(BlockState state, PoseStack matrixStack, VertexConsumer builder, Level world, BlockPos pos) {
        matrixStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().renderBatched(state, pos, world, matrixStack, builder, false, world.random, getModelData(world, pos), null);
        matrixStack.popPose();
    }

    default void renderRunes(PoseStack matrixStack, MultiBufferSource buffer, IRuneHandler handler, float tick, int light, int overlay) {
        int runeCount = handler.getRuneCount();

        matrixStack.pushPose();
        matrixStack.translate(0.5F, 0.75F, 0.5F);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick / 2));
        handler.getRunes().forEach(rune -> {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F / runeCount));
            matrixStack.pushPose();
            matrixStack.translate(0.75F, 0F, 0F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
            matrixStack.scale(1F / 64F, 1F / 64F, 1F / 64F);
            ECRendererHelper.renderIcon(matrixStack, buffer, rune.getSprite(), 16, -16, light, overlay);
            matrixStack.popPose();
        });
        matrixStack.popPose();
    }

    @Deprecated
    default void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockState state, int light, int overlay) {
        renderModel(model, matrixStack, buffer, state, light, overlay, ModelData.EMPTY);
    }

    default void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockState state, int light, int overlay, ModelData data) {
        var renderType = state != null ? ItemBlockRenderTypes.getRenderType(state, false) : Sheets.cutoutBlockSheet();

        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(renderType), state, model, 1, 1, 1, light, overlay, data, renderType);
    }

    default float getClientTicks(float partialTicks) {
        return TickHandler.getTicksInGame() + partialTicks;
    }

    default ModelData getModelData(Level level, BlockPos pos) {
        var data = level.getModelDataManager().getAt(pos);

        if (data == null) {
            return ModelData.EMPTY;
        }
        return data;
    }

}
