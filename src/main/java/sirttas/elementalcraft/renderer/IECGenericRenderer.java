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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import sirttas.elementalcraft.api.renderer.ECRenderTypes;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.event.TickHandler;


public interface IECGenericRenderer {

    float SPACING = 0.001F;

    default Quaternion getRotation(Direction direction) {
        return switch (direction) {
            case SOUTH -> Vector3f.YP.rotationDegrees(180.0F);
            case WEST -> Vector3f.YP.rotationDegrees(90.0F);
            case EAST -> Vector3f.YP.rotationDegrees(-90.0F);
            default -> Quaternion.ONE.copy();
        };
    }

    default void renderItem(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, light, overlay, poseStack, buffer, 0);
    }

    default void renderBlock(BlockState state, PoseStack poseStack, MultiBufferSource buffer) {
        renderBlock(state, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY);
    }

    default void renderBlock(BlockState state, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, ModelData data) {
        if (state.isAir()) {
            return;
        }
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, light, overlay, data, null);
    }

    default void renderGhost(BlockState state, PoseStack poseStack, MultiBufferSource buffer, Level level, BlockPos pos) {
        renderBatched(state, poseStack, buffer.getBuffer(ECRenderTypes.GHOST), level, pos);
    }

    default void renderBatched(BlockState state, PoseStack poseStack, VertexConsumer consumer, Level level, BlockPos pos) {
        poseStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().renderBatched(state, pos, level, poseStack, consumer, false, level.random, getModelData(level, pos), null);
        poseStack.popPose();
    }

    default void renderBatched(BlockState state, PoseStack poseStack, MultiBufferSource buffer, Level level, BlockPos pos) {
        renderBatched(state, poseStack, buffer, level, pos, getModelData(level, pos));
    }

    default void renderBatched(BlockState state, PoseStack poseStack, MultiBufferSource buffer, Level level, BlockPos pos, ModelData data) {
        poseStack.pushPose();
        var blockRenderer = Minecraft.getInstance().getBlockRenderer();
        var rand = level.random;

        if (state.getRenderShape() != RenderShape.INVISIBLE) {
            var model = blockRenderer.getBlockModel(state);

            for (var renderType : model.getRenderTypes(state, rand, data)) {
                var consumer = buffer.getBuffer(renderType);

                blockRenderer.renderBatched(state, pos, level, poseStack, consumer, false, rand, data, renderType);
            }
        }
        poseStack.popPose();
    }

    default void renderFluid(BlockState state, PoseStack poseStack, MultiBufferSource buffer) {
        var fluidState = state.getFluidState();
        var props = IClientFluidTypeExtensions.of(fluidState);
        var overlayTexture = props.getOverlayTexture();
        var sprites =  new TextureAtlasSprite[] {
                Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(props.getStillTexture()),
                Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(props.getFlowingTexture()),
                overlayTexture == null ? null : Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(overlayTexture),
        };
        var consumer = buffer.getBuffer(ItemBlockRenderTypes.getRenderLayer(fluidState));
        int tintColor = props.getTintColor();
        float alpha = (tintColor >> 24 & 255) / 255.0F;
        float r = (tintColor >> 16 & 255) / 255.0F;
        float g = (tintColor >> 8 & 255) / 255.0F;
        float b = (tintColor & 255) / 255.0F;

        float f18;
        float f19;
        float f20;
        float f21;
        float f22;
        float f23;
        float f24;
        float f25;

        TextureAtlasSprite sprite1 = sprites[0];
        f18 = sprite1.getU(0.0D);
        f22 = sprite1.getV(0.0D);
        f19 = f18;
        f23 = sprite1.getV(16.0D);
        f20 = sprite1.getU(16.0D);
        f24 = f23;
        f21 = f20;
        f25 = f22;


        float f49 = (f18 + f19 + f20 + f21) / 4.0F;
        float f50 = (f22 + f23 + f24 + f25) / 4.0F;
        float f51 = sprite1.getWidth() / (sprite1.getU1() - sprite1.getU0());
        float f52 = sprite1.getHeight() / (sprite1.getV1() - sprite1.getV0());
        float f53 = 4.0F / Math.max(f52, f51);
        f18 = Mth.lerp(f53, f18, f49);
        f19 = Mth.lerp(f53, f19, f49);
        f20 = Mth.lerp(f53, f20, f49);
        f21 = Mth.lerp(f53, f21, f49);
        f22 = Mth.lerp(f53, f22, f50);
        f23 = Mth.lerp(f53, f23, f50);
        f24 = Mth.lerp(f53, f24, f50);
        f25 = Mth.lerp(f53, f25, f50);

        this.fluidVertex(poseStack, consumer, 0.0F, 1.0F - SPACING, 0.0F, r, g, b, alpha, f18, f22);
        this.fluidVertex(poseStack, consumer, 0.0F, 1.0F - SPACING, 1.0F, r, g, b, alpha, f19, f23);
        this.fluidVertex(poseStack, consumer, 1.0F, 1.0F - SPACING, 1.0F, r, g, b, alpha, f20, f24);
        this.fluidVertex(poseStack, consumer, 1.0F, 1.0F - SPACING, 0.0F, r, g, b, alpha, f21, f25);

        float f40 = sprite1.getU0();
        float f41 = sprite1.getU1();
        float f42 = sprite1.getV0();
        float f43 = sprite1.getV1();

        this.fluidVertex(poseStack, consumer, 0, SPACING, 1.0F, r, g, b, alpha, f40, f43);
        this.fluidVertex(poseStack, consumer, 0, SPACING, 0, r, g, b, alpha, f40, f42);
        this.fluidVertex(poseStack, consumer,  1.0F, SPACING, 0, r, g, b, alpha, f41, f42);
        this.fluidVertex(poseStack, consumer, 1.0F, SPACING, 1.0F, r, g, b, alpha, f41, f43);


        for(Direction direction : Direction.Plane.HORIZONTAL) {
            float f44;
            float f45;
            float d3;
            float d4;
            float d5;
            float d6;
            switch (direction) {
                case NORTH -> {
                    f44 = 1.0F - SPACING;
                    f45 = 1.0F - SPACING;
                    d3 = 0;
                    d5 = 1.0F;
                    d4 = SPACING;
                    d6 = SPACING;
                }
                case SOUTH -> {
                    f44 = 1.0F - SPACING;
                    f45 = 1.0F - SPACING;
                    d3 = 1.0F;
                    d5 = 0;
                    d4 = 1.0F - SPACING;
                    d6 = 1.0F - SPACING;
                }
                case WEST -> {
                    f44 = 1.0F - SPACING;
                    f45 = 1.0F - SPACING;
                    d3 = SPACING;
                    d5 = SPACING;
                    d4 = 1.0F;
                    d6 = 0;
                }
                default -> {
                    f44 = 1.0F - SPACING;
                    f45 = 1.0F - SPACING;
                    d3 = 1.0F - SPACING;
                    d5 = 1.0F - SPACING;
                    d4 = 0;
                    d6 = 1.0F;
                }
            }

            var sprite2 =  sprites[2] != null ?  sprites[2] :sprites[1];

            float f54 = sprite2.getU(0.0D);
            float f55 = sprite2.getU(8.0D);
            float f33 = sprite2.getV(((1.0F - f44) * 16.0F * 0.5F));
            float f34 = sprite2.getV(((1.0F - f45) * 16.0F * 0.5F));
            float f35 = sprite2.getV(8.0D);

            this.fluidVertex(poseStack, consumer, d3, f44, d4, r, g, b, alpha, f54, f33);
            this.fluidVertex(poseStack, consumer, d5, f45, d6, r, g, b, alpha, f55, f34);
            this.fluidVertex(poseStack, consumer, d5, SPACING, d6, r, g, b, alpha, f55, f35);
            this.fluidVertex(poseStack, consumer, d3, SPACING, d4, r, g, b, alpha, f54, f35);
            if (sprite2 != ModelBakery.WATER_OVERLAY.sprite()) {
                this.fluidVertex(poseStack, consumer, d3, SPACING, d4, r, g, b, alpha, f54, f35);
                this.fluidVertex(poseStack, consumer, d5, SPACING, d6, r, g, b, alpha, f55, f35);
                this.fluidVertex(poseStack, consumer, d5, f45, d6, r, g, b, alpha, f55, f34);
                this.fluidVertex(poseStack, consumer, d3, f44, d4, r, g, b, alpha, f54, f33);
            }
        }
    }

    private void fluidVertex(PoseStack poseStack, VertexConsumer consumer, float x, float y, float z, float r, float g, float b, float alpha, float u, float v) {
        var last = poseStack.last();

        consumer.vertex(last.pose(), x, y, z).color(r, g, b, alpha).uv(u, v).uv2(15728880).normal(last.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

    default void renderRunes(PoseStack poseStack, MultiBufferSource buffer, IRuneHandler handler, float tick, int light, int overlay) {
        int runeCount = handler.getRuneCount();

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.75F, 0.5F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(tick / 2));
        handler.getRunes().forEach(rune -> {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90F / runeCount));
            poseStack.pushPose();
            poseStack.translate(0.75F, 0F, 0F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            poseStack.scale(1F / 64F, 1F / 64F, 1F / 64F);
            ECRendererHelper.renderIcon(poseStack, buffer, rune.getSprite(), 16, -16, light, overlay);
            poseStack.popPose();
        });
        poseStack.popPose();
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
        var modelDataManager = level.getModelDataManager();

        if (modelDataManager == null) {
            return ModelData.EMPTY;
        }

        var data = modelDataManager.getAt(pos);

        if (data == null) {
            return ModelData.EMPTY;
        }
        return data;
    }

    default void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockEntity te, int light, int overlay) {
        renderModel(model, matrixStack, buffer, te.getBlockState(), light, overlay, getModelData(model, te));
    }

    default ModelData getModelData(BakedModel model, BlockEntity te) {
        Level level = te.getLevel();
        BlockPos pos = te.getBlockPos();

        if (level == null) {
            return ModelData.EMPTY;
        }
        return model.getModelData(level, pos, te.getBlockState(), getModelData(level, pos));
    }

}
