package sirttas.elementalcraft.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;

public class ECRendererHelper {

    private ECRendererHelper() {}

    public static Material getBlockMaterial(String name)  {
        return getBlockMaterial(ElementalCraft.createRL(name));
    }

    public static Material getBlockMaterial(ResourceLocation loc)  {
        return new Material(TextureAtlas.LOCATION_BLOCKS, loc);
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, Material renderMaterial, int width, int height) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), 0, 0, renderMaterial, width, height, 1F, 1F, 1F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, Material renderMaterial, int width, int height, int light, int overlay) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), 0, 0, renderMaterial, width, height, 1F, 1F, 1F, light, overlay);
    }

    public static void renderIcon(PoseStack poseStack, VertexConsumer builder, Material renderMaterial, int width, int height, int light, int overlay) {
        renderIcon(poseStack, builder, 0, 0, renderMaterial, width, height, 1F, 1F, 1F, light, overlay);
    }

    public static void renderIcon(PoseStack poseStack, MultiBufferSource buffer, float x, float y, Material renderMaterial, int width, int height, float r, float g, float b, int light, int overlay) {
        renderIcon(poseStack, renderMaterial.buffer(buffer, RenderType::entityTranslucent), x, y, renderMaterial, width, height, r, g, b, light, overlay);
    }

    public static void renderIcon(PoseStack poseStack, VertexConsumer builder, float x, float y, Material renderMaterial, int width, int height, float r, float g, float b, int light, int overlay) {
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        TextureAtlasSprite sprite = renderMaterial.sprite();

        builder.vertex(matrix, x, y, 0).color(r, g, b, 1F).uv(sprite.getU0(), sprite.getV0()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
        builder.vertex(matrix, x + width, y, 0).color(r, g, b, 1F).uv(sprite.getU1(), sprite.getV0()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
        builder.vertex(matrix, x + width, y + height, 0).color(r, g, b, 1F).uv(sprite.getU1(), sprite.getV1()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
        builder.vertex(matrix, x, y + height, 0).color(r, g, b, 1F).uv(sprite.getU0(), sprite.getV1()).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
    }
}
