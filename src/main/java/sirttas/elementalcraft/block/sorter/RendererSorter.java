package sirttas.elementalcraft.block.sorter;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

public class RendererSorter extends AbstractRendererEC<TileSorter> {

	public RendererSorter(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@SuppressWarnings("resource")
	@Override
	public void render(TileSorter sorter, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		RayTraceResult mouseOver = Minecraft.getInstance().objectMouseOver;
		boolean sneeking =  Minecraft.getInstance().player.isSneaking();
		List<ItemStack> stacks = sorter.getStacks();

		if (mouseOver != null && mouseOver.getType() == RayTraceResult.Type.BLOCK && !stacks.isEmpty()) {
			BlockRayTraceResult result = (BlockRayTraceResult) mouseOver;
			
			if (sorter.getPos().equals(result.getPos())) {
				int index = sorter.getIndex();
				Quaternion rotation = result.getFace().getRotation();
				Vector3f newPos = new Vector3f(0, 2F / 16, 1F / 16);

				matrixStack.translate(0.5, 0.5, 0.5);
				newPos.transform(rotation);
				matrixStack.translate(newPos.getX(), newPos.getY(), newPos.getZ());
				matrixStack.rotate(rotation);
				matrixStack.rotate(Vector3f.XP.rotationDegrees(-90F));
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.renderItem(stacks.get(index), matrixStack, buffer, light, overlay);
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				matrixStack.push();
				translate(matrixStack, 0.5, sneeking);
				for (int i = index - 1; i >= 0; i--) {
					translate(matrixStack, 0.5, sneeking);
					this.renderItem(stacks.get(i), matrixStack, buffer, light, overlay);
				}
				matrixStack.pop();
				matrixStack.push();
				translate(matrixStack, -0.5, sneeking);
				for (int i = index + 1; i < stacks.size(); i++) {
					translate(matrixStack, -0.5, sneeking);
					this.renderItem(stacks.get(i), matrixStack, buffer, light, overlay);
				}
				matrixStack.pop();
			}
		}
	}
	
	private void translate(MatrixStack matrixStack, double value, boolean sneeking) {
		if (sneeking) {
			matrixStack.translate(-value, 0, 0);
		} else {
			matrixStack.translate(0, value, 0);
		}
	}
}
