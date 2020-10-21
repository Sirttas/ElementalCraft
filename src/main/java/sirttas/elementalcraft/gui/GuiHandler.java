package sirttas.elementalcraft.gui;

import net.minecraft.client.MainWindow;
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
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

@SuppressWarnings("resource")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class GuiHandler {

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		RayTraceResult result = minecraft.objectMouseOver;

		if (event.getType() == ElementType.ALL) {
			if (result != null) {
				BlockPos pos = result.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) result).getPos() : null;
				TileEntity tile = pos != null ? minecraft.player.world.getTileEntity(pos) : null;

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
				int amount = holder.getElementAmount(stack);
				Spell spell = getSpell();

				doRenderElementGauge(amount, holder.getElementAmountMax(), holder.getElementType());
				if (spell.isValid()) {
					doRenderCanCast(amount >= spell.getConsumeAmount());
				}
			}
		}
	}

	private static ItemStack getElementHolder() {
		ClientPlayerEntity player = Minecraft.getInstance().player;

		return EntityHelper.handStream(player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ItemElementHolder) {
				return stack;
			} else if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return ItemElementHolder.find(player, SpellHelper.getSpell(stack).getElementType());
			}
			return ItemStack.EMPTY;
		}).filter(s -> !s.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
	}

	private static Spell getSpell() {
		return EntityHelper.handStream(Minecraft.getInstance().player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack);
			}
			return Spells.none;
		}).filter(Spell::isValid).findFirst().orElse(Spells.none);
	}

	private static void doRenderElementGauge(int element, int max, sirttas.elementalcraft.ElementType type) {
		MainWindow window = Minecraft.getInstance().getMainWindow();

		GuiHelper.renderElementGauge(window.getScaledWidth() / 2 - 32, window.getScaledHeight() / 2 - 8, element, max, type);
	}

	private static void doRenderCanCast(boolean canCast) {
		MainWindow window = Minecraft.getInstance().getMainWindow();

		GuiHelper.renderCanCast(window.getScaledWidth() / 2 - 21, window.getScaledHeight() / 2 + 3, canCast);
	}
}
