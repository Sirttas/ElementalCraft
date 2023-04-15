package sirttas.elementalcraft.block.instrument.io.mill.grindstone;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class AirMillGrindstoneRenderer implements BlockEntityRenderer<AirMillGrindstoneBlockEntity> {

	public static final ResourceLocation BLADES_LOCATION = ElementalCraft.createRL("block/air_mill_grindstone_blades");
	
	private BakedModel bladesModel;

	@Override
	public void render(@Nonnull AirMillGrindstoneBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		if (bladesModel == null) {
			bladesModel = Minecraft.getInstance().getModelManager().getModel(BLADES_LOCATION);
		}
		Container inv = te.getInventory();
		ItemStack stack = inv.getItem(0);
		ItemStack stack2 = inv.getItem(1);
		float tick = ECRendererHelper.getClientTicks(partialTicks);

		ECRendererHelper.renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.pushPose();
		matrixStack.translate(0, 1 / 4D, 0);
		if (te.isRunning()) {
			matrixStack.translate(0.5, 0, 0.5);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-5 * tick));
			matrixStack.translate(-0.5, 0, -0.5);
		}
		ECRendererHelper.renderModel(bladesModel, matrixStack, buffer, te, light, overlay);
		matrixStack.popPose();
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.translate(0.5, 0.3, 0.5);
			matrixStack.mulPose(ECRendererHelper.getRotation(te.getBlockState().getValue(AirMillGrindstoneBlock.FACING)));
			matrixStack.translate(0, 0, -3 / 8D);
			if (!stack.isEmpty()) {
				matrixStack.pushPose();
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 0, 3 / 4D);
				matrixStack.mulPose(Vector3f.YP.rotationDegrees(tick));
				ECRendererHelper.renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
