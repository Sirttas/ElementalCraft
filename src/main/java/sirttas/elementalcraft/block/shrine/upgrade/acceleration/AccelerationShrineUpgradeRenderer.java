package sirttas.elementalcraft.block.shrine.upgrade.acceleration;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class AccelerationShrineUpgradeRenderer implements BlockEntityRenderer<AccelerationShrineUpgradeBlockEntity> {

	private static final Vector3f POSITION = new Vector3f(0, 2F / 16, 0);

	public static final ResourceLocation CLOCK_LOCATION = ElementalCraftApi.createRL("block/shrine_upgrade_acceleration_clock");

	private BakedModel clockModel;

	@Override
	public void render(AccelerationShrineUpgradeBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		var state = te.getBlockState();
		var facing = state.getValue(AbstractDirectionalShrineUpgradeBlock.FACING);
		var rotation = facing.getRotation();
		var newPos = new Vector3f(POSITION);

		if (clockModel == null) {
			clockModel = Minecraft.getInstance().getModelManager().getModel(CLOCK_LOCATION);
		}
		matrixStack.translate(0.5, 0.5, 0.5);
		matrixStack.mulPose(Axis.of(facing.step()).rotation((float) Math.toRadians(ECRendererHelper.getClientTicks(partialTicks))));
		rotation.transform(newPos);
		matrixStack.translate(newPos.x(), newPos.y(), newPos.z());
		matrixStack.mulPose(rotation);
		ECRendererHelper.renderModel(clockModel, matrixStack, buffer, te, light, overlay);
	}
}
