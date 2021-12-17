package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;

import javax.annotation.Nonnull;

public class AccelerationShrineUpgradeRenderer implements IECRenderer<AccelerationShrineUpgradeBlockEntity> {

	private static final Vector3f POSITION = new Vector3f(0, 2F / 16, 0);

	public static final ResourceLocation CLOCK_LOCATION = ElementalCraft.createRL("block/shrine_upgrade_acceleration_clock");

	private BakedModel clockModel;

	@Override
	public void render(AccelerationShrineUpgradeBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		BlockState state = te.getBlockState();
		Direction facing = state.getValue(AbstractDirectionalShrineUpgradeBlock.FACING);
		Quaternion rotation = facing.getRotation();
		Vector3f newPos = POSITION.copy();

		if (clockModel == null) {
			clockModel = Minecraft.getInstance().getModelManager().getModel(CLOCK_LOCATION);
		}
		matrixStack.translate(0.5, 0.5, 0.5);
		matrixStack.mulPose(facing.step().rotation((float) Math.toRadians(getAngle(partialTicks))));
		newPos.transform(rotation);
		matrixStack.translate(newPos.x(), newPos.y(), newPos.z());
		matrixStack.mulPose(rotation);
		this.renderModel(clockModel, matrixStack, buffer, te, light, overlay);
	}
	
}
