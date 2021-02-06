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
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.block.tile.renderer.RendererEC;

public class RendererSorter extends RendererEC<TileSorter> {

	public RendererSorter(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@SuppressWarnings("resource")
	@Override
	public void render(TileSorter sorter, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		RayTraceResult mouseOver = Minecraft.getInstance().objectMouseOver;
		List<ItemStack> stacks = sorter.getStacks();

		if (mouseOver != null && mouseOver.getType() == RayTraceResult.Type.BLOCK && !stacks.isEmpty()) {
			BlockRayTraceResult result = (BlockRayTraceResult) mouseOver;
			
			if (sorter.getPos().equals(result.getPos())) {
				int index = sorter.getIndex();
				Quaternion rotation = result.getFace().getRotation();
				Vector3f newPos = new Vector3f(0, 2F * BlockEC.BIT_SIZE, 1F * BlockEC.BIT_SIZE);

				matrixStack.translate(0.5, 0.5, 0.5);
				newPos.transform(rotation);
				matrixStack.translate(newPos.getX(), newPos.getY(), newPos.getZ());
				matrixStack.rotate(rotation);
				matrixStack.rotate(Vector3f.XP.rotationDegrees(-90F));
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				this.renderItem(stacks.get(index), matrixStack, buffer, light, overlay);
				matrixStack.scale(0.5F, 0.5F, 0.5F);
				matrixStack.push();
				matrixStack.translate(0, 0.5, 0);
				for (int i = index - 1; i >= 0; i--) {
					matrixStack.translate(0, 0.5, 0);
					this.renderItem(stacks.get(i), matrixStack, buffer, light, overlay);
				}
				matrixStack.pop();
				matrixStack.push();
				matrixStack.translate(0, -0.5, 0);
				for (int i = index + 1; i < stacks.size(); i++) {
					matrixStack.translate(0, -0.5, 0);
					this.renderItem(stacks.get(i), matrixStack, buffer, light, overlay);
				}
				matrixStack.pop();

			}
		}
	}
}
