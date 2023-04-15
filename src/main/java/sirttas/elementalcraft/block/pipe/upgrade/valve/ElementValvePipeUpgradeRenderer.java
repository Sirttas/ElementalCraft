package sirttas.elementalcraft.block.pipe.upgrade.valve;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.renderer.IPipeUpgradeRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class ElementValvePipeUpgradeRenderer implements IPipeUpgradeRenderer<ElementValvePipeUpgrade> {

    public static final ResourceLocation OPEN_LOCATION = ElementalCraft.createRL(PipeUpgrade.FOLDER + "element_valve_open");
    public static final ResourceLocation CLOSE_LOCATION = ElementalCraft.createRL(PipeUpgrade.FOLDER + "element_valve_close");

    private BakedModel openModel;
    private BakedModel closeModel;

    @Override
    public void render(ElementValvePipeUpgrade upgrade, ElementPipeBlockEntity pipe, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (openModel == null || closeModel == null) {
            ModelManager modelManager = Minecraft.getInstance().getModelManager();

            openModel = modelManager.getModel(OPEN_LOCATION);
            closeModel = modelManager.getModel(CLOSE_LOCATION);
        }

        if (upgrade.isOpen()) {
            ECRendererHelper.renderModel(openModel, poseStack, buffer, pipe, light, overlay);
            renderParticles(upgrade, pipe);
        } else {
            ECRendererHelper.renderModel(closeModel, poseStack, buffer, pipe, light, overlay);
        }
    }

    private static void renderParticles(ElementValvePipeUpgrade upgrade, ElementPipeBlockEntity pipe) {
        var level = pipe.getLevel();

        if (level == null) {
            return;
        }

        var random = level.random;

        if (random.nextFloat() >= 0.01F) {
            return;
        }

        var direction = upgrade.getDirection();
        var pos = pipe.getBlockPos();
        var f = -4.5F / 16.0F;
        var x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D - f * direction.getStepX();
        var y = pos.getY() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D - f * direction.getStepY();
        var z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D - f * direction.getStepZ();

        level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
    }
}
