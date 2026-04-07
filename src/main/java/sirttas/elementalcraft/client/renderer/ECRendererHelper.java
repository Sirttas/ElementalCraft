package sirttas.elementalcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.event.TickHandler;

import java.util.List;

public class ECRendererHelper {

    private ECRendererHelper() {}

    public static Material getBlockMaterial(String name)  {
        return new Material(ElementalCraftApi.createRL(name));
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, Material renderMaterial, int width, int height) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), 0, 0, width, height, 1F, 1F, 1F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, Material renderMaterial, int width, int height, int light, int overlay) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), 0, 0, width, height, 1F, 1F, 1F, light, overlay);
    }

    public static void renderIcon(PoseStack poseStack, VertexConsumer builder, float x, float y, int width, int height, float r, float g, float b, int light, int overlay) {
        var pose = poseStack.last();
        var matrix = pose.pose();

        builder.addVertex(matrix, x, y, 0)
                .setColor(r, g, b, 1F)
                .setUv(0, 0)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);
        builder.addVertex(matrix, x + width, y, 0)
                .setColor(r, g, b, 1F)
                .setUv(1, 0)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);
        builder.addVertex(matrix, x + width, y + height, 0)
                .setColor(r, g, b, 1F)
                .setUv(1, 1)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);
        builder.addVertex(matrix, x, y + height, 0)
                .setColor(r, g, b, 1F)
                .setUv(0, 1)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, 0, 1, 0);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, Material.Baked renderMaterial, int width, int height, int light) {
        submitIcon(poseStack, nodeCollector, RenderTypes.entityTranslucent(renderMaterial.sprite().atlasLocation()), 0, 0, width, height, 1F, 1F, 1F, light);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, float x, float y, Material renderMaterial, int width, int height, float r, float g, float b, int light) {
        submitIcon(poseStack, nodeCollector, RenderTypes.entityTranslucent(renderMaterial.sprite()), x, y, width, height, r, g, b, light);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, RenderType renderType, float x, float y, int width, int height, float r, float g, float b, int light) {;
        nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, builder) -> {
            builder.addVertex(pose, x, y, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
            builder.addVertex(pose, x + width, y, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
            builder.addVertex(pose, x + width, y + height, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
            builder.addVertex(pose, x, y + height, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
        });
    }

    public static Quaternionf getRotation(Direction direction) {
        return switch (direction) {
            case SOUTH -> Axis.YP.rotationDegrees(180.0F);
            case WEST -> Axis.YP.rotationDegrees(90.0F);
            case EAST -> Axis.YP.rotationDegrees(-90.0F);
            default -> new Quaternionf();
        };
    }

    public static void renderItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
    }

    public static void renderBlock(BlockState state, PoseStack poseStack, MultiBufferSource buffer) {
        renderBlock(state, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY);
    }

    public static void renderBlock(BlockState state, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, ModelData data) {
        if (state.isAir()) {
            return;
        }
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, light, overlay, data, null);
    }

    public static void renderGhost(BlockState state, PoseStack poseStack, MultiBufferSource buffer, Level level, BlockPos pos) {
        renderBatched(state, poseStack, buffer.getBuffer(ECRenderTypes.GHOST), level, pos);
    }

    public static void renderBatched(BlockState state, PoseStack poseStack, VertexConsumer consumer, Level level, BlockPos pos) {
        poseStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().renderBatched(state, pos, level, poseStack, consumer, false, RandomSource.create(), null, null);
        poseStack.popPose();
    }

    public static void renderBatched(BlockState state, PoseStack poseStack, MultiBufferSource buffer, Level level, BlockPos pos) {
        renderBatched(state, poseStack, buffer, level, pos, null);
    }

    public static void renderBatched(BlockState state, PoseStack poseStack, MultiBufferSource buffer, Level level, BlockPos pos, ModelData data) {
        poseStack.pushPose();
        var blockRenderer = Minecraft.getInstance().getBlockRenderer();
        var rand = RandomSource.create();

        if (state.getRenderShape() != RenderShape.INVISIBLE) {
            var model = blockRenderer.getBlockModel(state);

            for (var renderType : model.getRenderTypes(state, rand, data)) {
                var consumer = buffer.getBuffer(renderType);

                blockRenderer.renderBatched(state, pos, level, poseStack, consumer, false, rand, data, renderType);
            }
        }
        poseStack.popPose();
    }

    public static void renderFluid(BlockState state, PoseStack poseStack, MultiBufferSource buffer) {
        FluidStateModelSet fluidModelSet = Minecraft.getInstance().getModelManager().getFluidStateModelSet();
        FluidRenderer fluidRenderer = new FluidRenderer(fluidModelSet);
        FluidState fluidState = state.getFluidState();

        if (!fluidState.isEmpty()) {
            var customRenderer = fluidModelSet.get(fluidState).customRenderer();
            var pose = poseStack.last();
            var consumer = buffer.getBuffer(RenderTypes.solidMovingBlock());
            var wrapper = new VertexConsumer() {
                @Override
                public @NotNull VertexConsumer addVertex(float x, float y, float z) {
                    return consumer.addVertex(pose, x, y, z);
                }

                @Override
                public @NotNull VertexConsumer setColor(int r, int g, int b, int a) {
                    return consumer.setColor(r, g, b, a);
                }

                @Override
                public @NotNull VertexConsumer setColor(int color) {
                    return consumer.setColor(color);
                }

                @Override
                public @NotNull VertexConsumer setUv(float u, float v) {
                    return consumer.setUv(u, v);
                }

                @Override
                public @NotNull VertexConsumer setUv1(int u, int v) {
                    return consumer.setUv1(u, v);
                }

                @Override
                public @NotNull VertexConsumer setUv2(int u, int v) {
                    return consumer.setUv2(u, v);
                }

                @Override
                public @NotNull VertexConsumer setNormal(float x, float y, float z) {
                    return consumer.setNormal(x, y, z);
                }

                @Override
                public @NotNull VertexConsumer setLineWidth(float width) {
                    return consumer.setLineWidth(width);
                }
            };

            if (customRenderer == null || !customRenderer.renderFluid(fluidRenderer, fluidState, BlockAndTintGetter.EMPTY, BlockPos.ZERO, _ -> wrapper, state)) {
                fluidRenderer.tesselate(BlockAndTintGetter.EMPTY, BlockPos.ZERO, _ -> wrapper, state, fluidState);
            }
        }

    }

    public static void renderRunes(PoseStack poseStack, MultiBufferSource buffer, IRuneHandler handler, float tick, int light, int overlay) {
        int runeCount = handler.getRuneCount();

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.75F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(tick / 2));
        handler.getRunes().forEach(rune -> {
            poseStack.mulPose(Axis.YP.rotationDegrees(90F / runeCount));
            poseStack.pushPose();
            poseStack.translate(0.75F, 0F, 0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.scale(1F / 64F, 1F / 64F, 1F / 64F);
            ECRendererHelper.renderIcon(poseStack, buffer, rune.value().getSprite(), 16, -16, light, overlay);
            poseStack.popPose();
        });
        poseStack.popPose();
    }

    public static float getClientTicks(float partialTicks) {
        return TickHandler.getTicksInGame() + partialTicks;
    }

    public static void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockEntity te, int light, int overlay) {
        renderModel(model, matrixStack, buffer, te.getBlockState(), light, overlay, null);
    }

    public static void submitModel(@NotNull List<BlockStateModelPart> models, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        nodeCollector.submitBlockModel(poseStack, RenderTypes.solidMovingBlock(), models, BlockModelRenderState.EMPTY_TINTS, lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }

    public static void submitModel(@NotNull BlockStateModelPart model, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
       submitModel(List.of(model), poseStack, nodeCollector, lightCoords);
    }
}
