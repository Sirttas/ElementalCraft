package sirttas.elementalcraft.block.shrine.budding;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.block.shrine.ShrineRenderer;
import sirttas.elementalcraft.client.model.ECModelResolver;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;

public class BuddingShrineRenderer extends ShrineRenderer<BuddingShrineBlockEntity, BuddingShrineRenderState> {

    private final BuddingShrinePlateModelResolver buddingShrinePlateModelResolver;

    public BuddingShrineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        buddingShrinePlateModelResolver = ECModelResolver.get(BuddingShrinePlateModelResolver.IDENTIFIER);
    }

    @Override
    public @NotNull BuddingShrineRenderState createRenderState() {
        return new BuddingShrineRenderState();
    }

    @Override
    public void extractRenderState(BuddingShrineBlockEntity blockEntity, BuddingShrineRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.plate = buddingShrinePlateModelResolver.getModel(blockEntity.getBudType());
    }

    @Override
    public void submit(BuddingShrineRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        ECRendererHelper.submitModel(renderState.plate.getModel(), poseStack, nodeCollector, renderState.lightCoords);
    }
}
