package sirttas.elementalcraft.block.container;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import sirttas.elementalcraft.api.renderer.ECRenderTypes;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerRenderer<T extends AbstractElementContainerBlockEntity> implements IECRenderer<T> {
    @Override
    public void render(@Nonnull T container, float pPartialTick, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (Boolean.FALSE.equals(ECConfig.CLIENT.renderInstrumentShadow.get())) {
            return;
        }

        var player = Minecraft.getInstance().player;
        var level = container.getLevel();

        if (level == null || player == null) {
            return;
        }

        var pos = container.getBlockPos();
        var iterator =  List.of(player.getMainHandItem(), player.getOffhandItem()).iterator();
        boolean wasRendered = false;

        while(iterator.hasNext() && !wasRendered) {
            var stack = iterator.next();

            if (stack.getItem() instanceof BlockItem blockItem && stack.is(container.isSmall() ? ECTags.Items.SMALL_CONTAINER_COMPATIBLES : ECTags.Items.CONTAINER_TOOLS)) {
                var block = blockItem.getBlock();
                var instrumentPos = pos.above();

                if (level.getBlockState(instrumentPos).isAir()) {
                    var state = block.getStateForPlacement(new DirectionalPlaceContext(level, instrumentPos, Direction.DOWN, stack, Direction.UP));

                    if (state != null && state.canSurvive(level, instrumentPos)) {
                        poseStack.pushPose();
                        poseStack.translate(0, 1, 0);
                        renderBlock(state, poseStack, bufferSource.getBuffer(ECRenderTypes.GHOST), level, instrumentPos);
                        poseStack.popPose();
                        wasRendered = true;
                    }
                }
            }
        }
    }
}
