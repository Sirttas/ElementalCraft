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
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractBlockDirectionalShrineUpgrade;
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

public class RedererAccelerationShrineUpgrade extends AbstractRendererEC<TileAccelerationShrineUpgrade> {

	public static final RenderMaterial IRON_TEXTURE =  ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("block/iron"));
	private static final Vector3f POSITION = new Vector3f(0, 2F / 16, 0);

	private ModelRenderer model;

	public RedererAccelerationShrineUpgrade(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		this.model = new ModelRenderer(16, 16, 0, 0);
		this.model.addBox(-0.5F, -0.5F, 1F, 1F, 1F, 6F, 0.0F);
	}

	@Override
	public void render(TileAccelerationShrineUpgrade te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		BlockState state = te.getBlockState();
		Direction facing = state.get(AbstractBlockDirectionalShrineUpgrade.FACING);
		Quaternion rotation = facing.getRotation();
		Vector3f newPos = POSITION.copy();

		matrixStack.translate(0.5, 0.5, 0.5);
		matrixStack.rotate(facing.toVector3f().rotation((float) Math.toRadians(getAngle(partialTicks))));
		newPos.transform(rotation);
		matrixStack.translate(newPos.getX(), newPos.getY(), newPos.getZ());
		matrixStack.rotate(rotation);
		model.render(matrixStack, IRON_TEXTURE.getBuffer(buffer, RenderType::getEntitySolid), light, overlay);

	}

}
