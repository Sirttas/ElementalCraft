package sirttas.elementalcraft.block.instrument.io.mill;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class MillRenderer<T extends AbstractMillBlockEntity<?, ?>> implements BlockEntityRenderer<T> {

	public static final ResourceLocation WATER_MILL_GRINDSTONE_SHAFT_LOCATION = ElementalCraftApi.createRL("block/water_mill_grindstone_shaft");
	public static final ResourceLocation AIR_MILL_GRINDSTONE_SHAFT_LOCATION = ElementalCraftApi.createRL("block/air_mill_grindstone_shaft");
	public static final ResourceLocation WATER_MILL_WOOD_SAW_SHAFT_LOCATION = ElementalCraftApi.createRL("block/water_mill_wood_saw_shaft");
	public static final ResourceLocation AIR_MILL_WOOD_SAW_SHAFT_LOCATION = ElementalCraftApi.createRL("block/air_mill_wood_saw_shaft");

	private final ResourceLocation modelLocation;
	private BakedModel model;

	public MillRenderer(ResourceLocation modelLocation) {
		this.modelLocation = modelLocation;
	}

	@Override
	public void render(@Nonnull T te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		if (model == null && modelLocation != null) {
			model = Minecraft.getInstance().getModelManager().getModel(modelLocation);
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
			matrixStack.mulPose(Axis.YP.rotationDegrees(-5 * tick));
			matrixStack.translate(-0.5, 0, -0.5);
		}
		ECRendererHelper.renderModel(model, matrixStack, buffer, te, light, overlay);
		matrixStack.popPose();
		if (!stack.isEmpty() || !stack2.isEmpty()) {
			matrixStack.translate(0.5, 0.3, 0.5);
			matrixStack.mulPose(ECRendererHelper.getRotation(te.getBlockState().getValue(AbstractMillBlock.FACING)));
			matrixStack.translate(0, 0, -3 / 8D);
			if (!stack.isEmpty()) {
				matrixStack.pushPose();
				matrixStack.mulPose(Axis.YP.rotationDegrees(tick));
				ECRendererHelper.renderItem(stack, matrixStack, buffer, light, overlay);
				matrixStack.popPose();
			}
			if (!stack2.isEmpty()) {
				matrixStack.translate(0, 0, 3 / 4D);
				matrixStack.mulPose(Axis.YP.rotationDegrees(tick));
				ECRendererHelper.renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
