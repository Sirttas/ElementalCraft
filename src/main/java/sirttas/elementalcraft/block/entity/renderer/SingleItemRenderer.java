package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.inventory.IInventoryTile;

@OnlyIn(Dist.CLIENT)
public class SingleItemRenderer<T extends TileEntity & IInventoryTile> extends AbstractECRenderer<T> {

	private final Vector3d position;
	private final float size;

	public SingleItemRenderer(TileEntityRendererDispatcher rendererDispatcher, Vector3d position) {
		this(rendererDispatcher, position, 1);
	}

	public SingleItemRenderer(TileEntityRendererDispatcher rendererDispatcher, Vector3d position, float size) {
		super(rendererDispatcher);
		this.position = position;
		this.size = size;
	}

	@Override
	public void render(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = te.getInventory().getItem(0);
		float tick = getAngle(partialTicks);

		CapabilityRuneHandler.get(te).ifPresent(handler -> renderRunes(matrixStack, buffer, handler, tick, light, overlay));
		if (!stack.isEmpty()) {
			matrixStack.translate(position.x, position.y, position.z);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(getAngle(partialTicks)));
			matrixStack.scale(size, size, size);
			renderItem(stack, matrixStack, buffer, light, overlay);
		}
	}
}
