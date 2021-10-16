package sirttas.elementalcraft.block.source;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.renderer.IECRenderer;

public class SourceRenderer implements IECRenderer<SourceBlockEntity> {

	public static final ResourceLocation STABILIZER_LOCATION = ElementalCraft.createRL("block/source_stabilizer");
	
	private BakedModel stabilizerModel;
	
	public SourceRenderer(Context context) {}

	@SuppressWarnings("resource")
	@Override
	public void render(SourceBlockEntity source, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		Minecraft minecraft = Minecraft.getInstance();

		if (stabilizerModel == null) {
			stabilizerModel = minecraft.getModelManager().getModel(STABILIZER_LOCATION);
		}
		
		if (source.isStabalized()) {
			ElementType elementType = source.getElementStorage().getElementType();
			float r = elementType.getRed();
			float g = elementType.getGreen();
			float b = elementType.getBlue();

			matrixStack.translate(0.5, 0, 0.5);
			matrixStack.mulPose(Vector3f.YP.rotation(getAngle(partialTicks) / 20F));
			matrixStack.translate(-0.5, 0, -0.5);
			matrixStack.translate(0, -1 / 4D, 0);
			minecraft.getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderType.translucent()), source.getBlockState(), stabilizerModel, r, g, b, light,
					overlay, getModelData(stabilizerModel, source));
		}
	}
	
}
