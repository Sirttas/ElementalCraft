package sirttas.elementalcraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
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
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.event.TickHandler;

import java.util.List;

public class ECRendererHelper {

    private ECRendererHelper() {}

    public static Material getBlockMaterial(String name)  {
        return new Material(ElementalCraftApi.createRL(name));
    }

    public static float getClientTicks(float partialTicks) {
        return TickHandler.getTicksInGame() + partialTicks;
    }

    public static Quaternionf getRotation(Direction direction) {
        return switch (direction) {
            case SOUTH -> Axis.YP.rotationDegrees(180.0F);
            case WEST -> Axis.YP.rotationDegrees(90.0F);
            case EAST -> Axis.YP.rotationDegrees(-90.0F);
            default -> new Quaternionf();
        };
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, Material.Baked renderMaterial, int width, int height, int light) {
        var sprite = renderMaterial.sprite();

        submitIcon(poseStack, nodeCollector, RenderTypes.entityTranslucent(sprite.atlasLocation()), 0, 0, width, height, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1(), 1F, 1F, 1F, light);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, Identifier sprite, int width, int height, int light) {
        submitIcon(poseStack, nodeCollector, RenderTypes.entityTranslucent(sprite), 0, 0, width, height, 1, 1, 1, light);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, Identifier sprite, int width, int height, float r, float g, float b, int light) {
        submitIcon(poseStack, nodeCollector, RenderTypes.entityTranslucent(sprite), 0, 0, width, height, r, g, b, light);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, float x, float y, Identifier sprite, int width, int height, float r, float g, float b, int light) {
        submitIcon(poseStack, nodeCollector, RenderTypes.entityTranslucent(sprite), x, y, width, height, r, g, b, light);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, RenderType renderType, float x, float y, int width, int height, float r, float g, float b, int light) {
        submitIcon(poseStack, nodeCollector, renderType, x, y, width, height, 0, 0, 1, 1, r, g, b, light);
    }

    public static void submitIcon(PoseStack poseStack, SubmitNodeCollector nodeCollector, RenderType renderType, float x, float y, int width, int height, float u0, float v0, float u1, float v1, float r, float g, float b, int light) {
        nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, builder) -> {
            builder.addVertex(pose, x, y, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(u0, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
            builder.addVertex(pose, x + width, y, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(u1, v0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
            builder.addVertex(pose, x + width, y + height, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(u1, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
            builder.addVertex(pose, x, y + height, 0)
                    .setColor(r, g, b, 1F)
                    .setUv(u0, v1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0, 1, 0);
        });
    }

    public static void submitModel(@NotNull List<BlockStateModelPart> models, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        nodeCollector.submitBlockModel(poseStack, RenderTypes.solidMovingBlock(), models, BlockModelRenderState.EMPTY_TINTS, lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }

    public static void submitModel(@NotNull BlockStateModelPart model, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        submitModel(List.of(model), poseStack, nodeCollector, lightCoords);
    }

    public static void renderBlock(BlockState state, PoseStack poseStack, MultiBufferSource buffer) {
        if (state.isAir()) {
            return;
        }
        // TODO
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
}
