package sirttas.elementalcraft.block.tile.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import sirttas.elementalcraft.event.TickHandler;

public abstract class RendererEC<T extends TileEntity> extends TileEntityRenderer<T> {

	public RendererEC(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	public void renderItem(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, overlay, matrixStack, buffer);
	}

	public void renderBlock(BlockState state, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, light, overlay, EmptyModelData.INSTANCE);
	}

	public void renderBlock(BlockState state, MatrixStack matrixStack, IVertexBuilder builder, World world) {
		Minecraft.getInstance().getBlockRendererDispatcher().renderModel(state, BlockPos.ZERO, world, matrixStack, builder, false, world.rand, EmptyModelData.INSTANCE);
	}

	public float getAngle(float partialTicks) {
		return TickHandler.getTicksInGame() + partialTicks % 360;
	}

}
