package sirttas.elementalcraft.block.pipe.upgrade.pump;

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

public class ElementPumpPipeUpgradeRenderer implements IPipeUpgradeRenderer<ElementPumpPipeUpgrade> {

    public static final ResourceLocation PUMP_LOCATION = ElementalCraft.createRL(PipeUpgrade.FOLDER + "element_pump_pump");
    private BakedModel pumpModel;

    @Override
    public void render(ElementPumpPipeUpgrade upgrade, ElementPipeBlockEntity pipe, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (pumpModel == null) {
            ModelManager modelManager = Minecraft.getInstance().getModelManager();

            pumpModel = modelManager.getModel(PUMP_LOCATION);
        }

        var tick = ECRendererHelper.getClientTicks(partialTicks) % 30;

        poseStack.pushPose();
        if (tick < 10 && tick >= 0) {
            poseStack.translate(0, (0 - tick) / 50F, 0);
        } else if (tick >= 10 && tick < 15) {
            poseStack.translate(0, -10 / 50F, 0);
        } else if (tick >= 15 && tick < 25) {
            poseStack.translate(0, -10 / 50F + (tick - 15) / 50F, 0);
        }
        ECRendererHelper.renderModel(pumpModel, poseStack, buffer, pipe, light, overlay);
        poseStack.popPose();
    }
}
