package sirttas.elementalcraft.block.pipe.upgrade.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.renderer.IPipeUpgradeRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class ElementBeamPipeUpgradeRenderer implements IPipeUpgradeRenderer<ElementBeamPipeUpgrade> {

    public static final ResourceLocation RING_1_LOCATION = ElementalCraft.createRL(PipeUpgrade.FOLDER + "element_beam_ring_1");
    public static final ResourceLocation RING_2_LOCATION = ElementalCraft.createRL(PipeUpgrade.FOLDER + "element_beam_ring_2");
    public static final ResourceLocation RING_3_LOCATION = ElementalCraft.createRL(PipeUpgrade.FOLDER + "element_beam_ring_3");

    private BakedModel ring1Model;
    private BakedModel ring2Model;
    private BakedModel ring3Model;

    @Override
    public void render(ElementBeamPipeUpgrade upgrade, ElementPipeBlockEntity pipe, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (ring1Model == null || ring2Model == null || ring3Model == null) {
            ModelManager modelManager = Minecraft.getInstance().getModelManager();

            ring1Model = modelManager.getModel(RING_1_LOCATION);
            ring2Model = modelManager.getModel(RING_2_LOCATION);
            ring3Model = modelManager.getModel(RING_3_LOCATION);
        }

        if (!upgrade.isLinked()) {
            ECRendererHelper.renderModel(ring1Model, poseStack, buffer, pipe, light, overlay);
            ECRendererHelper.renderModel(ring2Model, poseStack, buffer, pipe, light, overlay);
            ECRendererHelper.renderModel(ring3Model, poseStack, buffer, pipe, light, overlay);
        } else {
            var tick = ECRendererHelper.getClientTicks(partialTicks) % 50;

            poseStack.pushPose();
            translateRing(tick, 0, 5, 20, 30, poseStack);
            ECRendererHelper.renderModel(ring1Model, poseStack, buffer, pipe, light, overlay);
            poseStack.popPose();

            poseStack.pushPose();
            translateRing(tick, 5, 10, 25, 35, poseStack);
            ECRendererHelper.renderModel(ring2Model, poseStack, buffer, pipe, light, overlay);
            poseStack.popPose();

            poseStack.pushPose();
            translateRing(tick, 10, 15, 30, 40, poseStack);
            ECRendererHelper.renderModel(ring3Model, poseStack, buffer, pipe, light, overlay);
            poseStack.popPose();
        }
    }

    private static void translateRing(float tick, int from, int to, int from2, int to2, @Nonnull PoseStack poseStack) {
        if (tick < to && tick >= from) {
            poseStack.translate(0, (from - tick) / 100F, 0);
        } else if (tick >= to && tick < from2) {
            poseStack.translate(0, -5 / 100F, 0);
        } else if (tick >= from2 && tick < to2) {
            poseStack.translate(0, -5 / 100F + (tick - from2) / 200F, 0);
        }
    }
}
