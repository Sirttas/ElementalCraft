package sirttas.elementalcraft.block.instrument.infuser;


import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.tile.renderer.RendererEC;
import sirttas.elementalcraft.item.ItemEC;

@OnlyIn(Dist.CLIENT)
public class RendererInfuser extends RendererEC<TileInfuser> {

	public RendererInfuser(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(TileInfuser te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = te.getStackInSlot(0);
		
		if (!ItemEC.isEmpty(stack)) {
			matrixStack.translate(0.5F, 0.2F, 0.5F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(getAngle(partialTicks)));
			renderItem(stack, matrixStack, buffer, light, overlay);
		}
	}
}
