package sirttas.elementalcraft.block.tile.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;

@OnlyIn(Dist.CLIENT)
public class RuneRenderer<T extends TileEntity> extends AbstractRendererEC<T> {

	public RuneRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		te.getCapability(CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY).ifPresent(handler -> renderRunes(matrixStack, buffer, handler, getAngle(partialTicks), light, overlay));
	}
}
