package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class EnchantmentLiquefierRenderer implements BlockEntityRenderer<EnchantmentLiquefierBlockEntity> {

	@Override
	public void render(EnchantmentLiquefierBlockEntity te, float partialTicks, PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		float tick = ECRendererHelper.getClientTicks(partialTicks);
		InstrumentContainer inv = (InstrumentContainer) te.getInventory();

		matrixStack.translate(0F, 0.25F, 0F);
		ECRendererHelper.renderRunes(matrixStack, buffer, te.getRuneHandler(), tick, light, overlay);
		matrixStack.translate(0.5F, 0.45F, 0.5F);
		matrixStack.pushPose();
		matrixStack.scale(0.75F, 0.75F, 0.75F);
		matrixStack.mulPose(Axis.YP.rotationDegrees(tick));
		ECRendererHelper.renderItem(inv.getItem(1), matrixStack, buffer, light, overlay);
		matrixStack.popPose();
		matrixStack.translate(0F, 0.75F, 0F);
		matrixStack.scale(0.75F, 0.75F, 0.75F);
		matrixStack.mulPose(Axis.YP.rotationDegrees(-tick));
		ECRendererHelper.renderItem(inv.getItem(0), matrixStack, buffer, light, overlay);
	}
}
