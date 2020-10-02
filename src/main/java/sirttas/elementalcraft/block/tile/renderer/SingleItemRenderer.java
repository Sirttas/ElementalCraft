package sirttas.elementalcraft.block.tile.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.inventory.IInventoryTile;

@OnlyIn(Dist.CLIENT)
public class SingleItemRenderer<T extends TileEntity & IInventoryTile> extends RendererEC<T> {

	Vector3d position;

	public SingleItemRenderer(TileEntityRendererDispatcher rendererDispatcher, Vector3d position) {
		super(rendererDispatcher);
		this.position = position;
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = te.getInventory().getStackInSlot(0);

		if (!stack.isEmpty()) {
			matrixStack.translate(position.x, position.y, position.z);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(getAngle(partialTicks)));
			renderItem(stack, matrixStack, buffer, light, overlay);
		}
	}
}
