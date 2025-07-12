package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SingleItemRenderer<T extends BlockEntity & IContainerBlockEntity> implements BlockEntityRenderer<T> {

	private final Vec3 position;
	private final float size;

	public SingleItemRenderer(Vec3 position) {
		this(position, 1);
	}

	public SingleItemRenderer(Vec3 position, float size) {
		this.position = position;
		this.size = size;
	}

	@Override
	public void render(@Nonnull T blockEntity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = blockEntity.getInventory().getItem(0);

		ECRendererHelper.renderRunes(poseStack, buffer, blockEntity, partialTicks, light, overlay);
		if (!stack.isEmpty()) {
			poseStack.translate(position.x, position.y, position.z);
			poseStack.mulPose(Axis.YP.rotationDegrees(ECRendererHelper.getClientTicks(partialTicks)));
			poseStack.scale(size, size, size);
			ECRendererHelper.renderItem(stack, poseStack, buffer, light, overlay);
		}
	}
}
