package sirttas.elementalcraft.block.instrument.purifier;


import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.block.tile.renderer.RendererEC;

@OnlyIn(Dist.CLIENT)
public class RendererPurifier extends RendererEC<TilePurifier> {

	public RendererPurifier(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	public Quaternion getRotation(Direction direction) {
		switch (direction) {
		case SOUTH:
			return Vector3f.YP.rotationDegrees(180.0F);
		case WEST:
			return Vector3f.YP.rotationDegrees(90.0F);
		case EAST:
			return Vector3f.YP.rotationDegrees(-90.0F);
		case NORTH:
		default:
			return Quaternion.ONE.copy();
		}
	}

	@Override
	public void render(TilePurifier te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		IInventory inv = te.getInventory();
		ItemStack stack = inv.getStackInSlot(0);
		ItemStack stack2 = inv.getStackInSlot(1);
		
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.translate(0.5, 0.5, 0.5);
			matrixStack.rotate(getRotation(te.getBlockState().get(BlockPurifier.FACING)));
			matrixStack.translate(0, -5D * BlockEC.BIT_SIZE, -6 * BlockEC.BIT_SIZE);
			if (!stack.isEmpty()) {
				renderItem(stack, matrixStack, buffer, light, overlay);
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 0.6, 6 * BlockEC.BIT_SIZE);
				matrixStack.rotate(Vector3f.YP.rotationDegrees(getAngle(partialTicks)));
				renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
