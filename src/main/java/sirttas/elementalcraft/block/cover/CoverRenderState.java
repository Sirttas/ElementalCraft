package sirttas.elementalcraft.block.cover;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class CoverRenderState {

    private static final AABB BOX = new AABB(0, 0, 0, 1, 1, 1);
    private static final int COLOR = ARGB.colorFromFloat(1, 0F, 0F, 0F);
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelRenderState blockModelRenderState;
    private boolean isCovered;
    private boolean showCover;

    public CoverRenderState() {
        this.blockModelRenderState = new BlockModelRenderState();
        isCovered = false;
        showCover = false;
    }

    public boolean showCover() {
        return showCover;
    }

    public void update(BlockModelResolver resolver, Coverable coverable) {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;

        this.isCovered = coverable.isCovered();
        this.showCover = coverable.showCover(player);
        resolver.update(blockModelRenderState, showCover ? coverable.getCoverState() : coverable.getUncoveredState(), BLOCK_DISPLAY_CONTEXT);
    }

    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, int lightCoords) {
        if (showCover || isCovered) {
            blockModelRenderState.submit(poseStack, nodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
        if (isCovered) {
            Gizmos.cuboid(BOX, GizmoStyle.stroke(COLOR));
        }
    }
}
