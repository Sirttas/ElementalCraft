package sirttas.elementalcraft.event;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EnchantmentHandler {

	private EnchantmentHandler() {}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (ECTags.Items.SPELL_CAST_TOOLS.contains(left.getItem()) && right.getItem() == ECItems.SCROLL && SpellHelper.getSpellCount(left) < ECConfig.COMMON.focusMaxSpell.get()) {
			ItemStack result = left.copy();
			int n = 4 * (SpellHelper.getSpellCount(left) + 1);

			if (StringUtils.isBlank(event.getName())) {
				if (left.hasCustomHoverName()) {
					n++;
					result.resetHoverName();
				}
			} else if (!event.getName().equals(left.getHoverName().getString())) {
				n++;
				result.setHoverName(new TextComponent(event.getName()));
			}
			SpellHelper.addSpell(result, SpellHelper.getSpell(right));
			event.setCost(n);
			event.setOutput(result);
		}
	}
}
