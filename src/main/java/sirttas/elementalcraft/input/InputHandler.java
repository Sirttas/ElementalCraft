package sirttas.elementalcraft.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.spell.ChangeSpellMessage;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class InputHandler {

	private InputHandler() {}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		
		if (player != null && player.isShiftKeyDown()) {
			getFirstSpellCastTool(EntityHelper.handStream(player)).ifPresent(stack -> {
				var index = SpellHelper.getSelected(stack);

				if (event.getScrollDelta() > 0) {
					setSelectedSpell(player, stack, index - 1);
					event.setCanceled(true);
				} else if (event.getScrollDelta() < 0) {
					setSelectedSpell(player, stack, index + 1);
					event.setCanceled(true);
				}
			});
		}
	}

	@Nonnull
	private static Optional<ItemStack> getFirstSpellCastTool(Stream<ItemStack> player) {
		return player
				.filter(i -> i.is(ECTags.Items.SPELL_CAST_TOOLS))
				.findFirst();
	}

	private static void setSelectedSpell(LocalPlayer player, ItemStack stack, int i) {
		SpellHelper.setSelected(stack, i);
		player.displayClientMessage(SpellHelper.getSpell(stack).getDisplayName(), true);
		new ChangeSpellMessage(i).send();
	}


	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(event.side == LogicalSide.CLIENT && event.player instanceof LocalPlayer localPlayer && event.player == Minecraft.getInstance().player && event.phase == TickEvent.Phase.END) {
			getFirstSpellCastTool(EntityHelper.handStream(localPlayer)).ifPresent(stack -> {
				var index = 0;

				for (var key : ECKeyMappings.CHANGE_TO_SPELL) {
					if (!key.isUnbound() && key.consumeClick() && SpellHelper.getSpellsAsMap(stack).size() > index) {
						setSelectedSpell(localPlayer, stack, index);
					}
					index++;
				}
			});
		}
	}
}
