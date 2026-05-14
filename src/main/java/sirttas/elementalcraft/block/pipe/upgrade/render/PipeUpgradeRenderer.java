package sirttas.elementalcraft.block.pipe.upgrade.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderState;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;

public interface PipeUpgradeRenderer<T extends PipeUpgrade, S extends PipeUpgradeRenderState> {

   @Nullable S createRenderState();

    default void extractRenderState(T pipeUpgrade, S state, ElementPipeSectionRenderState sectionRenderState, float partialTicks, Vec3 cameraPosition) {
        PipeUpgradeRenderState.extractBase(pipeUpgrade, state, sectionRenderState);
    }

    void submit(final S state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera);

}
