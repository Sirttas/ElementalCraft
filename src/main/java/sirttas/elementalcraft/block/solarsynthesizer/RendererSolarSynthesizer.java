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
import sirttas.elementalcraft.block.tile.renderer.AbstractRendererEC;

public class RendererSolarSynthesizer extends AbstractRendererEC<TileSolarSynthesizer> {

	public static final RenderMaterial BEAM = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/solar_synthesizer_beam"));
	public static final ResourceLocation LENSE_LOCATION = ElementalCraft.createRL("block/solar_synthesizer_lense");

	private IBakedModel lenseModel;

	public RendererSolarSynthesizer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(TileSolarSynthesizer te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = te.getInventory().getStackInSlot(0);
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

				matrixStack.push();
				matrixStack.translate(0.5, 14.5 / 16, 0.5);
				if (isWorking) {
					matrixStack.rotate(Vector3f.ZP.rotation(te.getWorld().getCelestialAngleRadians(partialTicks)));
				} else {
					matrixStack.rotate(Vector3f.ZP.rotationDegrees(90));
				}
				matrixStack.translate(-3D / 16, -1D / 32, -3D / 16);
				minecraft.getBlockRendererDispatcher().getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderType.getTranslucent()), te.getBlockState(), lenseModel, r, g,
						b, light, overlay, EmptyModelData.INSTANCE);
				matrixStack.pop();
				if (isWorking) {
					Vector3d beamVect = Vector3d.copyCentered(te.getPos()).subtract(minecraft.getRenderManager().info.getProjectedView()).mul(1, 0, 1).normalize();
					
					matrixStack.push();
					matrixStack.translate(0.5, 0.5, 0.5);
					matrixStack.rotate(Vector3f.YP.rotation((float) Math.acos(beamVect.z * (beamVect.x > 0 ? 1 : -1))));
					matrixStack.scale(0.006F, 0.006F, 0.006F);
					this.renderIcon(matrixStack, buffer, -21, 38, BEAM, 42, -76, r, g, b, light, overlay);
					matrixStack.pop();
				}
			}
		}
	}
}
