package sirttas.elementalcraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.element.IElementStorage;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class GuiHandler {

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		RayTraceResult result = minecraft.objectMouseOver;

		if (event.getType() == ElementType.ALL && result != null) {
			BlockPos pos = result.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) result).getPos() : null;
			TileEntity tile = pos != null ? minecraft.world.getTileEntity(pos) : null;

			if (tile instanceof IElementStorage) {
				IElementStorage storage = (IElementStorage) tile;

				if (storage.doesRenderGauge() || GuiHelper.showDebugInfo()) {
					GuiHelper.renderElementGauge(event.getMatrixStack(), minecraft.getMainWindow().getScaledWidth() / 2 - 32, minecraft.getMainWindow().getScaledHeight() / 2 - 8,
							storage.getElementAmount(), storage.getMaxElement(), storage.getElementType());
				}
			}
		}
	}
}
