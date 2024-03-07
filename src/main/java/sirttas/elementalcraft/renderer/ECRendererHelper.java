package sirttas.elementalcraft.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Quaternionf;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.renderer.ECRenderTypes;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.capability.PipeUpgradeCapabilities;
import sirttas.elementalcraft.event.TickHandler;

public class ECRendererHelper {

    public static final float SPACING = 0.001F;

    private ECRendererHelper() {}

    public static Material getBlockMaterial(String name)  {
        return getBlockMaterial(ElementalCraftApi.createRL(name));
    }

    public static Material getBlockMaterial(ResourceLocation loc)  {
        return new Material(TextureAtlas.LOCATION_BLOCKS, loc);
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, Material renderMaterial, int width, int height) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), 0, 0, width, height, 1F, 1F, 1F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, Material renderMaterial, int width, int height, int light, int overlay) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), 0, 0, width, height, 1F, 1F, 1F, light, overlay);
    }

    public static void renderIcon(PoseStack poseStack, VertexConsumer builder, int width, int height, int light, int overlay) {
        renderIcon(poseStack, builder, 0, 0, width, height, 1F, 1F, 1F, light, overlay);
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, float x, float y, Material renderMaterial, int width, int height, float r, float g, float b, int light, int overlay) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), x, y, width, height, r, g, b, light, overlay);
    }

    public static void renderIcon(PoseStack poseStack, VertexConsumer builder, float x, float y, int width, int height, float r, float g, float b, int light, int overlay) {
        var matrix = poseStack.last().pose();
        var normal = poseStack.last().normal();

        builder.vertex(matrix, x, y, 0).color(r, g, b, 1F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
        builder.vertex(matrix, x + width, y, 0).color(r, g, b, 1F).uv(1, 0).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
        builder.vertex(matrix, x + width, y + height, 0).color(r, g, b, 1F).uv(1, 1).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
        builder.vertex(matrix, x, y + height, 0).color(r, g, b, 1F).uv(0, 1).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
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
        Minecraft.getInstance().getBlockRenderer().renderBatched(state, pos, level, poseStack, consumer, false, RandomSource.create(), getModelData(level, pos), null);
        poseStack.popPose();
    }

    public static void renderBatched(BlockState state, PoseStack poseStack, MultiBufferSource buffer, Level level, BlockPos pos) {
        renderBatched(state, poseStack, buffer, level, pos, getModelData(level, pos));
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
        f18 = sprite1.getU(0.0F);
        f22 = sprite1.getV(0.0F);
        f19 = f18;
        f23 = sprite1.getV(16.0F);
        f20 = sprite1.getU(16.0F);
        f24 = f23;
        f21 = f20;
        f25 = f22;


        float f49 = (f18 + f19 + f20 + f21) / 4.0F;
        float f50 = (f22 + f23 + f24 + f25) / 4.0F;
        float f51 = sprite1.uvShrinkRatio();
        f18 = Mth.lerp(f51, f18, f49);
        f19 = Mth.lerp(f51, f19, f49);
        f20 = Mth.lerp(f51, f20, f49);
        f21 = Mth.lerp(f51, f21, f49);
        f22 = Mth.lerp(f51, f22, f50);
        f23 = Mth.lerp(f51, f23, f50);
        f24 = Mth.lerp(f51, f24, f50);
        f25 = Mth.lerp(f51, f25, f50);

        fluidVertex(poseStack, consumer, 0.0F, 1.0F - SPACING, 0.0F, r, g, b, alpha, f18, f22);
        fluidVertex(poseStack, consumer, 0.0F, 1.0F - SPACING, 1.0F, r, g, b, alpha, f19, f23);
        fluidVertex(poseStack, consumer, 1.0F, 1.0F - SPACING, 1.0F, r, g, b, alpha, f20, f24);
        fluidVertex(poseStack, consumer, 1.0F, 1.0F - SPACING, 0.0F, r, g, b, alpha, f21, f25);

        float f40 = sprite1.getU0();
        float f41 = sprite1.getU1();
        float f42 = sprite1.getV0();
        float f43 = sprite1.getV1();

        fluidVertex(poseStack, consumer, 0, SPACING, 1.0F, r, g, b, alpha, f40, f43);
        fluidVertex(poseStack, consumer, 0, SPACING, 0, r, g, b, alpha, f40, f42);
        fluidVertex(poseStack, consumer,  1.0F, SPACING, 0, r, g, b, alpha, f41, f42);
        fluidVertex(poseStack, consumer, 1.0F, SPACING, 1.0F, r, g, b, alpha, f41, f43);


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

            float f54 = sprite2.getU(0.0F);
            float f55 = sprite2.getU(8.0F);
            float f33 = sprite2.getV(((1.0F - f44) * 16.0F * 0.5F));
            float f34 = sprite2.getV(((1.0F - f45) * 16.0F * 0.5F));
            float f35 = sprite2.getV(8.0F);

            fluidVertex(poseStack, consumer, d3, f44, d4, r, g, b, alpha, f54, f33);
            fluidVertex(poseStack, consumer, d5, f45, d6, r, g, b, alpha, f55, f34);
            fluidVertex(poseStack, consumer, d5, SPACING, d6, r, g, b, alpha, f55, f35);
            fluidVertex(poseStack, consumer, d3, SPACING, d4, r, g, b, alpha, f54, f35);
            if (sprite2 != ModelBakery.WATER_OVERLAY.sprite()) {
                fluidVertex(poseStack, consumer, d3, SPACING, d4, r, g, b, alpha, f54, f35);
                fluidVertex(poseStack, consumer, d5, SPACING, d6, r, g, b, alpha, f55, f35);
                fluidVertex(poseStack, consumer, d5, f45, d6, r, g, b, alpha, f55, f34);
                fluidVertex(poseStack, consumer, d3, f44, d4, r, g, b, alpha, f54, f33);
            }
        }
    }

    private static void fluidVertex(PoseStack poseStack, VertexConsumer consumer, float x, float y, float z, float r, float g, float b, float alpha, float u, float v) {
        var last = poseStack.last();

        consumer.vertex(last.pose(), x, y, z).color(r, g, b, alpha).uv(u, v).uv2(15728880).normal(last.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void renderRunes(PoseStack poseStack, MultiBufferSource buffer, PipeUpgrade pu, float tick, int light, int overlay) {
        var handler = pu.getCapability(PipeUpgradeCapabilities.RUNE_HANDLER, null);

        if (handler != null && !handler.isEmpty()) {
            ECRendererHelper.renderRunes(poseStack, buffer, handler, getClientTicks(tick), light, overlay);
        }
    }

    public static void renderRunes(PoseStack poseStack, MultiBufferSource buffer, BlockEntity be, float tick, int light, int overlay) {
        var handler = BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandler.BLOCK, be, null);

        if (handler != null && !handler.isEmpty()) {
            ECRendererHelper.renderRunes(poseStack, buffer, handler, getClientTicks(tick), light, overlay);
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
            ECRendererHelper.renderIcon(poseStack, buffer, rune.getSprite(), 16, -16, light, overlay);
            poseStack.popPose();
        });
        poseStack.popPose();
    }

    @Deprecated
    public static void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockState state, int light, int overlay) {
        renderModel(model, matrixStack, buffer, state, light, overlay, ModelData.EMPTY);
    }

    public static void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockState state, int light, int overlay, ModelData data) {
        var renderType = state != null ? ItemBlockRenderTypes.getRenderType(state, false) : Sheets.cutoutBlockSheet();

        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(renderType), state, model, 1, 1, 1, light, overlay, data, renderType);
    }

    public static float getClientTicks(float partialTicks) {
        return TickHandler.getTicksInGame() + partialTicks;
    }

    public static ModelData getModelData(Level level, BlockPos pos) {
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

    public static void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, BlockEntity te, int light, int overlay) {
        renderModel(model, matrixStack, buffer, te.getBlockState(), light, overlay, getModelData(model, te));
    }

    public static ModelData getModelData(BakedModel model, BlockEntity te) {
        Level level = te.getLevel();
        BlockPos pos = te.getBlockPos();

        if (level == null) {
            return ModelData.EMPTY;
        }
        return model.getModelData(level, pos, te.getBlockState(), getModelData(level, pos));
    }
}
