package sirttas.elementalcraft.block.instrument.mill;


import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

@OnlyIn(Dist.CLIENT)
public class RendererAirMill extends AbstractRendererEC<TileAirMill> {

	public static final ResourceLocation BLADES_LOCATION = ElementalCraft.createRL("block/air_mill_blades");
	
	private IBakedModel bladesModel;
	
	public RendererAirMill(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(TileAirMill te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		Minecraft minecraft = Minecraft.getInstance();
		
		if (bladesModel == null) {
			bladesModel = minecraft.getModelManager().getModel(BLADES_LOCATION);
		}
		IInventory inv = te.getInventory();
		ItemStack stack = inv.getStackInSlot(0);
		ItemStack stack2 = inv.getStackInSlot(1);
		float tick = getAngle(partialTicks);
		
		renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.push();
		matrixStack.translate(0, 1 / 4D, 0);
		if (te.isRunning()) {
			matrixStack.translate(0.5, 0, 0.5);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(-5 * tick));
			matrixStack.translate(-0.5, 0, -0.5);
		}
		minecraft.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getCutout()), te.getBlockState(), bladesModel, 1, 1, 1, light,
				overlay, EmptyModelData.INSTANCE);
		matrixStack.pop();
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.translate(0.5, 0.3, 0.5);
			matrixStack.rotate(getRotation(te.getBlockState().get(BlockAirMill.FACING)));
			matrixStack.translate(0, 0, -3 / 8D);
			if (!stack.isEmpty()) {
				matrixStack.push();
				matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
				renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.pop();
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 0, 3 / 4D);
				matrixStack.rotate(Vector3f.YP.rotationDegrees(tick));
				renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
