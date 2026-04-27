package sirttas.elementalcraft.client.renderer.state;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

public class MillModelRenderState {

    private float animationTime;
    private BlockStateModelPart model;
    private boolean broken;
    private boolean running;


    public MillModelRenderState() {
        animationTime = 0;
        model = null;
        broken = false;
        running = false;
    }

    public void update(BlockStateModelPart model, boolean broken, boolean running, float partialTicks) {
        this.model = model;
        this.animationTime = ECRendererHelper.getClientTicks(partialTicks) / 2;
        this.broken = broken;
        this.running = running;
    }

    public void clear() {
        this.animationTime = 0;
        this.model = null;
        this.broken = false;
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isBroken() {
        return broken;
    }

    public void submit(@NotNull BlockEntityRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector) {
        this.submit(poseStack, nodeCollector, renderState.lightCoords);
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        if (broken) {
            return;
        }
        poseStack.pushPose();
        if (running) {
            poseStack.translate(0.5, 0, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(-5 * animationTime));
            poseStack.translate(-0.5, 0, -0.5);
        }
        ECRendererHelper.submitModel(model, poseStack, nodeCollector, lightCoords);
        poseStack.popPose();
    }
}
