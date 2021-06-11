package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;

public class AccelerationShrineUpgradeRenderer extends AbstractECRenderer<AccelerationShrineUpgradeBlockEntity> {

	public static final RenderMaterial IRON_TEXTURE =  ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("block/iron"));
	private static final Vector3f POSITION = new Vector3f(0, 2F / 16, 0);

	private ModelRenderer model;

	public AccelerationShrineUpgradeRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		this.model = new ModelRenderer(16, 16, 0, 0);
		this.model.addBox(-0.5F, -0.5F, 1F, 1F, 1F, 6F, 0.0F);
	}

	@Override
	public void render(AccelerationShrineUpgradeBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		BlockState state = te.getBlockState();
		Direction facing = state.getValue(AbstractDirectionalShrineUpgradeBlock.FACING);
		Quaternion rotation = facing.getRotation();
		Vector3f newPos = POSITION.copy();

		matrixStack.translate(0.5, 0.5, 0.5);
		matrixStack.mulPose(facing.step().rotation((float) Math.toRadians(getAngle(partialTicks))));
		newPos.transform(rotation);
		matrixStack.translate(newPos.x(), newPos.y(), newPos.z());
		matrixStack.mulPose(rotation);
		model.render(matrixStack, IRON_TEXTURE.buffer(buffer, RenderType::entitySolid), light, overlay);

	}

}
