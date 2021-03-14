package sirttas.elementalcraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.network.message.ECMessage;
import sirttas.elementalcraft.spell.SpellHelper;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class InputHandler {

	private InputHandler() {}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onMouseScroll(InputEvent.MouseScrollEvent event) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		
		if (player.isSneaking()) {
			EntityHelper.handStream(player).filter(i -> i.getItem() == ECItems.FOCUS).findFirst().ifPresent(i -> {
				if (event.getScrollDelta() > 0) {
					handleFocusScroll(player, i, -1);
					ECMessage.SCROLL_BACKWORD.send();
					event.setCanceled(true);
				} else if (event.getScrollDelta() < 0) {
					handleFocusScroll(player, i, 1);
					ECMessage.SCROLL_FORWARD.send();
					event.setCanceled(true);
				}
			});
		}
	}

	private static void handleFocusScroll(ClientPlayerEntity player, ItemStack stack, int i) {
		SpellHelper.moveSelected(stack, i);
		player.sendStatusMessage(SpellHelper.getSpell(stack).getDisplayName(), true);
	}
}
