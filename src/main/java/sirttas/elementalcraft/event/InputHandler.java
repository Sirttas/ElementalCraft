package sirttas.elementalcraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.network.message.ECMessage;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class InputHandler {

	private InputHandler() {}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		
		if (player.isShiftKeyDown()) {
			EntityHelper.handStream(player)
					.filter(i -> i.is(ECTags.Items.SPELL_CAST_TOOLS))
					.findFirst()
					.ifPresent(i -> {
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

	private static void handleFocusScroll(LocalPlayer player, ItemStack stack, int i) {
		SpellHelper.moveSelected(stack, i);
		player.displayClientMessage(SpellHelper.getSpell(stack).getDisplayName(), true);
	}
}
