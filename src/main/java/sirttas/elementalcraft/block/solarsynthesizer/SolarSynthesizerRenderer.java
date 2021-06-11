package sirttas.elementalcraft.block.solarsynthesizer;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.entity.renderer.AbstractECRenderer;

public class SolarSynthesizerRenderer extends AbstractECRenderer<SolarSynthesizerBlockEntity> {

	public static final RenderMaterial BEAM = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/solar_synthesizer_beam"));
	public static final ResourceLocation LENSE_LOCATION = ElementalCraft.createRL("block/solar_synthesizer_lense");

	private IBakedModel lenseModel;

	public SolarSynthesizerRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(SolarSynthesizerBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = te.getInventory().getItem(0);
		Minecraft minecraft = Minecraft.getInstance();

		if (lenseModel == null) {
			lenseModel = minecraft.getModelManager().getModel(LENSE_LOCATION);
		}
		renderRunes(matrixStack, buffer, te.getRuneHandler(), getAngle(partialTicks), light, overlay);
		if (!stack.isEmpty()) {
			Item item = stack.getItem();
			ElementType elementType = item instanceof IElementTypeProvider ? ((IElementTypeProvider) item).getElementType() : ElementType.NONE;

			if (elementType != ElementType.NONE) {
				float r = elementType.getRed();
				float g = elementType.getGreen();
				float b = elementType.getBlue();
				boolean isWorking = te.isWorking();

				matrixStack.pushPose();
				matrixStack.translate(0.5, 14.5 / 16, 0.5);
				if (isWorking) {
					matrixStack.mulPose(Vector3f.ZP.rotation(te.getLevel().getSunAngle(partialTicks)));
				} else {
					matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
				}
				matrixStack.translate(-3D / 16, -1D / 32, -3D / 16);
				minecraft.getBlockRenderer().getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(RenderType.translucent()), te.getBlockState(), lenseModel, r, g,
						b, light, overlay, EmptyModelData.INSTANCE);
				matrixStack.popPose();
				if (isWorking) {
					Vector3d beamVect = Vector3d.atCenterOf(te.getBlockPos()).subtract(minecraft.getEntityRenderDispatcher().camera.getPosition()).multiply(1, 0, 1).normalize();
					
					matrixStack.pushPose();
					matrixStack.translate(0.5, 0.5, 0.5);
					matrixStack.mulPose(Vector3f.YP.rotation((float) Math.acos(beamVect.z * (beamVect.x > 0 ? 1 : -1))));
					matrixStack.scale(0.006F, 0.006F, 0.006F);
					this.renderIcon(matrixStack, buffer, -21, 38, BEAM, 42, -76, r, g, b, light, overlay);
					matrixStack.popPose();
				}
			}
		}
	}
}
