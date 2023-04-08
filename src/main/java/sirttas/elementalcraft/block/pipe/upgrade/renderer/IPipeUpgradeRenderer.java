package sirttas.elementalcraft.block.pipe.upgrade.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.renderer.IECGenericRenderer;

import javax.annotation.Nonnull;

public interface IPipeUpgradeRenderer<T extends PipeUpgrade> extends IECGenericRenderer {

    void render(T upgrade, ElementPipeBlockEntity pipe, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay);
}
