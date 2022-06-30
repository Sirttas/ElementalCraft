package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SingleItemRenderer<T extends BlockEntity & IContainerBlockEntity> implements RuneRenderer<T> {

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
	public void render(@Nonnull T te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = te.getInventory().getItem(0);

		RuneRenderer.super.render(te, partialTicks, matrixStack, buffer, light, overlay);
		if (!stack.isEmpty()) {
			matrixStack.translate(position.x, position.y, position.z);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(getClientTicks(partialTicks)));
			matrixStack.scale(size, size, size);
			renderItem(stack, matrixStack, buffer, light, overlay);
		}
	}
}
