package sirttas.elementalcraft.interaction.jei.category.shrine;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.renderer.IECGenericRenderer;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public abstract class AbstractShrineRecipeCategory<T> extends AbstractECRecipeCategory<T> implements IECGenericRenderer {

    protected AbstractShrineRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
        super(translationKey, icon, background);
    }


    public static void render3D(@Nonnull PoseStack poseStack, BiConsumer<PoseStack, MultiBufferSource> render) {
        var modelViewStack = RenderSystem.getModelViewStack();

        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        modelViewStack.translate(0, 0, 1050);
        modelViewStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();

        var stack = new PoseStack();

        stack.translate(0, 0, 1000);
        stack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        stack.scale(30,30, 30);
        Lighting.setupForEntityInInventory();

        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.runAsFancy(() -> render.accept(stack, bufferSource));
        bufferSource.endBatch();
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

}
