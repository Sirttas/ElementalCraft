package sirttas.elementalcraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
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
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.holder.ItemElementHolder;
import sirttas.elementalcraft.item.spell.ISpellHolder;
import sirttas.elementalcraft.spell.SpellHelper;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class GuiHandler {

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		RayTraceResult result = minecraft.objectMouseOver;

		if (event.getType() == ElementType.ALL) {
			if (result != null) {
				BlockPos pos = result.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) result).getPos() : null;
				TileEntity tile = pos != null ? minecraft.world.getTileEntity(pos) : null;

				if (tile instanceof IElementStorage) {
					IElementStorage storage = (IElementStorage) tile;

					if (storage.doesRenderGauge() || GuiHelper.showDebugInfo()) {
						doRenderElementGauge(storage.getElementAmount(), storage.getMaxElement(), storage.getElementType());
						return;
					}
				}
			}
			ItemStack stack = getElementHolder();

			if (!stack.isEmpty() && stack.getItem() instanceof ItemElementHolder) {
				ItemElementHolder holder = (ItemElementHolder) stack.getItem();

				doRenderElementGauge(holder.getElementAmount(stack), holder.getElementAmountMax(), holder.getElementType());
			}
		}
	}

	@SuppressWarnings("resource")
	private static ItemStack getElementHolder() {
		ClientPlayerEntity player = Minecraft.getInstance().player;

		return EntityHelper.handStream(player).map(s -> {
			if (!s.isEmpty() && s.getItem() instanceof ItemElementHolder) {
				return s;
			} else if (!s.isEmpty() && s.getItem() instanceof ISpellHolder) {
				return ItemElementHolder.find(player, SpellHelper.getSpell(s).getElementType());
			}
			return ItemStack.EMPTY;
		}).filter(s -> !s.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
	}

	private static void doRenderElementGauge(int element, int max, sirttas.elementalcraft.ElementType type) {
		GuiHelper.renderElementGauge(Minecraft.getInstance().getMainWindow().getScaledWidth() / 2 - 32, Minecraft.getInstance().getMainWindow().getScaledHeight() / 2 - 8, element, max, type);
	}
}
