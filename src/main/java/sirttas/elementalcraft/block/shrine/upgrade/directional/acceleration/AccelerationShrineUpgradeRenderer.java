package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;

public class AccelerationShrineUpgradeRenderer implements IECRenderer<AccelerationShrineUpgradeBlockEntity> {

	public static final Material IRON_TEXTURE =  ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("block/iron"));
	private static final Vector3f POSITION = new Vector3f(0, 2F / 16, 0);

	//TODO private ModelPart model;

	public AccelerationShrineUpgradeRenderer(Context context) {
//		this.model = new ModelPart(16, 16, 0, 0);
//		this.model.addBox(-0.5F, -0.5F, 1F, 1F, 1F, 6F, 0.0F);
	}

	@Override
	public void render(AccelerationShrineUpgradeBlockEntity te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		BlockState state = te.getBlockState();
		Direction facing = state.getValue(AbstractDirectionalShrineUpgradeBlock.FACING);
		Quaternion rotation = facing.getRotation();
		Vector3f newPos = POSITION.copy();

		matrixStack.translate(0.5, 0.5, 0.5);
		matrixStack.mulPose(facing.step().rotation((float) Math.toRadians(getAngle(partialTicks))));
		newPos.transform(rotation);
		matrixStack.translate(newPos.x(), newPos.y(), newPos.z());
		matrixStack.mulPose(rotation);
		//model.render(matrixStack, IRON_TEXTURE.buffer(buffer, RenderType::entitySolid), light, overlay);

	}
	
}
