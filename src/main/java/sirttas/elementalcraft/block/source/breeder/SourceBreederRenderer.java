package sirttas.elementalcraft.block.source.breeder;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.BooleanUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.renderer.SingleItemRenderer;
import sirttas.elementalcraft.block.source.SourceRendererHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class SourceBreederRenderer extends SingleItemRenderer<SourceBreederBlockEntity> {

    private final BlockState pedestalState;

    public SourceBreederRenderer() {
        super(new Vec3(0.5, 1, 0.5));
        pedestalState = ECBlocks.SOURCE_BREEDER_PEDESTAL.get().defaultBlockState();
    }

    @Override
    public void render(@Nonnull SourceBreederBlockEntity breeder, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        renderPedestalShadow(breeder, poseStack, buffer);

        var stack = breeder.getInventory().getItem(0);

        poseStack.translate(0, 1, 0);
        if (stack.is(ECItems.RECEPTACLE.get())) {
            ECRendererHelper.renderRunes(poseStack, buffer, breeder.getRuneHandler(), ECRendererHelper.getClientTicks(partialTicks), light, overlay);

            var type = ReceptacleHelper.getElementType(stack);

            if (type == ElementType.NONE) {
                return;
            }
            poseStack.translate(0, 1, 0);
            SourceRendererHelper.renderSource(poseStack, buffer, partialTicks, light, overlay, type, false, 1);
            return;
        }

        super.render(breeder, partialTicks, poseStack, buffer, light, overlay);
    }

    private void renderPedestalShadow(@Nonnull SourceBreederBlockEntity breeder, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer) {
        if (BooleanUtils.isTrue(ECConfig.CLIENT.renderPedestalShadow.get())) {
            var directions = breeder.getPedestalsDirections();

            if (directions.size() < 2) {
                for (var direction : Direction.Plane.HORIZONTAL) {
                    if (directions.contains(direction)) {
                        continue;
                    }
                    poseStack.pushPose();
                    poseStack.translate(direction.getStepX() * 2D, 0, direction.getStepZ() * 2D);
                    ECRendererHelper.renderGhost(pedestalState, poseStack, buffer, breeder.getLevel(), breeder.getBlockPos().relative(direction, 2));
                    poseStack.popPose();
                }
            }
        }
    }
}
