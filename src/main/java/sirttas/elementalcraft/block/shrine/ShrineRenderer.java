package sirttas.elementalcraft.block.shrine;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.renderer.ECRendererHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ShrineRenderer<T extends AbstractShrineBlockEntity> implements BlockEntityRenderer<T> {


	@Override
	public void render(T shrine, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
		if (shrine.showsRange()) {
			BlockPos pos = shrine.getBlockPos();
			
			LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), shrine.getRange().move(-pos.getX(), -pos.getY(), -pos.getZ()), 1, 1, 0.6F, 1);
		}

		if (Boolean.TRUE.equals(ECConfig.CLIENT.renderShrineUpgradeShadow.get())) {
			renderGhostUpgrades(shrine, poseStack, bufferSource);
		}
	}

	private void renderGhostUpgrades(T shrine, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource) {
		var player = Minecraft.getInstance().player;
		var level = shrine.getLevel();

		if (level == null || player == null) {
			return;
		}

		var pos = shrine.getBlockPos();
		var iterator =  List.of(player.getMainHandItem(), player.getOffhandItem()).iterator();
		boolean wasRendered = false;

		while(iterator.hasNext() && !wasRendered) {
			var stack = iterator.next();

			if (stack.getItem() instanceof BlockItem blockItem && stack.is(ECTags.Items.SHRINE_UPGRADES)) {
				var block = blockItem.getBlock();

				for (var direction : shrine.getUpgradeDirections()) {
					var upgradePos = pos.relative(direction);

					if (level.getBlockState(upgradePos).isAir()) {
						var state = block.getStateForPlacement(new DirectionalPlaceContext(level, upgradePos, direction.getOpposite(), stack, direction));

						if (state != null && state.canSurvive(level, upgradePos)) {
							poseStack.pushPose();
							poseStack.translate(direction.getStepX(), direction.getStepY(), direction.getStepZ());
							ECRendererHelper.renderGhost(state, poseStack, bufferSource, level, upgradePos);
							poseStack.popPose();
							wasRendered = true;
						}
					}
				}
			}
		}
	}
}
