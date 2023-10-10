package sirttas.elementalcraft.interaction.jei.category.shrine;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public abstract class AbstractShrineRecipeCategory<T> extends AbstractECRecipeCategory<T> {

    protected AbstractShrineRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
        super(translationKey, icon, background);
    }


    public static void render3D(@Nonnull GuiGraphics guiGraphics, BiConsumer<PoseStack, MultiBufferSource> render) {
        var poseStack = guiGraphics.pose();
        var modelViewStack = RenderSystem.getModelViewStack();

        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        modelViewStack.translate(0, 0, 1050);
        modelViewStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();

        var stack = new PoseStack();

        stack.translate(0, 0, 1000);
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        stack.scale(30,30, 30);
        Lighting.setupForEntityInInventory();

        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.runAsFancy(() -> render.accept(stack, bufferSource));
        bufferSource.endBatch();
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    protected static void setupPose(PoseStack p) {
        p.translate(-1.5, -2.3, 0);
        p.mulPose(Axis.XP.rotationDegrees(-30.0F));
        p.mulPose(Axis.YP.rotationDegrees(40.0F));
    }

}
