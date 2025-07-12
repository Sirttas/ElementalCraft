package sirttas.elementalcraft.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.jewel.JewelItem;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class EnchantmentHandler {

	private EnchantmentHandler() {}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		var left = event.getLeft();
		var right = event.getRight();
		var name = event.getName();
		var spellList = SpellHelper.getSpellList(left);

		if (left.is(ECTags.Items.SPELL_CAST_TOOLS) && right.is(ECItems.SCROLL) && !spellList.isFull()) {
			var result = left.copy();
			var n = 4 * (spellList.count() + 1);

			if (applyRename(left, result, name)) {
				n++;
			}
			SpellHelper.addSpell(result, SpellHelper.getSpell(right));
			event.setCost(n);
			event.setOutput(result);
		} else if (left.is(ECTags.Items.JEWEL_SOCKETABLES) && right.getItem() instanceof JewelItem jewelItem && JewelHelper.getJewel(left) == null) {
			var result = left.copy();
			var n = 10;

			if (applyRename(left, result, name)) {
				n++;
			}
			JewelHelper.setJewel(result, jewelItem.getJewel());
			event.setCost(n);
			event.setOutput(result);
		}
	}

	private static boolean applyRename(ItemStack input, ItemStack result, String name) {
		if (StringUtils.isBlank(name)) {
			if (input.has(DataComponents.CUSTOM_NAME)) {
				result.remove(DataComponents.CUSTOM_NAME);
				return true;
			}
		} else if (!name.equals(input.getHoverName().getString())) {
			result.set(DataComponents.CUSTOM_NAME, Component.literal(name));
			return true;
		}
		return false;
	}
}
